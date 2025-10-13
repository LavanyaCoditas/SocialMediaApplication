package com.SocialMedia.Social.Media.Platform.project.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

    public String fetchUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
