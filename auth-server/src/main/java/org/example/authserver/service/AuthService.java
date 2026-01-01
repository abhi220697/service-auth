package org.example.authserver.service;


import Requests.CreateUserRequest;
import Responses.UserResponse;
import jakarta.transaction.Transactional;
import org.apache.catalina.webresources.CachedResource;
import org.example.authclient.request.RegisterRequest;
import org.example.authclient.response.AuthResponse;
import org.example.authserver.client.UserServiceClient;
import org.example.authserver.data.AuthUser;
import org.example.authserver.data.Role;
import org.example.authserver.repository.AuthRepository;
import org.example.authserver.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {


    private final AuthRepository  authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RoleRepository roleRepository;
    private final UserServiceClient userServiceClient;


    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, RoleRepository roleRepository, UserServiceClient userServiceClient) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.roleRepository = roleRepository;
        this.userServiceClient = userServiceClient;
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request){

        if (authRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }


        UUID userId = UUID.randomUUID();
        AuthUser authUser = new AuthUser();
        authUser.setId(userId);
        authUser.setUsername(request.username());
        authUser.setEmail(request.email());
        authUser.setPassword(
                passwordEncoder.encode(request.password())
        );

        Set<Role> roles = new HashSet<>();

        if (request.roles() == null || request.roles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow();
            roles.add(userRole);
        } else {
            request.roles().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Invalid role"));
                roles.add(role);
            });
        }

        authRepository.save(authUser);
        //todo
        // call the user-service client here

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail(request.email());
        createUserRequest.setName(request.firstName()+request.lastName());
        createUserRequest.setPhoneNo(request.phone());
        createUserRequest.setAge(25);
        createUserRequest.setPassword(authUser.getPassword());
        ResponseEntity<UserResponse> response = userServiceClient.createUser(createUserRequest);


        String accessToken = jwtTokenService.generateAccessToken(authUser);
        AuthResponse authResponse =  AuthResponse.builder().
                accessToken(accessToken).
                userId(userId).
                refreshToken(accessToken).
                username(request.username()).
                roles(authUser.getRoles().stream().map(Role::getName).toArray(String[]::new)).build();

        return null;
    }
}
