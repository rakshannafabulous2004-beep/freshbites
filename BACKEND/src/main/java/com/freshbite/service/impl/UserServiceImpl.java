package com.freshbite.service.impl;

import com.freshbite.config.AdminConfig;
import com.freshbite.dto.LoginRequest;
import com.freshbite.dto.RegisterRequest;
import com.freshbite.dto.UserResponse;
import com.freshbite.entity.User;
import com.freshbite.exception.BadRequestException;
import com.freshbite.exception.ResourceNotFoundException;
import com.freshbite.exception.UnauthorizedException;
import com.freshbite.repository.UserRepository;
import com.freshbite.service.UserService;
import com.freshbite.util.MapperUtil;
import com.freshbite.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AdminConfig adminConfig;

    public UserServiceImpl(UserRepository userRepository, AdminConfig adminConfig) {
        this.userRepository = userRepository;
        this.adminConfig = adminConfig;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        if (adminConfig.isAdminEmail(request.getEmail())) {
            throw new BadRequestException("This email cannot be used for registration");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);
        return MapperUtil.toUserResponse(savedUser, false);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        if (adminConfig.isAdminEmail(request.getEmail())) {
            if (!adminConfig.getAdminPassword().equals(request.getPassword())) {
                throw new UnauthorizedException("Invalid email or password");
            }
            return new UserResponse(0L, "Admin", adminConfig.getAdminEmail(), "", true);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return MapperUtil.toUserResponse(user, false);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return MapperUtil.toUserResponse(user, false);
    }
}
