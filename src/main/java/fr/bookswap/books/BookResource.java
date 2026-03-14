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

import java.time.LocalDateTime;
import java.util.Map;
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
    public Response getAll() { // To verify
        return Response.ok(bookService.getAllBooks()).build();
    }

    @GET
    @Path("/{id}")
    public Response getDetails(@PathParam("id") Long id) { //TODO
        return Response.ok(bookService.getBookById(id)).build();
    }

    @POST
    public Response add() { //TODO
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Not implemented.",
                        "message", "This request has not yet been implemented"
                ))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Book edit(UpdateBookRequest updateBookRequest, @PathParam("id") Long bookId) {
        return bookService.updateBookById(bookId, jwtService.getUserId(), updateBookRequest);
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") Long bookId) {
        bookService.deleteBook(bookId, jwtService.getUserId());
        return Response.ok().build();
    }
}
