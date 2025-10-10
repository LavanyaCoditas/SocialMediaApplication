package com.SocialMedia.Social.Media.Platform.project.DTO;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;


}