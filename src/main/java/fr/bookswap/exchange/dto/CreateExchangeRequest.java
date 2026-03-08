package fr.bookswap.exchange.dto;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateExchangeRequest {

    @NotBlank(message = "Le demandeur est obligatoire")
    public User requester;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    public String description;

    @NotNull(message = "Le type est obligatoire")
    public Exchange.Type type;

    public CreateExchangeRequest() {}

    public Exchange toExchange() {
        Exchange exchange = new Exchange();
        exchange.requester = this.requester;
        exchange.description = this.description;
        exchange.type = this.type;
        return exchange;
    }
}