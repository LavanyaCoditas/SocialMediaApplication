package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.UserListDto;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Service.UserServiceImpl;
import com.SocialMedia.Social.Media.Platform.project.Utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_SUPER_ADMIN')")
public class UserController
{
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    AppUtils appUtils;

    @GetMapping("current/user/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        String currentUsername = appUtils.fetchUsername();
        User user = userServiceImpl.getUserById(id, currentUsername);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        String currentUsername = appUtils.fetchUsername();
        try {
            userServiceImpl.deleteUser(id, currentUsername);
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User not found")) {
                return ResponseEntity.status(404).body(Map.of("message", "User not found"));
            }
            return ResponseEntity.status(403).body(Map.of("message", "Unauthorized access"));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        String currentUsername = appUtils.fetchUsername();
        List<UserListDto> users = userServiceImpl.getAllUsers(currentUsername);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/moderators")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<UserListDto>> getAllModerators() {
//
            List<UserListDto> moderators = userServiceImpl.getAllModerators();
            return ResponseEntity.ok(moderators);
//
    }
}
