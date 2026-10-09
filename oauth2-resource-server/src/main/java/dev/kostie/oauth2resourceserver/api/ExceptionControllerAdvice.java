package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.model.ErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<ErrorDetails> invalidTaskException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Task Error");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
