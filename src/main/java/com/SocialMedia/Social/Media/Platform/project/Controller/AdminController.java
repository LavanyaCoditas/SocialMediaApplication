package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
public class AdminController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/{userId}/make-moderator")
        public ResponseEntity<User> makeModerator(@PathVariable Long userId) {
            User updatedUser = userServiceImpl.makeModerator(userId);
            return ResponseEntity.ok(updatedUser);
        }

        @PostMapping("/{userId}/remove-moderator")
        public ResponseEntity<User> removeModerator(@PathVariable Long userId) {
            User updatedUser = userServiceImpl.removeModerator(userId);
            return ResponseEntity.ok(updatedUser);
        }
}