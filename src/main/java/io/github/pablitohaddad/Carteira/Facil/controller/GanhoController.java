package io.github.pablitohaddad.Carteira.Facil.controller;

import io.github.pablitohaddad.Carteira.Facil.dto.GanhoDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Ganho;
import io.github.pablitohaddad.Carteira.Facil.service.GanhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/ganhos")
@Tag(name = "Ganhos", description = "CRUD para gestão de receitas")
public class GanhoController {

    @Autowired
    private GanhoService ganhoService;

    @Operation(summary = "Cria um novo registro de ganho")
    @PostMapping
    public ResponseEntity<Ganho> criar(@Valid @RequestBody GanhoDTO dto) {
        Ganho novoGanho = ganhoService.criarGanho(dto);
        return new ResponseEntity<>(novoGanho, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todos os ganhos registrados")
    @GetMapping
    public ResponseEntity<List<Ganho>> listar() {
        return ResponseEntity.ok(ganhoService.listarTodos());
    }

    @Operation(summary = "Lista ganhos de um usuário")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ganho>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ganhoService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Busca um ganho pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Ganho> buscarPorId(@PathVariable Long id) {
        return ganhoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza um registro de ganho existente")
    @PutMapping("/{id}")
    public ResponseEntity<Ganho> atualizar(@PathVariable Long id, @Valid @RequestBody GanhoDTO dto) {
        return ganhoService.atualizarGanho(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um registro de ganho pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            ganhoService.deletarGanho(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // ou um tratamento de erro mais específico
        }
    }
}