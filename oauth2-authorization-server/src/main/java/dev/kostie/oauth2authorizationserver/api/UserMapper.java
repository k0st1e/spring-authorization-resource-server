package dev.kostie.oauth2authorizationserver.api;

import dev.kostie.oauth2authorizationserver.api.model.RegisterRequest;
import dev.kostie.oauth2authorizationserver.api.model.RegisterResponse;
import dev.kostie.oauth2authorizationserver.entities.User;
import dev.kostie.oauth2authorizationserver.entities.UserAuthority;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // `RegisterRequest` DTO to `User` Entity.
    // Hard-coded with an Authority of `READ`.
    public User toEntity(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.username())
                .password(bCryptPasswordEncoder.encode(registerRequest.password()))
                .authority(UserAuthority.USER)
                .build();
    }

    // `User` to `RegisterResponse` DTO.
    // Returns only the `username` field.
    public RegisterResponse toResponse(User user) {
        return RegisterResponse.builder()
                .username(user.getUsername())
                .build();
    }
}
