package com.SocialMedia.Social.Media.Platform.project.Repository;

import com.SocialMedia.Social.Media.Platform.project.Constants.CommentStatus;
import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comments, Long> {
    List<Comments> findByStatusOrderById(CommentStatus status);

    List<Comments> findByUser(User user);

    @Query("SELECT c FROM Comments c WHERE :moderatorId MEMBER OF c.disapprovedBy")
    List<Comments> findByDisapprovedByContaining(String moderatorId);

    List<Comments> findByPost(Posts post);

    List<Comments> findByPostAndStatus(Posts post, CommentStatus status);
}