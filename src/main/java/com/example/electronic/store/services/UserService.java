package com.example.electronic.store.services;

import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

    // Create
    UserDto CreateUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId) throws IOException;

    //get all user
    //List<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDirection);
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDirection);

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
