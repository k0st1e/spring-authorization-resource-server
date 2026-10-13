package dev.kostie.oauth2resourceserver.api;

import dev.kostie.oauth2resourceserver.api.model.TaskRequest;
import dev.kostie.oauth2resourceserver.entities.Task;
import dev.kostie.oauth2resourceserver.repositories.TaskRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.mysql.MySQLContainer;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

// Full App Ctx with Test App Properties.
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;

    // One (1) container for many tests.
    static MySQLContainer mysql = new MySQLContainer("mysql:8.0.36");

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }
    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    // Instead of app properties, do this:
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    TaskRepository taskRepository;

    // Mock the JWT Converter
    @MockitoBean
    JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost:" + port;

        // Needed since if we start another test old data must be purged.
        taskRepository.deleteAll();

        // Default token has `USER`, requests are authorized.
        Jwt jwt = Jwt
                .withTokenValue("testing-token")
                // Not signed.
                .header("alg", "none")
                // `testing-user` will live in the database in the `userSub` column.
                .claim("sub", "testing-user")
                // `USER` authority claim.
                .claim("authorities", List.of("USER"))
                .build();

        // Stubbing method, when `decode` is called return the `jwt` above.
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

    }

    @Test
    void fetchAllTasks_withNoTasks_returns404() {
        given()
                .header("Authorization", "Bearer testing-token")

                .when().get("/api/v1/tasks")

                .then()
                    .statusCode(404)
                    .body("message", equalTo("No tasks found to load"));
    }

    @Test
    void fetchAllTasks_withTasks_returns200() {

        taskRepository.save(Task.builder()
                        .title("Get Bread!")
                        // Same sub as in the `@BeforeEach`.
                        .userSub("testing-user")
                        .build());

        taskRepository.save(Task.builder()
                        .title("Get milk.")
                        // Same sub as in the `@BeforeEach`.
                        .userSub("testing-user")
                        .build());

        given()

                .header("Authorization", "Bearer testing-token")

                .when().get("/api/v1/tasks")

                .then()
                    .statusCode(200)
                    .body("size()", equalTo(2))
                    .body("title", hasItems("Get Bread!", "Get milk."));
    }

    @Test
    void fetchAllTasks_withInvalidTokenAuthority_returns403() {

        Jwt jwt = Jwt
                .withTokenValue("testing-token")
                .header("alg", "none")
                .claim("sub", "testing-user")
                // Jwt with `NOT_USER` Claim.
                .claim("authorities", List.of("NOT_USER"))
                .build();

        // Override the `USER` default token from `@BeforeEach`.
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        given()
                .header("Authorization", "Bearer testing-token")

                .when().get("/api/v1/tasks")

                .then()
                    .statusCode(403);
    }

    @Test
    void fetchAllTasks_withNoToken_returns401() {
        given()
                .when().get("/api/v1/tasks")
                .then()
                    .statusCode(401);
    }

    @Test
    void createTask_withValidRequest_returns201() {
        given()
                .header("Authorization", "Bearer testing-token")
                .contentType(ContentType.JSON)
                .body(new TaskRequest("Get bread"))

                .when().post("/api/v1/tasks")

                .then()
                    .statusCode(201)
                    .body("title", equalTo("Get bread"));
    }

    @Test
    void editTask_withValidRequest_returns200() {
        Task savedTask = taskRepository.save(Task.builder()
                                        .title("Get bread")
                                        // Same sub as in the `@BeforeEach`.
                                        .userSub("testing-user")
                                        .build());

        given()
                .header("Authorization", "Bearer testing-token")
                .contentType(ContentType.JSON)
                .body(new TaskRequest("Get white bread"))

                .when().put("/api/v1/tasks/{id}", savedTask.getId())

                .then()
                    .statusCode(200)
                    .body("title", equalTo("Get white bread"))
                    .body("id", equalTo(savedTask.getId()));
    }

    @Test
    void deleteTask_withValidRequest_returns204() {
        Task savedTask = taskRepository.save(Task.builder()
                                        .title("Get bread")
                                        // Same sub as in the `@BeforeEach`.
                                        .userSub("testing-user")
                                        .build());

        given()
                .header("Authorization", "Bearer testing-token")

                .when().delete("/api/v1/tasks/{id}", savedTask.getId())

                .then()
                    .statusCode(204);
    }
}