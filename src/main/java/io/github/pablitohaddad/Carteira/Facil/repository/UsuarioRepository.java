package io.github.pablitohaddad.Carteira.Facil.repository;

import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Object> findByEmail(String email);
}