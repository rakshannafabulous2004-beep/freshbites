package com.freshbite.controller;

import com.freshbite.dto.ApiResponse;
import com.freshbite.dto.LoginRequest;
import com.freshbite.dto.RegisterRequest;
import com.freshbite.dto.UserResponse;
import com.freshbite.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse user = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", user));
    }
}
