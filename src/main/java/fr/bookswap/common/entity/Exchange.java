package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange")
public class Exchange extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank(message = "Le demandeur est obligatoire")
    @JoinColumn(nullable = false)
    public User requester;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(length = 500)
    public String description;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Exchange.Status status = Status.PENDING;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Exchange.Type type;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime requestedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    // Constructeur par défaut requis par JPA
    public Exchange() {}

    public Exchange(User requester, String description, Type type) {
        this.requester = requester;
        this.description = description;
        this.type = type;
        this.status = Status.PENDING;
    }

    public enum Status {
        PENDING, ACCEPTED, REFUSED
    }

    public enum Type {
        EXCHANGE, LOAN
    }
}