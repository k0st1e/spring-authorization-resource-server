package dev.kostie.oauth2resourceserver.api.model;

import lombok.Builder;

@Builder
public record TaskResponse(
        int id,
        String title
) {
}
