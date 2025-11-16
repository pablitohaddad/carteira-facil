package io.github.pablitohaddad.Carteira.Facil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            .cors(withDefaults()) // habilita CORS usando o CorsConfigurationSource definido abaixo
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // permitir preflight OPTIONS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Permitir o registro público
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                // Permitir o acesso à documentação
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Permitir páginas e ativos estáticos (login/registro)
                .requestMatchers("/", "/index.html", "/cadastro.html", "/recuperar-senha.html", "/nova-senha.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // Demais requisições precisam de autenticação
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()); // usa Customizer para compatibilidade com versões mais recentes

        return http.build();
    }

    // Cors configuration que permite o Live Server em 127.0.0.1:5500 e autoriza o header Authorization
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Em desenvolvimento permitimos padrões amplos; em produção restrinja os origins.
        configuration.setAllowedOriginPatterns(List.of("http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:8080", "http://127.0.0.1:8080", "*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
