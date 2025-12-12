package org.example.smartguard.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthDummyConfig {
// Poziționează această metodă în clasa ta @Configuration, de ex., AuthDummyConfig

// ... în interiorul clasei @Configuration ...

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Folosim BCryptPasswordEncoder, standardul industrial recomandat.
        return new BCryptPasswordEncoder();
    }
    // Acesta este SecurityFilterChain care permite toate cererile (permitAll)
    // și dezactivează Basic Auth și CSRF.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Dezactivează CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // PERMITE ACCESUL LA ORICE CALE
                );

        // Asigură-te că nu există alte filtre active care ar bloca cererea
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}