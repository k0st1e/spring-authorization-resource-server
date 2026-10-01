package dev.kostie.oauth2authorizationserver.entities;

public enum UserAuthority {
    // Currently, every registered user
    // is hardcoded with `USER` authority.
    USER,
    ADMIN,
    READ,
    WRITE,
    EDIT,
    DELETE,
}
