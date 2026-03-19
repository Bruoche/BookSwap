package fr.bookswap.exchange.dto;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.exception.NotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class CreateExchangeRequest {

    @NotNull(message = "Le livre est obligatoire")
    public Long bookId;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    public Exchange.Type type;

    // Constructeur par défaut requis par JPA
    public CreateExchangeRequest() {}

    public Exchange toExchange(Long requesterId) {
        Exchange exchange = new Exchange();
		UserBook book = UserBook.findById(this.bookId);
		if (book == null) {
			throw new NotFoundException("Le livre demandé n'existe pas.");
		}
		User requester = User.findById(requesterId);
		if (requester == null) {
			throw new NotFoundException(requesterId);
		}
		exchange.book = book;
		exchange.owner = book.user;
        exchange.requester = requester;
        exchange.type = this.type;
        return exchange;
    }
}