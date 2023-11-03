package com.example.festival.auth.repository;

import com.example.festival.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
    //User findByUsername(String username);
    User findByIdentify(String identify);
    boolean existsByIdentify(String identify);
}
