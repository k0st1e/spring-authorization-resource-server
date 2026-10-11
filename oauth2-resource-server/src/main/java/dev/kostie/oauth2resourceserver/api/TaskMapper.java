package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.model.TaskRequest;
import dev.kostie.oauth2resourceserver.api.model.TaskResponse;
import dev.kostie.oauth2resourceserver.entities.Task;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest taskRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Build a `Task` Entity and use the `sub` from the auth object which was converted from the JWT.
        return Task.builder()
                .title(taskRequest.title())
                .userSub(authentication.getName())
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .build();
    }
}
