package fr.bookswap.library.dto;

import java.time.OffsetDateTime;

import fr.bookswap.common.entity.UserBook;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookResponse {

	@NotNull(message = "L'id de l'entrée est obligatoire")
	public Long id;

    @NotBlank(message = "L'utilisateur lié est obligatoire")
    public String user;

    @NotNull(message = "Les informations du livre sont obligatoires")
    @Size(min = 10, max = 13, message = "L'ISBN d'un livre doit faire 13 caractères (10 pour les livres d'avant 2007)")
    public BookInfoResponse infos;

    @NotNull(message = "Le status est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Status status;

    @NotNull(message = "L'état du livre est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Condition condition;

    public boolean isAvailableForExchange;

    public boolean isAvailableForLoan;

    public OffsetDateTime addedAt;

    // Constructeur par défaut requis par JPA
    public BookResponse() {}

    public static BookResponse fromUserBook(UserBook book) {
        BookResponse dto = new BookResponse(); 
		dto.id = book.id;
		dto.user = book.user.username;
        dto.infos = BookInfoResponse.fromBook(book.book);
        dto.status = book.status;
        dto.condition = book.condition;
        dto.isAvailableForExchange = book.isAvailableForExchange;
        dto.isAvailableForLoan = book.isAvailableForLoan;
		return dto;
    }

}
