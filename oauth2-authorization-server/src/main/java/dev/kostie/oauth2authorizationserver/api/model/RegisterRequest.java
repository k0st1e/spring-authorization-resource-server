package dev.kostie.oauth2authorizationserver.api.model;

public record RegisterRequest(
        String username,
        String password
) {
}
