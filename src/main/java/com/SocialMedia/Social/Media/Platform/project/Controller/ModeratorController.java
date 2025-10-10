package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Service.CommentService;
import com.SocialMedia.Social.Media.Platform.project.Service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moderator")
@PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
public class ModeratorController {

    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{id}/approve")
    public ResponseEntity<?> approvePost(@PathVariable Long id) {
        Posts post = moderatorService.approvePost(id);
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));

    }
    @PostMapping("/posts/{id}/disapprove")
    public ResponseEntity<?> disapprovePost(@PathVariable Long id) {
        Posts post = moderatorService.disapprovePost(id);//disapproving post
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));
    }

    @GetMapping("/posts/pending")
    public ResponseEntity<List<PostResponse>> getPendingPosts() { return ResponseEntity.ok(moderatorService.getPendingPosts());
    }

    @GetMapping("/posts/blocked")
    public ResponseEntity<List<PostResponse>> getBlockedPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(moderatorService.getBlockedPostsByModerator(username));
    }
    @PostMapping("/{id}/disapprove")
    public ResponseEntity<?> disapproveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.disapproveComment(id, username);
        return ResponseEntity.ok(new CommentResponse(comment.getId(),comment.getContent(),comment.getStatus().toString(),comment.getPost().getId(),comment.getUser().getUsername()));
    }
    private Long id;
    private String content;
    private String status;
    private Long postId;
    private String username;
    //this is done by moderator only
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveComment(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comments comment = commentService.approveComment(id, username);
        return ResponseEntity.ok(new CommentResponse(comment.getId(),comment.getContent(),comment.getStatus().toString(),comment.getPost().getId(),comment.getUser().getUsername()));
    }

}