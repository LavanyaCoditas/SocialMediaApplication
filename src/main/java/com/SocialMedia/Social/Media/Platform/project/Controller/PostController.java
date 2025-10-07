package com.SocialMedia.Social.Media.Platform.project.Controller;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Service.PostService;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_SUPER_ADMIN')")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.createPost(postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.editPost(id, postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @GetMapping
    public ResponseEntity<List<Posts>> getApprovedPosts() {


        return ResponseEntity.ok(postService.getApprovedPosts());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getUserPosts(username));
    }


}
