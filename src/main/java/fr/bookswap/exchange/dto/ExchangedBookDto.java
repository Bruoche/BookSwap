package fr.bookswap.exchange.dto;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

import fr.bookswap.common.entity.UserBook;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class ExchangedBookDto {
	
    @NotBlank(message = "L'ISBN est obligatoire")
    @Size(min = 10, max = 13, message = "L'ISBN d'un livre doit faire 13 caractères (10 pour les livres d'avant 2007)")
    public String isbn;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    public String title;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    public String description;

    @NotNull(message = "L'année est obligatoire")
    @Min(1000)
    @Max(2500)
    public int publicationYear;

    public String coverUrl;

    @NotEmpty(message = "Il doit y avoir au moins un auteur")
    public Set<String> authors;

    @NotEmpty(message = "Il faut au moins renseigner un genre")
    public Set<String> genres;

    @NotBlank(message = "L'utilisateur lié est obligatoire")
    public String owner;

    @NotNull(message = "Le status est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Status status;

    @NotNull(message = "L'état du livre est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Condition condition;

    public boolean isAvailableForExchange = false;

    public boolean isAvailableForLoan = false;

    @CreationTimestamp
    public OffsetDateTime addedAt;

	public ExchangedBookDto() {}

	public static ExchangedBookDto fromBook(UserBook userbook) {
		ExchangedBookDto dto = new ExchangedBookDto();
		dto.isbn = userbook.book.isbn;
		dto.title = userbook.book.title;
		dto.description = userbook.book.description;
		dto.publicationYear = userbook.book.publicationYear;
		dto.coverUrl = userbook.book.coverUrl;
		dto.authors = userbook.book.authors
			.stream()
			.map(author -> author.firstname + " " + author.lastname)
			.collect(Collectors.toSet());
		dto.owner = userbook.user.username;
		dto.status = userbook.status;
		dto.condition = userbook.condition;
		dto.isAvailableForExchange = userbook.isAvailableForExchange;
		dto.isAvailableForLoan = userbook.isAvailableForLoan;
		return dto;
	}
}
