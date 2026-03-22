package fr.bookswap.common.security;

import fr.bookswap.common.entity.User;
import jakarta.validation.constraints.NotBlank;

public class Token {

	@NotBlank(message = "Le token d'authentification est obligatoire")
    public String authToken;
	public String refreshToken;
	public User user;

	public Token(String authToken, String refreshToken, User user) {
		this.authToken = authToken;
		this.refreshToken = refreshToken;
		this.user = user;
	}
}
