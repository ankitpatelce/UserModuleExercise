package com.example.electronic.store.services.impl;

import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.entities.User;
import com.example.electronic.store.exceptions.ResourceNotFoundException;
import com.example.electronic.store.helper.Helper;
import com.example.electronic.store.repositories.UserRepository;
import com.example.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
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

    public void deleteUserImage(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // -- Delete User Image ------
        try {
            String fileNameWithPath = imageUploadPath + File.separator + user.getImageName();
            Path path = Paths.get(fileNameWithPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // -- Delete User Image ------
        try {
            String fileNameWithPath = imageUploadPath + File.separator + user.getImageName();
            Path path = Paths.get(fileNameWithPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        // -- Delete User ------
        userRepository.delete(user);
    }

//    @Override
//    public List<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDirection) {
////        Sort sort = Sort.by(sortBy);
//        Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//
//// ---- Stage 1 -- Here we get data without passing pageNumber and pageSize --optional
////        List<User> allUsers = userRepository.findAll();
////        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//
//// ---- Stage 2 -- Here we get data with passing pageNumber, pageSize, sortBy and sortDirection -- optional
//        Page<User> page = userRepository.findAll(pageable);
//        List<User> allUsers = page.getContent();
//        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDirection) {
//        Sort sort = Sort.by(sortBy);
        Sort sort = (sortDirection.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

// ---- Stage 1 -- Here we get data without passing pageNumber and pageSize --optional
//        List<User> allUsers = userRepository.findAll();
//        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

// ---- Stage 2 -- Here we get data with passing pageNumber, pageSize, sortBy and sortDirection -- optional
//        Page<User> page = userRepository.findAll(pageable);
//        List<User> allUsers = page.getContent();
//        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

// ---- Stage 3 -- Here we get data with passing pageNumber, pageSize, sortBy and sortDirection -- optional
//              -- and make pageable response with Content and page details in response.
//        Page<User> page = userRepository.findAll(pageable);
//        List<User> allUsers = page.getContent();
//        List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//        PageableResponse<UserDto> response = new PageableResponse<>();
//        response.setContent(allDtos);
//        response.setPageNumber(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalPages(page.getTotalPages());
//        response.setTotalElements(page.getTotalElements());
//        response.setLastPages(page.isLast());

// ---- Stage 4 -- Here we get data with passing pageNumber, pageSize, sortBy and sortDirection -- optional
//              -- and make pageable response with Content and page details in response.
//              -- and make Static method in Helper class to use as a reusable.
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given Email"));
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

