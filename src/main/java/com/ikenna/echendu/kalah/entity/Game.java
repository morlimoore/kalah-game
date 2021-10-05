package com.ikenna.echendu.kalah.entity;

import com.ikenna.echendu.kalah.model.Enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends BaseEntity {

    @Column(unique = true, nullable = false, length = 40)
    private String code;

    @Column(nullable = false, length = 40)
    private String creatorUsername;

    @Column(length = 40)
    private String opponentUsername;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Enum.GameStatus status;

    @Column(length = 40)
    private String winner;
}
