package com.SocialMedia.Social.Media.Platform.project.Service;

import com.SocialMedia.Social.Media.Platform.project.Entity.*;
import com.SocialMedia.Social.Media.Platform.project.Repository.CommentRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    public Comments createComment(String content, Long postId, String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Posts post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        // Optional: Check if post is approved before allowing comments
        if (post.getStatus() != PostStatus.APPROVED) {
            throw new RuntimeException("Cannot comment on unapproved post");
        }
        Comments comment = new Comments();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setStatus(CommentStatus.PENDING); // Default per entity
        return commentRepo.save(comment);
    }

    public Comments editComment(Long id, String content, String username) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Only owner can edit");
        }
        comment.setContent(content);
        comment.setApprovedBy(new java.util.ArrayList<>());
        comment.setDisapprovedBy(new java.util.ArrayList<>());
        comment.setStatus(CommentStatus.PENDING);
        return commentRepo.save(comment);
    }

    public void deleteComment(Long id, String username) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepo.findByUsername(username);
        boolean isOwner = comment.getUser().getUsername().equals(username);
        boolean isModerator = user.getRole() == Role.ROLE_MODERATOR || user.getRole() == Role.ROLE_SUPER_ADMIN;
        if (!isOwner && !isModerator) {
            throw new RuntimeException("Unauthorized: Only owner or moderator can delete");
        }
        commentRepo.delete(comment);
    }

    public Comments approveComment(Long id, String moderatorUsername) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized: Only moderators or super admins can approve");
        }
        String moderatorId = moderator.getId().toString();
        if (!comment.getApprovedBy().contains(moderatorId)) {
            comment.getApprovedBy().add(moderatorId);
        }
        if (comment.getApprovedBy().size() >= 1 && comment.getDisapprovedBy().isEmpty()) {
            comment.setStatus(CommentStatus.APPROVED);
        }
        return commentRepo.save(comment);
    }
    public Comments disapproveComment(Long id, String moderatorUsername) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized: Only moderators or super admins can disapprove");
        }
        String moderatorId = moderator.getId().toString();
        if (!comment.getDisapprovedBy().contains(moderatorId)) {
            comment.getDisapprovedBy().add(moderatorId);
        }
        if (comment.getDisapprovedBy().size() >= 1) {
            comment.setStatus(CommentStatus.BLACKLISTED);
        }
        return commentRepo.save(comment);
    }



    public List<Comments> getApprovedCommentsForPost(Long postId) {
        Posts post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepo.findByPostAndStatus(post, CommentStatus.APPROVED);
    }

    public List<Comments> getUserComments(String username) {
        User user = userRepo.findByUsername(username);
        return commentRepo.findByUser(user); // All statuses for user's profile
    }

    public List<Comments> getPendingComments() {
        return commentRepo.findByStatusOrderById(CommentStatus.PENDING);
    }


    public List<Comments> getBlockedComments(String moderatorUsername) {
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized: Only moderators or super admins can view blocked comments");
        }
        return commentRepo.findByDisapprovedByContaining(moderator.getId().toString());
    }
    // Additional: Get all comments (admin/moderator only, all statuses)
    public List<Comments> getAllComments() {
        return commentRepo.findAll();
    }
}