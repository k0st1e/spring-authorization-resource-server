package dev.kostie.oauth2resourceserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public CustomJwtAuthenticationToken convert(Jwt source) {

        // Cats `"authorities"` claim into a `List<String>`.
        List<String> authorities = (List<String>) source.getClaims().get("authorities");

        // Create the `authenticationObject` with the `source`, and the `authorities`.
        CustomJwtAuthenticationToken authenticationObject = new  CustomJwtAuthenticationToken(
                source,
                authorities
                        // Stream the JWT claim and make them `SimpleGrantedAuthority`.
                        .stream()
                        // Map each element into a new `SimpleGrantedAuthority` object.
                        .map(SimpleGrantedAuthority::new)
                        // Get the list of the converted stuff.
                        .toList()
        );

        return authenticationObject;

    }

}