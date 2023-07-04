package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.Role;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.UserRepository;
import com.hieutt.ecommerceweb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(mapToEntity(userDto));
        return mapToDto(user);
    }

    @Override
    public Map<String, String> createAdmin(RegisterDto registerDto) {
        Map<String, String> message = new HashMap<>();

        // check if email already exist or not
        if (checkEmailExist(registerDto)) {
            message.put("type", "error");
            message.put("detail", "This email already exists!");
            return message;
        }

        // check if the password is matched with confirm password
        if (checkMatchedPassword(registerDto)) {
            message.put("type", "error");
            message.put("detail", "The password is not matched!");
            return message;
        }

        User admin = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        message.put("type", "success");
        message.put("detail", "Registered successfully 🎉");
        return message;
    }

    @Override
    public Map<String, String> createCustomer(RegisterDto registerDto) {
        Map<String, String> message = new HashMap<>();

        // check if email already exist or not
        if (checkEmailExist(registerDto)) {
            message.put("type", "error");
            message.put("detail", "This email already exists!");
            return message;
        }

        // check if the password is matched with confirm password
        if (checkMatchedPassword(registerDto)) {
            message.put("type", "error");
            message.put("detail", "The password is not matched!");
            return message;
        }

        User customer = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(customer);

        message.put("type", "success");
        message.put("detail", "Registered successfully 🎉");
        return message;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = findUserById(id);
        return mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = findUserById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setImage(userDto.getImage());

        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }


    private User findUserById(Long id) {
        return userRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));
    }

    private boolean checkEmailExist(RegisterDto registerDto) {
        return userRepository.findByEmail(registerDto.getEmail()).isPresent();
    }

    private boolean checkMatchedPassword(RegisterDto registerDto) {
        return !registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
