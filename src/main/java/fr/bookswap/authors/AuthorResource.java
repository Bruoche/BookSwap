package fr.bookswap.authors;

import fr.bookswap.authors.dto.CreateAuthorRequest;
import fr.bookswap.common.entity.Author;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

	@Inject 
	AuthorService authorService;

    @GET
    public List<Author> getAll() {
        return authorService.getAll();
    }

    @GET
    @Path("/{id}")
    public Author getDetails(@PathParam("id") Long id) {
        return authorService.getById(id);
    }

    @POST
	@RolesAllowed({"USER", "ADMIN"})
    public Response createAuthor(@Valid CreateAuthorRequest request) {
        Author created = authorService.createAuthor(request.toAuthor());
		return Response.created(URI.create("/api/author/" + created.id))
                .entity(created)
                .build();
    }
}
