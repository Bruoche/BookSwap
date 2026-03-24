package fr.bookswap.exchange;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExchangeResourceTest {

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
    @Order(1)
    void getExchanges_withoutToken_returns401() {
        given()
            .when().get("/api/exchanges")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(2)
    void getExchanges_asOwner_returnsList() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/exchanges")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(3)
    void getExchanges_filterByStatusPending_returnsPendingExchanges() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .queryParam("status", "PENDING")
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/exchanges")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("status", everyItem(equalTo("PENDING")));
    }

    @Test
    @Order(4)
    void getExchangeById_existing_returnsExchange() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/exchanges/1")
            .then()
            .statusCode(200)
            .body("status", equalTo("PENDING"))
            .body("type", equalTo("EXCHANGE"));
    }

    @Test
    @Order(5)
    void getExchangeById_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().get("/api/exchanges/9999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(6)
    void getExchangeById_nonOwner_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .when().get("/api/exchanges/1")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(7)
    void createExchange_valid_returns201() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .contentType("application/json")
            .body("{\"bookId\": 2, \"type\": \"EXCHANGE\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(201)
            .body("status", equalTo("PENDING"))
            .body("type", equalTo("EXCHANGE"));
    }

    @Test
    @Order(8)
    void createExchange_duplicate_returns409() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .contentType("application/json")
            .body("{\"bookId\": 2, \"type\": \"EXCHANGE\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(409);
    }

    @Test
    @Order(9)
    void createExchange_missingFields_returns400() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(10)
    void acceptExchange_asOwner_returnsAccepted() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().patch("/api/exchanges/1/accept")
            .then()
            .statusCode(200)
            .body("status", equalTo("ACCEPTED"));
    }

    @Test
    @Order(11)
    void refuseExchange_asOwner_returnsRefused() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().patch("/api/exchanges/1/refuse")
            .then()
            .statusCode(200)
            .body("status", equalTo("REFUSED"));
    }

    @Test
    @Order(12)
    void createExchange_loanType_returns201() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"bookId\": 4, \"type\": \"LOAN\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(201)
            .body("status", equalTo("PENDING"))
            .body("type", equalTo("LOAN"));
    }

    @Test
    @Order(13)
    void createExchange_selfExchange_returnsBadRequest() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"bookId\": 1, \"type\": \"EXCHANGE\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(14)
    void createExchange_bookNotOwned_returnsBadRequest() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .contentType("application/json")
            .body("{\"bookId\": 6, \"type\": \"EXCHANGE\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(15)
    void createExchange_nonExistentBook_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .contentType("application/json")
            .body("{\"bookId\": 9999, \"type\": \"EXCHANGE\"}")
            .when().post("/api/exchanges")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    void acceptExchange_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().patch("/api/exchanges/9999/accept")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(17)
    void acceptExchange_byNonOwner_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .when().patch("/api/exchanges/2/accept")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    void refuseExchange_nonExistent_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .when().patch("/api/exchanges/9999/refuse")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(19)
    void refuseExchange_byNonOwner_returns404() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .when().patch("/api/exchanges/2/refuse")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(20)
    void getExchanges_asRequester_returnsList() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(2L, "aminata"))
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/exchanges")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(21)
    void getExchanges_filterByRefusedStatus_returnsRefusedOnly() {
        given()
            .header("Authorization", "Bearer " + tokenForUser(1L, "otman"))
            .queryParam("status", "REFUSED")
            .queryParam("index", 0).queryParam("pageSize", 100)
            .when().get("/api/exchanges")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("status", everyItem(equalTo("REFUSED")));
    }
}
