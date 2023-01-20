package com.example.Personal.Data.Management.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class UserIdUtil {

    public static String getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String UserId = authentication.getName();
        return UserId;
    }
}
