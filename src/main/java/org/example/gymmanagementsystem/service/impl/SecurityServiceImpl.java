package org.example.gymmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.RoleEntity;
import org.example.gymmanagementsystem.dao.repository.UserRepository;
import org.example.gymmanagementsystem.exceptions.ForbiddenException;
import org.example.gymmanagementsystem.service.SecurityService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    throw new UsernameNotFoundException("User not found: " + username);
                });

        log.info("User found: {}", username);
        return new User(userEntity.getUsername(), userEntity.getPassword(), getAuthorities(userEntity.getRoles()));
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<RoleEntity> roles) {
        log.info("Fetching authorities for roles: {}", roles);

        return roles.stream()
                .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName()))
                .toList();
    }

    @Override
    public void checkUserPermission(String username) {
        log.info("Checking permissions for user: {}", username);

        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found: " + username);
                });

        boolean hasPermission = userEntity.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (!hasPermission) {
            log.warn("User {} does not have ADMIN permissions.", username);
            throw new ForbiddenException("You do not have permission to access this resource");
        }

        log.info("User {} has ADMIN permissions.", username);
    }
}
