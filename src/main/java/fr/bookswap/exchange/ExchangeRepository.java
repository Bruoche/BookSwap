package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExchangeRepository implements PanacheRepository<Exchange> {

    // PanacheRepository fournit déjà : listAll(), findById(), persist(), deleteById()...
    // On ajoute seulement les requêtes spécifiques :

    public List<Exchange> searchByStatus(Exchange.Status status) {
        return list("status", status);
    }
}