package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "genre")
public class Genre extends PanacheEntity {
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "La nom ne doit pas dépasser 50 caractères")
    @Column(unique = true, nullable = false, length=50)
    public String name;

    // Constructeur par défaut requis par JPA
    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }
}
