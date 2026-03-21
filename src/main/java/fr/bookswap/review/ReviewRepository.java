package fr.bookswap.review;

import fr.bookswap.common.entity.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class ReviewRepository implements PanacheRepository<Review> {

    @Transactional
    public List<Review> findByBookId(Long bookId) {
        return list("book_id", bookId);
    }

    public Double getAverageRating(Long bookId) {
        return find("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = ?1", bookId)
                .project(Double.class)
                .firstResultOptional()
                .orElse(0.0); // Return 0.0 if no reviews exist
    }
}
