package fr.bookswap.books;

import fr.bookswap.books.dto.UpdateBookDto;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.security.JwtService;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class BookRepository implements PanacheRepository<Book> {

    @Inject
    JwtService jwt;

    @Transactional
    public Book update(Long userId, UpdateBookDto bookUpdate) {
        Book targetedBook = find("id = ?1 and ownerId = ?2", bookUpdate.id, userId).firstResult();
        if  (targetedBook == null) {
            throw new ForbiddenException("Book not found or you are not the owner.");
        }
        targetedBook.authors = bookUpdate.authors;
        targetedBook.title = bookUpdate.title;
        targetedBook.description = bookUpdate.description;
        targetedBook.coverUrl = bookUpdate.coverUrl;
        targetedBook.isbn = bookUpdate.isbn;
        targetedBook.publicationYear = bookUpdate.publicationYear;
        targetedBook.genres = bookUpdate.genres;

        return targetedBook;
    }

    @Transactional
    public boolean deleteIfOwned(Long currentUserId, Long id) {
        long deletedCount = delete("id = ?1 and ownerId = ?2", id, currentUserId);

        return deletedCount > 0;
    }
}
