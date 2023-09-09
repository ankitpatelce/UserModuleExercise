package com.example.electronic.store.dtos;

import com.example.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min = 2, max = 20, message = "Invalid Name !!")
    private String name;

    @Email(message = "Invalid User Email")
    @NotBlank(message = "Email is required !!")
    private String email;

    //Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$",message = "Invalid Password")
    @Size(min = 4, max = 10, message = "Use password between 4 to 10 ")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender!!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    //@ImageNameValid
    private String imageName;

    //@Pattern
    //custom validation
}
