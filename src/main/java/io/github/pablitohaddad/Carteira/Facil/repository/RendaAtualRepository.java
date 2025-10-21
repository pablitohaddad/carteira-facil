package io.github.pablitohaddad.Carteira.Facil.repository;

import io.github.pablitohaddad.Carteira.Facil.model.RendaAtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RendaAtualRepository extends JpaRepository<RendaAtual, Long> {

    // Método para buscar a renda pelo ID do usuário (o que será mais comum)
    Optional<RendaAtual> findByUsuarioId(Long usuarioId);
}