package fr.bookswap.authors;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @GET
    public Response getAll() { //TODO
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Not implemented.",
                        "message", "This request has not yet been implemented"
                ))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getDetails() { //TODO
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Not implemented.",
                        "message", "This request has not yet been implemented"
                ))
                .build();
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
}
