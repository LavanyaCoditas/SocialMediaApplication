package com.SocialMedia.Social.Media.Platform.project.Repository;

import com.SocialMedia.Social.Media.Platform.project.Entity.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepo extends JpaRepository<Posts,Long> {
    List<Posts> findByStatusOrderById(PostStatus status);

    List<Posts> findByUser(User user);

    @Query("SELECT p FROM Posts p WHERE :moderatorId MEMBER OF p.disapprovedBy")
    List<Posts> findByDisapprovedByContaining(String moderatorId);
}
