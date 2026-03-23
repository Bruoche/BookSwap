package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Entity
@Table(name = "review")
public class Review extends PanacheEntity {
	@OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(nullable = true)
    public User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Le livre lié est obligatoire")
    @JoinColumn(nullable = false)
    public Book book;

    @NotNull(message = "L'année est obligatoire")
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    public int rating;

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 2048, message = "Le commentaire ne doit pas dépasser 2048 caractères")
    @Column(nullable = false, length=2048)
    public String comment;

    @CreationTimestamp
    @Column(nullable = false)
    public OffsetDateTime createdAt;

    // Constructeur par défaut requis par JPA
    public Review() {}

    public Review(@NotNull(message = "L'utilisateur lié est obligatoire") User author, Book book, int rating, String comment) {
        this.author = author;
        this.book = book;
        this.rating = rating;
        this.comment = comment;
    }
}
