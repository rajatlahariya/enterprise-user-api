package com.rajat.user_api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.rajat.user_api.specification.UserSpecification;
import com.rajat.user_api.dto.request.UserPatchRequest;
import com.rajat.user_api.dto.request.UserRequest;
import com.rajat.user_api.dto.request.UserUpdateRequest;
import com.rajat.user_api.dto.response.UserResponse;
import com.rajat.user_api.entity.User;
import com.rajat.user_api.exception.UserNotFoundException;
import com.rajat.user_api.mapper.UserMapper;
import com.rajat.user_api.repository.UserRepository;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public UserResponse saveUser(UserRequest request) {

		logger.info("Creating new user with email: {}", request.getEmail());

		User user = userMapper.toEntity(request);

		User savedUser = userRepository.save(user);

		logger.info("User created successfully with id: {}", savedUser.getId());

		return userMapper.toResponse(savedUser);
	}

	public List<UserResponse> getAllUsers() {

		logger.info("Fetching all users");

		List<UserResponse> users = userRepository.findAll()
				.stream()
				.map(userMapper::toResponse)
				.toList();

		logger.info("Fetched {} users", users.size());

		return users;
	}

	public UserResponse getUserById(Long id) {

		logger.info("Fetching user with id: {}", id);

		User user = findUserOrThrow(id);

		logger.info("User found with id: {}", id);

		return userMapper.toResponse(user);
	}

	public UserResponse updateUser(Long id, UserUpdateRequest request) {

		logger.info("Updating user with id: {}", id);

		User user = findUserOrThrow(id);

		userMapper.updateEntity(user, request);

		User updatedUser = userRepository.save(user);

		logger.info("User updated successfully with id: {}", updatedUser.getId());

		return userMapper.toResponse(updatedUser);
	}

	public UserResponse patchUser(Long id, UserPatchRequest request) {

		logger.info("Partially updating user with id: {}", id);

		User user = findUserOrThrow(id);

		userMapper.patchEntity(user, request);

		User patchedUser = userRepository.save(user);

		logger.info("User partially updated successfully with id: {}", patchedUser.getId());

		return userMapper.toResponse(patchedUser);
	}

	@Transactional
	public void deleteUser(Long id) {

		logger.info("Soft deleting user with id: {}", id);

		User user = findUserOrThrow(id);
		user.setIsActive(false);
		userRepository.save(user);

		logger.info("User soft deleted successfully with id: {}", id);
	}

	private User findUserOrThrow(Long id) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.warn("User not found with id: {}", id);
	                return new UserNotFoundException(id);
	            });

	    if (Boolean.FALSE.equals(user.getIsActive())) {
	        logger.warn("User with id {} is inactive", id);
	        throw new UserNotFoundException(id);
	    }

	    return user;
	}

	public Page<UserResponse> getUsers(Pageable pageable) {

		logger.info("Fetching users with pagination: page={}, size={}",
				pageable.getPageNumber(),
				pageable.getPageSize());

		Page<UserResponse> users = userRepository.findAll(pageable)
				.map(userMapper::toResponse);

		logger.info("Fetched {} users out of total {} users",
				users.getNumberOfElements(),
				users.getTotalElements());

		return users;
	}

	public UserResponse getUserByEmail(String email) {

		logger.info("Searching user by email: {}", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() ->
				new UserNotFoundException("User not found with email: " + email));

		logger.info("User found with email: {}", email);

		return userMapper.toResponse(user);
	}

	public Page<UserResponse> searchUsers(String firstName,
			String username,
			String email,
			String role,
			Boolean isActive,
			Integer minAge,
			Integer maxAge,
			Pageable pageable) {

		logger.info(
				"Searching users with filters: firstName={}, username={}, email={}, role={}, isActive={}, minAge={}, maxAge={}, page={}, size={}, sort={}",
				firstName,
				username,
				email,
				role,
				isActive,
				minAge,
				maxAge,
				pageable.getPageNumber(),
				pageable.getPageSize(),
				pageable.getSort()
				);

		Specification<User> spec = Specification
				.where(UserSpecification.hasFirstName(firstName))
				.and(UserSpecification.hasUsername(username))
				.and(UserSpecification.hasEmail(email))
				.and(UserSpecification.hasRole(role))
				.and(UserSpecification.hasActiveStatus(isActive))
				.and(UserSpecification.ageGreaterThanOrEqual(minAge))
				.and(UserSpecification.ageLessThanOrEqual(maxAge));

		Page<UserResponse> users = userRepository.findAll(spec, pageable)
				.map(userMapper::toResponse);

		logger.info("Filtered users found: {}", users.getTotalElements());

		return users;
	}



}