package fr.bookswap.common.entity;

import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "refresh_token")
public class RefreshToken extends PanacheEntity {
	
	@NotBlank(message = "Le token est obligatoire")
	public String token;
	
	@NotNull
	public Instant expiryDate;

	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public User user;

	public static RefreshToken find(String token) {
		return find("token", token).firstResult();
	}

	public static void deleteAllForUser(Long userId) {
		delete("user.id", userId);
	}
}
