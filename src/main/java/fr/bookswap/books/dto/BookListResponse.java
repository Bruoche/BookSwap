package fr.bookswap.books.dto;

import java.util.Set;
import java.util.stream.Collectors;

import fr.bookswap.common.entity.Book;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookListResponse {

	public Long id;

    @Size(min = 10, max = 13, message = "L'ISBN d'un livre doit faire 13 caractères (10 pour les livres d'avant 2007)")
    public String isbn;

    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    public String title;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    public String description;

    @Min(1000)
    @Max(2500)
    public int publicationYear;

    public String coverUrl;

	public String createdBy;

    public Set<String> authors;

    public Set<String> genres;

    public BookListResponse() {}

    public static BookListResponse fromBook(Book book) {
        BookListResponse dto = new BookListResponse();
		dto.id = book.id;
		dto.isbn = book.isbn;
        dto.title = book.title;
        dto.description = book.description;
        dto.publicationYear = book.publicationYear;
        dto.coverUrl = book.coverUrl;
		dto.createdBy = book.createdBy.username;
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
