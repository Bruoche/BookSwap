package fr.bookswap.books.dto;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Genre;
import fr.bookswap.common.entity.User;
import jakarta.validation.constraints.*;

import java.util.Set;

public class CreateBookRequest {

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
    public Set<Long> authors;

    @NotEmpty(message = "Il faut au moins renseigner un genre")
    public Set<Long> genres;


    public CreateBookRequest() {}

    public Book toBook(User creator, Set<Author> authors, Set<Genre> genres) {
        Book book = new Book();
		book.isbn = this.isbn;
        book.title = this.title;
        book.description = this.description;
        book.publicationYear = this.publicationYear;
        book.coverUrl = this.coverUrl;
		book.createdBy = creator;
        book.authors = authors;
        book.genres = genres;
		return book;
    }
}
