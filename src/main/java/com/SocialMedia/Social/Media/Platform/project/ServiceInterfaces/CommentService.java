package com.SocialMedia.Social.Media.Platform.project.ServiceInterfaces;

import com.SocialMedia.Social.Media.Platform.project.DTO.CommentResponse;
import com.SocialMedia.Social.Media.Platform.project.Entity.Comments;

import java.util.List;

public interface CommentService {
    public Comments createComment(String content, Long postId, String username);
    public Comments editComment(Long id, String content, String username);
    public void deleteComment(Long id, String username);
    public Comments approveComment(Long id, String moderatorUsername);
    public Comments disapproveComment(Long id, String moderatorUsername);
    public List<CommentResponse> getApprovedCommentsForPost(Long postId);
    public List<CommentResponse> getUserComments(String username);
    public List<CommentResponse> getPendingComments();
    public List<CommentResponse> getBlockedComments(String moderatorUsername);
    public List<CommentResponse> getAllComments();
}
