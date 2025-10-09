package com.SocialMedia.Social.Media.Platform.project.Service;

import com.SocialMedia.Social.Media.Platform.project.Constants.CommentStatus;
import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.SocialMedia.Social.Media.Platform.project.Constants.Role;
import com.SocialMedia.Social.Media.Platform.project.DTO.CommentOfUserDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.*;
import com.SocialMedia.Social.Media.Platform.project.Repository.CommentRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.PostRepo;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    public Comments createComment(String content, Long postId, String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Posts post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found! Maybe it is blacklisted "));

        if (post.getStatus() != PostStatus.APPROVED) {
            throw new RuntimeException("Cannot comment on unapproved post :( sorry ");
        }
        Comments comment = new Comments();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        //keeping the default status of each comment pending
        comment.setStatus(CommentStatus.PENDING);
        return commentRepo.save(comment);
    }

    public Comments editComment(Long id, String content, String username)
    {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized: Only owner can edit");
        }
        comment.setContent(content);
        comment.setApprovedBy(new java.util.ArrayList<>());
        comment.setDisapprovedBy(new java.util.ArrayList<>());
        comment.setStatus(CommentStatus.PENDING);
        return commentRepo.save(comment);
    }

    public void deleteComment(Long id, String username) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepo.findByUsername(username);
        boolean isOwner = comment.getUser().getUsername().equals(username);
        boolean isModerator = user.getRole() == Role.ROLE_MODERATOR || user.getRole() == Role.ROLE_SUPER_ADMIN;
        if (!isOwner && !isModerator) {
            throw new RuntimeException("Unauthorized: Only owner or moderator can delete");
        }
        commentRepo.delete(comment);
    }

    @Transactional
    public Comments approveComment(Long id, String moderatorUsername) {
        // Fetch comment
        Comments comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));

        // Fetch moderator
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null) {
            throw new RuntimeException("Moderator not found with username: " + moderatorUsername);
        }
        if (!(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized access: Only moderators or super admins can approve");
        }

        // Prevent moderator from approving their own comment
        User commentUser = comment.getUser();
        if (commentUser != null && commentUser.getUsername().equals(moderator.getUsername())) {
            throw new RuntimeException("Moderators cannot approve their own comments!");
        }

        // Initialize lists if null
        if (comment.getApprovedBy() == null) {
            comment.setApprovedBy(new ArrayList<>());
        }
        if (comment.getDisapprovedBy() == null) {
            comment.setDisapprovedBy(new ArrayList<>());
        }

        // Approval logic
        String moderatorId = moderator.getId().toString();
        if (!comment.getApprovedBy().contains(moderatorId)) {
            comment.getApprovedBy().add(moderatorId);
        }
        if (comment.getApprovedBy().size() >= 1 && comment.getDisapprovedBy().isEmpty()) {
            comment.setStatus(CommentStatus.APPROVED);
        }

        return commentRepo.save(comment);
    }
//    public Comments approveComment(Long id, String moderatorUsername) {
//        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
//        User moderator = userRepo.findByUsername(moderatorUsername);
//        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
//            throw new RuntimeException("Unauthorized acces : Only moderators or super admins can approve");
//        }
//        String moderatorId = moderator.getId().toString();
//        if (!comment.getApprovedBy().contains(moderatorId)) {
//            comment.getApprovedBy().add(moderatorId);
//        }
//        if (comment.getApprovedBy().size() >= 1 && comment.getDisapprovedBy().isEmpty()) {
//            comment.setStatus(CommentStatus.APPROVED);
//        }
//        return commentRepo.save(comment);
//    }
    public Comments disapproveComment(Long id, String moderatorUsername) {
        Comments comment = commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized: Only moderators or super admins can disapprove");
        }
        String moderatorId = moderator.getId().toString();
        if (!comment.getDisapprovedBy().contains(moderatorId)) {
            comment.getDisapprovedBy().add(moderatorId);
        }
        if (comment.getDisapprovedBy().size() >= 1) {
            comment.setStatus(CommentStatus.BLACKLISTED);
        }
        return commentRepo.save(comment);
    }



    public List<CommentResponse> getApprovedCommentsForPost(Long postId)
    {
        Posts post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        List<Comments> commentsList=commentRepo.findByPostAndStatus(post,CommentStatus.APPROVED);
        List<CommentResponse> response = new ArrayList<>();
        for (int i = 0; i < commentsList.size(); i++)
        {
          String content = commentsList.get(i).getContent().toString();
          Long userId= commentsList.get(i).getUser().getId();
          response.add(new CommentResponse(content,userId));
        }
        return response;
    }

    public List<CommentOfUserDto> getUserComments(String username) {
        User user = userRepo.findByUsername(username);
        Long id = user.getId();
        List<Comments> list = userRepo.findById(id).get().getCommentsList();
        List<CommentOfUserDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String content = list.get(i).getContent();
            Long commentId= list.get(i).getId();
            String status=list.get(i).getStatus().toString();
            response.add(new CommentOfUserDto(content,commentId,status));
        }
        return response;
    }

    public List<CommentOfUserDto> getPendingComments() {
        List<Comments> list= commentRepo.findByStatusOrderById(CommentStatus.PENDING);

        List<CommentOfUserDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String content = list.get(i).getContent();
            Long commentId= list.get(i).getId();
            String status=list.get(i).getStatus().toString();
            response.add(new CommentOfUserDto(content,commentId,status));
        }
        return response;
    }


    public List<CommentOfUserDto> getBlockedComments(String moderatorUsername)
    {
        User moderator = userRepo.findByUsername(moderatorUsername);
        if (moderator == null || !(moderator.getRole() == Role.ROLE_MODERATOR || moderator.getRole() == Role.ROLE_SUPER_ADMIN)) {
            throw new RuntimeException("Unauthorized: Only moderators or super admins can view blocked comments");
        }
        List<Comments> list=commentRepo.findByDisapprovedByContaining(moderator.getId().toString());
        List<CommentOfUserDto> response=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String content = list.get(i).getContent();
            Long commentId= list.get(i).getId();
            String status=list.get(i).getStatus().toString();
            response.add(new CommentOfUserDto(content,commentId,status));
        }
        return response;

    }

    public List<CommentOfUserDto> getAllComments()
    {
        List<Comments> list= commentRepo.findAll();

        List<CommentOfUserDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String content = list.get(i).getContent();
            Long commentId= list.get(i).getId();
            String status=list.get(i).getStatus().toString();
            response.add(new CommentOfUserDto(content,commentId,status));
        }
        return response;

    }
}