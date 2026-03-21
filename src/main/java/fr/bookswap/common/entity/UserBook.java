package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_book")
public class UserBook extends PanacheEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "L'utilisateur lié est obligatoire")
    @JoinColumn(nullable = false)
    public User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Le livre lié est obligatoire")
    @JoinColumn(nullable = false)
    public Book book;

    @NotNull(message = "Le status est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public UserBook.Status status;

    @NotNull(message = "L'état du livre est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public UserBook.Condition condition;

    @Column(nullable = false)
    public boolean isAvailableForExchange = false;

    @Column(nullable = false)
    public boolean isAvailableForLoan = false;

    @CreationTimestamp
    @Column(nullable = false)
    public OffsetDateTime addedAt;

    // Constructeur par défaut requis par JPA
    public UserBook() {}

    public UserBook(User user, Book book, Status status, Condition condition, boolean isAvailableForExchange, boolean isAvailableForLoan) {
        this.user = user;
        this.book = book;
        this.status = status;
        this.condition = condition;
        this.isAvailableForExchange = isAvailableForExchange;
        this.isAvailableForLoan = isAvailableForLoan;
    }

    public enum Status {
        OWNED, WISHLIST, READ
    }

    public enum Condition {
        NEW, GOOD, WORN
    }
}
