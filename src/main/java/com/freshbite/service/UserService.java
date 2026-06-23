package com.freshbite.service;

import com.freshbite.dto.LoginRequest;
import com.freshbite.dto.RegisterRequest;
import com.freshbite.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse login(LoginRequest request);

    UserResponse getProfile(Long userId);
}
