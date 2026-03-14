package fr.bookswap.books.dto;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Genre;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.util.Set;

public class BookDetailsResponse {

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

    public Set<Author> authors;

    public Set<Genre> genres;

    @Min(0)
    @Max(5)
    public Double rating;

    public BookDetailsResponse() {}

    public BookDetailsResponse(Book book) {
        this.isbn = book.isbn;
        this.title = book.title;
        this.description = book.description;
        this.publicationYear = book.publicationYear;
        this.coverUrl = book.coverUrl;
        this.authors = book.authors;
        this.genres = book.genres;
    }

    public BookDetailsResponse(Book book, Double rating) {
        this.isbn = book.isbn;
        this.title = book.title;
        this.description = book.description;
        this.publicationYear = book.publicationYear;
        this.coverUrl = book.coverUrl;
        this.authors = book.authors;
        this.genres = book.genres;
        this.rating = rating;
    }
}
