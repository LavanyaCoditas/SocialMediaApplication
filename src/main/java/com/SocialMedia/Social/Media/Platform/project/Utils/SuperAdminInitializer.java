//package com.SocialMedia.Social.Media.Platform.project.Utils;
//
//import com.SocialMedia.Social.Media.Platform.project.Constants.Role;
//import com.SocialMedia.Social.Media.Platform.project.Entity.User;
//import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
//import jakarta.transaction.Transactional;
//import lombok.Value;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class SuperAdminInitializer implements CommandLineRunner {
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Value("${superadmin.username}")
//    private String superAdminUsername;
//    @Value("${superadmin.email}")
//    private String superAdminEmail;
//    @Value("${superadmin.password}")
//    private String superAdminPassword;
//    @Value("${superadmin.role}")
//    private String superAdminRole;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        if (userRepo.findByUsername(superAdminUsername) == null && userRepo.findByEmail(superAdminEmail) == null) {
//            User superAdmin = new User();
//            superAdmin.setUsername(superAdminUsername);
//            superAdmin.setEmail(superAdminEmail);
//            superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
//            superAdmin.setRole(Role.valueOf(superAdminRole));
//            superAdmin.setModerator(true);
//            superAdmin.setCreatedAt(LocalDateTime.now());
//            superAdmin.setUpdatedAt(LocalDateTime.now());
//            userRepo.save(superAdmin);
//            System.out.println("Super Admin created: " + superAdminUsername);
//        } else {
//            System.out.println("Super Admin already exists: " + superAdminUsername);
//        }
//    }
//}
