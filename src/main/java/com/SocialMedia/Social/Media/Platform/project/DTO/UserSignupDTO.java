package com.SocialMedia.Social.Media.Platform.project.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserSignupDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 12, message = "Password must be between 6 and 8 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;


    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}