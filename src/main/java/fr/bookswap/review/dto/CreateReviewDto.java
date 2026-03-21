package fr.bookswap.review.dto;

import jakarta.validation.constraints.*;

public class CreateReviewDto {
    @NotNull
    @Min(1) @Max(5)
    public int rating;

    @NotBlank
    @Size(max = 2048)
    public String comment;
}