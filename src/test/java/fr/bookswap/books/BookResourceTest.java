package fr.bookswap.books;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookResourceTest {

    private static final String ISSUER = "https://exchange-manager-api.com";
    private static Long createdBookId;

    private String tokenForUser(Long userId, String username) {
        return Jwt.issuer(ISSUER)
                .upn(username)
                .subject(userId.toString())
                .groups(Set.of("USER"))
                .expiresIn(3600)
                .sign();
    }

    private String adminToken() {
        return Jwt.issuer(ISSUER)
                .upn("otman")
                .subject("1")
                .groups(Set.of("ADMIN"))
                .expiresIn(3600)
                .sign();
    }

    @Test
    @Order(1)
    void getAll_withoutToken_returns200() {
        given()
            .when().get("/api/books")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(6));
    }

    @Test
    @Order(2)
    void getAll_filterByIsbn_returnsMatchingBook() {
        given()
            .queryParam("isbn", "9782070408504")
            .when().get("/api/books")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].title", equalTo("Les Miserables"));
    }

    @Test
    @Order(3)
    void getAll_filterByAuthor_returnsMatchingBooks() {
        given()
            .queryParam("author", "Hugo")
            .when().get("/api/books")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("title", hasItem("Les Miserables"));
    }

    @Test
    @Order(4)
    void getAll_filterByGenre_returnsMatchingBooks() {
        given()
            .queryParam("genre", "Roman")
            .when().get("/api/books")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(5)
    void getAll_filterByYear_returnsRecentBooks() {
        given()
            .queryParam("publicationYear", 1960)
            .when().get("/api/books")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    @Order(6)
    void getDetails_existing_returns200() {
        given()
            .when().get("/api/books/5")
            .then()
            .statusCode(200)
            .body("title", equalTo("Dune"))
            .body("rating", equalTo(5.0f));
    }

    @Test
    @Order(7)
    void getDetails_nonExistent_returns404() {
        given()
            .when().get("/api/books/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(8)
    void add_withoutToken_returns401() {
        given()
            .contentType("application/json")
            .body("{\"isbn\": \"9781234567890\", \"title\": \"Test\", \"description\": \"Test desc\", \"publicationYear\": 2020, \"authors\": [1], \"genres\": [1]}")
            .when().post("/api/books")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(9)
    void add_authenticated_returns200() {
        createdBookId = given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"isbn\": \"9781234567890\", \"title\": \"Test Book\", \"description\": \"A test book for testing\", \"publicationYear\": 2020, \"authors\": [1], \"genres\": [1]}")
            .when().post("/api/books")
            .then()
            .statusCode(200)
            .body("title", equalTo("Test Book"))
            .body("isbn", equalTo("9781234567890"))
            .extract()
            .jsonPath()
            .getLong("id");
    }

    @Test
    @Order(10)
    void edit_asCreator_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"isbn\": \"9782070408504\", \"title\": \"Les Miserables Updated\", \"description\": \"Updated description\", \"publicationYear\": 1862, \"authors\": [1], \"genres\": [1]}")
            .when().put("/api/books/1")
            .then()
            .statusCode(200)
            .body("title", equalTo("Les Miserables Updated"));
    }

    @Test
    @Order(11)
    void edit_asNonCreator_returns400() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .contentType("application/json")
            .body("{\"isbn\": \"9782070408504\", \"title\": \"Hacked\", \"description\": \"Hacked\", \"publicationYear\": 1862, \"authors\": [1], \"genres\": [1]}")
            .when().put("/api/books/1")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(12)
    void edit_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"isbn\": \"9781234567890\", \"title\": \"Ghost\", \"description\": \"Ghost book\", \"publicationYear\": 2020, \"authors\": [1], \"genres\": [1]}")
            .when().put("/api/books/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(13)
    void remove_withoutToken_returns401() {
        given()
            .when().delete("/api/books/" + createdBookId)
            .then()
            .statusCode(401);
    }

    @Test
    @Order(14)
    void remove_asUser_returns403() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().delete("/api/books/" + createdBookId)
            .then()
            .statusCode(403);
    }

    @Test
    @Order(15)
    void remove_asAdmin_returns200() {
        given()
            .header("Authorization", "Bearer " + adminToken())
            .when().delete("/api/books/" + createdBookId)
            .then()
            .statusCode(200);
    }

    @Test
    @Order(16)
    void getReviews_withoutToken_returns200() {
        given()
            .when().get("/api/books/5/reviews")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(17)
    void getReviews_nonExistentBook_returns404() {
        given()
            .when().get("/api/books/9999/reviews")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    void addReview_withoutToken_returns401() {
        given()
            .contentType("application/json")
            .body("{\"rating\": 4, \"comment\": \"Great book!\"}")
            .when().post("/api/books/1/reviews")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(19)
    void addReview_authenticated_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"rating\": 4, \"comment\": \"A wonderful read!\"}")
            .when().post("/api/books/1/reviews")
            .then()
            .statusCode(200)
            .body("rating", equalTo(4))
            .body("comment", equalTo("A wonderful read!"));
    }

    @Test
    @Order(20)
    void addReview_duplicate_returns409() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"rating\": 3, \"comment\": \"Duplicate review\"}")
            .when().post("/api/books/1/reviews")
            .then()
            .statusCode(409);
    }
}
