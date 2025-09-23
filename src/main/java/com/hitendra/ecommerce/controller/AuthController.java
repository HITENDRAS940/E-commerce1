package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.model.AppRole;
import com.hitendra.ecommerce.model.Role;
import com.hitendra.ecommerce.model.Users;
import com.hitendra.ecommerce.repository.RoleRepository;
import com.hitendra.ecommerce.security.request.LoginRequest;
import com.hitendra.ecommerce.security.response.MessageResponse;
import com.hitendra.ecommerce.security.response.SignUpRequest;
import com.hitendra.ecommerce.security.response.UserInfoResponse;
import com.hitendra.ecommerce.repository.UserRepository;
import com.hitendra.ecommerce.security.jwt.JwtUtils;
import com.hitendra.ecommerce.security.services.UserDetailsImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId() , userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(
                    HttpHeaders.SET_COOKIE,
                    jwtCookie.toString()
                ).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsUsersByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(
                    new MessageResponse("Error: username is already taken"),
                    HttpStatus.BAD_REQUEST
            );
        }
        if(userRepository.existsUsersByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new MessageResponse("Error: Email is already registered"),
                    HttpStatus.BAD_REQUEST
            );
        }

        Users user = new Users(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role userRole = roleRepository
                    .findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(
                    role -> {
                        switch (role) {
                            case "admin":
                                Role adminRole = roleRepository
                                        .findByRoleName(AppRole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                                roles.add(adminRole);
                                break;
                            case "seller":
                                Role sellerRole = roleRepository
                                        .findByRoleName(AppRole.ROLE_SELLER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                                roles.add(sellerRole);
                                break;
                            default:
                                Role userRole = roleRepository
                                        .findByRoleName(AppRole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                                roles.add(userRole);
                        }
                    }
            );
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {

        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response = new UserInfoResponse(userDetails.getId() , userDetails.getUsername(), roles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/username")
    public String getUsername(Authentication authentication) {
        if(authentication!=null)
            return authentication.getName();
        else
            return "";
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        ).body(new MessageResponse("You have been signed out."));
    }
}
