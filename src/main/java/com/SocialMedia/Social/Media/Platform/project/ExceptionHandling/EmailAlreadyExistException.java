package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

public class EmailAlreadyExistException extends RuntimeException {
  public EmailAlreadyExistException(String message) {
    super(message);
  }
}
