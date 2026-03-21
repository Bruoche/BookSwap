package fr.bookswap.common.repository;

import java.util.List;

import fr.bookswap.common.entity.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public List<Book> searchByYear(int year) {
        return list("?1 is null or publicationYear >= ?1", year);
    }

    public List<Book> searchByIsbnAndYear(String isbn, int year) {
        return list(
			"(?1 is null or isbn like ?1) and (?2 is null or publicationYear >= ?2)", 
			"%"+isbn+"%", year
		);
    }
	
}
