package fr.bookswap.common.security;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import fr.bookswap.common.entity.RefreshToken;
import fr.bookswap.common.entity.User;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;
	@ConfigProperty(name = "jwt.expiration.time", defaultValue = "3600")
	Long jwtExpirationTime;
	@ConfigProperty(name = "jwt.refresh.expiration.time", defaultValue = "604800")
	Long refreshExpirationTime;

    @Inject
    JsonWebToken jwt;

	private final SecureRandom secureRandom = new SecureRandom();
	private final Base64.Encoder tokenEncoder = Base64.getUrlEncoder().withoutPadding();

    /**
     * Génère un token d'auth et de rafraîchissement associés.
     * @param user à authentifier.
	 * @return token
     */
    public Token generateTokens(User user) {
		if (!user.active) {
			throw new UnauthorizedException("Le compte est suspendu.");
		}
		String authToken = generateAuthToken(user);
		String refreshToken = generateRefreshToken(user);
		return new Token(authToken, refreshToken, user);
    }

	/**
	 * Génère un token JWT signé pour l'authentification d'un utilisateur donné.
	 * Le nom d'utilisateur servira le "upn" du token.
	 * @param user
	 * @return token string
	 */
	private String generateAuthToken(User user) {
		return Jwt.issuer(issuer)
			.upn(user.username)
			.subject(user.id.toString())
			.groups(user.roles)
			.expiresIn(jwtExpirationTime)
			.sign();
	}

	/**
	 * Persistence d'un token statefull en base de donnée pour le refresh d'un utilisateur donné
	 * (statefull pour pouvoir être révoqué)
	 * @param user authentifié
	 * @return token string
	 */
	private String generateRefreshToken(User user) {
		RefreshToken.deleteAllForUser(user.id);
        RefreshToken refreshToken = new RefreshToken();
		byte[] randomBytes = new byte[32];
		secureRandom.nextBytes(randomBytes);
		refreshToken.token = tokenEncoder.encodeToString(randomBytes);
		refreshToken.user = user;
		refreshToken.expiryDate = Instant.now().plus(refreshExpirationTime, ChronoUnit.SECONDS);
		refreshToken.persist();
		return refreshToken.token;
	}

    public Long getUserId() {
        String sub = jwt.getSubject();
        if (sub == null || sub.isBlank()) {
            throw new UnauthorizedException("User ID not found in token");
        }
        try {
            return Long.valueOf(sub);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token 'sub' claim is not a valid ID: " + sub);
        }
    }

	public boolean isAdmin() {
		return jwt.containsClaim("ADMIN");
	}
}