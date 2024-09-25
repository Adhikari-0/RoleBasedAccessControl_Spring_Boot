package com.authapi.bootstrap;

import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.authapi.dtos.RegisterUserDto;
import com.authapi.entities.Role;
import com.authapi.entities.RoleEnum;
import com.authapi.entities.User;
import com.authapi.repositories.RoleRepository;
import com.authapi.repositories.UserRepository;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public AdminSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		RoleSeeder sdaSeeder = new RoleSeeder(roleRepository);
		sdaSeeder.onApplicationEvent(event);
		this.createSuperAdministrator();

	}

	private void createSuperAdministrator() {
		RegisterUserDto userDto = new RegisterUserDto();
		userDto.setFullName("Super Admin");
		userDto.setEmail("super.admin@email.com");
		userDto.setPassword("123456");

		Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
		Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

		if (optionalRole.isEmpty() || optionalUser.isPresent()) {
			return;
		}

		User user = new User();
		user.setFullName(userDto.getFullName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRole(optionalRole.get());

		userRepository.save(user);
	}

}
