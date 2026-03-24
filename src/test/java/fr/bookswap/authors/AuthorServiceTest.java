package fr.bookswap.authors;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.exception.NotFoundException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class AuthorServiceTest {

    @Inject
    AuthorService authorService;

    // --- getAll ---

    @Test
    void getAll_returnsAllAuthors() {
        List<Author> authors = authorService.getAll(0, 100);
        assertNotNull(authors);
        assertTrue(authors.size() >= 6);
    }

    @Test
    void getById_existingAuthor_returnsAuthor() {
        Author author = authorService.getById(1L);
        assertNotNull(author);
        assertEquals("Victor", author.firstname);
        assertEquals("Hugo", author.lastname);
    }

    @Test
    void getById_anotherAuthor_returnsCorrectData() {
        Author author = authorService.getById(3L);
        assertNotNull(author);
        assertEquals("J.K.", author.firstname);
        assertEquals("Rowling", author.lastname);
    }

    @Test
    void getById_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> authorService.getById(9999L));
    }

    @Test
    void createAuthor_valid_persistsAndReturns() {
        Author author = new Author("Jules", "Verne");
        Author created = authorService.createAuthor(author);
        assertNotNull(created);
        assertNotNull(created.id);
        assertEquals("Jules", created.firstname);
        assertEquals("Verne", created.lastname);
    }

    @Test
    void createAuthor_anotherValid_persistsAndReturns() {
        Author author = new Author("Emile", "Zola");
        Author created = authorService.createAuthor(author);
        assertNotNull(created);
        assertNotNull(created.id);
        assertEquals("Emile", created.firstname);
        assertEquals("Zola", created.lastname);
    }
}
