package fr.bookswap.common.security;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Set;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    Inject
    JsonWebToken jwt;

    /**
     * Génère un token JWT signé
     * @param username le nom d'utilisateur (devient le "upn" du token)
     * @param roles les rôles séparés par virgule, ex : "USER,ADMIN"
     */
    public String generateToken(Long userId, String username, Set<String> roles) {
        return Jwt.issuer(issuer)
                .upn(username)
                .subject(userId.toString())
                .groups(roles)
                .expiresIn(3600) // 1 heure
                .sign();
    }

    public String getSubject() {
        return jwt.getSubject();
    }

    public Long getUserId() {
        String sub = getSubject();
        if (sub == null || sub.isBlank()) {
            throw new UnauthorizedException("User ID not found in token");
        }
        try {
            return Long.valueOf(sub);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token 'sub' claim is not a valid ID: " + sub);
        }
    }
}