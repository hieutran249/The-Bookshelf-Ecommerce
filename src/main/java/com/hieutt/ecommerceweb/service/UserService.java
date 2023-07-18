package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.ChangePasswordDto;
import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.ResetPasswordDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.Role;
import com.hieutt.ecommerceweb.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    List<UserDto> getUsersByRole(Role role);
    UserDto getUserById(Long id);
    User getCurrentUser(Principal principal);
    Map<String, String> updateUser(Long id, UserDto userDto, MultipartFile image) throws IOException;
    void deleteUser(Long id);
    Map<String, String> createAdmin(RegisterDto registerDto);
    Map<String, String> createCustomer(RegisterDto registerDto);
    UserDto getUserByEmail(String email);
    Map<String, String> updatePassword(Long userId, ChangePasswordDto passwordDto);
    Map<String, String> resetPassword(Long userId, ResetPasswordDto resetPasswordDto);
    Map<String, String> requestResetPassword(String email);
}
