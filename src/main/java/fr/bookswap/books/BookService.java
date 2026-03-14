package fr.bookswap.books;

import fr.bookswap.books.dto.UpdateBookRequest;
import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.security.JwtService;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    @Inject
    JwtService jwtService;

    public Book getBookById(Long id) {
        return bookRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public List<Book> getAllBooks() {
        return bookRepository.listAll();
    }

    @Transactional
    public Book createBook(Book book) {
        bookRepository.persist(book);
        return book;
    }

    @Transactional
    public Book updateBookById(UpdateBookRequest bookUpdate) {
        Long currentUserId = Long.valueOf(jwtService.getSubject());
        return bookRepository.update(currentUserId, bookUpdate);
    }

    @Transactional
    public void deleteBook(Long id) {
        Long currentUserId = jwtService.getUserId();
        boolean deleted = bookRepository.deleteIfOwned(currentUserId, id);
        if (!deleted) {
            throw new ForbiddenException("You do not have permission to delete this book.");
        }
    }

    @Transactional
    public List<Author> getBookAuthors(Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new ForbiddenException("Unkown book id: " + id);
        }
        return new ArrayList<>(book.authors);
    }
}
