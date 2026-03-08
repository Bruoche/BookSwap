package fr.bookswap.common.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtUtils {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    /**
     * Génère un token JWT signé
     * @param username le nom d'utilisateur (devient le "upn" du token)
     * @param roles les rôles séparés par virgule, ex : "USER,ADMIN"
     */
    public String generateToken(String username, Set<String> roles) {
        return Jwt.issuer(issuer)
                .upn(username)
                .groups(roles)
                .expiresIn(3600) // 1 heure
                .sign();
    }
}