package io.github.pablitohaddad.Carteira.Facil;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pablitohaddad.Carteira.Facil.dto.GanhoDTO;
import io.github.pablitohaddad.Carteira.Facil.dto.GastoDTO;
import io.github.pablitohaddad.Carteira.Facil.dto.UsuarioCreateDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Configura o MockMvc para simular requisições HTTP
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Garante a ordem de execução
public class CarteiraFacilApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// Variável estática para armazenar o ID do usuário criado, permitindo que outros testes dependam dele.
	private static Long USUARIO_ID_TESTE;

	// --- 1. Teste de Cadastro de Usuário ---
	@Test
	@Order(1)
	void deveCadastrarUsuarioERetornarStatus201() throws Exception {
		UsuarioCreateDTO usuarioDTO = new UsuarioCreateDTO();
		usuarioDTO.setNome("Teste Integracao User");
		usuarioDTO.setEmail("integracao.teste@email.com");
		usuarioDTO.setSenha("teste123");

		MvcResult result = mockMvc.perform(post("/api/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(usuarioDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nome").value("Teste Integracao User"))
				.andReturn();

		// Extrai o ID do usuário criado da resposta para ser usado nos próximos testes
		String response = result.getResponse().getContentAsString();
		// O ID retornado é um Long
		USUARIO_ID_TESTE = objectMapper.readTree(response).get("id").asLong();
	}

	// --- 2. Teste de Cadastro de Ganho ---
	@Test
	@Order(2)
	void deveCadastrarGanhoERetornarStatus201() throws Exception {
		// Validação: Garante que o usuário foi criado no teste anterior
		if (USUARIO_ID_TESTE == null) {
			deveCadastrarUsuarioERetornarStatus201();
		}

		GanhoDTO ganhoDTO = new GanhoDTO();
		ganhoDTO.setUsuarioId(USUARIO_ID_TESTE); // Usa o ID criado
		ganhoDTO.setNome("Salario Teste");
		ganhoDTO.setCategoria("Renda");
		ganhoDTO.setValor(new BigDecimal("3000.00"));
		ganhoDTO.setData(LocalDate.now());

		mockMvc.perform(post("/api/ganhos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ganhoDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.valor").value(3000.00));
	}

	// --- 3. Teste de Cadastro de Gasto ---
	@Test
	@Order(3)
	void deveCadastrarGastoERetornarStatus201() throws Exception {
		// Validação: Garante que o usuário foi criado no teste anterior
		if (USUARIO_ID_TESTE == null) {
			deveCadastrarUsuarioERetornarStatus201();
		}

		GastoDTO gastoDTO = new GastoDTO();
		gastoDTO.setUsuarioId(USUARIO_ID_TESTE); // Usa o ID criado
		gastoDTO.setNome("Aluguel Teste");
		gastoDTO.setCategoria("Moradia");
		gastoDTO.setValor(new BigDecimal("1000.00"));
		gastoDTO.setData(LocalDate.now());

		mockMvc.perform(post("/api/gastos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(gastoDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.valor").value(1000.00));
	}

	// --- 4. Teste de Integração (Fluxo Completo/Renda Atual) ---
	@Test
	@Order(4)
	void deveConsultarRendaAtualEstarCorreta() throws Exception {
		if (USUARIO_ID_TESTE == null) {
			deveCadastrarUsuarioERetornarStatus201();
		}

		mockMvc.perform(get("/api/renda/usuario/" + USUARIO_ID_TESTE)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.rendaTotal").value(2000.00));
	}
}