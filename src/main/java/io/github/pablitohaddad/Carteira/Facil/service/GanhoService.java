package io.github.pablitohaddad.Carteira.Facil.service;

import io.github.pablitohaddad.Carteira.Facil.dto.GanhoDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Ganho;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import io.github.pablitohaddad.Carteira.Facil.repository.GanhoRepository;
import io.github.pablitohaddad.Carteira.Facil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class GanhoService {

    @Autowired
    private GanhoRepository ganhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar o usuário

    public Ganho criarGanho(GanhoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Ganho novoGanho = new Ganho();
        novoGanho.setUsuario(usuario);
        novoGanho.setNome(dto.getNome());
        novoGanho.setCategoria(dto.getCategoria());
        novoGanho.setValor(dto.getValor());
        novoGanho.setData(dto.getData());

        return ganhoRepository.save(novoGanho);
    }

    public List<Ganho> listarTodos() {
        return ganhoRepository.findAll();
    }

    // listar por usuario
    public List<Ganho> listarPorUsuario(Long usuarioId) {
        return ganhoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Ganho> buscarPorId(Long id) {
        return ganhoRepository.findById(id);
    }

    public Optional<Ganho> atualizarGanho(Long id, GanhoDTO dto) {
        return ganhoRepository.findById(id).map(ganhoExistente -> {

            // Note que o usuario_id só pode ser alterado se for um novo usuário válido
            Usuario novoUsuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Novo Usuário não encontrado com ID: " + dto.getUsuarioId()));

            ganhoExistente.setUsuario(novoUsuario);
            ganhoExistente.setNome(dto.getNome());
            ganhoExistente.setCategoria(dto.getCategoria());
            ganhoExistente.setValor(dto.getValor());
            ganhoExistente.setData(dto.getData());

            return ganhoRepository.save(ganhoExistente);
        });
    }

    public void deletarGanho(Long id) {
        ganhoRepository.deleteById(id);
    }
}