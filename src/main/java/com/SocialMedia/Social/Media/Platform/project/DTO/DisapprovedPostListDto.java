package com.SocialMedia.Social.Media.Platform.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisapprovedPostListDto {
    private Long id;

    private String title;


    private String content;

    private String username;
}
