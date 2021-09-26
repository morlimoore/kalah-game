package com.ikenna.echendu.kalah.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends BaseEntity {

    @Column(unique = true, nullable = false, length = 40)
    private String gameCode;

    @Column(nullable = false, length = 40)
    private String creatorUsername;

    @Column(length = 40)
    private String opponentUsername;

    @Column(nullable = false, length = 5)
    private boolean isOver;

    @Column(length = 40)
    private String winner;
}
