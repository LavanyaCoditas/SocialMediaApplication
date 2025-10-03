package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/moderator")
@PreAuthorize("hasAnyRole('MODERATOR', 'SUPER_ADMIN')")
public class ModeratorController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts/{id}/approve")
    public ResponseEntity<?> approvePost(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.approvePost(id, username);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @PostMapping("/posts/{id}/disapprove")
    public ResponseEntity<?> disapprovePost(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.disapprovePost(id, username);//disapproving post
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @GetMapping("/posts/pending")
    public ResponseEntity<List<Posts>> getPendingPosts() {
        return ResponseEntity.ok(postService.getPendingPosts());
    }

    @GetMapping("/posts/blocked")
    public ResponseEntity<List<Posts>> getBlockedPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getBlockedPosts(username));
    }
}