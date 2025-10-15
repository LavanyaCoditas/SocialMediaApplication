//package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;
//
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@ControllerAdvice(basePackages = "com.SocialMedia.Social.Media.Platform.project")
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, Object> errors = new HashMap<>();
//
//        // Get field-specific errors
//        List<String> errorMessages = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        errors.put("timestamp", LocalDateTime.now());
//        errors.put("message", "Validation failed");
//        errors.put("errors", errorMessages); // This will show specific field errors
//        errors.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UserNameAlreadyExistsException.class)
//    public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExists(UserNameAlreadyExistsException ex) {
//        Map<String, Object> errors = new HashMap<>();
//        errors.put("timestamp", LocalDateTime.now());
//        errors.put("message", ex.getMessage());
//        errors.put("status", HttpStatus.CONFLICT.value());
//        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(EmailAlreadyExistException.class)
//    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistException ex) {
//        Map<String, Object> errors = new HashMap<>();
//        errors.put("timestamp", LocalDateTime.now());
//        errors.put("message", ex.getMessage());
//        errors.put("status", HttpStatus.CONFLICT.value());
//        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(PostNotFoundException.class)
//    public ResponseEntity<Object> handlePostNotFound(PostNotFoundException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidPasswordException.class)
//    public ResponseEntity<Object> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.UNAUTHORIZED.value());
//        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
//    }
//     //handles change in request in postman
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
//        Map<String, Object> errorResponse = new LinkedHashMap<>();
//        errorResponse.put("timestamp", LocalDateTime.now());
//        errorResponse.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
//        errorResponse.put("error", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
//        errorResponse.put("message", "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint. Supported methods: " +
//                (ex.getSupportedHttpMethods() != null ? ex.getSupportedHttpMethods() : "None"));
//        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
//    }
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(CommentNotFoundException.class)
//    public ResponseEntity<Object> handleCommentNotFound(CommentNotFoundException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//
//    @ExceptionHandler(UnauthorizedActionException.class)
//    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.UNAUTHORIZED.value());
//        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(InvalidCommentPostAssociationException.class)
//    public ResponseEntity<Object> handleInvalidCommentPostAssociationException(InvalidCommentPostAssociationException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("message", ex.getMessage());
//        errorResponse.put("timestamp", LocalDateTime.now());
//        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }
//
//}
package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.SocialMedia.Social.Media.Platform.project")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        errors.put("timestamp", LocalDateTime.now());
        errors.put("message", "Validation failed");
        errors.put("errors", errorMessages);
        errors.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        errorResponse.put("error", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        errorResponse.put("message", "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint. Supported methods: " +
                (ex.getSupportedHttpMethods() != null ? ex.getSupportedHttpMethods() : "None"));
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Object> handleUnauthorizedActionException(UnauthorizedActionException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFound(CommentNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handelEmailNotFound(EmailNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidCommentPostAssociationException.class)
    public ResponseEntity<Object> handleInvalidCommentPostAssociationException(InvalidCommentPostAssociationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExists(UserNameAlreadyExistsException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("message", ex.getMessage());
        errors.put("status", HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllOther(Exception e) {
        Map<String, Object> data = new HashMap<>();
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

        @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}