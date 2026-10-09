package dev.kostie.oauth2authorizationserver.api;

import dev.kostie.oauth2authorizationserver.api.model.RegisterRequest;
import dev.kostie.oauth2authorizationserver.api.model.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @RequestBody RegisterRequest registerRequest) {

        RegisterResponse registerResponse = userService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerResponse);
    }
}
