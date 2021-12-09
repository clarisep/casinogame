package com.slots.game.model;

import javax.persistence.*;

@Entity
public class Player {

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Id
    private String playerId;
    private String playerName;

}
