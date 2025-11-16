// Backend API base. Se o frontend estiver sendo servido por outro origin (ex: Live Server em 127.0.0.1:5500),
// sobrescreva definindo window.__API_BASE__ antes de carregar o módulo (ex.: window.__API_BASE__ = 'http://localhost:8080/api')
const DEFAULT_API = 'http://localhost:8080/api';
const API_BASE = window.__API_BASE__ || DEFAULT_API;

// Utilitário para header com Basic Auth — o frontend usará credenciais inseridas pelo usuário (email:senha)
function basicAuthHeader(email, senha) {
    return 'Basic ' + btoa(`${email}:${senha}`);
}

export async function registerUser(nome, email, senha, options = {}) {
    // permitir sobrescrever role/subscriptionType quando necessário (ex: ADMIN, PREMIUM)
    const role = options.role || 'CLIENT';
    const subscriptionType = options.subscriptionType || 'FREE';
    const payload = { nome, email, senha, role, subscriptionType };
    const res = await fetch(`${API_BASE}/usuarios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
        mode: 'cors'
    });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao registrar: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function fetchAuthUser(email, senha) {
    const res = await fetch(`${API_BASE}/usuarios/me`, {
        headers: { 'Authorization': basicAuthHeader(email, senha) },
        mode: 'cors'
    });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao obter usuário: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function listGastos(email, senha) {
    const res = await fetch(`${API_BASE}/gastos`, { headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao listar gastos: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function createGasto(dto, email, senha) {
    const res = await fetch(`${API_BASE}/gastos`, { method: 'POST', headers: { 'Content-Type':'application/json', 'Authorization': basicAuthHeader(email, senha) }, body: JSON.stringify(dto), mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao criar gasto: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function updateGasto(id, dto, email, senha) {
    const res = await fetch(`${API_BASE}/gastos/${id}`, { method: 'PUT', headers: { 'Content-Type':'application/json', 'Authorization': basicAuthHeader(email, senha) }, body: JSON.stringify(dto), mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao atualizar gasto: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function deleteGasto(id, email, senha) {
    const res = await fetch(`${API_BASE}/gastos/${id}`, { method: 'DELETE', headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (res.status !== 204) {
        const txt = await res.text();
        throw new Error('Falha ao deletar gasto: ' + res.status + ' - ' + txt);
    }
    return true;
}

export async function listGanhos(email, senha) {
    const res = await fetch(`${API_BASE}/ganhos`, { headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao listar ganhos: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function createGanho(dto, email, senha) {
    const res = await fetch(`${API_BASE}/ganhos`, { method: 'POST', headers: { 'Content-Type':'application/json', 'Authorization': basicAuthHeader(email, senha) }, body: JSON.stringify(dto), mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao criar ganho: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function updateGanho(id, dto, email, senha) {
    const res = await fetch(`${API_BASE}/ganhos/${id}`, { method: 'PUT', headers: { 'Content-Type':'application/json', 'Authorization': basicAuthHeader(email, senha) }, body: JSON.stringify(dto), mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao atualizar ganho: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function deleteGanho(id, email, senha) {
    const res = await fetch(`${API_BASE}/ganhos/${id}`, { method: 'DELETE', headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (res.status !== 204) {
        const txt = await res.text();
        throw new Error('Falha ao deletar ganho: ' + res.status + ' - ' + txt);
    }
    return true;
}

export async function listUsuarios(email, senha) {
    const res = await fetch(`${API_BASE}/usuarios`, { headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao listar usuários: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function updateUsuario(id, dto, email, senha) {
    const res = await fetch(`${API_BASE}/usuarios/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': basicAuthHeader(email, senha) },
        body: JSON.stringify(dto),
        mode: 'cors'
    });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao atualizar usuário: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function deleteUsuario(id, email, senha) {
    const res = await fetch(`${API_BASE}/usuarios/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': basicAuthHeader(email, senha) },
        mode: 'cors'
    });
    if (res.status !== 204 && !res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao deletar usuário: ' + res.status + ' - ' + txt);
    }
    return true;
}

export async function listGastosByUser(usuarioId, email, senha) {
    const res = await fetch(`${API_BASE}/gastos/usuario/${usuarioId}`, { headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao listar gastos do usuário: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export async function listGanhosByUser(usuarioId, email, senha) {
    const res = await fetch(`${API_BASE}/ganhos/usuario/${usuarioId}`, { headers: { 'Authorization': basicAuthHeader(email, senha) }, mode: 'cors' });
    if (!res.ok) {
        const txt = await res.text();
        throw new Error('Falha ao listar ganhos do usuário: ' + res.status + ' - ' + txt);
    }
    return res.json();
}

export default {
    registerUser, fetchAuthUser, listGastos, createGasto, updateGasto, deleteGasto,
    listGanhos, createGanho, updateGanho, deleteGanho,
    listUsuarios, updateUsuario, deleteUsuario,
    listGastosByUser, listGanhosByUser
};
