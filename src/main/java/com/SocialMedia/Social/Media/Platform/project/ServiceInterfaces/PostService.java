package com.SocialMedia.Social.Media.Platform.project.ServiceInterfaces;

import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;

import java.util.List;

public interface PostService {
    public Posts createPost(PostDto postDTO, String username);
    public Posts editPost(Long id, PostDto postDTO, String username);
    public List<PostResponse> getApprovedPosts();
    public List<PostResponse> getUserPosts(String username);
    public List<PostResponse> getUsersDeniedPosts(String username);
    public void deletePost(Long postId, String currentUsername);
    public Posts approvePost(Long id);
    public Posts disapprovePost(Long id);
    public List<PostResponse> getBlockedPostsByModerator(String username);
    public List<PostResponse> getPendingPosts();
    public List<PostResponse> getPosts(String username);
}
