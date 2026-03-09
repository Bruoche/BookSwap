package fr.bookswap.auth;

import fr.bookswap.common.entity.User;
import fr.bookswap.auth.dto.LoginRequest;
import fr.bookswap.auth.dto.LoginResponse;
import fr.bookswap.auth.dto.RegisterRequest;
import fr.bookswap.common.exception.ErrorResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    AuthService authService;

    /**
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    @PermitAll  // Accessible sans token
    public LoginResponse login(@Valid LoginRequest request) {
        LOG.info("Tentative de connexion : " + request.username);
        String token = authService.login(request.username, request.password);
        User user = User.findByUsername(request.username);
        return new LoginResponse(token, user.username, user.roles);
    }

    /**
     * POST /api/auth/register
     */
    @POST
    @Path("/register")
    @PermitAll  // Accessible sans token
    public Response register(@Valid RegisterRequest request) {
        LOG.info("Inscription : " + request.username);
        User user = authService.register(request.username, request.password, request.email);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @GET
    @Path("/me")
    public Response userProfile() { //TODO
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
    @Path("/me")
    public Response editProfile() { //TODO
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
    @Path("/refresh")
    public Response refreshToken() { //TODO
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