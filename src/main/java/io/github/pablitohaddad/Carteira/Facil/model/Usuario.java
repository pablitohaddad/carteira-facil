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

    // Nota: O campo 'email_valido' é uma CONSTRAINT CHECK no banco.
    // O Spring Boot deve respeitar isso, mas validações em nível de código (DTO/Service) são melhores.
}