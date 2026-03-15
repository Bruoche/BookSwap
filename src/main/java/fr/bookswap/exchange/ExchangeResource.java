package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.exchange.dto.CreateExchangeRequest;
import fr.bookswap.exchange.dto.ExchangeResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/api/exchanges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"USER", "ADMIN"})  // Tous les endpoints nécessitent USER ou ADMIN
public class ExchangeResource {

    @Inject
    ExchangeService exchangeService;

    @Inject
    JwtService jwt;  // Token JWT de l'utilisateur connecté

    @GET
    public List<ExchangeResponse> getExchanges(@QueryParam("status") String status) {
        if (status != null && !status.isBlank()) {
			return exchangeService.searchUserExchanges(status, jwt.getUserId())
				.stream()
				.map(exchange -> ExchangeResponse.fromExchange(exchange))
				.toList();
		}
        return exchangeService.getUserExchanges(jwt.getUserId())
			.stream()
			.map(exchange -> ExchangeResponse.fromExchange(exchange))
			.toList();
    }

    @GET
    @Path("/{id}")
    public ExchangeResponse getExchangeById(@PathParam("id") Long id) {
        return ExchangeResponse.fromExchange(exchangeService.getUserExchangeById(id, jwt.getUserId()));
    }

    @POST
    public Response createExchange(@Valid CreateExchangeRequest request) {
        Exchange created = exchangeService.createExchange(request.toExchange(jwt.getUserId()));
        return Response.created(URI.create("/api/exchanges/" + created.id))
                .entity(created)
                .build();
    }

    @PATCH
    @Path("/{id}/accept")
    public ExchangeResponse acceptExchange(@PathParam("id") Long id) {
        return ExchangeResponse.fromExchange(exchangeService.acceptExchange(id, jwt.getUserId()));
    }

    @PATCH
    @Path("/{id}/refuse")
    public ExchangeResponse refuseExchange(@PathParam("id") Long id) {
        return ExchangeResponse.fromExchange(exchangeService.refuseExchange(id, jwt.getUserId()));
    }
}