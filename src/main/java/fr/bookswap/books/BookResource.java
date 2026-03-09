package fr.bookswap.books;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

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

    @PUT
    @Path("/{id}")
    public Response edit() { //TODO
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Not implemented.",
                        "message", "This request has not yet been implemented"
                ))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove() { //TODO
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
    @Path("/{id}/reviews")
    public Response getReviews() { //TODO
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
    @Path("/{id}/reviews")
    public Response addReview() { //TODO
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
