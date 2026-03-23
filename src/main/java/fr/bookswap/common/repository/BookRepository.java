package fr.bookswap.common.repository;

import java.util.List;

import fr.bookswap.common.entity.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public List<Book> searchByYear(int year, int index, int pageSize) {
        return find("?1 is null or publicationYear >= ?1", year).page(index, pageSize).list();
    }

    public List<Book> searchByIsbnAndYear(String isbn, int year, int index, int pageSize) {
		if (isbn == null) {
			throw new BadRequestException("The isbn must not be null.");
		}
        return find(
			"(?1 is null or isbn like ?1) and (?2 is null or publicationYear >= ?2)", 
			"%"+isbn+"%", year
		).page(index, pageSize).list();
    }
	
}
