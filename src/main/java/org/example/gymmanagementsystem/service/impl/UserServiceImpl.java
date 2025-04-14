package org.example.gymmanagementsystem.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.BlacklistedTokenEntity;
import org.example.gymmanagementsystem.dao.entity.RoleEntity;
import org.example.gymmanagementsystem.dao.entity.UserEntity;
import org.example.gymmanagementsystem.dao.repository.BlackListedTokenRepository;
import org.example.gymmanagementsystem.dao.repository.UserRepository;
import org.example.gymmanagementsystem.exceptions.InvalidOldPasswordException;
import org.example.gymmanagementsystem.exceptions.UserNotFoundException;
import org.example.gymmanagementsystem.exceptions.UsernameAlreadyExistsException;
import org.example.gymmanagementsystem.mapper.UserMapper;
import org.example.gymmanagementsystem.model.UserDto;
import org.example.gymmanagementsystem.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final BlackListedTokenRepository blackListedTokenRepository;

    @Override
    public void register(UserDto userDto, Boolean isAdmin) {
        log.info("Attempting to register user with username: {}", userDto.getUsername());

        if (userRepository.existsByUsername(userDto.getUsername())) {
            log.error("Username already exists: {}", userDto.getUsername());
            throw new UsernameAlreadyExistsException("USERNAME_ALREADY_EXISTS");
        }

        var entity = userMapper.toUserEntity(userDto);

        if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            entity.setPassword(passwordEncoder.encode(userDto.getPassword()));
            entity.setRoles(getRolesForUser(entity, isAdmin));

            userRepository.save(entity);
            log.info("User registered successfully: {}", userDto.getUsername());
        } else {
            log.error("Passwords do not match for username: {}", userDto.getUsername());
            throw new InvalidOldPasswordException("INVALID_PASSWORD");
        }
    }

    private List<RoleEntity> getRolesForUser(UserEntity entity, Boolean isAdmin) {
        var roles = new ArrayList<RoleEntity>();

        if (isAdmin) {
            roles.add(RoleEntity.builder().name("ROLE_ADMIN").user(entity).build());
        } else {
            roles.add(RoleEntity.builder().name("ROLE_USER").user(entity).build());
        }

        return roles;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword, HttpServletRequest request) {
        log.info("Attempting to change password for user: {}", username);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    throw new UserNotFoundException("USERNAME_NOT_FOUND");
                });

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("Old password does not match for user: {}", username);
            throw new InvalidOldPasswordException("OLD_PASSWORD_NOT_MATCH");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", username);

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token != null && !token.isEmpty()) {
            BlacklistedTokenEntity blacklistedToken = new BlacklistedTokenEntity();
            blacklistedToken.setToken(token);
            blackListedTokenRepository.save(blacklistedToken);
            log.info("Blacklisted old token for user: {}", username);
        }

        SecurityContextHolder.clearContext();
        log.info("Security context cleared for user: {}", username);
    }
}
