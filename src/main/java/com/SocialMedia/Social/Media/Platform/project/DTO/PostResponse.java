package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

    private Long postId;
    private PostStatus status;



}