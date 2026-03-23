package fr.bookswap.auth.dto;

import java.time.OffsetDateTime;
import java.util.Set;

import fr.bookswap.common.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class UserResponse {

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
    public String username;

	@NotEmpty(message = "Les roles sont obligatoire")
    public Set<String> roles;

	@NotBlank(message = "L'email est obligatoire")
    public String email;

    public boolean active;

    public OffsetDateTime createdAt;

    public UserResponse() {}

    public static UserResponse fromUser(User user) {
        UserResponse dto = new UserResponse();
		dto.username = user.username;
        dto.roles = user.roles;
        dto.email = user.email;
        dto.active = user.active;
		return dto;
    }
	
}
