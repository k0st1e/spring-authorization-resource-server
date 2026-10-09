package dev.kostie.oauth2resourceserver.repositories;

import dev.kostie.oauth2resourceserver.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}