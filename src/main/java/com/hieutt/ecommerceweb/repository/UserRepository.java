package com.hieutt.ecommerceweb.repository;

import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
