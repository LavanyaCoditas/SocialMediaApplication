package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommentResponse {
    private Long id;
    private String content;
    private String status;
    private Long postId;
    private String username;
}
// final int id;
//  final String content;
//  final String status;
//  final int postId;
//  final String username;
