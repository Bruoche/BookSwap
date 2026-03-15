package fr.bookswap.books;

import fr.bookswap.common.entity.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class BookRepository implements PanacheRepository<Book> {
}
