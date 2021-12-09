package com.slots.game.repo;

import com.slots.game.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {

    public Player findByPlayerId(String playerId);
    public Player findByPlayerName(String playerName);



}
