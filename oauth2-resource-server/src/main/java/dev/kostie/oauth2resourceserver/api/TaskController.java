package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.model.TaskRequest;
import dev.kostie.oauth2resourceserver.api.model.TaskResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.fetchAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody TaskRequest taskRequest) {

        TaskResponse taskResponse = taskService.createTask(taskRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskResponse);
    }
}