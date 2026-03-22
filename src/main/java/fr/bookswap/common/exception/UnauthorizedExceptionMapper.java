package fr.bookswap.common.exception;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.Map;

@Provider  // Enregistre automatiquement ce mapper dans JAX-RS
public class UnauthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    @Override
    public Response toResponse(NotAuthorizedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
			.entity(Map.of(
					"timestamp", LocalDateTime.now().toString(),
					"status", 401,
					"error", "Unauthorized",
					"message", String.join(", ", 
						exception.getChallenges()
							.stream()
							.map(object -> object.toString())
							.toList()
						)
			))
			.build();
    }
}