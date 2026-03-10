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
}
