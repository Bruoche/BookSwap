package fr.bookswap.books.dto;

import java.time.OffsetDateTime;

import fr.bookswap.common.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewResponse {

	@NotNull(message = "L'identifiant est obligatoire")
	public Long id;

    @NotBlank(message = "L'utilisateur lié est obligatoire")
    public String author;

    @NotBlank(message = "Le livre lié est obligatoire")
    public String book;

    @Min(1)
    @Max(5)
    public int rating;

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 2048, message = "Le commentaire ne doit pas dépasser 2048 caractères")
    public String comment;

    public OffsetDateTime createdAt;

    // Constructeur par défaut requis par JPA
    public ReviewResponse() {}

    public static ReviewResponse fromReview(Review review) {
		ReviewResponse dto = new ReviewResponse();
		dto.id = review.id;
        dto.author = review.author.username;
        dto.book = review.book.title;
        dto.rating = review.rating;
        dto.comment = review.comment;
		return dto;
    }
}