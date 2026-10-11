package dev.kostie.oauth2resourceserver.repositories;

import dev.kostie.oauth2resourceserver.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByUserSub(String userSub);
    Optional<Task> findByIdAndUserSub(int id, String userSub);
}