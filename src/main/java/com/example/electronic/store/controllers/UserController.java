package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.services.UserService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**** Create User ****/
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto userDto1 = userService.CreateUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    /**** Update User ****/
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDto userDto) {
        UserDto updatedUserDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto1, HttpStatus.OK);
    }

    /**** Delete User ****/
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Deleted Sucessfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
//    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId){
//        userService.deleteUser(userId);
//        return new ResponseEntity<>("User Deleted",HttpStatus.OK);
//    }


    /**** Get All User ****/
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> userDtoList = userService.getAllUser();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**** Get User by Id ****/
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserbyId(@PathVariable("userId") String userId) {
        UserDto updatedUserDto1 = userService.getUserById(userId);
        return new ResponseEntity<>(updatedUserDto1, HttpStatus.OK);
    }

    /**** Get User by Email ****/
    @GetMapping("/email/{emailId}")
    public ResponseEntity<UserDto> getUserbyEmail(@PathVariable("emailId") String emailId) {
        UserDto updatedUserDto1 = userService.getUserByEmail(emailId);
        return new ResponseEntity<>(updatedUserDto1, HttpStatus.OK);
    }

    /**** Search User ****/
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords) {
        List<UserDto> userList = userService.searchUser(keywords);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    /**** Search Any Word ****/
//    @GetMapping("/search/any/{keywords}")
//    public ResponseEntity<List<UserDto>> searchAnyKeyword(@PathVariable("keywords") String keywords){
//        List<UserDto> userList = userService.searchAnyKeyword(keywords);
//        return new ResponseEntity<>(userList, HttpStatus.OK);
//    }
}
