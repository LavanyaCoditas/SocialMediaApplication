package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
