package com.ikenna.echendu.kalah.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageUtil {

    public static String errorMessage = "";

    public static void setErrorMessage(String error) {
        errorMessage = error;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
