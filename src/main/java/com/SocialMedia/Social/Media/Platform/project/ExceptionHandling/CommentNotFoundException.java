package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException() {
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}



