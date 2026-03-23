package fr.bookswap.admin;

import fr.bookswap.admin.dto.UserDto;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class AdminServiceTest {

    @Inject
    AdminService adminService;

    @Test
    void getAllUsers_returnsAllUsers() {
        List<User> users = adminService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 3);
    }

    @Test
    void getUserById_existing_returnsUser() {
        User user = adminService.getUserById(1L);
        assertNotNull(user);
        assertEquals("otman", user.username);
    }

    @Test
    void getUserById_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> adminService.getUserById(9999L));
    }

    @Test
    void suspendUser_existing_setsActiveToFalse() {
        UserDto dto = adminService.suspendUser(1L);
        assertNotNull(dto);
        assertEquals("otman", dto.username);
        assertFalse(dto.active);
    }

    @Test
    void suspendUser_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> adminService.suspendUser(9999L));
    }

    @Test
    void deleteUser_existing_deletesSuccessfully() {
        assertDoesNotThrow(() -> adminService.deleteUser(3L));
        assertThrows(NotFoundException.class, () -> adminService.getUserById(3L));
    }

    @Test
    void deleteUser_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> adminService.deleteUser(9999L));
    }

    // Reviews
    @Test
    void deleteReview_existing_deletesSuccessfully() {
        assertDoesNotThrow(() -> adminService.deleteReview(1L));
    }

    @Test
    void deleteReview_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> adminService.deleteReview(9999L));
    }
}
