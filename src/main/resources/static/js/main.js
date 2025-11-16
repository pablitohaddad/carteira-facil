import api from './api.js';

// Simples gerenciador de sessão no localStorage (armazenando email, senha e metadados)
const sessionKey = 'cf_auth';
function saveAuth(email, senha, meta = {}) { localStorage.setItem(sessionKey, JSON.stringify(Object.assign({email, senha}, meta))); }
function getAuth() { const v = localStorage.getItem(sessionKey); return v ? JSON.parse(v) : null; }
function clearAuth() { localStorage.removeItem(sessionKey); }

// Pages helpers
async function handleRegisterForm(e) {
    e.preventDefault();
    const nome = document.getElementById('reg-nome').value;
    const email = document.getElementById('reg-email').value;
    const senha = document.getElementById('reg-senha').value;
    try {
        await api.registerUser(nome, email, senha);
        alert('Registrado com sucesso. Agora faça login.');
        // usar caminho relativo para não forçar origem
        window.location.href = 'index.html';
    } catch (err) { console.error(err); alert(err.message); }
}

async function handleLoginForm(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const senha = document.getElementById('login-senha').value;
    try {
        const me = await api.fetchAuthUser(email, senha);
        // extrair userId/role/subscription quando possível
        const userId = me?.user?.id || me?.id || me?.userId || null;
        const role = me?.user?.role || me?.role || (me?.user && me.user.role) || null;
        const subscriptionType = me?.user?.subscriptionType || me?.subscriptionType || null;
        saveAuth(email, senha, { userId, role, subscriptionType });
        // caminho relativo
        window.location.href = 'dashboard.html';
    } catch (err) { console.error(err); alert('Login falhou. Verifique credenciais.'); }
}

// Dashboard
function formatCurrency(number) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(number);
}

function createRow(item, type) {
    const tr = document.createElement('tr');
    tr.dataset.type = type;
    tr.dataset.id = item.id;
    tr.dataset.value = Number(item.valor);
    const isIncome = type === 'ganho';
    const date = new Date(item.data).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
    tr.innerHTML = `
        <td class="col-name">${item.nome}</td>
        <td class="col-category">${item.categoria || (isIncome? 'Ganho':'') }</td>
        <td class="col-value ${isIncome? 'income':''}">${isIncome? '+ ' : ''}${new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2 }).format(Number(item.valor))}</td>
        <td class="col-date">${date}</td>
        <td class="actions"><button class="edit-btn">✏️</button><button class="delete-btn">🗑️</button></td>
    `;
    return tr;
}

async function loadDashboard() {
    const auth = getAuth();
    if (!auth) { window.location.href = 'index.html'; return; }
    try {
        const me = await api.fetchAuthUser(auth.email, auth.senha);
        console.log('me payload:', me);
        // extrair userId de várias formas possíveis
        const userId = (me && (me.user?.id || me.id || me.userId || me.user?.userId)) || null;
        if (!userId) {
            console.warn('Não foi possível extrair userId de /usuarios/me', me);
            alert('Erro: não foi possível identificar o usuário (userId). Verifique o servidor.');
            return;
        }
        let gastos = [];
        let ganhos = [];
        try {
            gastos = await api.listGastosByUser(userId, auth.email, auth.senha);
        } catch (err) {
            console.error('Erro ao buscar gastos do usuário:', err);
            alert('Falha ao buscar gastos: ' + err.message);
        }
        try {
            ganhos = await api.listGanhosByUser(userId, auth.email, auth.senha);
        } catch (err) {
            console.error('Erro ao buscar ganhos do usuário:', err);
            alert('Falha ao buscar ganhos: ' + err.message);
        }
        const tbody = document.getElementById('expenses-tbody');
        tbody.innerHTML = '';
        if (Array.isArray(ganhos)) ganhos.forEach(g => tbody.appendChild(createRow(g, 'ganho')));
        if (Array.isArray(gastos)) gastos.forEach(g => tbody.appendChild(createRow(g, 'gasto')));
        updateTotalsAndCount();
    } catch (err) {
        console.error(err);
        alert('Falha ao carregar dados: ' + err.message);
    }
}

function updateTotalsAndCount() {
    const tbody = document.getElementById('expenses-tbody');
    let totalEntrada = 0, totalSaida = 0;
    tbody.querySelectorAll('tr').forEach(r => {
        const val = Number(r.dataset.value);
        if (r.dataset.type === 'ganho') totalEntrada += val; else totalSaida += val;
    });
    document.getElementById('total-entrada').innerHTML = `${formatCurrency(totalEntrada)} <span class="label">Entrada</span>`;
    document.getElementById('total-saida').innerHTML = `${formatCurrency(totalSaida)} <span class="label">Saída</span>`;
    document.getElementById('records-count').textContent = `${tbody.querySelectorAll('tr').length} Registros`;
}

// Modal add
function openAddModal() { document.getElementById('addExpenseModal').classList.add('show'); }
function closeAddModal() { document.getElementById('addExpenseModal').classList.remove('show'); document.getElementById('addExpenseForm').reset(); }

