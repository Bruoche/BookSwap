package fr.bookswap.user;

import fr.bookswap.common.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class UserRepository implements PanacheRepository<User> {
}
