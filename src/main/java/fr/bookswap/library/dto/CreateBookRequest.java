package fr.bookswap.library.dto;

import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.entity.UserBook;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class CreateBookRequest {

    @NotNull(message = "Les informations du livre sont obligatoires")
    public Long bookId;

    @NotNull(message = "Le status est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Status status;

    @NotNull(message = "L'état du livre est obligatoire")
    @Enumerated(EnumType.STRING)
    public UserBook.Condition condition;

    public boolean isAvailableForExchange;

    public boolean isAvailableForLoan;

    public CreateBookRequest() {}

    public UserBook toBook(User owner, Book book) {
        UserBook userBook = new UserBook(); 
		userBook.user = owner;
        userBook.book = book;
        userBook.status = this.status;
        userBook.condition = this.condition;
        userBook.isAvailableForExchange = this.isAvailableForExchange;
        userBook.isAvailableForLoan = this.isAvailableForLoan;
		return userBook;
    }
	
}
