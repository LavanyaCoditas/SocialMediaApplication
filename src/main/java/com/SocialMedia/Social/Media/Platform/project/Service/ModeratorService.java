package com.SocialMedia.Social.Media.Platform.project.Service;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ModeratorService {
    @Autowired
    PostRepo postRepo;
    @Autowired
    UserRepo userRepo;

@Transactional
public Posts approvePost(Long id) {
    // Fetch post
    Posts post = postRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

    // Get authenticated user
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    if (username == null || username.isEmpty()) {
        throw new RuntimeException("No authenticated user found in security context");
    }

    // Fetch moderator
    User moderator = userRepo.findByUsername(username);
    if (moderator == null) {
        throw new RuntimeException("Moderator not found with email: " + username);
    }

    // author cant approve own post
    User postUser = post.getUser();
    if (postUser != null && postUser.getUsername().equals(moderator.getUsername())) {
        throw new RuntimeException("Moderators cannot approve their own posts!");
    }

    if (post.getApprovedBy() == null) {
        post.setApprovedBy(new ArrayList<>());
    }
    if (post.getDisapprovedBy() == null) {
        post.setDisapprovedBy(new ArrayList<>());
    }
    if (post.getDisapprovedBy().isEmpty()) {
        post.getApprovedBy().add(moderator.getUsername());
        if (post.getApprovedBy().size() >= 1) {
            post.setStatus(PostStatus.APPROVED);
        }
    } else {
        throw new RuntimeException("Post cannot be approved: It has been disapproved");
    }

    return postRepo.save(post);
}

    @Transactional
    public Posts disapprovePost(Long id) {
        // Fetch post
        Posts post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        // Get authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("No authenticated user found in security context");
        }

        // Fetch moderator
        User moderator = userRepo.findByUsername(username);
        if (moderator == null) {
            throw new RuntimeException("Moderator not found with username: " + username);
        }

        // Author can't disapprove own post
        User postUser = post.getUser();
        if (postUser != null && postUser.getUsername().equals(moderator.getUsername())) {
            throw new RuntimeException("Moderators cannot disapprove their own posts!");
        }

        // Disapproval logic
        if (post.getApprovedBy() == null) {
            post.setApprovedBy(new ArrayList<>());
        }
        if (post.getDisapprovedBy() == null) {
            post.setDisapprovedBy(new ArrayList<>());
        }
        if (post.getApprovedBy().isEmpty()) {
            post.getDisapprovedBy().add(moderator.getUsername());
            if (post.getDisapprovedBy().size() >= 1) {
                post.setStatus(PostStatus.BLACKLISTED);
            }
        } else {
            throw new RuntimeException("Post cannot be disapproved: It has been approved");
        }

        return postRepo.save(post);
    }

    public List<Posts> getBlockedPostsByModerator(String username)
    {
        User user = userRepo.findByUsername(username);
        return postRepo.findByDisapprovedByContaining(user.getId().toString());
    }

    public List<PostDto> getPendingPosts()
    {
        List<Posts> list= postRepo.findByStatus(PostStatus.PENDING);
        List<PostDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String title = list.get(i).getTitle();
            String content = list.get(i).getContent();
            response.add(new PostDto(title,content));

        }
        return response;
    }
}
