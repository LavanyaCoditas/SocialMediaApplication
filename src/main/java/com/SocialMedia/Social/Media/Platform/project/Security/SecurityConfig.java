//package com.SocialMedia.Social.Media.Platform.project.Security;
//
//import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig {
//    @Autowired
//    UserRepo userRepo;
//
//    @Autowired
//    private JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll() // Public endpoints
//
//                        .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN") // Super admin only
//                        .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "SUPER_ADMIN") // Moderator and super admin
//                        .requestMatchers("/api/posts/**","/api/comments/**","/api/users/**").hasAnyRole("USER", "MODERATOR", "SUPER_ADMIN") // User actions
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
////
//}
package com.SocialMedia.Social.Media.Platform.project.Security;

import com.SocialMedia.Social.Media.Platform.project.ExceptionHandling.AccessDeniedHandlerException;
import com.SocialMedia.Social.Media.Platform.project.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AccessDeniedHandlerException accessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll() // Public endpoints
                        .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN") // Super admin only
                        .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "SUPER_ADMIN") // Moderator and super admin
                        .requestMatchers("/api/posts/**","/api/comments/**","/api/users/**").hasAnyRole("USER", "MODERATOR", "SUPER_ADMIN") // User actions
                        //actuators endpoint
                        .requestMatchers("/actuator/health").permitAll() // Allow health publicly
                        .requestMatchers("/actuator/**").hasRole("SUPER_ADMIN")
                        .anyRequest().authenticated()

                )
                // Configure exception handling to let GlobalExceptionHandler handle exceptions
                .exceptionHandling(exception -> exception
                                .accessDeniedHandler(accessDeniedHandler) // Only handle access denied
                        // Don't configure authenticationEntryPoint - let GlobalExceptionHandler handle auth failures
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}