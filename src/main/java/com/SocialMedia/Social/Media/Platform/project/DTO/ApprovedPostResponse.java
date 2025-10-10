package com.SocialMedia.Social.Media.Platform.project.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedPostResponse
{
    private Long id;



    private String title;


    private String content;

    private String username;

}
