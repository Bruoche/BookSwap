package fr.bookswap.library;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import fr.bookswap.common.entity.UserBook;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.library.dto.BookResponse;
import fr.bookswap.library.dto.CreateBookRequest;
import fr.bookswap.library.dto.UpdateBookRequest;

@Path("/api/library")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER"})
public class LibraryResource {

	@Inject
	LibraryService libraryService;

	@Inject
	JwtService jwt;

    @GET
    public List<BookResponse> getAll(@QueryParam("status") UserBook.Status status) {
		return libraryService.findByStatus(status, jwt.getUserId())
			.stream()
			.map(book -> BookResponse.fromUserBook(book))
			.toList();
    }

    @GET
	@Path("/{id}")
    public BookResponse getById(@PathParam("id") Long id) {
		return BookResponse.fromUserBook(libraryService.getBookById(id, jwt.getUserId()));
    }

    @POST
    public Response addBook(@Valid CreateBookRequest request) {
		BookResponse created = libraryService.create(request, jwt.getUserId());
        return Response.created(URI.create("api/library/" + created.id))
			.entity(created)
			.build();
    }

    @PUT
    @Path("/{id}")
    public BookResponse edit(@PathParam("id") Long id, @Valid UpdateBookRequest request) {
        return libraryService.updateBook(id, request, jwt.getUserId());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        libraryService.deleteBook(id);
		return Response.ok().build();
    }
}
