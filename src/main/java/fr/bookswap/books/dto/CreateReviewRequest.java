package fr.bookswap.books.dto;

import jakarta.validation.constraints.*;

public class CreateReviewRequest {
    @NotNull
    @Min(1) @Max(5)
    public int rating;

    @NotBlank
    @Size(max = 2048)
    public String comment;
}