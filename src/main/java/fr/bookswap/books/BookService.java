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

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

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
    public Book updateBookById(Long bookId, Long userId, UpdateBookRequest bookUpdate) {

        Book targetedBook = find("id = ?1 and ownerId = ?2", bookId, userId).firstResult();
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
    public void deleteBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        if (book.createdBy.id.equals(userId)) {
            bookRepository.deleteById(bookId);
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
