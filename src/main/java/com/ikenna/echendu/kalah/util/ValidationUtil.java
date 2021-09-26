package com.ikenna.echendu.kalah.util;

import com.ikenna.echendu.kalah.exception.ApiException;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ValidationUtil {

    public static void checkFieldErrors(BindingResult result) {
        if (result.hasErrors()) {
            Optional<Object> optionalObject = Optional.ofNullable(result.getFieldError());
            if (optionalObject.isPresent()) {
                Optional<String> optionalMessage = Optional.ofNullable(result.getFieldError().getDefaultMessage());
                if (optionalMessage.isPresent())
                    throw new ApiException(BAD_REQUEST, optionalMessage.get());
            }
        }
    }

}
