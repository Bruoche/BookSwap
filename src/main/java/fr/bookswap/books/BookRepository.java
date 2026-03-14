package fr.bookswap.books;

import fr.bookswap.books.dto.UpdateBookRequest;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.security.JwtService;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class BookRepository implements PanacheRepository<Book> {

    @Inject
    JwtService jwt;


    @Transactional
    public boolean deleteIfOwned(Long currentUserId, Long id) {
        long deletedCount = delete("id = ?1 and ownerId = ?2", id, currentUserId);

        return deletedCount > 0;
    }
}
