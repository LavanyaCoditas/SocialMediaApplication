package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.Constants.Role;
import com.SocialMedia.Social.Media.Platform.project.DTO.*;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import com.SocialMedia.Social.Media.Platform.project.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")

public class AuthController
{
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDTO userDTO){
            User user = userServiceImpl.signup(userDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", new SignupResponse(user.getId())) );

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = userServiceImpl.login(loginDTO);
        User user= userRepo.findByEmail(loginDTO.getEmail());
        Role role = user.getRole();
        return ResponseEntity.ok(new LoginResponse(true, token, "Login successful",role));
    }
}