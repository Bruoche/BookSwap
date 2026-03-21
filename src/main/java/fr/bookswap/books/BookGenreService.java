package fr.bookswap.books;

import fr.bookswap.common.entity.Genre;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.GenreRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

public@ApplicationScoped
 class BookGenreService {

	@Inject
	GenreRepository genreRepository;

	public Genre getGenreById(Long id) {
		Genre genre = genreRepository.findById(id);
		if (genre == null) {
			throw new NotFoundException(String.format("Le genre %d n'existe pas", id));
		}
		return genre;
	}

}
