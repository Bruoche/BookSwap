package fr.bookswap.authors;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorResourceTest {

    private static final String ISSUER = "https://exchange-manager-api.com";

    private String tokenForUser(Long userId, String username) {
        return Jwt.issuer(ISSUER)
                .upn(username)
                .subject(userId.toString())
                .groups(Set.of("USER"))
                .expiresIn(3600)
                .sign();
    }

    @Test
    @Order(2)
    void getAll_authenticated_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/authors")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(6));
    }

    @Test
    @Order(3)
    void getById_existingAuthor_returns200() {
        given()
            .when().get("/api/authors/1")
            .then()
            .statusCode(200)
            .body("firstname", equalTo("Victor"))
            .body("lastname", equalTo("Hugo"));
    }

    @Test
    @Order(5)
    void getById_nonExistent_returns404() {
        given()
            .when().get("/api/authors/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(6)
    void createAuthor_withoutToken_returns401() {
        String requestBody = """
            {
                "firstname": "Marcel",
                "lastname": "Proust"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/authors")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(7)
    void createAuthor_authenticated_returns201() {
        String requestBody = """
            {
                "firstname": "Marcel",
                "lastname": "Proust"
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/authors")
            .then()
            .statusCode(201)
            .body("firstname", equalTo("Marcel"))
            .body("lastname", equalTo("Proust"));
    }

    @Test
    @Order(8)
    void createAuthor_missingFirstname_returns400() {
        String requestBody = """
            {
                "lastname": "Proust"
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/authors")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(9)
    void createAuthor_missingLastname_returns400() {
        String requestBody = """
            {
                "firstname": "Marcel"
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/authors")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(10)
    void createAuthor_emptyBody_returns400() {
        String requestBody = """
            {}
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/authors")
            .then()
            .statusCode(400);
    }
}
