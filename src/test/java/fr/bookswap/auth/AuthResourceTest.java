package fr.bookswap.auth;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthResourceTest {

    private static final String ISSUER = "https://exchange-manager-api.com";
    private static Long registeredUserId;
    private static String refreshCookie;

    private String tokenForUser(Long userId, String username) {
        return Jwt.issuer(ISSUER)
                .upn(username)
                .subject(userId.toString())
                .groups(Set.of("USER"))
                .expiresIn(3600)
                .sign();
    }

    @Test
    @Order(1)
    void register_valid_returns201() {
        String requestBody = """
            {
                "username": "testuser",
                "password": "password123",
                "email": "testuser@example.com"
            }
            """;
        registeredUserId = given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/register")
            .then()
            .statusCode(201)
            .body("username", equalTo("testuser"))
            .body("email", equalTo("testuser@example.com"))
            .extract()
            .jsonPath()
            .getLong("id");
    }

    @Test
    @Order(2)
    void register_duplicateUsername_returns500() {
        String requestBody = """
            {
                "username": "testuser",
                "password": "password123",
                "email": "other@example.com"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/register")
            .then()
            .statusCode(500);
    }

    @Test
    @Order(3)
    void register_missingUsername_returns400() {
        String requestBody = """
            {
                "password": "password123",
                "email": "test@example.com"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/register")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(4)
    void register_invalidEmail_returns400() {
        String requestBody = """
            {
                "username": "emailtest",
                "password": "password123",
                "email": "not-an-email"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/register")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(5)
    void register_shortPassword_returns400() {
        String requestBody = """
            {
                "username": "shortpw",
                "password": "12345",
                "email": "short@example.com"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/register")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(6)
    void login_validCredentials_returns200WithToken() {
        String requestBody = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;
        var response = given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/login")
            .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("username", equalTo("testuser"))
            .body("roles", hasItem("USER"))
            .extract()
            .response();

        refreshCookie = response.getCookie("refreshToken");
    }

    @Test
    @Order(7)
    void login_wrongPassword_returns401() {
        String requestBody = """
            {
                "username": "testuser",
                "password": "wrongpassword"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/login")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(8)
    void login_nonExistentUser_returns401() {
        String requestBody = """
            {
                "username": "nonexistent",
                "password": "password123"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/login")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(9)
    void login_missingFields_returns400() {
        String requestBody = """
            {}
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/auth/login")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(10)
    void me_withoutToken_returns401() {
        given()
            .when().get("/api/auth/me")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(11)
    void me_authenticated_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .when().get("/api/auth/me")
            .then()
            .statusCode(200)
            .body("username", equalTo("aminata"))
            .body("email", equalTo("aminata@example.com"))
            .body("active", equalTo(true));
    }

    @Test
    @Order(12)
    void editProfile_withoutToken_returns401() {
        String requestBody = """
            {
                "username": "updated",
                "email": "updated@example.com"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/auth/me")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(13)
    void editProfile_authenticated_returns200() {
        String requestBody = """
            {
                "username": "testuser_updated",
                "email": "testuser_updated@example.com"
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(registeredUserId, "testuser"))
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/auth/me")
            .then()
            .statusCode(200)
            .body("username", equalTo("testuser_updated"))
            .body("email", equalTo("testuser_updated@example.com"));
    }

    @Test
    @Order(14)
    void editProfile_missingFields_returns400() {
        String requestBody = """
            {}
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(registeredUserId, "testuser"))
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/auth/me")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(15)
    void changePassword_withoutToken_returns401() {
        given()
            .queryParam("old_password", "password123")
            .queryParam("new_password", "newpassword123")
            .when().patch("/api/auth/password")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(16)
    void changePassword_wrongOldPassword_returns400() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(registeredUserId, "testuser"))
            .queryParam("old_password", "wrongpassword")
            .queryParam("new_password", "newpassword123")
            .when().patch("/api/auth/password")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(17)
    void changePassword_valid_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(registeredUserId, "testuser"))
            .queryParam("old_password", "password123")
            .queryParam("new_password", "newpassword123")
            .when().patch("/api/auth/password")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(18)
    void refresh_validToken_returns200() {
        given()
            .cookie("refreshToken", refreshCookie)
            .contentType("application/json")
            .when().post("/api/auth/refresh")
            .then()
            .statusCode(200)
            .body("token", notNullValue());
    }

    @Test
    @Order(19)
    void refresh_invalidToken_returns400() {
        given()
            .cookie("refreshToken", "invalidtoken")
            .contentType("application/json")
            .when().post("/api/auth/refresh")
            .then()
            .statusCode(400);
    }
}
