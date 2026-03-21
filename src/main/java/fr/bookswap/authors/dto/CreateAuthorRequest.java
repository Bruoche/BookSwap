package fr.bookswap.authors.dto;

import fr.bookswap.common.entity.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAuthorRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    public String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    public String lastname;

    public CreateAuthorRequest() {}

    public Author toAuthor() {
        Author author = new Author();
		author.firstname = this.firstname;
        author.lastname = this.lastname;
		return author;
    }

}
