package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.exception.NotFoundException;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ExchangeService {

    @Inject
    ExchangeRepository exchangeRepository;

    public List<Exchange> getUserExchanges(Long userId) {
        return exchangeRepository.listAllForUser(userId);
    }

    public List<Exchange> searchUserExchanges(String keyword, Long userId) {
        return exchangeRepository.searchByStatusForUser(Exchange.Status.valueOf(keyword), userId);
    }

    public Exchange getUserExchangeById(Long id, Long userId) {
        Exchange exchange = exchangeRepository.findByIdOptional(id)
			.orElseThrow(() -> new NotFoundException(id));
		if (exchange.owner.id != userId) {
			throw new NotFoundException(id);
		}
		return exchange;
    }

    @Transactional  // Les modifications en BD doivent être dans une transaction
    public Exchange createExchange(Exchange exchange) {
		if (exchange.owner.id == exchange.book.user.id) {
			throw new UnauthorizedException("Vous ne pouvez pas échanger un livre avec vous-même.");
		}
		if (exchange.book.status != UserBook.Status.OWNED) {
			throw new UnauthorizedException("Le livre proposé en échange doit avoir le status \"OWNED\" dans votre bibliothèque personelle.");
		}
        exchangeRepository.persist(exchange);
        return exchange;
    }

    @Transactional
    public Exchange acceptExchange(Long id, Long userId) {
        Exchange exchange = getUserExchangeById(id, userId);
        exchange.status = Exchange.Status.ACCEPTED;
        return exchange;
    }

    @Transactional
    public Exchange refuseExchange(Long id, Long userId) {
        Exchange exchange = getUserExchangeById(id, userId);
        exchange.status = Exchange.Status.REFUSED;
        return exchange;
    }

    @Transactional
    public void deleteExchange(Long id, Long userId) {
        Exchange exchange = getUserExchangeById(id, userId);
        exchangeRepository.delete(exchange);
    }
}