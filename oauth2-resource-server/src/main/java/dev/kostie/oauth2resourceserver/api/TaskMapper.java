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

        return Task.builder()
                .title(taskRequest.title())
                .user_sub(authentication.getName())
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .build();
    }
}
