package io.github.pablitohaddad.Carteira.Facil.repository;

import io.github.pablitohaddad.Carteira.Facil.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    java.util.List<Gasto> findByUsuarioId(Long usuarioId);
}