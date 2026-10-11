package dev.kostie.oauth2authorizationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String client_id;

    private String scope;

    private String auth_method;

    private String grant_type;

    private String redirect_uri;

    // Returns a `Client` from a `RegisteredClient`
    public static Client from(RegisteredClient registeredClient) {

        Client client = new Client();

        client.setClient_id(registeredClient.getClientId());

        client.setRedirect_uri(
                registeredClient.getRedirectUris().stream().findAny().get()
        );

        client.setScope(
                registeredClient.getScopes().stream().findAny().get()
        );

        client.setAuth_method(
                registeredClient.getClientAuthenticationMethods().stream().findAny().get().getValue()
        );

        client.setGrant_type(
                registeredClient.getAuthorizationGrantTypes().stream().findAny().get().getValue()
        );

        return client;
    }

    // Returns a `RegisteredClient` from a `Client`.
    public static RegisteredClient from(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))

                .clientId(client.getClient_id())

                .scope(client.getScope())

                .redirectUri(client.getRedirect_uri())

                .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuth_method()))

                .authorizationGrantType(new AuthorizationGrantType(client.getGrant_type()))

                .tokenSettings(TokenSettings.builder()

                        // Uncomment to work with Opaque Tokens:
                        // .accessTokenFormat(OAuth2TokenFormat.REFERENCE)

                        // Production token time to live shouldn't be long.
                        .accessTokenTimeToLive(Duration.ofMinutes(5))
                        .build())

                .build();
    }
}