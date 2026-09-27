package dev.kostie.oauth2authorizationserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    // Filter for the protocol endpoints for the Authorization Server.
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) {

        http
                // `securityMatcher` trims down the scope to a specific thing.
                // In this case it is for the Authorization Server Endpoints.
                .oauth2AuthorizationServer((authorizationServer) -> {
                    http.securityMatcher(authorizationServer.getEndpointsMatcher());
                    authorizationServer.oidc(Customizer.withDefaults());
                })

                // As such, the following:
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )

                // And, for the AS endpoints, the 1st request redirects to `/login`.
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                            new LoginUrlAuthenticationEntryPoint("/login"),
                            new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    // Authn and Authz for other security mechanisms.
    // Note it is of `Order(2)`. We do not want this to
    // run through everything before the `securityMatcher`
    // for the AS endpoints.
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) {

        // Default form login by Spring.
        http.formLogin(Customizer.withDefaults());


        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/register").permitAll()
                .anyRequest().authenticated()
        );

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/register")
        );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // JSON Web Key (JWK) source. Exposes a method for retrieving JWKs matching a specified selector.
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // `RSAKey`: Public and private RSA JSON Web Key (JWK)
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        // JSON object that contains an array of JSON Web Keys (JWKs).
        JWKSet jwkSet = new JWKSet(rsaKey);
        // Creates a new JWK source backed by an immutable JWK set.
        return new ImmutableJWKSet<>(jwkSet);
    }

    // Generate and have a simple in-memory holder for a key pair.
    // In prod, we could consume them from a vault implementation along with some nice key rotation.
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }

        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    // A facility for Authorization Server configuration settings.
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }

    // Adding Authorities to the JWT as Custom Claims.
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

                var authorities = context.getPrincipal().getAuthorities();

                // We are taking objects of `GrantedAuthority` and we make a list of strings here.
                context
                        .getClaims()
                        .claim("authorities",
                                // Stream the authorities.
                                authorities.stream()
                                        // Get each string from every `GrantedAuthority` object.
                                        .map(GrantedAuthority::getAuthority)
                                        // Put them into a list.
                                        .toList());
            }
        };
    }

}