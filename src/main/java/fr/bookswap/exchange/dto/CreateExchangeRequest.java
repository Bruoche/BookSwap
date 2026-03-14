package fr.bookswap.exchange.dto;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.entity.UserBook;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateExchangeRequest {

    @NotBlank(message = "Le demandeur est obligatoire")
    public User requester;

    @NotBlank(message = "Le livre est obligatoire")
    public UserBook book;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    public Exchange.Type type;

    // Constructeur par défaut requis par JPA
    public CreateExchangeRequest() {}

    public Exchange toExchange() {
        Exchange exchange = new Exchange();
        exchange.requester = this.requester;
		exchange.owner = this.book.user;
		exchange.book = this.book;
        exchange.type = this.type;
        exchange.status = Exchange.Status.PENDING;
        return exchange;
    }
}