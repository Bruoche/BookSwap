package fr.bookswap.admin;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import fr.bookswap.admin.dto.UserDto;

@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN"})  // Tous les endpoints nécessitent USER ou ADMIN
public class AdminResource {

	@Inject
	AdminService adminService;

    @GET
    @Path("/users")
    public List<UserDto> getUsers() {
		return adminService.getAllUsers()
			.stream()
			.map(user -> UserDto.fromUser(user))
			.toList();
    }

    @PATCH
    @Path("/users/{id}/suspend")
    public UserDto suspendUser(@PathParam("id") Long id) {
        return adminService.suspendUser(id);
    }

    @DELETE
    @Path("/users/{id}")
    public Response removeUser() { //TODO
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
    @Path("/reviews/id")
    public Response removeReview() { //TODO
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
