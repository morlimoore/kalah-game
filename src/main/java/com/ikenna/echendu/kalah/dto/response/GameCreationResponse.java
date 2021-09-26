package com.ikenna.echendu.kalah.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GameCreationResponse {
    private String gameCode;
}
