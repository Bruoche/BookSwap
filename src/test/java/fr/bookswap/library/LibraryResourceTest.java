package fr.bookswap.library;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryResourceTest {

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

    @Test
    @Order(1)
    void getAll_withoutToken_returns401() {
        given()
            .when().get("/api/library")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(2)
    void getAll_authenticated_returnsList() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/library")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
    @Order(3)
    void getAll_filterByStatusOwned_returnsOwnedOnly() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .queryParam("status", "OWNED")
            .when().get("/api/library")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("status", everyItem(equalTo("OWNED")));
    }

    @Test
    @Order(4)
    void getAll_filterByStatusWishlist_returnsWishlistOnly() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .queryParam("status", "WISHLIST")
            .when().get("/api/library")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("status", everyItem(equalTo("WISHLIST")));
    }

    @Test
    @Order(5)
    void getById_ownBook_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/library/1")
            .then()
            .statusCode(200)
            .body("user", equalTo("otman"))
            .body("status", equalTo("OWNED"));
    }

    @Test
    @Order(6)
    void getById_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/library/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(7)
    void getById_otherUsersBook_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/library/4")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(8)
    void addBook_withoutToken_returns401() {
        String requestBody = """
            {
                "bookId": 3,
                "status": "OWNED",
                "condition": "NEW"
            }
            """;
        given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/library")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(9)
    void addBook_valid_returns201() {
        String requestBody = """
            {
                "bookId": 3,
                "status": "OWNED",
                "condition": "NEW",
                "isAvailableForExchange": true,
                "isAvailableForLoan": false
            }
            """;
        
        createdBookId = given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/library")
            .then()
            .statusCode(201)
            .body("user", equalTo("otman"))
            .body("status", equalTo("OWNED"))
            .body("condition", equalTo("NEW"))
            .extract()
            .jsonPath()
            .getLong("id");
    }

    @Test
    @Order(10)
    void addBook_nonExistentBook_returns404() {
        String requestBody = """
            {
                "bookId": 9999,
                "status": "OWNED",
                "condition": "GOOD"
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/library")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(11)
    void addBook_missingFields_returns400() {
        String requestBody = """
            {}
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/library")
            .then()
            .statusCode(400);
    }

    // --- PUT /api/library/{id} (RolesAllowed USER) ---

    @Test
    @Order(12)
    void edit_asOwner_returns200() {
        String requestBody = """
            {
                "status": "READ",
                "condition": "WORN",
                "isAvailableForExchange": false,
                "isAvailableForLoan": true
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/library/1")
            .then()
            .statusCode(200)
            .body("status", equalTo("READ"))
            .body("condition", equalTo("WORN"));
    }

    @Test
    @Order(13)
    void edit_asNonOwner_returns400() {
        String requestBody = """
            {
                "status": "OWNED",
                "condition": "GOOD",
                "isAvailableForExchange": false,
                "isAvailableForLoan": false
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/library/1")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(14)
    void edit_nonExistent_returns404() {
        String requestBody = """
            {
                "status": "OWNED",
                "condition": "GOOD",
                "isAvailableForExchange": false,
                "isAvailableForLoan": false
            }
            """;
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body(requestBody)
            .when().put("/api/library/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(15)
    void delete_withoutToken_returns401() {
        given()
            .when().delete("/api/library/" + createdBookId)
            .then()
            .statusCode(401);
    }

    @Test
    @Order(16)
    void delete_authenticated_returns200() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().delete("/api/library/" + createdBookId)
            .then()
            .statusCode(200);
    }

    @Test
    @Order(17)
    void getPublicLibrary_existingUser_returns200() {
        given()
            .when().get("/api/users/otman/library")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
    @Order(18)
    void getPublicLibrary_nonExistentUser_returnsEmptyList() {
        given()
            .when().get("/api/users/unknown/library")
            .then()
            .statusCode(200)
            .body("size()", equalTo(0));
    }
}
