package com.SocialMedia.Social.Media.Platform.project.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignupResponse {
    private boolean success;
    private Long userId;
    private String message;




}