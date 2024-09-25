package com.authapi.services;

import com.authapi.dtos.RegisterUserDto;
import com.authapi.entities.Role;
import com.authapi.entities.RoleEnum;
import com.authapi.entities.User;
import com.authapi.repositories.RoleRepository;
import com.authapi.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
//	private final UserRepository userRepository;
//
//	public UserService(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
//
//	public List<User> allUsers() {
//		List<User> users = new ArrayList<>();
//
//		userRepository.findAll().forEach(users::add);
//
//		return users;
//	}

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> allUsers() {
		List<User> users = new ArrayList<>();

		userRepository.findAll().forEach(users::add);

		return users;
	}

	public User createAdministrator(RegisterUserDto input) {
		Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

		if (optionalRole.isEmpty()) {
			return null;
		}

		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setRole(optionalRole.get());

		return userRepository.save(user);
	}
}