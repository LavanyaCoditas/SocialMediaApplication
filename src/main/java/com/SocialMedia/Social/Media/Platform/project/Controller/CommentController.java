package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.CommentOfUserDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;
import com.SocialMedia.Social.Media.Platform.project.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'SUPER_ADMIN')")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Comments> addComment(@PathVariable Long postId, @RequestBody Map<String, String> body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String content = body.get("content");

        if (content == null || content.isBlank())
        {
            throw new RuntimeException("Content is required");
        }
        Comments comment = commentService.createComment(content, postId, username);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Comments> editComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content is required");
        }
        Comments comment = commentService.editComment(id, content, username);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    //fetches the comments on current post you clicked on
    @GetMapping("/post/approved/{postId}")
    public List<CommentResponse> getApprovedCommentsForPost(@PathVariable Long postId) {
        return commentService.getApprovedCommentsForPost(postId);
    }

    //return all the comments
    @GetMapping("/profile")
    public ResponseEntity<List<CommentOfUserDto>> getUserComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getUserComments(username));
    }

    //gets all comments only accessible to super admin and moderator
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<CommentOfUserDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    //this is done by moderator only
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.approveComment(id, username);
        return ResponseEntity.ok(Map.of("commentId", comment.getId(), "status", comment.getStatus()));
    }

    @PostMapping("/{id}/disapprove")
    public ResponseEntity<?> disapproveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.disapproveComment(id, username);
        return ResponseEntity.ok(Map.of("commentId", comment.getId(), "status", comment.getStatus()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CommentOfUserDto>> getPendingComments() {
        return ResponseEntity.ok(commentService.getPendingComments());
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<CommentOfUserDto>> getBlockedComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getBlockedComments(username));
    }
}