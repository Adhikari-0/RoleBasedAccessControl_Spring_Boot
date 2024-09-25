package com.authapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authapi.entities.Role;
import com.authapi.entities.RoleEnum;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Optional<Role> findByName(RoleEnum name);

}
