package com.authapi.services;

import com.authapi.dtos.LoginUserDto;
import com.authapi.dtos.RegisterUserDto;
import com.authapi.entities.Role;
import com.authapi.entities.RoleEnum;
import com.authapi.entities.User;
import com.authapi.repositories.RoleRepository;
import com.authapi.repositories.UserRepository;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

//	public User signup(RegisterUserDto input) {
//
//		User user = new User();
//		user.setFullName(input.getFullName());
//		user.setEmail(input.getEmail());
//		user.setPassword(passwordEncoder.encode(input.getPassword()));
//
//		return userRepository.save(user);
//	}

	public User signup(RegisterUserDto input) {
		Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
		System.out.println("optionalRole: "+optionalRole.get());

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

	public User authenticate(LoginUserDto input) {
		System.out.println(input.getEmail()+ " "+ input.getPassword());
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}
}