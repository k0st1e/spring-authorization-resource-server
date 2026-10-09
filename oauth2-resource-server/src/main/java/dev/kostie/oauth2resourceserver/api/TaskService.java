package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.model.TaskRequest;
import dev.kostie.oauth2resourceserver.api.model.TaskResponse;
import dev.kostie.oauth2resourceserver.entities.Task;
import dev.kostie.oauth2resourceserver.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskRequest taskRequest) {

        if (taskRequest.title().isBlank()) {
            throw new InvalidTaskException();
        }

        Task task = taskMapper.toEntity(taskRequest);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }
}
