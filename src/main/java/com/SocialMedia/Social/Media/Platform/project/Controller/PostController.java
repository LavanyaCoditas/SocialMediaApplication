package com.SocialMedia.Social.Media.Platform.project.Controller;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Service.PostService;
import com.SocialMedia.Social.Media.Platform.project.Utils.AppUtils;
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
public class PostController
{
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private AppUtils appUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto) {
        String username = appUtils.fetchUsername();
        Posts post = postService.createPost(postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> editPost(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        String username = appUtils.fetchUsername();
        Posts post = postService.editPost(id, postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));
    }
    
    //live feed
    @GetMapping("/live")
    public List<PostResponse> getApprovedPosts()
    {
        return postService.getApprovedPosts();
    }

    @GetMapping("/profile")
    public ResponseEntity<List<PostResponse>>getUserPosts() {
        String username = appUtils.fetchUsername();
        return ResponseEntity.ok(postService.getUserPosts(username));
    }

    @GetMapping("/disapproved")
    public ResponseEntity<List<PostResponse>> getDisapprovedPosts()
    {
        String currentUsername = appUtils.fetchUsername();
        return ResponseEntity.ok(postService.getBlockedPostsByModerator(currentUsername));
    }

    //users own posts which have been denied approval
    @GetMapping("/denied")
    public List<PostResponse> getDeniedPosts()
    {
        String username= appUtils.fetchUsername();
        return postService.getUsersDeniedPosts(username);
    }

    @DeleteMapping("/{post_id}")
    public void deletePost (@PathVariable Long post_id) {
    String currentUserName = appUtils.fetchUsername();
    postService.deletePost(post_id, currentUserName);
     }
     }
