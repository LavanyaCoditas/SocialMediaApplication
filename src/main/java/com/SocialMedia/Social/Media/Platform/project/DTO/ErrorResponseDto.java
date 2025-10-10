package com.SocialMedia.Social.Media.Platform.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    HttpStatus status;
    String error;
    String message;
    LocalDateTime timestamp;
}