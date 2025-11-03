package com.hitendra.ecommerce.utils;

import com.hitendra.ecommerce.model.User;
import com.hitendra.ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findUsersByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository
                .findUsersByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }
}
