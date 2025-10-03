package com.SocialMedia.Social.Media.Platform.project.Repository;

import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comments,Long> {
}
