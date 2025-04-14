package org.example.gymmanagementsystem.service;

import org.example.gymmanagementsystem.model.LoginRequestDto;
import org.example.gymmanagementsystem.model.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
     LoginResponseDto authenticateUser(LoginRequestDto loginRequest);
     boolean validateToken(String token);
     ResponseEntity<String> resetPassword(String email, String code, String newPassword);
     ResponseEntity<String> forgotPassword(String email);
}
