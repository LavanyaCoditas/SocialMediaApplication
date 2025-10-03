package com.SocialMedia.Social.Media.Platform.project.Repository;

import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
