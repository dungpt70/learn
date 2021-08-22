package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.model.Role;

public interface RoleService {
	List<Role> getAllRoles();
	Role findById (Long roleId);
}
