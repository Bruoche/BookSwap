package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange")
public class Exchange extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank(message = "Le demandeur est obligatoire")
    @JoinColumn(nullable = false)
    public User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank(message = "Le possesseur est obligatoire")
    @JoinColumn(nullable = false)
    public User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank(message = "Le livre est obligatoire")
    @JoinColumn(nullable = false)
    public UserBook book;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Exchange.Type type;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Exchange.Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    public LocalDateTime requestedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    // Constructeur par défaut requis par JPA
    public Exchange() {}

    public Exchange(User requester, UserBook userBook, Type type) {
        this.requester = requester;
		this.owner = userBook.user;
		this.book = userBook;
        this.type = type;
    }

    public enum Status {
        PENDING, ACCEPTED, REFUSED
    }

    public enum Type {
        EXCHANGE, LOAN
    }
}