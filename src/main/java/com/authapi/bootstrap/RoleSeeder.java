package com.authapi.bootstrap;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.authapi.entities.Role;
import com.authapi.entities.RoleEnum;
import com.authapi.repositories.RoleRepository;

public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

	private final RoleRepository roleRepository;

	public RoleSeeder(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.loadRoles();

	}

	private void loadRoles() {
		RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN };
		Map<RoleEnum, String> roleDescriptionMap = Map.of(RoleEnum.USER, "Default user role", RoleEnum.ADMIN,
				"Administrator role", RoleEnum.SUPER_ADMIN, "Super Administrator role");

		Arrays.stream(roleNames).forEach((roleName) -> {
			Optional<Role> optionalRole = roleRepository.findByName(roleName);

			optionalRole.ifPresentOrElse(System.out::println, () -> {
				Role roleToCreate = new Role();

				roleToCreate.setName(roleName);
				roleToCreate.setDescription(roleDescriptionMap.get(roleName));

				roleRepository.save(roleToCreate);
			});
		});
	}

}
