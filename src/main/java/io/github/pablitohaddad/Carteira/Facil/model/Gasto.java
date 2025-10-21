package io.github.pablitohaddad.Carteira.Facil.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore; // Importação necessária

@Entity
@Table(name = "gastos")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // APLICANDO @JsonIgnore AQUI: Evita que o Jackson tente serializar o proxy do Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore // <-- CORREÇÃO
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;
}
