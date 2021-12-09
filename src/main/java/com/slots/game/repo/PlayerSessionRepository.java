package com.slots.game.repo;

import com.slots.game.model.Player;
import com.slots.game.model.PlayerSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PlayerSessionRepository extends CrudRepository<PlayerSession, String> {

    @Query(value = "select sum(ps.amount) from PlayerSession ps where ps.player.playerId in :playerId")

    BigDecimal getBalanceByPlayerId(@Param("playerId") String playerId);

    List<PlayerSession> findAllByPlayerOrderByActionTimestampDesc(@Param("Player") Player player, Pageable pageable);

}

