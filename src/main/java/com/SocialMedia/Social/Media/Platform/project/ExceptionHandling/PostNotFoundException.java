package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;


public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException (String msg) {
        super(msg);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostNotFoundException(Throwable cause) {
        super(cause);
    }
}
