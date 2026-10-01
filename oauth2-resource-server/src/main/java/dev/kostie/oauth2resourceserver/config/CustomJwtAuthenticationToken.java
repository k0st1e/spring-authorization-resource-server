package dev.kostie.oauth2resourceserver.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

// By extending the `JwtAuthenticationToken` we can augment the Authentication Object.
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    // Add attributes as we find feat.
    private String customStatus;

    // We need at least a constructor.
    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        customStatus = "JWT placeholder text";
    }
}
