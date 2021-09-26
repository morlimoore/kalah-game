package com.ikenna.echendu.kalah.util;

import com.ikenna.echendu.kalah.exception.ApiException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtil {

    public static String getLoggedInUsername() {
        String username = "";
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }
        if (username.isEmpty())
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while validating user, " +
                    "please try again.");
        return username;
    }
}
