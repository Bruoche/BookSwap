package fr.bookswap.books;

import fr.bookswap.books.dto.BookDetailsResponse;
import fr.bookswap.books.dto.BookListResponse;
import fr.bookswap.books.dto.CreateBookRequest;
import fr.bookswap.books.dto.CreateReviewRequest;
import fr.bookswap.common.entity.Author;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.BookRepository;
import fr.bookswap.common.repository.ReviewRepository;
import fr.bookswap.common.repository.UserRepository;
import fr.bookswap.common.exception.BadRequestException;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    @Inject
    ReviewRepository reviewRepository;

    @Inject
    UserRepository userRepository;

	@Inject
	BookAuthorService authorService;

	@Inject
	BookGenreService genreService;

    public BookDetailsResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("This book doesn't exist");
        }
        Double averageRating = reviewRepository.getAverageRating(bookId);
        return BookDetailsResponse.fromBook(book, averageRating);
    }

    public List<Book> getAllBooks(String authors, String genres, int year) {
        return bookRepository.searchByYear(year)
			.stream() // On filtre après requête car logique trop complexe pour requête sql maintenable
			.filter(book -> {
				if (authors == null) {
					return true;
				}
				for (String searchedAuthor : authors.split(" ")) {
					for (Author bookAuthor : book.authors) {
						if (bookAuthor.firstname.toLowerCase().contains(searchedAuthor.toLowerCase()) || bookAuthor.lastname.contains(searchedAuthor.toLowerCase())) {
							return true;
						}
					}
					return false; // Tout les mot-clefs doivent être satisfaits.
				}
				return false;
			}).filter(book -> {
				if (genres == null) {
					return true;
				}
				for (String searchedGenre : genres.split(" ")) {
					for (Genre genre : book.genres) {
						if (genre.name.toLowerCase().contains(searchedGenre.toLowerCase())) {
							return true;
						}
					}
					return false; // Tout les mot-clefs doivent être satisfaits.
				}
				return false;
			}).toList();
    }

    @Transactional
    public Book createBook(Long userId, CreateBookRequest bookDto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
		Set<Author> authors = bookDto.authors
			.stream()
			.map(authorId -> authorService.getAuthorById(authorId))
			.collect(Collectors.toSet());
		Set<Genre> genres = bookDto.genres
			.stream()
			.map(genreId -> genreService.getGenreById(genreId))
			.collect(Collectors.toSet());
        Book newBook = bookDto.toBook(user, authors, genres);
        bookRepository.persist(newBook);
        return newBook;
    }

    @Transactional
    public BookListResponse updateBook(Long bookId, Long userId, CreateBookRequest request, boolean isAdmin) {
        Book book = bookRepository.findById(bookId);
        if  (book == null) {
            throw new NotFoundException(bookId);
        }
		if (book.createdBy.id != userId && !isAdmin) {
			throw new BadRequestException("Vous ne pouvez pas modifier le livre de quelqu'un d'autre.");
		}
		book.isbn = request.isbn;
        book.title = request.title;
        book.description = request.description;
        book.publicationYear = request.publicationYear;
        book.coverUrl = request.coverUrl;
        book.authors = request.authors
			.stream()
			.map(authorId -> authorService.getAuthorById(authorId))
			.collect(Collectors.toSet());
        book.genres = request.genres
			.stream()
			.map(genreId -> genreService.getGenreById(genreId))
			.collect(Collectors.toSet());
        return BookListResponse.fromBook(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Transactional
    public Review addReview(Long bookId, Long currentUserId, CreateReviewRequest request) {
        User currentUser = userRepository.findById(currentUserId);
        if (currentUser == null) {
            throw new UnauthorizedException("Authenticated user not found in database.");
        }
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }
        long existing = reviewRepository.count("author = ?1 and book = ?2", currentUser, book);
        if (existing > 0) {
            throw new BadRequestException("You have already reviewed this book.");
        }

        Review review = new Review(
                currentUser,
                book,
                request.rating,
                request.comment
        );

        reviewRepository.persist(review);

        return review;
    }

    public List<Review>  getReviews(Long bookId) {
        Book book = bookRepository.findById(bookId);
        // Need to handle error with interceptors ?
        if (book == null) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }
        return reviewRepository.findByBookId(bookId);
    }
}
