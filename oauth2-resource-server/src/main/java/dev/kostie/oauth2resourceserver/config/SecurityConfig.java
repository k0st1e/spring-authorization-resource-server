package dev.kostie.oauth2resourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // URI of Authorization Server's JWK Endpoint
    @Value("${jwksUri}")
    private String jwksUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http

                .authorizeHttpRequests((authorize) -> authorize
                        // For Tasks API end-points.
                        .requestMatchers("/api/v1/tasks/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )

                // Resource Server Configuration
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt((jwt) -> jwt

                                // Authorization Server JWK URI - Commented out see bean below.
                                // .jwkSetUri(jwksUri)

                                // Our custom Authentication Converter for the RS.
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationTokenConverter())
                        )
                );

        return http.build();
    }

    // `JwtDecoder` as a `@Bean` so we can mock it with `Testcontainers`.
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
}
