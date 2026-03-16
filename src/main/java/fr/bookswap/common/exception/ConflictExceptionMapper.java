package fr.bookswap.common.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.Map;

@Provider  // Enregistre automatiquement ce mapper dans JAX-RS
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Override
    public Response toResponse(ConflictException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 409,
                        "error", "Conflict",
                        "message", exception.getMessage()
                ))
                .build();
    }
}