package com.leonardo.controlefinanceiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // Mantém a autenticação realizada pelos controllers.
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // Mantém a proteção contra requisições indevidas.
            .csrf(csrf -> {})

            // Desativa o formulário padrão do Spring Security.
            .formLogin(form -> form.disable())

            // Desativa a autenticação HTTP Basic.
            .httpBasic(basic -> basic.disable())

            // Mantém o logout personalizado do sistema.
            .logout(logout -> logout.disable());

        return http.build();
    }
}