async function handleAddSubmit(e) {
    e.preventDefault();
    const auth = getAuth(); if (!auth) return window.location.href = 'index.html';
    const tipo = document.getElementById('g-tipo').value;
    const nome = document.getElementById('g-nome').value.trim();
    const categoria = document.getElementById('g-cat').value.trim();
    const rawValor = document.getElementById('g-val').value;
    const valor = Number(rawValor);
    const data = document.getElementById('g-data').value; // yyyy-mm-dd

    // Validação cliente
    if (!nome) { alert('Nome é obrigatório'); return; }
    if (!data) { alert('Data é obrigatória'); return; }
    if (!rawValor || isNaN(valor) || valor < 0.01) { alert('Valor inválido. Deve ser >= 0.01'); return; }

    const dto = { usuarioId: null, nome, categoria: categoria || null, valor: Number(valor.toFixed(2)), data };
    try {
        // usar userId salvo sempre que possível
        if (auth.userId) {
            dto.usuarioId = auth.userId;
        } else {
            const me = await api.fetchAuthUser(auth.email, auth.senha);
            dto.usuarioId = me?.user?.id || me?.id || me?.userId || null;
        }
        if (!dto.usuarioId) { alert('Não foi possível identificar o usuário. Faça login novamente.'); return; }
        if (tipo === 'gasto') {
            const created = await api.createGasto(dto, auth.email, auth.senha);
            document.getElementById('expenses-tbody').appendChild(createRow(created, 'gasto'));
        } else {
            const created = await api.createGanho(dto, auth.email, auth.senha);
            document.getElementById('expenses-tbody').appendChild(createRow(created, 'ganho'));
        }
        updateTotalsAndCount();
        closeAddModal();
    } catch (err) { console.error(err); alert('Erro ao salvar: '+err.message); }
}

// Table actions (edit/delete)
async function handleTableClick(ev) {
    const btn = ev.target.closest('button'); if (!btn) return;
    const row = btn.closest('tr'); const id = row.dataset.id; const type = row.dataset.type;
    const auth = getAuth(); if (!auth) return window.location.href = 'index.html';
    if (btn.classList.contains('delete-btn')) {
        if (!confirm('Tem certeza?')) return;
        try {
            if (type === 'gasto') await api.deleteGasto(id, auth.email, auth.senha); else await api.deleteGanho(id, auth.email, auth.senha);
            row.remove(); updateTotalsAndCount();
        } catch (err) { console.error(err); alert('Erro ao deletar'); }
    } else if (btn.classList.contains('edit-btn')) {
        // transformar em inputs
        const nameCell = row.querySelector('.col-name');
        const catCell = row.querySelector('.col-category');
        const valCell = row.querySelector('.col-value');
        const dateCell = row.querySelector('.col-date');
        const currVal = row.dataset.value;
        const dateParts = dateCell.textContent.split('/');
        const year = new Date().getFullYear();
        const dateForInput = `${year}-${dateParts[1]}-${dateParts[0]}`;
        nameCell.innerHTML = `<input value="${nameCell.textContent}" />`;
        catCell.innerHTML = `<input value="${catCell.textContent}" />`;
        valCell.innerHTML = `<input type="number" step="0.01" value="${currVal}" />`;
        dateCell.innerHTML = `<input type="date" value="${dateForInput}" />`;
        btn.textContent = '💾'; btn.classList.remove('edit-btn'); btn.classList.add('save-btn');
    } else if (btn.classList.contains('save-btn')) {
        // ler inputs e enviar
        const name = row.querySelector('.col-name input').value.trim();
        const categoria = row.querySelector('.col-category input').value.trim();
        const rawValor = row.querySelector('.col-value input').value;
        const valor = Number(rawValor);
        const data = row.querySelector('.col-date input').value;

        if (!name) { alert('Nome é obrigatório'); return; }
        if (!data) { alert('Data é obrigatória'); return; }
        if (!rawValor || isNaN(valor) || valor < 0.01) { alert('Valor inválido. Deve ser >= 0.01'); return; }

        const dto = { usuarioId: null, nome: name, categoria: categoria || null, valor: Number(valor.toFixed(2)), data };
        try {
            if (auth.userId) {
                dto.usuarioId = auth.userId;
            } else {
                const me = await api.fetchAuthUser(auth.email, auth.senha);
                dto.usuarioId = me?.user?.id || me?.id || me?.userId || null;
            }
            if (!dto.usuarioId) { alert('Não foi possível identificar o usuário. Faça login novamente.'); return; }
            let updated;
            if (type === 'gasto') updated = await api.updateGasto(id, dto, auth.email, auth.senha); else updated = await api.updateGanho(id, dto, auth.email, auth.senha);
            // atualizar linha
            row.querySelector('.col-name').textContent = updated.nome;
            row.querySelector('.col-category').textContent = updated.categoria;
            row.querySelector('.col-value').textContent = `${type==='ganho'? '+ ' : ''}${new Intl.NumberFormat('pt-BR', { minimumFractionDigits:2 }).format(Number(updated.valor))}`;
            const dt = new Date(updated.data).toLocaleDateString('pt-BR', { day:'2-digit', month:'2-digit' });
            row.querySelector('.col-date').textContent = dt;
            row.dataset.value = Number(updated.valor);
            updateTotalsAndCount();
        } catch (err) { console.error(err); alert('Erro ao atualizar: '+err.message); }
    }
}

