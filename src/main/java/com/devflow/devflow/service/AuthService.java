package com.devflow.devflow.service;

import com.devflow.devflow.dto.AuthResponse;
import com.devflow.devflow.dto.LoginRequest;
import com.devflow.devflow.dto.RegisterRequest;
import com.devflow.devflow.entity.User;
import com.devflow.devflow.entity.Role;
import com.devflow.devflow.repository.RoleRepository;
import com.devflow.devflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String test() {
        return "Auth Service Working";
    }

    public String register(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already exists";
        }

        Role employeeRole = roleRepository
                .findByName("EMPLOYEE")
                .orElseThrow(() ->
                        new RuntimeException("EMPLOYEE role not found"));

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(employeeRole);

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Invalid Password");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }
}
