package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.PasswordDto;
import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    User getCurrentUser(Principal principal);
    Map<String, String> updateUser(Long id, UserDto userDto, MultipartFile image) throws IOException;
    void deleteUser(Long id);
    Map<String, String> createAdmin(RegisterDto registerDto);
    Map<String, String> createCustomer(RegisterDto registerDto);
    UserDto getUserByEmail(String email);
    Map<String, String> updatePassword(Long userId, PasswordDto passwordDto);
}
