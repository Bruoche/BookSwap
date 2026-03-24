package fr.bookswap.library;

import java.util.List;

import fr.bookswap.library.dto.BookResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/api/users/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserLibraryResource {

	@Inject
	LibraryService libraryService;
	
    @GET
    @Path("/{username}/library")
    public List<BookResponse> getPublic(
		@PathParam("username") String username,
		@QueryParam("index") int index, 
		@QueryParam("pageSize") int pageSize
	) {
        return libraryService.getUserLibrary(username, index, pageSize)
			.stream()
			.map(book -> BookResponse.fromUserBook(book))
			.toList();
    }

}
