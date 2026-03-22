package fr.bookswap.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class EditUserRequest {

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
    public String username;

	@NotBlank(message = "Le mot de passe est obligatoire")
    public String password;

	@NotBlank(message = "L'email est obligatoire")
    public String email;

    public EditUserRequest() {}
	
}
