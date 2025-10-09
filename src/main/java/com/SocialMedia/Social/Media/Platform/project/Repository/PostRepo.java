package com.SocialMedia.Social.Media.Platform.project.Repository;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepo extends JpaRepository<Posts,Long> {
    List<Posts> findByStatus(PostStatus status);

    List<Posts> findByUser(User user);

    @Query("SELECT p FROM Posts p WHERE :moderatorId MEMBER OF p.disapprovedBy")
    List<Posts> findByDisapprovedByContaining(String moderatorId);

    //method to find the posts based on the user and the status of posts
    List<Posts> findByUserUsernameAndStatusOrderById(String username, PostStatus status);
}
