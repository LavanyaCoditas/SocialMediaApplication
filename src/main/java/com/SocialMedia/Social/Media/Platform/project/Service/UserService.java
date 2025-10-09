package com.SocialMedia.Social.Media.Platform.project.Service;

import com.SocialMedia.Social.Media.Platform.project.DTO.LoginDTO;
import com.SocialMedia.Social.Media.Platform.project.DTO.UserSignupDTO;
import com.SocialMedia.Social.Media.Platform.project.Constants.Role;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import com.SocialMedia.Social.Media.Platform.project.Utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthUtil authUtil;

    public User signup(UserSignupDTO userDTO) {
        // Check for existing username or email
        if (userRepo.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
         // Generate positive Long ID
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash password

        user.setRole(Role.ROLE_USER); // Default role
        user.setModerator(false); // Default moderator status
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }

    public String login(LoginDTO loginDTO)
    {
        User user = userRepo.findByEmail(loginDTO.getEmail());
        if(user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Wrong credentials");
        }
        return authUtil.generateAccessToken(user);
    }

    public User makeModerator(Long userId, String action) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if ("make".equals(action)) {
            user.setRole(Role.ROLE_MODERATOR);
            user.setModerator(true);
        } else if ("remove".equals(action)) {
            user.setRole(Role.ROLE_USER);
            user.setModerator(false);
        } else {
            throw new RuntimeException("Invalid action");
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    public User getUserById(Long id, String currentUsername) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        User currentUser = userRepo.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new RuntimeException("Unauthorized access");
        }
        // Allow access if user is viewing their own profile, or is a moderator/admin
        if (!user.getUsername().equals(currentUsername) &&
                !(currentUser.getRole() == Role.ROLE_MODERATOR || currentUser.getRole() == Role.ROLE_SUPER_ADMIN || currentUser.getRole()==Role.ROLE_USER)) {
            throw new RuntimeException("Unauthorized access");
        }
        return user;
    }

    //user can delete his own profile  and super admin can also delete any user , but not th moderator
    public void deleteUser(Long id, String currentUsername) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        User currentUser = userRepo.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new RuntimeException("Unauthorized access");
        }
        // Allow deletion if user is deleting their own account or is a super admin
        if (!user.getUsername().equals(currentUsername) && currentUser.getRole() != Role.ROLE_SUPER_ADMIN) {
            throw new RuntimeException("Unauthorized access");
        }
        userRepo.delete(user);
    }

    public List<User> getAllUsers(String currentUsername) {
      return   userRepo.findAll();
    }

    public List<User> getAllModerators() {
        return userRepo.findByRole(Role.ROLE_MODERATOR);
    }
}


