package io.github.pablitohaddad.Carteira.Facil.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GanhoDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "O nome/descrição é obrigatório")
    private String nome;

    private String categoria;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;
}