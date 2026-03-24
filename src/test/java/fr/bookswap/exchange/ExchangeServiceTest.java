package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.exception.BadRequestException;
import fr.bookswap.common.exception.ConflictException;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.exchange.dto.ExchangeResponse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ExchangeServiceTest {

    @Inject
    ExchangeService exchangeService;

    @Test
    @TestTransaction
    void getUserExchanges_userWithExchanges_returnsList() {
        List<Exchange> exchanges = exchangeService.getUserExchanges(1L, 0, 100);
        assertFalse(exchanges.isEmpty());
    }

    @Test
    @TestTransaction
    void getUserExchanges_userWithNoExchanges_returnsEmptyList() {
        List<Exchange> exchanges = exchangeService.getUserExchanges(9999L, 0, 100);
        assertTrue(exchanges.isEmpty());
    }

    @Test
    @TestTransaction
    void searchUserExchanges_byPending_returnsOnlyPendingExchanges() {
        User requester = User.findById(1L);
        UserBook book = UserBook.findById(7L);
        Exchange exchange = new Exchange(requester, book, Exchange.Type.EXCHANGE);
        exchangeService.createExchange(exchange);

        List<Exchange> result = exchangeService.searchUserExchanges(Exchange.Status.PENDING, 1L, 0, 100);
        assertFalse(result.isEmpty());
        result.forEach(e -> assertEquals(Exchange.Status.PENDING, e.status));
    }

    @Test
    @TestTransaction
    void searchUserExchanges_byAccepted_returnsEmptyWhenNoneExist() {
        List<Exchange> result = exchangeService.searchUserExchanges(Exchange.Status.ACCEPTED, 9999L, 0, 100);
        assertTrue(result.isEmpty());
    }

    @Test
    @TestTransaction
    void getUserExchangeById_asOwner_returnsExchange() {
        Exchange exchange = exchangeService.getUserExchangeById(1L, 1L);
        assertNotNull(exchange);
        assertEquals(1L, exchange.owner.id);
    }

    @Test
    @TestTransaction
    void getUserExchangeById_nonExistentId_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> exchangeService.getUserExchangeById(9999L, 1L));
    }

    @Test
    @TestTransaction
    void getUserExchangeById_asRequesterNotOwner_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> exchangeService.getUserExchangeById(1L, 2L));
    }

    @Test
    @TestTransaction
    void createExchange_valid_returnsPendingExchange() {
        User requester = User.findById(1L);
        UserBook book = UserBook.findById(8L);
        Exchange exchange = new Exchange(requester, book, Exchange.Type.EXCHANGE);

        Exchange result = exchangeService.createExchange(exchange);

        assertNotNull(result);
        assertEquals(Exchange.Status.PENDING, result.status);
        assertEquals(Exchange.Type.EXCHANGE, result.type);
        assertEquals(1L, result.requester.id);
    }

    @Test
    @TestTransaction
    void createExchange_duplicate_throwsConflictException() {
        User requester = User.findById(1L);
        UserBook book = UserBook.findById(9L);
        exchangeService.createExchange(new Exchange(requester, book, Exchange.Type.EXCHANGE));
        assertThrows(ConflictException.class,
                () -> exchangeService.createExchange(new Exchange(requester, book, Exchange.Type.EXCHANGE)));
    }

    @Test
    @TestTransaction
    void createExchange_selfExchange_throwsBadRequestException() {
        User requester = User.findById(1L);
        UserBook ownBook = UserBook.findById(1L);
        Exchange exchange = new Exchange(requester, ownBook, Exchange.Type.EXCHANGE);
        assertThrows(BadRequestException.class,
                () -> exchangeService.createExchange(exchange));
    }

    @Test
    @TestTransaction
    void createExchange_bookNotOwned_throwsBadRequestException() {
        User requester = User.findById(1L);
        UserBook wishlistBook = UserBook.findById(6L);
        Exchange exchange = new Exchange(requester, wishlistBook, Exchange.Type.EXCHANGE);
        assertThrows(BadRequestException.class,
                () -> exchangeService.createExchange(exchange));
    }

    @Test
    @TestTransaction
    void acceptExchange_asOwner_setsStatusToAccepted() {
        User requester = User.findById(1L);
        UserBook book = UserBook.findById(10L);
        Exchange exchange = exchangeService.createExchange(new Exchange(requester, book, Exchange.Type.EXCHANGE));

        ExchangeResponse response = exchangeService.acceptExchange(exchange.id, 3L);

        assertEquals(Exchange.Status.ACCEPTED, response.status);
    }

    @Test
    @TestTransaction
    void refuseExchange_asOwner_setsStatusToRefused() {
        User requester = User.findById(1L);
        UserBook book = UserBook.findById(11L);
        Exchange exchange = exchangeService.createExchange(new Exchange(requester, book, Exchange.Type.EXCHANGE));

        ExchangeResponse response = exchangeService.refuseExchange(exchange.id, 3L);

        assertEquals(Exchange.Status.REFUSED, response.status);
    }

    @Test
    @TestTransaction
    void acceptExchange_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> exchangeService.acceptExchange(9999L, 1L));
    }

    @Test
    @TestTransaction
    void refuseExchange_nonExistent_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> exchangeService.refuseExchange(9999L, 1L));
    }
}
