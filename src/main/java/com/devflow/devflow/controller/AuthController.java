package com.devflow.devflow.controller;

import com.devflow.devflow.dto.AuthResponse;
import com.devflow.devflow.dto.LoginRequest;
import com.devflow.devflow.dto.RegisterRequest;
import com.devflow.devflow.service.AuthService;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public String test(){
        return authService.test();
    }

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request){
        return authService.login(request);
    }

    @GetMapping("/me")
    public String me() {
        return "Authenticated User";
    }
}
