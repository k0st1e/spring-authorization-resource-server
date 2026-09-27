package dev.kostie.oauth2authorizationserver.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    @Builder
    public User(String username, String password, UserAuthority authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}
