package dev.kostie.oauth2authorizationserver.api;

import dev.kostie.oauth2authorizationserver.api.model.ErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    // Exception handler associated with `InvalidRequirementsException`.
    // This exception can occur in the class `UserService`.
    @ExceptionHandler(InvalidRequirementsException.class)
    public ResponseEntity<ErrorDetails> invalidRequirements() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Invalid requirements");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
