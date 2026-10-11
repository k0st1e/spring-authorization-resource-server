package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.exceptions.CreateTaskException;
import dev.kostie.oauth2resourceserver.api.exceptions.NoTasksFoundException;
import dev.kostie.oauth2resourceserver.api.model.TaskRequest;
import dev.kostie.oauth2resourceserver.api.model.TaskResponse;
import dev.kostie.oauth2resourceserver.entities.Task;
import dev.kostie.oauth2resourceserver.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    // Get the current user sub from the Authentication Object.
    private String currentUserSubject() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Fetch all Tasks
    public List<TaskResponse> fetchAllTasks() {

        List<TaskResponse> tasks = taskRepository.findAllByUserSub(currentUserSubject())
                .stream()
                .map(taskMapper::toResponse)
                .toList();

        if (tasks.isEmpty()) {
            throw new NoTasksFoundException();
        }

        return tasks;
    }

    // Create Task
    public TaskResponse createTask(TaskRequest taskRequest) {

        if (taskRequest.title().isBlank()) {
            throw new CreateTaskException();
        }

        Task task = taskMapper.toEntity(taskRequest);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    // Delete Task
    public void deleteTask(int id) {
        Task task = taskRepository.findByIdAndUserSub(id, currentUserSubject())
                .orElseThrow(() -> new NoTasksFoundException());
        taskRepository.delete(task);
    }

    // Update Task
    public TaskResponse updateTask(int id, TaskRequest taskRequest) {
        Task task = taskRepository.findByIdAndUserSub(id, currentUserSubject())
                .orElseThrow(() -> new NoTasksFoundException());

        task.setTitle(taskRequest.title());

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }
}
