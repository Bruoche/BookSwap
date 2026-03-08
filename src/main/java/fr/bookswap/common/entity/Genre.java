package fr.bookswap.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "genre")
public class Genre {
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
