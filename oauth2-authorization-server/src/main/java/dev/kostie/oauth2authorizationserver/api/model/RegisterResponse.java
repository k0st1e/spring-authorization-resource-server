package dev.kostie.oauth2authorizationserver.api.model;

import lombok.Builder;

@Builder
public record RegisterResponse(
        String username
) {
}
