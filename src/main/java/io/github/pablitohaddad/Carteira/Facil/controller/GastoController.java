package io.github.pablitohaddad.Carteira.Facil.controller;

import io.github.pablitohaddad.Carteira.Facil.dto.GastoDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Gasto;
import io.github.pablitohaddad.Carteira.Facil.service.GastoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/gastos")
@Tag(name = "Gastos", description = "CRUD para gestão de despesas")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @Operation(summary = "Cria um novo registro de gasto")
    @PostMapping
    public ResponseEntity<Gasto> criar(@Valid @RequestBody GastoDTO dto) {
        Gasto novoGasto = gastoService.criarGasto(dto);
        return new ResponseEntity<>(novoGasto, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todos os gastos registrados")
    @GetMapping
    public ResponseEntity<List<Gasto>> listar() {
        return ResponseEntity.ok(gastoService.listarTodos());
    }

    @Operation(summary = "Lista gastos de um usuário")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Gasto>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(gastoService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Busca um gasto pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Gasto> buscarPorId(@PathVariable Long id) {
        return gastoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza um registro de gasto existente")
    @PutMapping("/{id}")
    public ResponseEntity<Gasto> atualizar(@PathVariable Long id, @Valid @RequestBody GastoDTO dto) {
        return gastoService.atualizarGasto(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um registro de gasto pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            gastoService.deletarGasto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}