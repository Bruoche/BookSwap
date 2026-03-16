package fr.bookswap.exchange.dto;

import java.time.LocalDateTime;

import fr.bookswap.common.entity.Exchange;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExchangeResponse {
	
    @NotBlank(message = "Le possesseur est obligatoire")
    public String ownerName;

    @NotBlank(message = "Le livre est obligatoire")
    public ExchangedBookDto book;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    public Exchange.Type type;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    public Exchange.Status status;

    public LocalDateTime requestedAt;

    public LocalDateTime updatedAt;

    // Constructeur par défaut requis par JPA
    public ExchangeResponse() {}

    public static ExchangeResponse fromExchange(Exchange exchange) {
        ExchangeResponse dto = new ExchangeResponse();
		dto.ownerName = exchange.owner.username;
		dto.book = ExchangedBookDto.fromBook(exchange.book);
		dto.type = exchange.type;
		dto.status = exchange.status;
		dto.requestedAt = exchange.requestedAt;
		dto.updatedAt = exchange.updatedAt;
		return dto;
    }
	
}
