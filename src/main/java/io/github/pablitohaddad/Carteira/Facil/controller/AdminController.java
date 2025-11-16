package io.github.pablitohaddad.Carteira.Facil.controller;

import io.github.pablitohaddad.Carteira.Facil.dto.AdminSummaryDTO;
import io.github.pablitohaddad.Carteira.Facil.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Endpoints administrativos")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Resumo de usuários (apenas admin)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users-summary")
    public ResponseEntity<AdminSummaryDTO> usersSummary() {
        // Checa autoridade via utilitário (simples RBAC de exemplo)
        AdminSummaryDTO resumo = usuarioService.obterResumoAdmin();
        return ResponseEntity.ok(resumo);
    }
}
