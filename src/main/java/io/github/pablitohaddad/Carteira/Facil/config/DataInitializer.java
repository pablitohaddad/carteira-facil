package io.github.pablitohaddad.Carteira.Facil.config;

import io.github.pablitohaddad.Carteira.Facil.model.Role;
import io.github.pablitohaddad.Carteira.Facil.model.SubscriptionType;
import io.github.pablitohaddad.Carteira.Facil.model.Usuario;
import io.github.pablitohaddad.Carteira.Facil.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@exemplo.com");
                admin.setSenhaHash(passwordEncoder.encode("adminpass"));
                admin.setRole(Role.ADMIN);
                admin.setSubscriptionType(SubscriptionType.FREE);
                usuarioRepository.save(admin);

                Usuario clienteFree = new Usuario();
                clienteFree.setNome("Cliente Free");
                clienteFree.setEmail("cliente_free@exemplo.com");
                clienteFree.setSenhaHash(passwordEncoder.encode("clientepass"));
                clienteFree.setRole(Role.CLIENT);
                clienteFree.setSubscriptionType(SubscriptionType.FREE);
                usuarioRepository.save(clienteFree);

                Usuario clientePremium = new Usuario();
                clientePremium.setNome("Cliente Premium");
                clientePremium.setEmail("cliente_premium@exemplo.com");
                clientePremium.setSenhaHash(passwordEncoder.encode("clientepass2"));
                clientePremium.setRole(Role.CLIENT);
                clientePremium.setSubscriptionType(SubscriptionType.PREMIUM);
                usuarioRepository.save(clientePremium);
            }
        };
    }
}
