package com.ikenna.echendu.kalah.repository;

import com.ikenna.echendu.kalah.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByCode(String gameCode);

    Boolean existsByCode(String gameCode);

    @Query(value = "SELECT * FROM games WHERE creator_username = ? AND status != 'CONCLUDED'", nativeQuery = true)
    List<Game> getOngoingGamesByCreatorUsername(String username);

    @Query(value = "SELECT * FROM games WHERE opponent_username = ? AND status != 'CONCLUDED'", nativeQuery = true)
    List<Game> getOngoingGamesByOpponentUsername(String username);

    Optional<Game> findById(Long id);

    @Query(value = "SELECT * FROM games WHERE creator_username = ? AND code = ?", nativeQuery = true)
    List<Game> getGamesByCreatorUsernameAndCode(String username, String gameCode);

    @Query(value = "SELECT * FROM games WHERE opponent_username = ? AND code = ?", nativeQuery = true)
    List<Game> getGamesByOpponentUsernameAndCode(String username, String gameCode);
}
