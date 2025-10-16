package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;
import com.SocialMedia.Social.Media.Platform.project.Service.CommentServiceImpl;
import com.SocialMedia.Social.Media.Platform.project.Utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'SUPER_ADMIN')")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @Autowired
    private AppUtils appUtils;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Comments> addComment(@PathVariable Long postId, @RequestBody Map<String, String> body) {
        String username = appUtils.fetchUsername();
        String content = body.get("content");

        if (content == null || content.isBlank())
        {
            throw new RuntimeException("Content is required");
        }
        Comments comment = commentServiceImpl.createComment(content, postId, username);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Comments> editComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String username = appUtils.fetchUsername();
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content is required");
        }
        Comments comment = commentServiceImpl.editComment(id, content, username);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        String username = appUtils.fetchUsername();
        commentServiceImpl.deleteComment(id, username);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    //fetches the comments on current post you clicked on
    @GetMapping("/post/approved/{postId}")
    public List<CommentResponse> getApprovedCommentsForPost(@PathVariable Long postId) {
        return commentServiceImpl.getApprovedCommentsForPost(postId);
    }

    //return all the comments
    @GetMapping("/profile")
    public ResponseEntity<List<CommentResponse>> getUserComments() {
        String username = appUtils.fetchUsername();
        return ResponseEntity.ok(commentServiceImpl.getUserComments(username));
    }

    //gets all comments only accessible to super admin and moderator
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<CommentResponse>> getAllComments()
    {
        return ResponseEntity.ok(commentServiceImpl.getAllComments());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CommentResponse>> getPendingComments() {
        return ResponseEntity.ok(commentServiceImpl.getPendingComments());
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<CommentResponse>> getBlockedComments() {
        String username = appUtils.fetchUsername();
        return ResponseEntity.ok(commentServiceImpl.getBlockedComments(username));
    }
}