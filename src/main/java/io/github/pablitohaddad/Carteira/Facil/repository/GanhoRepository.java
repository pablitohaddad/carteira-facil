package io.github.pablitohaddad.Carteira.Facil.repository;

import io.github.pablitohaddad.Carteira.Facil.model.Ganho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GanhoRepository extends JpaRepository<Ganho, Long> {
    List<Ganho> findByUsuarioId(Long usuarioId);
}