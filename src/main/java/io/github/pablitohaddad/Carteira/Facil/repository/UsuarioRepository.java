package io.github.pablitohaddad.Carteira.Facil.repository;

import io.github.pablitohaddad.Carteira.Facil.model.Role;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    // NOVO: Encontra todos os usuários que não são ADMIN, filtrando por Role.CLIENT
    List<Usuario> findAllByRole(Role role);
}