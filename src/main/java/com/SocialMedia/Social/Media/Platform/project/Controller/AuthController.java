package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.*;
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
  //  public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDTO userDTO) {

    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDTO userDTO){
        try {
            User user = userService.signup(userDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", new SignupResponse(user.getId())) );
           //  return  ResponseEntity.ok(new ApiResponse<>(true,"user created succesfully ",new LoginResponse()));
        }
        catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            return ResponseEntity.ok(new LoginResponse(true, token, "Login successful"));
           // return ResponseEntity.ok(new ApiResponse<>(true,"Login is successful",new LoginResponse(token)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}