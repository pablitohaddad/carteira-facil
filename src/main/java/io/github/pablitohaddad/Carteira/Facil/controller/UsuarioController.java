package io.github.pablitohaddad.Carteira.Facil.controller;

import io.github.pablitohaddad.Carteira.Facil.dto.UsuarioCreateDTO;
import io.github.pablitohaddad.Carteira.Facil.dto.UsuarioUpdateDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import io.github.pablitohaddad.Carteira.Facil.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "CRUD para gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioCreateDTO dto) {
        Usuario novoUsuario = usuarioService.criarUsuario(dto);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(summary = "Busca um usuário pelo ID")
    @GetMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @Operation(summary = "Atualiza um usuário existente pelo ID")
        @PutMapping("/{id}")
        @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
        public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {

        return usuarioService.atualizarUsuario(id, dto)
            .map(usuarioAtualizado -> new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK))
            .orElse(ResponseEntity.notFound().build());
        }

    // Exemplo de DELETE
    @Operation(summary = "Deleta um usuário pelo ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para retornar dados da conta do usuário autenticado
    @Operation(summary = "Informações do usuário autenticado / área do usuário")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> meuPerfil() {
        Long authId = io.github.pablitohaddad.Carteira.Facil.util.AuthUtils.getAuthenticatedUserId();
        io.github.pablitohaddad.Carteira.Facil.model.Role role = io.github.pablitohaddad.Carteira.Facil.util.AuthUtils.getAuthenticatedUserRole();

        if (role == io.github.pablitohaddad.Carteira.Facil.model.Role.ADMIN) {
            // Admin vê o resumo administrativo
            var resumo = usuarioService.obterResumoAdmin();
            return ResponseEntity.ok(resumo);
        }

        // Usuário comum vê seus dados públicos e recursos conforme assinatura
        var usuarioOpt = usuarioService.buscarPorId(authId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var u = usuarioOpt.get();
        var publicDto = new io.github.pablitohaddad.Carteira.Facil.dto.UsuarioPublicDTO();
        publicDto.setId(u.getId());
        publicDto.setNome(u.getNome());
        publicDto.setEmail(u.getEmail());
        publicDto.setRole(u.getRole());
        publicDto.setSubscriptionType(u.getSubscriptionType());

        // Monta payload com recursos diferenciados por assinatura
        var payload = new java.util.HashMap<String, Object>();
        payload.put("user", publicDto);

        if (u.getSubscriptionType() == io.github.pablitohaddad.Carteira.Facil.model.SubscriptionType.PREMIUM) {
            payload.put("resources", java.util.List.of("advanced-reports", "priority-support"));
        } else {
            payload.put("resources", java.util.List.of("basic-reports"));
        }

        return ResponseEntity.ok(payload);
    }
}