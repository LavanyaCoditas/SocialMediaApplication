package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
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