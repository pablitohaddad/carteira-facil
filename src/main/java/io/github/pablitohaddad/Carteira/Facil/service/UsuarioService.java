package io.github.pablitohaddad.Carteira.Facil.service;

import io.github.pablitohaddad.Carteira.Facil.dto.UsuarioCreateDTO;
import io.github.pablitohaddad.Carteira.Facil.dto.UsuarioUpdateDTO;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import io.github.pablitohaddad.Carteira.Facil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado"); // Lidar com exceções adequadamente
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenhaHash(dto.getSenha());

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {

            usuarioExistente.setNome(dto.getNome());

            if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
                usuarioExistente.setEmail(dto.getEmail());
            }

            if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
                usuarioExistente.setSenhaHash(dto.getNovaSenha());
            }

            return usuarioRepository.save(usuarioExistente);
        });
    }

    public void deletar(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Nao foi possivel encontrar esse usuario")
        );
        usuarioRepository.delete(usuario);
    }
}