package com.SocialMedia.Social.Media.Platform.project.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
   private boolean success;
    private String token;
   private String message;


}