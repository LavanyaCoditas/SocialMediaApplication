package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String content;

    private Long userId;
}
