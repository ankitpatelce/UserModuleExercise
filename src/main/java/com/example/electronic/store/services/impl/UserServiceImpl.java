package com.example.electronic.store.services.impl;

import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.entities.User;
import com.example.electronic.store.repositories.UserRepository;
import com.example.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto CreateUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        UserDto newUserDto = entityToDto(savedUser);
        return newUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDto.getName());
        //Email: we are not updating Email
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());

        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> allUsers = userRepository.findAll();
        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return allDtos;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given Id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with given Email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyWord) {
        List<User> Users = userRepository.findByNameContaining(keyWord);
        List<UserDto> allDtos = Users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return allDtos;
    }

//    @Override
//    public List<UserDto> searchAnyKeyword(String keyWord) {
//        List<User> Users = userRepository.findAnyByContaining(keyWord);
//        List<UserDto> allDtos = Users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//        return allDtos;
//    }


//    private UserDto entityToDto(User user){
//        UserDto userDto = UserDto.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .about(user.getAbout())
//                .gender(user.getGender())
//                .imageName(user.getImageName())
//                .build();
//        return userDto;
//    }
//    private User dtoToEntity(UserDto userDto){
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
//        return user;
//    }

    private UserDto entityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}

