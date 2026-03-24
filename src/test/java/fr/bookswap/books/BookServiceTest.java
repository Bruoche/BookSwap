package fr.bookswap.books;

import fr.bookswap.books.dto.BookDetailsResponse;
import fr.bookswap.books.dto.BookListResponse;
import fr.bookswap.books.dto.CreateBookRequest;
import fr.bookswap.books.dto.CreateReviewRequest;
import fr.bookswap.books.dto.ReviewResponse;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.exception.BadRequestException;
import fr.bookswap.common.exception.ConflictException;
import fr.bookswap.common.exception.NotFoundException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BookServiceTest {

    @Inject
    BookService bookService;

    @Test
    @TestTransaction
    void getBookById_existing_returnsBookDetails() {
        BookDetailsResponse result = bookService.getBookById(5L);
        assertNotNull(result);
        assertEquals("Dune", result.title);
        assertEquals(5.0, result.rating);
    }

    @Test
    @TestTransaction
    void getBookById_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> bookService.getBookById(9999L));
    }

    @Test
    @TestTransaction
    void getAllBooks_noFilters_returnsAllBooks() {
        List<Book> result = bookService.getAllBooks(null, null, null, 0, 0, 100);
        assertTrue(result.size() >= 6);
    }

    @Test
    @TestTransaction
    void getAllBooks_filterByIsbn_returnsMatchingBook() {
        List<Book> result = bookService.getAllBooks("9782070405077", null, null, 0, 0, 100);
        assertEquals(1, result.size());
        assertEquals("Dune", result.get(0).title);
    }

    @Test
    @TestTransaction
    void getAllBooks_filterByAuthor_returnsMatchingBooks() {
        List<Book> result = bookService.getAllBooks(null, "Hugo", null, 0, 0, 100);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(b ->
                b.authors.stream().anyMatch(a ->
                        a.lastname.toLowerCase().contains("hugo")
                )
        ));
    }

    @Test
    @TestTransaction
    void getAllBooks_filterByGenre_returnsMatchingBooks() {
        List<Book> result = bookService.getAllBooks(null, null, "Roman", 0, 0, 100);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(b ->
                b.genres.stream().anyMatch(g ->
                        g.name.toLowerCase().contains("roman")
                )
        ));
    }

    @Test
    @TestTransaction
    void getAllBooks_filterByYear_returnsRecentBooksOnly() {
        List<Book> result = bookService.getAllBooks(null, null, null, 1960, 0, 100);
        assertTrue(result.size() >= 2);
        assertTrue(result.stream().allMatch(b -> b.publicationYear >= 1960));
    }

    @Test
    @TestTransaction
    void createBook_valid_returnsBook() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567890";
        request.title = "Test Book";
        request.description = "A test book description";
        request.publicationYear = 2020;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        Book result = bookService.createBook(1L, request);

        assertNotNull(result);
        assertEquals("Test Book", result.title);
        assertEquals("9781234567890", result.isbn);
    }

    @Test
    @TestTransaction
    void createBook_nonExistentUser_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567891";
        request.title = "Wake Up Samurai";
        request.description = "Very Useful Description";
        request.publicationYear = 2077;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);
        assertThrows(NotFoundException.class,
                () -> bookService.createBook(9999L, request));
    }

    @Test
    @TestTransaction
    void createBook_nonExistentAuthor_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567892";
        request.title = "Wake Up Samurai";
        request.description = "Very Useful Description";
        request.publicationYear = 2077;
        request.authors = Set.of(9999L);
        request.genres = Set.of(1L);

        assertThrows(NotFoundException.class,
                () -> bookService.createBook(1L, request));
    }

    @Test
    @TestTransaction
    void createBook_nonExistentGenre_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567893";
        request.title = "Wake Up Samurai";
        request.description = "Very Useful Description";
        request.publicationYear = 2077;
        request.authors = Set.of(1L);
        request.genres = Set.of(9999L);

        assertThrows(NotFoundException.class,
                () -> bookService.createBook(1L, request));
    }

    // --- updateBook ---

    @Test
    @TestTransaction
    void updateBook_asCreator_returnsUpdatedBook() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9782070408504";
        request.title = "Les Miserables Updated";
        request.description = "Updated description";
        request.publicationYear = 1862;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        BookListResponse result = bookService.updateBook(1L, 1L, request, false);

        assertNotNull(result);
        assertEquals("Les Miserables Updated", result.title);
    }

    @Test
    @TestTransaction
    void updateBook_asNonCreator_throwsBadRequestException() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9782070408504";
        request.title = "Hacked";
        request.description = "Hacked";
        request.publicationYear = 1862;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        assertThrows(BadRequestException.class,
                () -> bookService.updateBook(1L, 2L, request, false));
    }

    @Test
    @TestTransaction
    void updateBook_asAdmin_updatesOthersBook() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9782070408504";
        request.title = "Les Miserables Admin Edit";
        request.description = "Admin updated";
        request.publicationYear = 1862;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        BookListResponse result = bookService.updateBook(1L, 2L, request, true);

        assertNotNull(result);
        assertEquals("Les Miserables Admin Edit", result.title);
    }

    @Test
    @TestTransaction
    void updateBook_nonExistent_throwsNotFoundException() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567890";
        request.title = "Ghost";
        request.description = "Ghost book";
        request.publicationYear = 2020;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        assertThrows(NotFoundException.class,
                () -> bookService.updateBook(9999L, 1L, request, false));
    }

    @Test
    @TestTransaction
    void deleteBook_existing_succeeds() {
        CreateBookRequest request = new CreateBookRequest();
        request.isbn = "9781234567894";
        request.title = "To Delete";
        request.description = "Will be deleted";
        request.publicationYear = 2020;
        request.authors = Set.of(1L);
        request.genres = Set.of(1L);

        Book book = bookService.createBook(1L, request);
        assertDoesNotThrow(() -> bookService.deleteBook(book.id));
    }

    @Test
    @TestTransaction
    void addReview_valid_returnsReviewResponse() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.rating = 4;
        request.comment = "A wonderful read!";
        ReviewResponse result = bookService.addReview(3L, 2L, request);

        assertNotNull(result);
        assertEquals(4, result.rating);
        assertEquals("A wonderful read!", result.comment);
    }

    @Test
    @TestTransaction
    void addReview_duplicate_throwsConflictException() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.rating = 3;
        request.comment = "Duplicate review";

        // Already reviewed in sql data → conflict
        assertThrows(ConflictException.class,
                () -> bookService.addReview(5L, 1L, request));
    }

    @Test
    @TestTransaction
    void addReview_nonExistentBook_throwsNotFoundException() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.rating = 4;
        request.comment = "Review for non-existent book";

        assertThrows(NotFoundException.class,
                () -> bookService.addReview(9999L, 1L, request));
    }

    @Test
    @TestTransaction
    void addReview_nonExistentUser_throwsNotFoundException() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.rating = 4;
        request.comment = "Review from non-existent user";

        assertThrows(NotFoundException.class,
                () -> bookService.addReview(1L, 9999L, request));
    }

    @Test
    @TestTransaction
    void getReviews_existingBook_returnsList() {
        List<Review> result = bookService.getReviews(5L);
        assertFalse(result.isEmpty());
    }

    @Test
    @TestTransaction
    void getReviews_nonExistentBook_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> bookService.getReviews(9999L));
    }
}
