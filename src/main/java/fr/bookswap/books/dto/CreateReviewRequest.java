package fr.bookswap.books.dto;

import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.entity.User;
import jakarta.validation.constraints.*;

public class CreateReviewRequest {
    @NotNull
    @Min(1) @Max(5)
    public int rating;

    @NotBlank
    @Size(max = 2048)
    public String comment;

	public CreateReviewRequest() {}

	public Review toReview(User author, Book book) {
		Review review = new Review();
		review.rating = this.rating;
		review.comment = this.comment;
		review.author = author;
		review.book = book;
		return review;
	}
}