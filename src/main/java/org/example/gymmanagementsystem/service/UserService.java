package org.example.gymmanagementsystem.service;
import jakarta.servlet.http.HttpServletRequest;
import org.example.gymmanagementsystem.model.UserDto;

public interface UserService {
    void register(UserDto userDto, Boolean isAdmin);
    void changePassword(String username, String oldPassword, String newPassword, HttpServletRequest request);


}
