package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.exceptions.CreateTaskException;
import dev.kostie.oauth2resourceserver.api.exceptions.NoTaskFoundException;
import dev.kostie.oauth2resourceserver.api.exceptions.NoTasksFoundException;
import dev.kostie.oauth2resourceserver.api.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception controller advice for adding, viewing, editing and deleting tasks.
@RestControllerAdvice
public class ExceptionControllerAdvice {

    // Exception when adding a new task.
    @ExceptionHandler(CreateTaskException.class)
    public ResponseEntity<ErrorDetails> invalidTaskException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Task creation error");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    // Exception when loading the tasks for a user.
    @ExceptionHandler(NoTasksFoundException.class)
    public ResponseEntity<ErrorDetails> noTasksFoundException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("No tasks found to load");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }

    // Exception when a user deletes or edits a task.
    @ExceptionHandler(NoTaskFoundException.class)
    public ResponseEntity<ErrorDetails> noTaskFoundException() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("No task found to delete or edit");
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetails);
    }
}
