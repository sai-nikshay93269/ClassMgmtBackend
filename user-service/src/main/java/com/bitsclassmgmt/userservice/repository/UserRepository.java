package com.bitsclassmgmt.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.userservice.enums.Active;
import com.bitsclassmgmt.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    List<User> findAllByActive(Active active);

}
