package fr.bookswap.auth;

import fr.bookswap.auth.dto.EditUserRequest;
import fr.bookswap.auth.dto.UserResponse;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.entity.RefreshToken;
import fr.bookswap.common.exception.BadRequestException;
import fr.bookswap.common.security.JwtService;
import fr.bookswap.common.security.Token;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    JwtService jwtService;

    /**
     * Vérifie les credentials et retourne un token JWT
     */
	@Transactional
    public Token login(String username, String password) {
        User user = User.findByUsername(username);

        if (user == null || !checkPassword(password, user.password)) {
            throw new NotAuthorizedException("Identifiants invalides");
        }
        return jwtService.generateTokens(user);
    }

    /**
     * Crée un nouvel utilisateur avec le rôle USER
     */
    @Transactional
    public User register(String username, String password, String email) {
        if (User.findByUsername(username) != null) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }
        // BcryptUtil.bcryptHash hash le mot de passe de façon sécurisée
        String hashedPassword = BcryptUtil.bcryptHash(password);
        User user = new User(username, hashedPassword, new HashSet<>(Set.of("USER")), email);
        user.persist();
        return user;
    }

	public User getUser() {
		Long id = jwtService.getUserId();
		User user = User.findById(id);
		if (user == null) {
			throw new NotFoundException(id);
		}
		return user;
	}

	@Transactional
	public User editUser(EditUserRequest request) {
		User user = getUser();
		user.username = request.username;
		user.email = request.email;
		return user;
	}

	@Transactional
	public UserResponse editPassword(String oldPassword, String newPassword) {
		User user = getUser();
		if (!checkPassword(oldPassword, user.password)) {
			throw new BadRequestException("L'ancien mot de passe est incorrect.");
		}
		user.password = BcryptUtil.bcryptHash(newPassword);
		return UserResponse.fromUser(user);
	}

	private boolean checkPassword(String tested, String password) {
		return BcryptUtil.matches(tested, password);
	}

	/**
	 * Rafraîchissement du token d'authentification.
	 * @param tokenString de rafraîchissement
	 * @return nouveau tokens
	 */
	@Transactional
	public Token refreshAuth(String tokenString) {
		RefreshToken token = RefreshToken.find(tokenString);
		if (token == null) {
			throw new BadRequestException("Le token de rafraîchissement n'existe pas");
		}
		if (token.expiryDate.isBefore(Instant.now())) {
			token.delete();
			throw new BadRequestException("Token de rafraîchissement expiré.");
		}
		return jwtService.generateTokens(token.user);
	}
}