package io.github.pablitohaddad.Carteira.Facil.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "usuarios")
@Data // Gera Getters, Setters, toString, equals e hashCode (requer Lombok)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    // NOVO: Campo para o Papel de Segurança (RBAC)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    // NOVO: Campo para o Tipo de Assinatura
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 50)
    private SubscriptionType subscriptionType;
}