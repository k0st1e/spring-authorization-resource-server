package dev.kostie.oauth2authorizationserver.api;

import dev.kostie.oauth2authorizationserver.api.model.RegisterRequest;
import dev.kostie.oauth2authorizationserver.api.model.RegisterResponse;
import dev.kostie.oauth2authorizationserver.entities.User;
import dev.kostie.oauth2authorizationserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {

        if (registerRequest.username().isBlank()
                || registerRequest.password().isBlank()) {
            // Simple checking, can be expanded. Jakarta is also an alternative.
            // See the exception handler -> `ExceptionControllerAdvice`.
            throw new InvalidRequirementsException();
        }

        // Map the request to an Entity with mapper.
        User user = userMapper.toEntity(registerRequest);

        // Save the user.
        User userSaved = userRepository.save(user);

        // With the mapper return using `toResponse` -> `RegisterResponse`.
        return userMapper.toResponse(userSaved);
    }
}
