package fr.bookswap.books;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookAuthorService {

	@Inject
	AuthorRepository authorRepository;

	public Author getAuthorById(Long id) {
		Author author = authorRepository.findById(id);
		if (author == null) {
			throw new NotFoundException(String.format("L'auteur %d n'existe pas", id));
		}
		return author;
	}

}
