package com.SocialMedia.Social.Media.Platform.project.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserSignupDTO {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$", message = "Username must start with a letter and can only contain letters, numbers, dots, underscores, or hyphens")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must start with a letter, should have @, have valid domain and be valid like example@gmail.com")
    private String email;

  //alpha numeric , must contain at leat 1 special character and min 6 to 14
   @NotBlank(message = "Password is required")
   @Size(min = 6,  message = "Password must be between 6 and 8 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;


    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}