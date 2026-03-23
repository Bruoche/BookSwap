package fr.bookswap.common.repository;

import fr.bookswap.common.entity.Genre;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenreRepository implements PanacheRepository<Genre> {
}
