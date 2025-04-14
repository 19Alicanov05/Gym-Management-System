package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.model.ForgotPasswordRequestDto;
import org.example.gymmanagementsystem.model.LoginRequestDto;
import org.example.gymmanagementsystem.model.LoginResponseDto;
import org.example.gymmanagementsystem.model.ResetPasswordRequestDto;
import org.example.gymmanagementsystem.service.impl.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate"
    )
    public ResponseEntity<LoginResponseDto> createAuthenticationToken(@RequestBody LoginRequestDto authenticationRequest) {
        LoginResponseDto loginResponse = authenticationService.authenticateUser(authenticationRequest);
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password"
    )
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto request) {
        authenticationService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset code has been successfully sent.");
    }


    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password"
    )
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        authenticationService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok("Password has been successfully reset.");
    }


}
