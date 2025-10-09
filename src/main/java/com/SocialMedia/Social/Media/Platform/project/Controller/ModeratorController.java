package com.SocialMedia.Social.Media.Platform.project.Controller;

import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Service.ModeratorService;
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
    private ModeratorService moderatorService;

    @PostMapping("/posts/{id}/approve")
    public ResponseEntity<?> approvePost(@PathVariable Long id) {
        Posts post = moderatorService.approvePost(id);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));

    }
    @PostMapping("/posts/{id}/disapprove")
    public ResponseEntity<?> disapprovePost(@PathVariable Long id) {
        Posts post = moderatorService.disapprovePost(id);//disapproving post
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @GetMapping("/posts/pending")
    public ResponseEntity<List<PostDto>> getPendingPosts() { return ResponseEntity.ok(moderatorService.getPendingPosts());
    }

    @GetMapping("/posts/blocked")
    public ResponseEntity<List<Posts>> getBlockedPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(moderatorService.getBlockedPostsByModerator(username));
    }

}