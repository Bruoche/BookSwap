package fr.bookswap.admin;

import java.util.List;

import fr.bookswap.admin.dto.UserDto;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdminService {

	@Inject
	UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.listAll();
	}

	public User getUserById(Long id) {
		User user = userRepository.findById(id);
		if (user == null) {
			throw new NotFoundException(id);
		}
		return user;
	}

	@Transactional
	public UserDto suspendUser(Long id) {
		User user = getUserById(id);
		user.active = false;
		userRepository.persist(user);
		return UserDto.fromUser(user);
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}
}
