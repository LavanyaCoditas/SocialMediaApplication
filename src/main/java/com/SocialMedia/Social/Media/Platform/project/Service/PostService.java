package com.SocialMedia.Social.Media.Platform.project.Service;


import com.SocialMedia.Social.Media.Platform.project.DTO.PostDto;
import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.DTO.PostResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.SocialMedia.Social.Media.Platform.project.ExceptionHandling.PostNotFoundException;
import com.SocialMedia.Social.Media.Platform.project.ExceptionHandling.UnauthorizedActionException;
import com.SocialMedia.Social.Media.Platform.project.ExceptionHandling.UserNotFoundException;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import com.SocialMedia.Social.Media.Platform.project.Utils.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService  implements com.SocialMedia.Social.Media.Platform.project.ServiceInterfaces.PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AppUtils appUtils;

    public Posts createPost(PostDto postDTO, String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Posts post = new Posts();

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);
        post.setStatus(PostStatus.PENDING);
        post.setDateTime(LocalDateTime.now());//default the status will be pending
        return postRepo.save(post);
    }

    public Posts editPost(Long id, PostDto postDTO, String username) {
        Posts post = postRepo.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException("Unauthorized");
        }
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setApprovedBy(new ArrayList<>());
        post.setDisapprovedBy(new ArrayList<>());
        post.setStatus(PostStatus.PENDING);
        post.setDateTime(LocalDateTime.now());
        return postRepo.save(post);
    }


    public List<PostResponse> getApprovedPosts() {
        List<Posts>listofPosts=postRepo.findByStatus(PostStatus.APPROVED);
        List<PostResponse> response = new ArrayList<>();
        for(int i = 0; i < listofPosts.size(); i++){
            Long id = listofPosts.get(i).getId();
            String title = listofPosts.get(i).getTitle();
            String content = listofPosts.get(i).getContent();
            PostStatus status = listofPosts.get(i).getStatus();
            LocalDateTime created_at= listofPosts.get(i).getDateTime();
            Long userId= listofPosts.get(i).getUser().getId();
            String username = listofPosts.get(i).getUser().getUsername();
            response.add(new PostResponse(id, title, content, status,created_at,userId,username));
        }

        return response;
    }

    public List<PostResponse> getUserPosts(String username) {
        User user = userRepo.findByUsername(username);
        List <Posts> list = postRepo.findByUser(user);
        List <PostResponse> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            Long postid= list.get(i).getId();
            String title = list.get(i).getTitle();
            String content = list.get(i).getContent();
            PostStatus status= list.get(i).getStatus();
            LocalDateTime dateTime = list.get(i).getDateTime();
            Long userId = list.get(i).getId();
            String user_name=list.get(i).getUser().getUsername();
            response.add(new PostResponse(postid,title,content,status,dateTime,userId,user_name));
        }
        return  response;
    }
    public List<PostResponse> getUsersDeniedPosts(String username)
    {
        User user = userRepo.findByUsername(username);
        List<Posts>listofPosts = postRepo.findByUserUsernameAndStatusOrderById(username,PostStatus.BLACKLISTED);
        List<PostResponse> response= new ArrayList<>();

        for(int i = 0; i < listofPosts.size(); i++) {
            Long id = listofPosts.get(i).getId();
            String title = listofPosts.get(i).getTitle();
            String content = listofPosts.get(i).getContent();
            PostStatus status = listofPosts.get(i).getStatus();
            LocalDateTime created_at = listofPosts.get(i).getDateTime();
            Long userId = listofPosts.get(i).getUser().getId();

            response.add(new PostResponse(id, title, content, status, created_at, userId, username));
        }
        return response;
        }

    public void deletePost(Long postId, String currentUsername) {
        User currentUser = userRepo.findByUsername(currentUsername);
        if(currentUser==null){
            throw new UserNotFoundException("No such user exits");
        }

        Posts post = postRepo.findById(postId).orElseThrow(()
                -> new PostNotFoundException("No post present for given post ID"));

        if(currentUser.getEmail().equals(post.getUser().getEmail())) {
            postRepo.delete(post);
        }
        throw new RuntimeException("User can only delete their own posts");
    }

        @Transactional
        public Posts approvePost(Long id) {
            // Fetch post
            Posts post = postRepo.findById(id)
                    .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

            // Get authenticated user
            String username = appUtils.fetchUsername();
            if (username == null || username.isEmpty()) {
                throw new UserNotFoundException("No authenticated user found in security context");
            }

            // Fetching the moderator
            User moderator = userRepo.findByUsername(username);
            if (moderator == null) {
                throw new UserNotFoundException("Moderator not found with username: " + username);
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
                    //clear disapprovals if post is approved
                    post.getDisapprovedBy().clear();
                }
            }

            return postRepo.save(post);
        }
        @Transactional
        public Posts disapprovePost(Long id) {
            // Fetch post
            Posts post = postRepo.findById(id)
                    .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

            // Get  user
            String username =appUtils.fetchUsername();
            if (username == null || username.isEmpty()) {
                throw new UserNotFoundException("No authenticated user found in security context");
            }

            // Fetch moderator
            User moderator = userRepo.findByUsername(username);
            if (moderator == null) {
                throw new UserNotFoundException("Moderator not found with username: " + username);
            }

            // Author can't disapprove own post
            User postUser = post.getUser();
            if (postUser != null && postUser.getUsername().equals(moderator.getUsername())) {
                throw new UserNotFoundException("Moderators cannot disapprove their own posts!");
            }


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
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            List<Posts>list= postRepo.findByDisapprovedByContaining(user.getUsername());
            List<PostResponse> response = new ArrayList<>();
            for (Posts post : list) {
                response.add(new PostResponse(post.getId(), post.getTitle(),
                        post.getContent(),post.getStatus(),post.getDateTime(),
                        post.getUser().getId(),post.getUser().getUsername()));
            }
            return response;
        }

        public List<PostResponse> getPendingPosts() {
            List<Posts> list = postRepo.findByStatus(PostStatus.PENDING);
            List<PostResponse> response = new ArrayList<>();
            for (Posts post : list) {
                response.add(new PostResponse(post.getId(), post.getTitle(),
                        post.getContent(),post.getStatus(),post.getDateTime(),
                        post.getUser().getId(),post.getUser().getUsername()));
            }
            return response;
        }
//use stream wherveer possible
    public List<PostResponse> getPosts(String username) {
        User user = userRepo.findByUsername(username);
        List<Posts> list = postRepo.findByUser(user);

        return list.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getStatus(),
                        post.getDateTime(),
                        post.getUser().getId(),
                        post.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }
    }


