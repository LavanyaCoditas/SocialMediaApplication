package com.SocialMedia.Social.Media.Platform.project.Controller;

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

    @PutMapping("/{id}")
    public ResponseEntity<Comments> editComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content is required");
        }
        Comments comment = commentService.editComment(id, content, username);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comments>> getApprovedCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getApprovedCommentsForPost(postId));
    }

    @GetMapping("/profile")
    public ResponseEntity<List<Comments>> getUserComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getUserComments(username));
    }

    // Get all comments (all statuses) - restrict to moderators/super admins
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<Comments>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping("/comments/{id}/approve")
    public ResponseEntity<?> approveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.approveComment(id, username);
        return ResponseEntity.ok(Map.of("commentId", comment.getId(), "status", comment.getStatus()));
    }

    @PostMapping("/comments/{id}/disapprove")
    public ResponseEntity<?> disapproveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.disapproveComment(id, username);
        return ResponseEntity.ok(Map.of("commentId", comment.getId(), "status", comment.getStatus()));
    }

    @GetMapping("/comments/pending")
    public ResponseEntity<List<Comments>> getPendingComments() {
        return ResponseEntity.ok(commentService.getPendingComments());
    }

    @GetMapping("/comments/blocked")
    public ResponseEntity<List<Comments>> getBlockedComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getBlockedComments(username));
    }
}