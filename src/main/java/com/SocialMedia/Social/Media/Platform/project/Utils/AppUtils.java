package com.SocialMedia.Social.Media.Platform.project.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

    //loggedInUserName()
    public String fetchUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
