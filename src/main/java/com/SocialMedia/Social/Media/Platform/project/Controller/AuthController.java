package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.LoginDTO;
import com.SocialMedia.Social.Media.Platform.project.DTO.LoginResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.SignupResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.UserSignupDTO;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDTO userDTO) {
        try {
            User user = userService.signup(userDTO);
            return ResponseEntity.ok(new SignupResponse(true, user.getId(), "User created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            return ResponseEntity.ok(new LoginResponse(true, token, "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}