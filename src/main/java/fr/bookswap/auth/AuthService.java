package fr.bookswap.auth;

import fr.bookswap.common.entity.User;
import fr.bookswap.common.security.JwtUtils;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    JwtUtils jwtService;

    /**
     * Vérifie les credentials et retourne un token JWT
     */
    public String login(String username, String password) {
        User user = User.findByUsername(username);

        if (user == null || !BcryptUtil.matches(password, user.password)) {
            throw new NotAuthorizedException("Identifiants invalides");
        }

        return jwtService.generateToken(user.username, user.roles);
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
}