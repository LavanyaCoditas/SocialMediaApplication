package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Service.UserService;
import com.SocialMedia.Social.Media.Platform.project.DTO.ModeratorActionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')") // Changed to hasAuthority
public class AdminController {

    @Autowired
    private UserService userService;

    @PutMapping("/moderators/{userId}")
    public ResponseEntity<?> makeModerator(@PathVariable Long userId, @RequestBody ModeratorActionDTO actionDTO)
    {
        User user = userService.makeModerator(userId, actionDTO.getAction());
        return ResponseEntity.ok("User updated successfully");
    }
}