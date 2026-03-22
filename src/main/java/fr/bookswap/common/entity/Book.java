package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book extends PanacheEntity {

    @NotBlank(message = "L'ISBN est obligatoire")
    @Size(min = 10, max = 13, message = "L'ISBN d'un livre doit faire 13 caractères (10 pour les livres d'avant 2007)")
    @Column(nullable = false, unique = true, length=13)
    public String isbn;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    @Column(nullable = false, length=100)
    public String title;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(nullable = false, length=500)
    public String description;

    @NotNull(message = "L'année est obligatoire")
    @Min(1000)
    @Max(2500)
    @Column(nullable = false)
    public int publicationYear;

    @Column
    public String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public User createdBy;

    @CreationTimestamp
    @Column(nullable = false)
    public OffsetDateTime createdAt;

    @ManyToMany
    @NotEmpty(message = "Il doit y avoir au moins un auteur")
    public Set<Author> authors;

    @ManyToMany
    @NotEmpty(message = "Il faut au moins renseigner un genre")
    public Set<Genre> genres;

    // Constructeur par défaut requis par JPA
    public Book() {}

    public Book(String isbn, String title, String description, int publicationYear, String coverUrl, @NotNull(message = "Le livre doit être créé par un utilisateur") User createdBy, Set<Author> authors, Set<Genre> genres) {
		this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publicationYear = publicationYear;
        this.coverUrl = coverUrl;
        this.createdBy = createdBy;
        this.authors = authors;
        this.genres = genres;
    }
}
