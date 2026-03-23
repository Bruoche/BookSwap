package fr.bookswap.common.repository;

import java.util.List;

import fr.bookswap.common.entity.UserBook;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LibraryRepository  implements PanacheRepository<UserBook> {

	public List<UserBook> listByStatus(UserBook.Status status, Long userId) {
		return list("(?1 is null or ?1 = status) and ?2 = user.id", status, userId);
	}

	public UserBook findForUser(Long id, Long userId) {
		return find("?1 = id and ?2 = user.id", id, userId).firstResult();
	}

	public List<UserBook> listForUser(String username) {
		return list("user.username", username);
	}

}
