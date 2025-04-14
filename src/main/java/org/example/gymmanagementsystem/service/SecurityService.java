package org.example.gymmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    public void checkUserPermission(String username);
}
