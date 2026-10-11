package dev.kostie.oauth2authorizationserver.api;

import dev.kostie.oauth2authorizationserver.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mysql.MySQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

// Full App Ctx with Test App Properties.
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

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
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        // Needed since if we start another test old data must be purged.
        userRepository.deleteAll();
    }

    @Test
    void userShouldRegister() {

        given()
                .contentType(ContentType.JSON)
                .body(
                        """
                            {
                                "username": "username",
                                "password": "very-strong-password"                    
                            }
                         """
                )
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body("username", equalTo("username"));
    }
}