package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private Long postId;
    private  String title;
    private String content;
    private PostStatus status;

    private LocalDateTime created_at;
    private Long userId;
    private String username;




}