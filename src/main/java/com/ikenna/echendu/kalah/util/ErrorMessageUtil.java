package com.ikenna.echendu.kalah.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageUtil {

    public static String authErrorMessage = "";

    public static void setAuthErrorMessage(String error) {
        authErrorMessage = error;
    }

    public static String getAuthErrorMessage() {
        return authErrorMessage;
    }
}
