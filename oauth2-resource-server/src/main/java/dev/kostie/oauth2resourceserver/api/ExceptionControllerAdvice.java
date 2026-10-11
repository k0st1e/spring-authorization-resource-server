package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.exceptions.CreateTaskException;
import dev.kostie.oauth2resourceserver.api.exceptions.NoTasksFoundException;
import dev.kostie.oauth2resourceserver.api.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception controller advice for adding, viewing, editing and deleting tasks.
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(CreateTaskException.class)
    public ResponseEntity<ErrorDetails> invalidTaskException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Task Creation Error");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(NoTasksFoundException.class)
    public ResponseEntity<ErrorDetails> noTasksFoundException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("No Tasks Found");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }
}
