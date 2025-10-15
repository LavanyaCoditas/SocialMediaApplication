package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
