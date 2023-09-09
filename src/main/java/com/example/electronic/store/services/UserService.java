package com.example.electronic.store.services;

import com.example.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    // Create
    UserDto CreateUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all user
    List<UserDto> getAllUser();

    //get single user by userid
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //Search User
    List<UserDto> searchUser(String keyWord);

    //Search Any Keyword
    //List<UserDto> searchAnyKeyword(String keyWord);

    //Other
}
