package com.slots.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
public class PlayerSession {

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Timestamp getActionTimestamp() {
        return actionTimestamp;
    }

    public Player getPlayer() {
        return player;
    }

    @Id
    private String transactionId;
    private BigDecimal amount;
    private Timestamp actionTimestamp;

    public PlayerSession(final String transactionId, final Player player, final BigDecimal amount, final Timestamp actionTimestamp) {
        this.transactionId = transactionId;
        this.player = player;
        this.amount = amount;
        this.actionTimestamp = actionTimestamp;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Player player;

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId + ", Player ID : " + " , amount: " + amount
                + ", Timestamp: " + actionTimestamp;

    }
}

