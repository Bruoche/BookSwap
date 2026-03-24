package fr.bookswap.admin;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminResourceTest {

    private static final String ISSUER = "https://exchange-manager-api.com";

    private String tokenForAdmin(Long userId, String username) {
        return Jwt.issuer(ISSUER)
                .upn(username)
                .subject(userId.toString())
                .groups(Set.of("ADMIN"))
                .expiresIn(3600)
                .sign();
    }

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
    void getUsers_withoutToken_returns401() {
        given()
            .when().get("/api/admin/users")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(2)
    void getUsers_asUser_returns403() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/admin/users")
            .then()
            .statusCode(403);
    }

    @Test
    @Order(3)
    void getUsers_asAdmin_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/admin/users")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
    @Order(4)
    void suspendUser_withoutToken_returns401() {
        given()
            .when().patch("/api/admin/users/1/suspend")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(5)
    void suspendUser_asUser_returns403() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().patch("/api/admin/users/1/suspend")
            .then()
            .statusCode(403);
    }

    @Test
    @Order(6)
    void suspendUser_asAdmin_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .when().patch("/api/admin/users/3/suspend")
            .then()
            .statusCode(200)
            .body("username", equalTo("charlie"))
            .body("active", equalTo(false));
    }

    @Test
    @Order(7)
    void suspendUser_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .when().patch("/api/admin/users/9999/suspend")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(8)
    void deleteReview_withoutToken_returns401() {
        given()
            .when().delete("/api/admin/reviews/1")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(9)
    void deleteReview_asUser_returns403() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().delete("/api/admin/reviews/1")
            .then()
            .statusCode(403);
    }

    @Test
    @Order(10)
    void deleteReview_asAdmin_returns202() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .when().delete("/api/admin/reviews/2")
            .then()
            .statusCode(202);
    }

    @Test
    @Order(11)
    void deleteReview_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .when().delete("/api/admin/reviews/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(12)
    void deleteUser_withoutToken_returns401() {
        given()
            .when().delete("/api/admin/users/3")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(13)
    void deleteUser_asUser_returns403() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().delete("/api/admin/users/3")
            .then()
            .statusCode(403);
    }

    @Test
    @Order(14)
    void deleteUser_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForAdmin(1L, "otman"))
            .when().delete("/api/admin/users/9999")
            .then()
            .statusCode(404);
    }
}
