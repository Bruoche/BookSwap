package fr.bookswap.books;

import fr.bookswap.books.dto.BookDetailsResponse;
import fr.bookswap.books.dto.BookListResponse;
import fr.bookswap.books.dto.CreateBookRequest;
import fr.bookswap.books.dto.CreateReviewRequest;
import fr.bookswap.books.dto.ReviewResponse;
import fr.bookswap.common.security.JwtService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    JwtService jwtService;

    @GET
	@PermitAll
    public List<BookListResponse> getAll(
			@QueryParam("isbn") String isbn,
            @QueryParam("author") String author,
            @QueryParam("genre") String genre,
            @QueryParam("publicationYear") int publicationYear,
			@QueryParam("index") int index, 
			@QueryParam("pageSize") int pageSize
    ) {
        return bookService.getAllBooks(isbn, author, genre, publicationYear, index, pageSize)
			.stream()
			.map(book -> BookListResponse.fromBook(book))
			.toList();
    }

    @GET
    @Path("/{id}")
	@PermitAll
    public BookDetailsResponse getDetails(@PathParam("id") Long id) {
        return bookService.getBookById(id);
    }

    @POST
    public BookListResponse add(CreateBookRequest  request) {
        return BookListResponse.fromBook(bookService.createBook(jwtService.getUserId(), request));
    }

    @PUT
    @Path("/{id}")
    public BookListResponse edit(CreateBookRequest request, @PathParam("id") Long bookId) {
        return bookService.updateBook(bookId, jwtService.getUserId(), request, jwtService.isAdmin());
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response remove(@PathParam("id") Long bookId) {
        bookService.deleteBook(bookId);
        return Response.ok().build();
    }

	@POST
    @Path("/{id}/reviews")
    public ReviewResponse addReview(CreateReviewRequest request, @PathParam("id") Long bookId) {
        return bookService.addReview(bookId, jwtService.getUserId(), request);
    }

    @GET
    @Path("/{id}/reviews")
	@PermitAll
    public List<ReviewResponse> getReviews(@PathParam("id") Long bookId) {
        return bookService.getReviews(bookId)
			.stream()
			.map(review -> ReviewResponse.fromReview(review))
			.toList();
    }
}
