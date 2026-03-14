package fr.bookswap.books;

import fr.bookswap.books.dto.UpdateBookRequest;
import fr.bookswap.common.entity.Book;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.exchange.ExchangeResource;
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
    public List<Book> getAll(
            @QueryParam("author") String author,
            @QueryParam("genre") String genre,
            @QueryParam("publicationYear") int publicationYear
    ) {
        return bookService.getAllBooks(author, genre, publicationYear);
    }

    @GET
    @Path("/{id}")
	@PermitAll
    public BookDetailsResponse getDetails(@PathParam("id") Long id) {
        return bookService.getBookById(id);
    }

    @POST
    public Book add(UpdateBookRequest  updateBookRequest) {
        return bookService.createBook(jwtService.getUserId(), updateBookRequest);
    }

    @PUT
    @Path("/{id}")
    public Book edit(UpdateBookRequest updateBookRequest, @PathParam("id") Long bookId) {
        return bookService.updateBookById(bookId, jwtService.getUserId(), updateBookRequest);
    }

    @RolesAllowed({"ADMIN"})
    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") Long bookId) {
        bookService.deleteBook(bookId, jwtService.getUserId());
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/reviews")
    public List<Review> getReviews(@PathParam("id") Long bookId) {
        return bookService.getReviews(bookId);
    }
}