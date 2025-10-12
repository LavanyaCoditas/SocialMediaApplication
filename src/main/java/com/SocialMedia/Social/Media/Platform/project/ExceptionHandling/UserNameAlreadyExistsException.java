package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class UserNameAlreadyExistsException extends RuntimeException{
    public UserNameAlreadyExistsException() {
    }

    public UserNameAlreadyExistsException(String message) {
        super(message);
    }

    public UserNameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
