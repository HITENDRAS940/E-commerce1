package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findUsersByUsername(String username);

    boolean existsUsersByUsername(String username);

    boolean existsUsersByEmail(String email);
}
