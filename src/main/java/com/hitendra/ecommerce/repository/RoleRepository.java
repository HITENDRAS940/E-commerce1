package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.AppRole;
import com.hitendra.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole roleName);
}
