package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    Map<String, String> createAdmin(RegisterDto registerDto);

    Map<String, String> createCustomer(RegisterDto registerDto);
}
