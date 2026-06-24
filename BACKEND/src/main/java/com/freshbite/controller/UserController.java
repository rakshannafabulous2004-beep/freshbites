package com.freshbite.controller;

import com.freshbite.dto.ApiResponse;
import com.freshbite.dto.UserResponse;
import com.freshbite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable Long userId) {
        UserResponse user = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", user));
    }
}
