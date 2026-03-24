package fr.bookswap.auth;

import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.BadRequestException;
import fr.bookswap.common.security.Token;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @Test
    @TestTransaction
    void login_validCredentials_returnsToken() {
        authService.register("logintest", "password123", "login@example.com");
        Token token = authService.login("logintest", "password123");
        assertNotNull(token);
        assertNotNull(token.authToken);
        assertNotNull(token.refreshToken);
        assertEquals("logintest", token.user.username);
    }

    @Test
    @TestTransaction
    void login_wrongPassword_throwsNotAuthorizedException() {
        authService.register("wrongpw", "password123", "wrongpw@example.com");
        assertThrows(NotAuthorizedException.class,
                () -> authService.login("wrongpw", "badpassword"));
    }

    @Test
    @TestTransaction
    void login_nonExistentUser_throwsNotAuthorizedException() {
        assertThrows(NotAuthorizedException.class,
                () -> authService.login("nonexistent", "password123"));
    }

    // --- register ---

    @Test
    @TestTransaction
    void register_valid_createsUser() {
        User user = authService.register("newuser", "password123", "new@example.com");
        assertNotNull(user);
        assertEquals("newuser", user.username);
        assertEquals("new@example.com", user.email);
        assertTrue(user.roles.contains("USER"));
        assertTrue(user.active);
    }

    @Test
    @TestTransaction
    void register_duplicateUsername_throwsIllegalArgument() {
        authService.register("dupuser", "password123", "dup@example.com");
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("dupuser", "password123", "dup2@example.com"));
    }

    // --- refreshAuth ---

    @Test
    @TestTransaction
    void refreshAuth_validToken_returnsNewTokens() {
        authService.register("refreshtest", "password123", "refresh@example.com");
        Token initial = authService.login("refreshtest", "password123");
        Token refreshed = authService.refreshAuth(initial.refreshToken);
        assertNotNull(refreshed);
        assertNotNull(refreshed.authToken);
        assertEquals("refreshtest", refreshed.user.username);
    }

    @Test
    @TestTransaction
    void refreshAuth_invalidToken_throwsBadRequest() {
        assertThrows(BadRequestException.class,
                () -> authService.refreshAuth("invalidtoken"));
    }
}
