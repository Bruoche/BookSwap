package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ExchangeService {

    @Inject
    ExchangeRepository exchangeRepository;

    public List<Exchange> getAllExchanges() {
        return exchangeRepository.listAll(); // TODO : faire selon l'utilisateur (reçues ou envoyées)
    }

    public Exchange getExchangeById(Long id) {
        return exchangeRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional  // Les modifications en BD doivent être dans une transaction
    public Exchange createExchange(Exchange exchange) {
        exchangeRepository.persist(exchange);
        return exchange;
    }

    @Transactional
    public Exchange acceptExchange(Long id) {
        Exchange exchange = getExchangeById(id);
        exchange.status = Exchange.Status.ACCEPTED;
        return exchange;
    }

    @Transactional
    public Exchange refuseExchange(Long id) {
        Exchange exchange = getExchangeById(id);
        exchange.status = Exchange.Status.REFUSED;
        return exchange;
    }

    @Transactional
    public void deleteExchange(Long id) {
        Exchange exchange = getExchangeById(id);
        exchangeRepository.delete(exchange);
    }

    public List<Exchange> searchExchanges(String keyword) {
        return exchangeRepository.searchByStatus(Exchange.Status.valueOf(keyword));
    }
}