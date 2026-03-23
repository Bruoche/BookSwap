package fr.bookswap.library;

import java.util.List;

import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.LibraryRepository;
import fr.bookswap.library.dto.BookResponse;
import fr.bookswap.library.dto.CreateBookRequest;
import fr.bookswap.library.dto.UpdateBookRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class LibraryService {

	@Inject
	LibraryRepository libraryRepostory;

	public List<UserBook> findByStatus(UserBook.Status status, Long userId) {
		return libraryRepostory.listByStatus(status, userId);
	}

	public UserBook getBookById(Long id, Long userId) {
		UserBook book = libraryRepostory.findForUser(id, userId);
		if (book == null) {
			throw new NotFoundException(id);
		}
		return book;
	}

	public List<UserBook> getUserLibrary(String username) {
		return libraryRepostory.listForUser(username);
	}

	@Transactional
	public BookResponse create(CreateBookRequest request, Long userId) {
		User user = User.findById(userId);
		if (user == null) {
			throw new NotFoundException("L'utilisateur n'existe pas.");
		}
		Book book = Book.findById(request.bookId);
		if (book == null) {
			throw new NotFoundException(request.bookId);
		}
		UserBook userBook = request.toBook(user, book);
		libraryRepostory.persist(userBook);
		return BookResponse.fromUserBook(userBook);
	}

	@Transactional
	public BookResponse updateBook(Long id, UpdateBookRequest request, Long userId) {
		UserBook book = libraryRepostory.findById(id);
		if (book == null) {
			throw new NotFoundException(id);
		}
		if (book.user.id != userId) {
			throw new BadRequestException("Vous ne pouvez pas modifier le livre de quelqu'un d'autre.");
		}
		book.status = request.status;
		book.condition = request.condition;
		book.isAvailableForExchange = request.isAvailableForExchange;
		book.isAvailableForLoan = request.isAvailableForLoan;
		return BookResponse.fromUserBook(book);
	}

	@Transactional
	public void deleteBook(Long id) {
		libraryRepostory.deleteById(id);
	}
}
