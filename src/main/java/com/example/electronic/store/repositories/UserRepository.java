package com.example.electronic.store.repositories;

import com.example.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    //User findByEmail(String email);
    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String keywords);

    //List<User> findAnyByContaining(String keywords);


}
