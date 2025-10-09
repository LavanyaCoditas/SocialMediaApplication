package com.SocialMedia.Social.Media.Platform.project.Controller;
import com.SocialMedia.Social.Media.Platform.project.DTO.ApprovedPostResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.DisapprovedPostListDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Service.PostService;
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
   //change endpoint name is not readable  , exposing of id in path , remove path variable , remove fetch of username
    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.editPost(id, postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @GetMapping("/live")
    public List<ApprovedPostResponse> getApprovedPosts() {

        return postService.getApprovedPosts();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getUserPosts(username));
    }

    @GetMapping("/disapproved")
    public List<Posts> getDisapprovedPosts()
    {String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return postService.getBlockedPostsByModerator(currentUsername);
    }

    //users own posts which are denied approval
    @GetMapping("/denied")
    public List<DisapprovedPostListDto> getDeniedPosts()
    {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.getUsersDeniedPosts(username);
    }

}
