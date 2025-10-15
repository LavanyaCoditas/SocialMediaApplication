package com.SocialMedia.Social.Media.Platform.project.ExceptionHandling;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String jsonResponse=String.format("{\"timestamp\": \"%s\", \"message\": \"Access denied: you are trying to acces " +
                        "wrong endpoint other than your authority\", \"status\": 403}",
                LocalDateTime.now(),
                request.getRequestURI());
        response.getWriter().write(jsonResponse);;

    }
}