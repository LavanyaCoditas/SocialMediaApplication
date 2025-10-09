package com.SocialMedia.Social.Media.Platform.project.Service;


import com.SocialMedia.Social.Media.Platform.project.DTO.ApprovedPostResponse;
import com.SocialMedia.Social.Media.Platform.project.DTO.DisapprovedPostListDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    public Posts createPost(PostDto postDTO, String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Posts post = new Posts();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);
        post.setStatus(PostStatus.PENDING); // Default per entity
        return postRepo.save(post);
    }

    public Posts editPost(Long id, PostDto postDTO, String username) {
        Posts post = postRepo.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setApprovedBy(new ArrayList<>());
        post.setDisapprovedBy(new ArrayList<>());
        post.setStatus(PostStatus.PENDING);
        return postRepo.save(post);
    }


    public List<ApprovedPostResponse> getApprovedPosts() {
        List<Posts>listofPosts=postRepo.findByStatus(PostStatus.APPROVED);
        List<ApprovedPostResponse> response = new ArrayList<>();
        for(int i = 0; i < listofPosts.size(); i++){
            Long id = listofPosts.get(i).getId();
            String title = listofPosts.get(i).getTitle();
            String content = listofPosts.get(i).getContent();
            String username = listofPosts.get(i).getUser().getUsername();

            response.add(new ApprovedPostResponse(id, title, content, username));
        }

        return response;
    }

    public List<PostDto> getUserPosts(String username) {
        User user = userRepo.findByUsername(username);
        List <Posts> list = postRepo.findByUser(user);
        List <PostDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String title = list.get(i).getTitle();
            String content = list.get(i).getContent();
            response.add(new PostDto(title,content));
        }
        return  response;

    }
    public List<DisapprovedPostListDto> getUsersDeniedPosts(String username)
    {
        User user = userRepo.findByUsername(username);
        List<Posts>listOfDenied = postRepo.findByUserUsernameAndStatusOrderById(username,PostStatus.BLACKLISTED);
        List<DisapprovedPostListDto> response= new ArrayList<>();

        for (int i = 0; i < listOfDenied.size(); i++) {
            Long id = listOfDenied.get(i).getId();
            String title = listOfDenied.get(i).getTitle();
            String content = listOfDenied.get(i).getContent();
            String userName = listOfDenied.get(i).getUser().getUsername();

            response.add(new DisapprovedPostListDto(id, title, content, userName));
        }
        return response;
    }

    public void deletePost(Long postId, String currentUsername) {
        User currentUser = userRepo.findByUsername(currentUsername);
        if(currentUser==null){
            throw new RuntimeException("No such user exits");
        }

        Posts post = postRepo.findById(postId).orElseThrow(()
                -> new RuntimeException("No post present for given post ID"));

        if(currentUser.getEmail().equals(post.getUser().getEmail())) {
            postRepo.delete(post);
        }
        throw new RuntimeException("User can only delete their own posts");
    }
}