function wireDashboardEvents() {
    document.getElementById('openModalBtn').addEventListener('click', openAddModal);
    document.getElementById('closeModalBtn').addEventListener('click', closeAddModal);
    document.getElementById('addExpenseForm').addEventListener('submit', handleAddSubmit);
    document.getElementById('expenses-tbody').addEventListener('click', handleTableClick);

    // Fechar modal de análise de gastos
    const closeAnalysisBtn = document.getElementById('closeAnalysisModalBtn');
    const analysisModal = document.getElementById('analysisModal');
    if (closeAnalysisBtn && analysisModal) {
        closeAnalysisBtn.addEventListener('click', () => analysisModal.classList.remove('show'));
        // fechar ao clicar na overlay (fora do modal-content)
        analysisModal.addEventListener('click', (ev) => { if (ev.target === analysisModal) analysisModal.classList.remove('show'); });
    }

    // Fechar modal de análise de ganhos
    const closeAnalysisGanhosBtn = document.getElementById('closeAnalysisGanhosModalBtn');
    const analysisGanhosModal = document.getElementById('analysisGanhosModal');
    if (closeAnalysisGanhosBtn && analysisGanhosModal) {
        closeAnalysisGanhosBtn.addEventListener('click', () => analysisGanhosModal.classList.remove('show'));
        analysisGanhosModal.addEventListener('click', (ev) => { if (ev.target === analysisGanhosModal) analysisGanhosModal.classList.remove('show'); });
    }

    const analysisCard = document.getElementById('analysis-card');
    if (analysisCard) {
        analysisCard.addEventListener('click', () => {
            const gastos = Array.from(document.querySelectorAll('tr[data-type="gasto"]')).map(r => ({ name: r.querySelector('.col-name').textContent, value: Number(r.dataset.value) }));
            const list = document.getElementById('analysis-list'); list.innerHTML = '';
            gastos.sort((a,b)=> b.value-a.value).forEach(g=>{ const li = document.createElement('li'); li.innerHTML = `<span class="item-name">${g.name}</span><span class="item-value">${formatCurrency(g.value)}</span>`; list.appendChild(li); });
            analysisModal?.classList.add('show');
        });
    }

    const analysisGanhosCard = document.getElementById('analysis-ganhos-card');
    if (analysisGanhosCard) {
        analysisGanhosCard.addEventListener('click', () => {
            const ganhos = Array.from(document.querySelectorAll('tr[data-type="ganho"]')).map(r => ({ name: r.querySelector('.col-name').textContent, value: Number(r.dataset.value) }));
            const list = document.getElementById('analysis-ganhos-list'); list.innerHTML = '';
            ganhos.sort((a,b)=> b.value-a.value).forEach(g=>{ const li = document.createElement('li'); li.innerHTML = `<span class="item-name">${g.name}</span><span class="item-value">${formatCurrency(g.value)}</span>`; list.appendChild(li); });
            analysisGanhosModal?.classList.add('show');
        });
    }

    const adminCard = document.getElementById('admin-card');
    if (adminCard) {
        adminCard.style.display = 'none'; // será mostrado após checar role
        adminCard.addEventListener('click', openAdminModal);
    }

    const exportBtn = document.getElementById('exportTransactionsBtn');
    if (exportBtn) exportBtn.addEventListener('click', exportTransactions);

    // admin modal handlers
    document.getElementById('admin-refresh')?.addEventListener('click', loadAdminUsers);
    document.getElementById('admin-users-tbody')?.addEventListener('click', adminTableClick);
    document.getElementById('admin-export-users')?.addEventListener('click', async () => { const auth = getAuth(); const users = await api.listUsuarios(auth.email, auth.senha); exportUsersCSV(users); });
    document.getElementById('closeAdminModalBtn')?.addEventListener('click', closeAdminModal);
}

// Router minimal
function init() {
    const path = window.location.pathname;
    if (path.endsWith('cadastro.html')) {
        document.getElementById('register-form').addEventListener('submit', handleRegisterForm);
    } else if (path.endsWith('index.html') || path === '/' ) {
        document.getElementById('login-form').addEventListener('submit', handleLoginForm);
    } else if (path.endsWith('dashboard.html')) {
        wireDashboardEvents();
        loadDashboard();
        // verificar role e mostrar card admin se necessário
        (async()=>{
            const auth = getAuth(); if (!auth) return;
            try {
                const me = await api.fetchAuthUser(auth.email, auth.senha);
                const role = me.user ? me.user.role : me.role || me.role;
                if (role === 'ADMIN') document.getElementById('admin-card').style.display = 'block';
            } catch (err) { console.error(err); }
        })();
    }
}

// Start
document.addEventListener('DOMContentLoaded', init);
