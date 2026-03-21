package fr.bookswap.library.dto;

import java.util.Set;
import java.util.stream.Collectors;

import fr.bookswap.common.entity.Book;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookInfoResponse {

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

    // Constructeur par défaut requis par JPA
    public BookInfoResponse() {}

    public static BookInfoResponse fromBook(Book book) {
        BookInfoResponse dto = new BookInfoResponse(); 
		dto.isbn = book.isbn;
        dto.title = book.title;
        dto.description = book.description;
        dto.publicationYear = book.publicationYear;
        dto.coverUrl = book.coverUrl;
        dto.authors = book.authors
			.stream()
			.map(author -> author.firstname + " " + author.lastname)
			.collect(Collectors.toSet());
        dto.genres = book.genres
			.stream()
			.map(genre -> genre.name)
			.collect(Collectors.toSet());
		return dto;
    }
}
