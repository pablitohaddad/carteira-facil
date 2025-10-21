package io.github.pablitohaddad.Carteira.Facil.controller;

import io.github.pablitohaddad.Carteira.Facil.model.RendaAtual;
import io.github.pablitohaddad.Carteira.Facil.service.RendaAtualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/renda")
@Tag(name = "Renda Atual", description = "Consulta do saldo consolidado de um usuário")
public class RendaAtualController {

    @Autowired
    private RendaAtualService rendaAtualService;

    @Operation(summary = "Busca a renda atual (saldo) de um usuário pelo seu ID")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<RendaAtual> buscarRendaPorUsuarioId(@PathVariable Long usuarioId) {
        return rendaAtualService.buscarRendaPorUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Não há endpoints POST, PUT ou DELETE, pois o saldo é gerenciado pelas triggers do banco.
}