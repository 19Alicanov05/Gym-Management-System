package org.example.gymmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.dao.entity.PasswordResetTokenEntity;
import org.example.gymmanagementsystem.dao.entity.RoleEntity;
import org.example.gymmanagementsystem.dao.entity.UserEntity;
import org.example.gymmanagementsystem.dao.repository.TokenRepository;
import org.example.gymmanagementsystem.dao.repository.UserRepository;
import org.example.gymmanagementsystem.exceptions.NotFoundException;
import org.example.gymmanagementsystem.exceptions.UnauthorizedException;
import org.example.gymmanagementsystem.model.LoginRequestDto;
import org.example.gymmanagementsystem.model.LoginResponseDto;
import org.example.gymmanagementsystem.service.AuthenticationService;
import org.example.gymmanagementsystem.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Incorrect Username or Password");
        }

        var userEntity = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.getUsername()));

        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                getAuthorities(userEntity.getRoles())
        );

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new LoginResponseDto(jwt);
    }

    public boolean validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            return jwtTokenUtil.validateToken(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid Token");
        }
    }



    private List<org.springframework.security.core.authority.SimpleGrantedAuthority> getAuthorities(List<RoleEntity> roles) {
        return roles.stream()
                .map(roleEntity -> new org.springframework.security.core.authority.SimpleGrantedAuthority(roleEntity.getName()))
                .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<String> forgotPassword(String email) {
        log.info("Password reset request: {}", email);

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.error("User with email {} not found", email);
            throw new NotFoundException("User with the provided email address not found.");
        }

        String verificationCode = String.format("%06d", new Random().nextInt(1000000));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(2);

        tokenRepository.findByEmailAndTokenAndIsUsedFalse(email, verificationCode)
                .ifPresent(token -> {
                    token.setUsed(true);
                    tokenRepository.save(token);
                });

        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity(email, verificationCode, expiresAt);
        tokenRepository.save(resetToken);

        emailService.sendSimpleEmail(email, "Password Reset Code", "Your code: " + verificationCode);
        log.info("Password reset code sent to email: {}", email);

        return ResponseEntity.ok("Password reset code has been sent to your email.");
    }



    @Override
    @Transactional
    public ResponseEntity<String> resetPassword(String email, String code, String newPassword) {
        log.info("Password reset attempt for email: {}", email);

        Optional<PasswordResetTokenEntity> tokenOptional = tokenRepository.findByEmailAndTokenAndIsUsedFalse(email, code);
        if (tokenOptional.isEmpty()) {
            log.warn("Invalid or expired token for password reset: {}", email);
            throw new NotFoundException("Invalid or expired token.");
        }

        if (tokenOptional.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Expired token for password reset: {}", email);
            throw new NotFoundException("Token has expired.");
        }

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("User not found during password reset: {}", email);
            throw new NotFoundException("User not found.");
        }

        UserEntity user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));

        PasswordResetTokenEntity resetToken = tokenOptional.get();
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        userRepository.save(user);

        log.info("Password successfully reset for user: {}", email);
        return ResponseEntity.ok("Password successfully reset.");
    }



}
