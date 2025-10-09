package com.SocialMedia.Social.Media.Platform.project.Controller;
import com.SocialMedia.Social.Media.Platform.project.DTO.ApprovedPostResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.DisapprovedPostListDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Service.ModeratorService;
import com.SocialMedia.Social.Media.Platform.project.Service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_SUPER_ADMIN')")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    PostRepo postRepo;
    @Autowired
    ModeratorService moderatorService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Posts post = postService.createPost(postDto, username);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getStatus()));
    }

    @PutMapping("update/{id}")
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
    public ResponseEntity<List<PostDto>>getUserPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getUserPosts(username));
    }

    @GetMapping("/disapproved")
    public ResponseEntity<List<Posts>> getDisapprovedPosts()
    {String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(moderatorService.getBlockedPostsByModerator(currentUsername));
    }

    //users own posts which have been denied approval
    @GetMapping("/denied")
    public List<DisapprovedPostListDto> getDeniedPosts()
    {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.getUsersDeniedPosts(username);
    }

    @DeleteMapping("/{post_id}")
public void deletePost (@PathVariable Long post_id) {
    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
    postService.deletePost(post_id, currentUserName);
}

}
