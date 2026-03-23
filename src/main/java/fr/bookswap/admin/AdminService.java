package fr.bookswap.admin;

import java.util.List;

import fr.bookswap.admin.dto.UserDto;
import fr.bookswap.common.entity.Review;
import fr.bookswap.common.entity.User;
import fr.bookswap.common.exception.NotFoundException;
import fr.bookswap.common.repository.ReviewRepository;
import fr.bookswap.common.repository.UserRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AdminService {

	@Inject
	UserRepository userRepository;

	@Inject
	ReviewRepository reviewRepository;

	public List<User> getAllUsers(int index, int pageSize) {
		return userRepository.findAll().page(Page.of(index, pageSize)).list();
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

	@Transactional
	public void deleteReview(Long id) {
		Review review = reviewRepository.findById(id);
		if (review == null) {
			throw new NotFoundException(id);
		}
		reviewRepository.delete(review);
	}
}
