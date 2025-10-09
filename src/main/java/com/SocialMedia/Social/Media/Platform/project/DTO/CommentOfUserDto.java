package com.SocialMedia.Social.Media.Platform.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentOfUserDto {
    private String content;

    private Long postId;
    private String status;
}
