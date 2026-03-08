package fr.bookswap.common.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@UserDefinition  // Indique à Quarkus que c'est l'entité d'authentification
public class User extends PanacheEntity {

    @Username
    @Column(unique = true, nullable = false)
    public String username;

    @Password  // Quarkus gère le hash BCrypt automatiquement à la vérification
    @Column(nullable = false)
    public String password;

    @Roles
    @Column(nullable = false)
    public Set<String> roles;  // Ex: "USER" ou "USER,ADMIN"

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public boolean active;

    @CreationTimestamp
    @Column(nullable = false)
    public OffsetDateTime createdAt;

    public User() {}

    public User(String username, String password, Set<String> roles, String email) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.active = true;
    }

    public static User findByUsername(String username) {
        return find("username", username).firstResult();
    }
}