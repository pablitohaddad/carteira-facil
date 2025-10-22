package io.github.pablitohaddad.Carteira.Facil.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore; // Importação necessária

@Entity
@Table(name = "renda_atual")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RendaAtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "renda_total", precision = 12, scale = 2)
    private BigDecimal rendaTotal;
}
