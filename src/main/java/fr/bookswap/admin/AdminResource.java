package fr.bookswap.admin;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import fr.bookswap.admin.dto.UserDto;
import fr.bookswap.common.security.JwtService;

@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN"})  // Tous les endpoints nécessitent USER ou ADMIN
public class AdminResource {

	@Inject
	JwtService jwt;

	@Inject
	AdminService adminService;

    @GET
    @Path("/users")
    public List<UserDto> getUsers(@QueryParam("index") int index, @QueryParam("pageSize") int pageSize) {
		return adminService.getAllUsers(index, pageSize)
			.stream()
			.map(user -> UserDto.fromUser(user))
			.toList();
    }

    @PATCH
    @Path("/users/{id}/suspend")
    public UserDto suspendUser(@PathParam("id") Long id) {
        return adminService.suspendUser(id, jwt.getUserId());
    }

    @DELETE
    @Path("/users/{id}")
    public Response removeUser(@PathParam("id") Long id) {
		adminService.deleteUser(id, jwt.getUserId());
        return Response.accepted().build();
    }

    @DELETE
    @Path("/reviews/{id}")
    public Response removeReview(@PathParam("id") Long id) {
        adminService.deleteReview(id);
		return Response.accepted().build();
    }
}
