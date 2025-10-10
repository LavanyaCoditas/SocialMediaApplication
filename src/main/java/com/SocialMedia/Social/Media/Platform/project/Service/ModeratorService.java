package com.SocialMedia.Social.Media.Platform.project.Service;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
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

        // Fetching the moderator
        User moderator = userRepo.findByUsername(username);
        if (moderator == null) {
            throw new RuntimeException("Moderator not found with username: " + username);
        }

        // Author can't approve own post
        User postUser = post.getUser();
        if (postUser != null && postUser.getUsername().equals(moderator.getUsername())) {
            throw new RuntimeException("Moderators cannot approve their own posts!");
        }

        // Initializing lists if null
        if (post.getApprovedBy() == null) {
            post.setApprovedBy(new ArrayList<>());
        }
        if (post.getDisapprovedBy() == null) {
            post.setDisapprovedBy(new ArrayList<>());
        }

        if (!post.getApprovedBy().contains(moderator.getUsername())) {
            post.getApprovedBy().add(moderator.getUsername());
            if (post.getApprovedBy().size() >= 1) {
                post.setStatus(PostStatus.APPROVED);
                // Optionally clear disapprovals if post is approved
                post.getDisapprovedBy().clear();
            }
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

        // Initialize lists if null
        if (post.getApprovedBy() == null) {
            post.setApprovedBy(new ArrayList<>());
        }
        if (post.getDisapprovedBy() == null) {
            post.setDisapprovedBy(new ArrayList<>());
        }

        // Disapproval logic
        //this if block return true only if the name of moderator is not present in the disapprovedByList
        if (!post.getDisapprovedBy().contains(moderator.getUsername())) {
            post.getDisapprovedBy().add(moderator.getUsername());
            if (post.getDisapprovedBy().size() >= 1) {
                post.setStatus(PostStatus.BLACKLISTED);
                //clear the approved list
                post.getApprovedBy().clear();
            }
        }

        return postRepo.save(post);
    }

    public List<PostResponse> getBlockedPostsByModerator(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
       List<Posts>list= postRepo.findByDisapprovedByContaining(user.getUsername());
        List<PostResponse> response = new ArrayList<>();
        for (Posts post : list) {
            response.add(new PostResponse(post.getId(), post.getTitle(), post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));
        }
        return response;
    }

    public List<PostResponse> getPendingPosts() {
        List<Posts> list = postRepo.findByStatus(PostStatus.PENDING);
        List<PostResponse> response = new ArrayList<>();
        for (Posts post : list) {
            response.add(new PostResponse(post.getId(), post.getTitle(), post.getContent(),post.getStatus(),post.getDateTime(),post.getUser().getId(),post.getUser().getUsername()));
        }
        return response;
    }

}
