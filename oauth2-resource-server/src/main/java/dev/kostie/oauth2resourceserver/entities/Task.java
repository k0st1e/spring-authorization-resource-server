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

    @Column(name = "user_sub")
    private String userSub;

    @Builder
    public Task(int id, String title, String userSub) {
        this.id = id;
        this.title = title;
        this.userSub = userSub;
    }
}
