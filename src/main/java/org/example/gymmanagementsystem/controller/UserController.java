package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymmanagementsystem.model.ChangePasswordRequest;
import org.example.gymmanagementsystem.model.UserDto;
import org.example.gymmanagementsystem.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register user"
    )
    public String register(@RequestBody @Valid UserDto userDto) {
        userService.register(userDto, false);
        return "User registered successfully!";
    }

    @PostMapping("/register/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register admin"
    )
    public String registerAdmin(@RequestBody @Valid UserDto userDto) {
        userService.register(userDto, true);
        return "Admin registered successfully!";
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Change password"
    )
    public String changePassword(@RequestBody @Valid ChangePasswordRequest request, HttpServletRequest httpRequest) {
        userService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword(), httpRequest);
        return "Password changed successfully! Please log in again.";
    }

}
