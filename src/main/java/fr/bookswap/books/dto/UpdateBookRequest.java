package fr.bookswap.books.dto;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Genre;
import fr.bookswap.common.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

public class UpdateBookRequest {

    @NotEmpty(message = "L'ID du livre est necessaire pour l'update")
    public Long id;

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

    @Column
    public String coverUrl;

    public User createdBy;

    @NotEmpty(message = "Il doit y avoir au moins un auteur")
    public Set<Author> authors;

    @NotEmpty(message = "Il faut au moins renseigner un genre")
    public Set<Genre> genres;
}
