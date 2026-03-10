package fr.bookswap.review;

import fr.bookswap.books.BookRepository;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.review.dto.CreateReviewDto;
import fr.bookswap.user.UserRepository;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@ApplicationScoped
public class ReviewService {

    @Inject
    ReviewRepository reviewRepository;

    @Inject
    BookRepository bookRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;

    @Transactional
    public Review addReview(Long bookId, CreateReviewDto dto) {
        Long currentUserId = Long.valueOf(jwt.getSubject());
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
        // Need to handle error with interceptors ?
        if (book == null) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }
        return reviewRepository.findByBookId(bookId);
    }
}