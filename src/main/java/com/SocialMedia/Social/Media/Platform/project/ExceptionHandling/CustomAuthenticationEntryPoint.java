//package com.example.Social_Media_Portal.Exception;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) {
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        try {
//            response.getWriter().write("{\"error\": \"Unauthorized \"}");
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//    }
//}