package fr.bookswap.admin.dto;


import java.time.OffsetDateTime;
import java.util.Set;

import fr.bookswap.common.entity.User;

public class UserDto {

    public String username;

    public Set<String> roles;  // Ex: "USER" ou "USER,ADMIN"

    public String email;

    public boolean active;

    public OffsetDateTime createdAt;

    public UserDto() {}

    public static UserDto fromUser(User user) {
		UserDto dto = new UserDto();
        dto.username = user.username;
        dto.roles = user.roles;
        dto.email = user.email;
        dto.active = user.active;
		dto.createdAt = user.createdAt;
		return dto;
    }
}