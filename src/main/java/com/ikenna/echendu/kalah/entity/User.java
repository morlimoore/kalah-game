package com.ikenna.echendu.kalah.entity;

import com.ikenna.echendu.kalah.model.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(unique = true, nullable = false, length = 40)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Enum.Role role;

    @Column(nullable = false)
    private long totalGamesPlayed;

    @Column(nullable = false)
    private long numberOfWins;
}