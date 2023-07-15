package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.PasswordDto;
import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.Role;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.ShoppingCartRepository;
import com.hieutt.ecommerceweb.repository.UserRepository;
import com.hieutt.ecommerceweb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> message;

    public UserServiceImpl(UserRepository userRepository, ShoppingCartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.message = new HashMap<>();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(mapToEntity(userDto));
        return mapToDto(user);
    }

    @Override
    public Map<String, String> createAdmin(RegisterDto registerDto) {
        // check if email already exist or not
        if (userRepository.existsByEmail(registerDto.getEmail())) {
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
        // check if email already exist or not
        if (userRepository.existsByEmail(registerDto.getEmail())) {
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

        // create a shopping cart for customer
        ShoppingCart cart = ShoppingCart.builder()
                .user(customer)
                .build();

        customer.setShoppingCart(cart);
        userRepository.save(customer);
        cartRepository.save(cart);

        message.put("type", "success");
        message.put("detail", "Registered successfully 🎉");
        return message;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        return mapToDto(user);
    }

    @Override
    public Map<String, String> updatePassword(Long userId, PasswordDto passwordDto) {
        User user = findUserById(userId);
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            message.put("type", "error");
            message.put("detail", "The password is not matched 😑");
            return message;
        }
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            message.put("type", "error");
            message.put("detail", "This password is not your current password 😐 bruhh");
            return message;
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);

        message.put("type", "success");
        message.put("detail", "Updated password successfully 🎉");
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
    public User getCurrentUser(Principal principal) {
        String email = principal.getName();
        return mapToEntity(getUserByEmail(email));
    }

    @Override
    public Map<String, String> updateUser(Long id, UserDto userDto, MultipartFile image) throws IOException {
        User user = findUserById(id);
        if (!userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            message.put("type", "error");
            message.put("detail", "This email already exists 🤷‍♂️");
            return message;
        }
        if (!image.isEmpty()) {
            user.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());

        userRepository.save(user);

        message.put("type", "success");
        message.put("detail", "Updated profile successfully 🎊");
        return message;
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
