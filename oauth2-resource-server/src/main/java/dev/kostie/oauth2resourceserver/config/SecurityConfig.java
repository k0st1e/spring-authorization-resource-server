package dev.kostie.oauth2resourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                        // Add matchers for Tasks API end-points.
                        // .requestMatchers("").hasAuthority("USER")
                        .anyRequest().authenticated()
                )

                // Resource Server Configuration
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt((jwt) -> jwt
                                // Authorization Server JWK URI.
                                .jwkSetUri(jwksUri)
                                // Our custom Authentication Converter for the RS.
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationTokenConverter())
                        )
                );

        return http.build();
    }
}
