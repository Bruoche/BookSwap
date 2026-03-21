package fr.bookswap.library.dto;

import fr.bookswap.common.entity.UserBook;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class UpdateBookRequest {

    @NotNull(message = "Le status est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Status status;

    @NotNull(message = "L'état du livre est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Condition condition;

    public boolean isAvailableForExchange;

    public boolean isAvailableForLoan;

    public UpdateBookRequest() {}
}
