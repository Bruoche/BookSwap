package fr.bookswap.books;

import fr.bookswap.books.dto.BookDetailsResponse;
import fr.bookswap.books.dto.CreateReviewDto;
import fr.bookswap.books.dto.UpdateBookRequest;
import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Genre;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.BookRepository;
import fr.bookswap.common.repository.ReviewRepository;
import fr.bookswap.common.repository.UserRepository;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

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

    @Inject
    UserRepository userRepository;

    public BookDetailsResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("This book doesn't exist");
        }
        Double averageRating = reviewRepository.getAverageRating(bookId);
        return new BookDetailsResponse(book, averageRating);
    }

    public List<Book> getAllBooks(String authors, String genres, int year) {
        return bookRepository.searchByYear(year)
			.stream() // On filtre après requête car logique trop complexe pour requête sql maintenable
			.filter(book -> {
				if (authors == null) {
					return true;
				}
				for (String searchedAuthor : authors.split(" ")) {
					for (Author bookAuthor : book.authors) {
						if (bookAuthor.firstname.toLowerCase().contains(searchedAuthor.toLowerCase()) || bookAuthor.lastname.contains(searchedAuthor.toLowerCase())) {
							return true;
						}
					}
					return false; // Tout les mot-clefs doivent être satisfaits.
				}
				return false;
			}).filter(book -> {
				if (genres == null) {
					return true;
				}
				for (String searchedGenre : genres.split(" ")) {
					for (Genre genre : book.genres) {
						if (genre.name.toLowerCase().contains(searchedGenre.toLowerCase())) {
							return true;
						}
					}
					return false; // Tout les mot-clefs doivent être satisfaits.
				}
				return false;
			}).toList();
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

    public List<Author> getBookAuthors(Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new ForbiddenException("Unkown book id: " + id);
        }
        return new ArrayList<>(book.authors);
    }

    @Transactional
    public Review addReview(Long bookId, Long currentUserId, CreateReviewDto dto) {
        User currentUser = userRepository.findById(currentUserId);
        if (currentUser == null) {
            throw new UnauthorizedException("Authenticated user not found in database.");
        }

        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }

        long existing = reviewRepository.count("author = ?1 and book = ?2", currentUser, book);
        if (existing > 0) {
            throw new BadRequestException("You have already reviewed this book.");
        }

        Review review = new Review(
                currentUser,
                book,
                dto.rating,
                dto.comment
        );

        reviewRepository.persist(review);

        return review;
    }

    @Transactional
    public List<Review>  getReviews(Long bookId) {
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }
        return reviewRepository.findByBookId(bookId);
    }
}
