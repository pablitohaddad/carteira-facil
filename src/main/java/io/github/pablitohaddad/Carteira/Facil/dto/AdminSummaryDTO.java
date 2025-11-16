package io.github.pablitohaddad.Carteira.Facil.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminSummaryDTO {
    private long totalClients;
    private long freeCount;
    private long premiumCount;
    private List<UsuarioPublicDTO> clients;
}
