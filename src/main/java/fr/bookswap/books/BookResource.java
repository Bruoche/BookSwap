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
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.OffsetDateTime;
import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})  // Are we sure there is no completely open endpoint ?
public class BookResource {

    private static final Logger LOG = Logger.getLogger(ExchangeResource.class);

    @Inject
    BookService bookService;

    @Inject
    JwtService jwtService;

    @GET
    public List<Book> getAll(
            @RestQuery String author,
            @RestQuery String genre,
            @RestQuery int publicationYear
    ) {
        return bookService.getAllBooks(author, genre, publicationYear);
    }

    @GET
    @Path("/{id}")
    public Book getDetails(@PathParam("id") Long id) { //TODO
        return bookService.getBookById(id);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @POST
    public Book add(UpdateBookRequest  updateBookRequest) {
        return bookService.createBook(jwtService.getUserId(), updateBookRequest);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PUT
    @Path("/{id}")
    public Book edit(UpdateBookRequest updateBookRequest, @PathParam("id") Long bookId) {
        return bookService.updateBookById(bookId, jwtService.getUserId(), updateBookRequest);
    }

    @RolesAllowed({"ADMIN"})
    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") Long bookId) {
        bookService.deleteBook(bookId);
        return Response.ok().build();
    }
}
