package com.ikenna.echendu.kalah.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GameStatusResponse {
    private String gameCode;
    private String creatorUsername;
    private String opponentUsername;
    private String nextTurn;
    @JsonProperty("isOver")
    private boolean isOver;
    private String winner;
}
