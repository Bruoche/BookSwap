package fr.bookswap.auth;

import fr.bookswap.common.entity.User;
import fr.bookswap.auth.dto.EditUserRequest;
import fr.bookswap.common.security.Token;
import fr.bookswap.auth.dto.LoginRequest;
import fr.bookswap.auth.dto.LoginResponse;
import fr.bookswap.auth.dto.RegisterRequest;
import fr.bookswap.auth.dto.UserResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

	private static final Logger LOG = Logger.getLogger(AuthResource.class);

	@ConfigProperty(name = "jwt.refresh.expiration.time", defaultValue = "604800")
	Long refreshExpirationTime;

    @Inject
    AuthService authService;

    /**
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    @PermitAll  // Accessible sans token
    public Response login(@Valid LoginRequest request) {
        LOG.info("Tentative de connexion : " + request.username);
        Token token = authService.login(request.username, request.password);
		return buildAuthResponse(token);
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
	@RolesAllowed({"USER", "ADMIN"})
    public UserResponse userProfile() {
        return UserResponse.fromUser(authService.getUser());
    }

    @PUT
    @Path("/me")
	@RolesAllowed({"USER", "ADMIN"})
    public UserResponse editProfile(@Valid EditUserRequest request) {
        return UserResponse.fromUser(authService.editUser(request));
    }

	@PATCH
	@Path("/password")
	@RolesAllowed({"USER", "ADMIN"})
	public UserResponse changePassword(@QueryParam("old_password") String oldPassword, @QueryParam("new_password") String newPassword) {
		return authService.editPassword(oldPassword, newPassword); 
	}

    @POST
    @Path("/refresh")
    public Response refreshToken(@CookieParam(COOKIE_REFRESH_TOKEN) String refreshToken) {
		Token token = authService.refreshAuth(refreshToken);
        return buildAuthResponse(token);
    }

	private Response buildAuthResponse(Token token) {
        LoginResponse response = new LoginResponse(token.authToken, token.user.username, token.user.roles);
		NewCookie cookie = new NewCookie.Builder(COOKIE_REFRESH_TOKEN)
			.value(token.refreshToken)
			.path("api/auth/refresh")
			.httpOnly(true)
			.secure(true)
			.maxAge(refreshExpirationTime.intValue())
			.build();
		return Response.ok(response)
			.cookie(cookie)
			.build();
	}
}