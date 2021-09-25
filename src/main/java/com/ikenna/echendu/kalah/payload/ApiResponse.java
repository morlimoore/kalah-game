package com.ikenna.echendu.kalah.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.ikenna.echendu.kalah.util.AppUtil.getDateTimeFormat;

@Getter
@Setter
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private String time = LocalDateTime.now().format(getDateTimeFormat());
    private T result;

    @Override
    public String toString() {
        return "{" +
                "\"status\": " + "\"" + status.getReasonPhrase().toUpperCase() + "\"" + "," +
                "\"message\": " + "\"" + message + "\"" + "," +
                "\"time\": " + "\"" + time + "\"" + "," +
                "\"result\": " + "\"" + result + "\"" +
                "}";
    }
}