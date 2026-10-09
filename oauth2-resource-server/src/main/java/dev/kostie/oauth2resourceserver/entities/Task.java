package dev.kostie.oauth2resourceserver.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String user_sub;

    @Builder
    public Task(int id, String title, String user_sub) {
        this.id = id;
        this.title = title;
        this.user_sub = user_sub;
    }
}
