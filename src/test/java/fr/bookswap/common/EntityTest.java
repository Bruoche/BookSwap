package fr.bookswap.common;

import fr.bookswap.common.entity.*;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class EntityTest {

    @Test
    void author_defaultConstructor_createsInstance() {
        Author author = new Author();
        assertNotNull(author);
    }

    @Test
    void author_paramConstructor_setsFields() {
        Author author = new Author("Victor", "Hugo");
        assertEquals("Victor", author.firstname);
        assertEquals("Hugo", author.lastname);
    }

    @Test
    void author_findById_returnsSeeded() {
        Author author = Author.findById(1L);
        assertNotNull(author);
        assertEquals("Victor", author.firstname);
        assertEquals("Hugo", author.lastname);
    }

    @Test
    void author_persist_assignsId() {
        Author author = new Author("Gustave", "Flaubert");
        author.persist();
        assertNotNull(author.id);
    }

    @Test
    void genre_defaultConstructor_createsInstance() {
        Genre genre = new Genre();
        assertNotNull(genre);
    }

    @Test
    void genre_paramConstructor_setsName() {
        Genre genre = new Genre("Horreur");
        assertEquals("Horreur", genre.name);
    }

    @Test
    void genre_findById_returnsSeeded() {
        Genre genre = Genre.findById(1L);
        assertNotNull(genre);
        assertEquals("Roman", genre.name);
    }

    @Test
    void genre_persist_assignsId() {
        Genre genre = new Genre("Biographie");
        genre.persist();
        assertNotNull(genre.id);
    }

    @Test
    void user_defaultConstructor_createsInstance() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void user_findByUsername_returnsSeeded() {
        User user = User.findByUsername("aminata");
        assertNotNull(user);
        assertEquals("aminata@example.com", user.email);
        assertTrue(user.active);
    }

    @Test
    void user_findByUsername_nonExistent_returnsNull() {
        User user = User.findByUsername("nonexistent");
        assertNull(user);
    }

    @Test
    void book_defaultConstructor_createsInstance() {
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    void book_paramConstructor_setsFields() {
        User user = User.findById(1L);
        Author author = Author.findById(1L);
        Genre genre = Genre.findById(1L);
        Book book = new Book("9781234567890", "Test Book", "Description", 2020, null, user, Set.of(author), Set.of(genre));
        assertEquals("9781234567890", book.isbn);
        assertEquals("Test Book", book.title);
        assertEquals(2020, book.publicationYear);
        assertEquals(user, book.createdBy);
    }

    @Test
    void book_findById_returnsSeeded() {
        Book book = Book.findById(5L);
        assertNotNull(book);
        assertEquals("Dune", book.title);
        assertEquals("9782070405077", book.isbn);
    }

    @Test
    void review_defaultConstructor_createsInstance() {
        Review review = new Review();
        assertNotNull(review);
    }

    @Test
    void review_paramConstructor_setsFields() {
        User user = User.findById(1L);
        Book book = Book.findById(1L);
        Review review = new Review(user, book, 4, "Great book");
        assertEquals(user, review.author);
        assertEquals(book, review.book);
        assertEquals(4, review.rating);
        assertEquals("Great book", review.comment);
    }

    @Test
    void review_findById_returnsSeeded() {
        Review review = Review.findById(1L);
        assertNotNull(review);
        assertEquals(5, review.rating);
    }

    @Test
    void userBook_defaultConstructor_createsInstance() {
        UserBook ub = new UserBook();
        assertNotNull(ub);
    }

    @Test
    void userBook_paramConstructor_setsFields() {
        User user = User.findById(1L);
        Book book = Book.findById(1L);
        UserBook ub = new UserBook(user, book, UserBook.Status.OWNED, UserBook.Condition.GOOD, true, false);
        assertEquals(user, ub.user);
        assertEquals(book, ub.book);
        assertEquals(UserBook.Status.OWNED, ub.status);
        assertEquals(UserBook.Condition.GOOD, ub.condition);
        assertTrue(ub.isAvailableForExchange);
        assertFalse(ub.isAvailableForLoan);
    }

    @Test
    void userBook_findById_returnsSeeded() {
        UserBook ub = UserBook.findById(1L);
        assertNotNull(ub);
        assertEquals(UserBook.Status.OWNED, ub.status);
    }

    @Test
    void exchange_defaultConstructor_createsInstance() {
        Exchange exchange = new Exchange();
        assertNotNull(exchange);
    }

    @Test
    void exchange_paramConstructor_setsFields() {
        User requester = User.findById(2L);
        UserBook ub = UserBook.findById(1L);
        Exchange exchange = new Exchange(requester, ub, Exchange.Type.EXCHANGE);
        assertEquals(requester, exchange.requester);
        assertEquals(ub.user, exchange.owner);
        assertEquals(ub, exchange.book);
        assertEquals(Exchange.Type.EXCHANGE, exchange.type);
    }

    @Test
    void exchange_findById_returnsSeeded() {
        Exchange exchange = Exchange.findById(1L);
        assertNotNull(exchange);
        assertEquals(Exchange.Status.PENDING, exchange.status);
        assertEquals(Exchange.Type.EXCHANGE, exchange.type);
    }

    @Test
    void refreshToken_find_nonExistent_returnsNull() {
        RefreshToken token = RefreshToken.find("nonexistent");
        assertNull(token);
    }

    @Test
    void refreshToken_deleteAllForUser_doesNotThrow() {
        assertDoesNotThrow(() -> RefreshToken.deleteAllForUser(9999L));
    }
}
