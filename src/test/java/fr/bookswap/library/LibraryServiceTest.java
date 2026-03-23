package fr.bookswap.library;

import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.exception.BadRequestException;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.library.dto.BookResponse;
import fr.bookswap.library.dto.CreateBookRequest;
import fr.bookswap.library.dto.UpdateBookRequest;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LibraryServiceTest {

    @Inject
    LibraryService libraryService;

    @Test
    @TestTransaction
    void findByStatus_owned_returnsOwnedBooks() {
        List<UserBook> result = libraryService.findByStatus(UserBook.Status.OWNED, 1L);
        assertFalse(result.isEmpty());
        result.forEach(ub -> assertEquals(UserBook.Status.OWNED, ub.status));
    }

    @Test
    @TestTransaction
    void findByStatus_wishlist_returnsWishlistBooks() {
        List<UserBook> result = libraryService.findByStatus(UserBook.Status.WISHLIST, 2L);
        assertFalse(result.isEmpty());
        result.forEach(ub -> assertEquals(UserBook.Status.WISHLIST, ub.status));
    }

    @Test
    @TestTransaction
    void findByStatus_nullStatus_returnsAllUserBooks() {
        List<UserBook> result = libraryService.findByStatus(null, 1L);
        assertTrue(result.size() >= 3);
    }

    @Test
    @TestTransaction
    void findByStatus_nonExistentUser_returnsEmptyList() {
        List<UserBook> result = libraryService.findByStatus(null, 9999L);
        assertTrue(result.isEmpty());
    }

    @Test
    @TestTransaction
    void getBookById_ownBook_returnsUserBook() {
        UserBook result = libraryService.getBookById(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.user.id);
    }

    @Test
    @TestTransaction
    void getBookById_nonExistentId_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> libraryService.getBookById(9999L, 1L));
    }

    @Test
    @TestTransaction
    void getBookById_otherUsersBook_throwsNotFoundException() {
        // ub4.user = 2
        assertThrows(NotFoundException.class,
                () -> libraryService.getBookById(4L, 1L));
    }

    // user

    @Test
    @TestTransaction
    void getUserLibrary_existingUser_returnsBooks() {
        List<UserBook> result = libraryService.getUserLibrary("otman");
        assertTrue(result.size() >= 3);
    }

    @Test
    @TestTransaction
    void getUserLibrary_nonExistentUser_returnsEmptyList() {
        List<UserBook> result = libraryService.getUserLibrary("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    @TestTransaction
    void create_nonExistentUser_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.bookId = 1L;
        request.status = UserBook.Status.OWNED;
        request.condition = UserBook.Condition.GOOD;

        assertThrows(NotFoundException.class,
                () -> libraryService.create(request, 9999L));
    }

    @Test
    @TestTransaction
    void create_nonExistentBook_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.bookId = 9999L;
        request.status = UserBook.Status.OWNED;
        request.condition = UserBook.Condition.GOOD;

        assertThrows(NotFoundException.class,
                () -> libraryService.create(request, 1L));
    }

    @Test
    @TestTransaction
    void updateBook_asOwner_returnsUpdated() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.status = UserBook.Status.READ;
        request.condition = UserBook.Condition.WORN;
        request.isAvailableForExchange = false;
        request.isAvailableForLoan = true;
        BookResponse result = libraryService.updateBook(1L, request, 1L);

        assertNotNull(result);
        assertEquals(UserBook.Status.READ, result.status);
        assertEquals(UserBook.Condition.WORN, result.condition);
        assertTrue(result.isAvailableForLoan);
    }

    @Test
    @TestTransaction
    void updateBook_nonExistent_throwsNotFoundException() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.status = UserBook.Status.OWNED;
        request.condition = UserBook.Condition.GOOD;
        assertThrows(NotFoundException.class,
                () -> libraryService.updateBook(9999L, request, 1L));
    }

    @Test
    @TestTransaction
    void updateBook_asNonOwner_throwsBadRequestException() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.status = UserBook.Status.OWNED;
        request.condition = UserBook.Condition.GOOD;
        // ub1.user = 1
        assertThrows(BadRequestException.class,
                () -> libraryService.updateBook(1L, request, 2L));
    }

    @Test
    @TestTransaction
    void deleteBook_existing_succeeds() {
        CreateBookRequest request = new CreateBookRequest();
        request.bookId = 6L;
        request.status = UserBook.Status.WISHLIST;
        request.condition = UserBook.Condition.NEW;
        BookResponse created = libraryService.create(request, 1L);
        assertDoesNotThrow(() -> libraryService.deleteBook(created.id));
    }
}
