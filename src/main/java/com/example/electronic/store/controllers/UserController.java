package com.example.electronic.store.controllers;

import com.example.electronic.store.dtos.ApiResponseMessage;
import com.example.electronic.store.dtos.ImageResponse;
import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.services.FileService;
import com.example.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.catalina.LifecycleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**** Create User ****/
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.CreateUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    /**** Update User ****/
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid
            @PathVariable("userId") String userId,
            @RequestBody UserDto userDto) {
        UserDto updatedUserDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto1, HttpStatus.OK);
    }

    /**** Delete User ****/
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) throws IOException {
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


//    /**** Get All User ****/
//    @GetMapping
//    public ResponseEntity<List<UserDto>> getAllUser(
//            @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
//            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
//            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
//            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDirection
//    ){
//        List<UserDto> userDtoList = userService.getAllUser(pageNumber,pageSize,sortBy,sortDirection);
//        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
//    }

    /**** Get All User ****/
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDirection
    ) {
        PageableResponse<UserDto> userDtoList = userService.getAllUser(pageNumber, pageSize, sortBy, sortDirection);
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

    /**** Upload User Image ****/
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {

        UserDto user = userService.getUserById(userId);

        //--- First we check if any profile file related with user si available in our dir then we delete it.
        if (!user.getImageName().isBlank()) {
            logger.info(" 1 more Image Found with this user : {}", user.getImageName());
            try {
                String fileNameWithPath = imageUploadPath + File.separator + user.getImageName();
                Path path = Paths.get(fileNameWithPath);
                if (Files.exists(path)) {
                    Files.delete(path);
                    logger.info("User Image {} deleted", user.getImageName());
                }
            } catch (NoSuchFileException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
                //e.printStackTrace();
            }
        }
        // --- Second upload new file.
        String imageName = fileService.uploadFile(image, imageUploadPath);
        //UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);

        ImageResponse response = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**** Serve User Image ****/
    @PostMapping("/image/get/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        String imageName = user.getImageName();
        InputStream resource = fileService.getResource(imageUploadPath, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
