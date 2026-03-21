package fr.bookswap.authors;

import java.util.List;

import fr.bookswap.common.entity.Author;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthorService {

	@Inject
	AuthorRepository authorRepository;

	public List<Author> getAll() {
		return authorRepository.listAll(); 
	}

	public Author getById(Long id) {
		Author author = authorRepository.findById(id);
		if (author == null) {
			throw new NotFoundException(id);
		}
		return author;
	}

	@Transactional
	public Author createAuthor(Author author) {
		authorRepository.persist(author);
		return author;
	}
}