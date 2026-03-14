package fr.bookswap.exchange;

import fr.bookswap.common.entity.Exchange;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.exchange.dto.CreateExchangeRequest;
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
    public List<Exchange> getExchanges(@QueryParam("status") String status) {
        if (status != null && !status.isBlank()) return exchangeService.searchUserExchanges(status, jwt.getUserId());
        return exchangeService.getUserExchanges(jwt.getUserId());
    }

    @GET
    @Path("/{id}")
    public Exchange getExchangeById(@PathParam("id") Long id) {
        return exchangeService.getUserExchangeById(id, jwt.getUserId());
    }

    @POST
    public Response createExchange(@Valid CreateExchangeRequest request) {
        Exchange created = exchangeService.createExchange(request.toExchange(), jwt.getUserId());
        return Response.created(URI.create("/api/exchanges/" + created.id))
                .entity(created)
                .build();
    }

    @PATCH
    @Path("/{id}/accept")
    public Exchange acceptExchange(@PathParam("id") Long id) {
        return exchangeService.acceptExchange(id, jwt.getUserId());
    }

    @PATCH
    @Path("/{id}/refuse")
    public Exchange refuseExchange(@PathParam("id") Long id) {
        return exchangeService.refuseExchange(id, jwt.getUserId());
    }
}