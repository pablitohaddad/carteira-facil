package io.github.pablitohaddad.Carteira.Facil.dto;

import io.github.pablitohaddad.Carteira.Facil.model.Role;
import io.github.pablitohaddad.Carteira.Facil.model.SubscriptionType;
import lombok.Data;

@Data
public class UsuarioPublicDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
    private SubscriptionType subscriptionType;
}
