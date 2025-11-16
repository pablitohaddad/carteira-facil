package io.github.pablitohaddad.Carteira.Facil.service;

import io.github.pablitohaddad.Carteira.Facil.dto.GastoDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Gasto;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import io.github.pablitohaddad.Carteira.Facil.repository.GastoRepository;
import io.github.pablitohaddad.Carteira.Facil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Gasto criarGasto(GastoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Gasto novoGasto = new Gasto();
        novoGasto.setUsuario(usuario);
        novoGasto.setNome(dto.getNome());
        novoGasto.setCategoria(dto.getCategoria());
        novoGasto.setValor(dto.getValor());
        novoGasto.setData(dto.getData());

        return gastoRepository.save(novoGasto);
    }

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    // listar por usuario
    public List<Gasto> listarPorUsuario(Long usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Gasto> buscarPorId(Long id) {
        return gastoRepository.findById(id);
    }

    public Optional<Gasto> atualizarGasto(Long id, GastoDTO dto) {
        return gastoRepository.findById(id).map(gastoExistente -> {

            Usuario novoUsuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Novo Usuário não encontrado com ID: " + dto.getUsuarioId()));

            gastoExistente.setUsuario(novoUsuario);
            gastoExistente.setNome(dto.getNome());
            gastoExistente.setCategoria(dto.getCategoria());
            gastoExistente.setValor(dto.getValor());
            gastoExistente.setData(dto.getData());

            return gastoRepository.save(gastoExistente);
        });
    }

    public void deletarGasto(Long id) {
        gastoRepository.deleteById(id);
    }
}