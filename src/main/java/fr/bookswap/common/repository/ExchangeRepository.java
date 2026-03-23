package fr.bookswap.common.repository;

import fr.bookswap.common.entity.Exchange;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExchangeRepository implements PanacheRepository<Exchange> {

    public List<Exchange> searchByStatusForUser(Exchange.Status status, Long id, int index, int pageSize) {
        return find("status = ?1 and (requester.id = ?2 or owner.id = ?2)", status, id).page(index, pageSize).list();
    }

    public List<Exchange> listAllForUser(Long id, int index, int pageSize) {
        return find("requester.id = ?1 or owner.id = ?1", id).page(index, pageSize).list();
    }

	public Boolean exchangeAlreadyExists(Long ownerId, Long requesterId, Long bookId) {
		return list("owner.id = ?1 and requester.id = ?2 and book.id = ?3", ownerId, requesterId, bookId).size() > 0;
	}
}