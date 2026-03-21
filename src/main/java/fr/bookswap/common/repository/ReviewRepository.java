package fr.bookswap.common.repository;

import fr.bookswap.common.entity.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {

    public List<Review> findByBookId(Long bookId) {
        return list("book.id = ?1", bookId);
    }

    public Double getAverageRating(Long bookId) {
        return find("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = ?1", bookId)
                .project(Double.class)
                .firstResultOptional()
                .orElse(0.0); // Return 0.0 if no reviews exist
    }

	public Long countReviewsOf(Long userId, Long bookId) {
		return count("author.id = ?1 and book.id = ?2", userId, bookId);
	}
}
