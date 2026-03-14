package fr.bookswap.books;

import fr.bookswap.books.dto.BookDetailsResponse;
import fr.bookswap.books.dto.UpdateBookRequest;
import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.review.ReviewRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    @Inject
    ReviewRepository reviewRepository;

    public BookDetailsResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("This book doesn't exist");
        }
        Double averageRating = reviewRepository.getAverageRating(bookId);
        return new BookDetailsResponse(book, averageRating);
    }

    @Transactional
    public List<Book> getAllBooks(String author, String genre, int startDate) {
        String query = "(?1 is null or author = ?1) " +
                "and (?2 is null or genre = ?2) " +
                "and (?3 is null or publicationYear >= ?3)";

        return bookRepository.find(query, author, genre, startDate).list();
    }

    @Transactional
    public Book createBook(Long userId, UpdateBookRequest bookDto) {
        Book newBook = bookDto.toBook();
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        newBook.createdBy = user;
        newBook.createdAt = OffsetDateTime.now();
        bookRepository.persist(newBook);
        return newBook;
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
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
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
