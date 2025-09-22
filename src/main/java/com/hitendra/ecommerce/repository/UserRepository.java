package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users>  findUsersByUsername(String username);

    boolean existsUsersByUsername(String username);

    boolean existsUsersByEmail(String email);
}
