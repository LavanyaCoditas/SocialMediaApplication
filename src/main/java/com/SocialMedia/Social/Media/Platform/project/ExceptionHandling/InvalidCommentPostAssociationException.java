package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class InvalidCommentPostAssociationException extends RuntimeException {
    public InvalidCommentPostAssociationException(String message) {
        super(message);
    }
}
