package com.slots.game.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class SlotGameServiceImplTest {

    @BeforeEach
    void setUp() {
     System.out.println(new Date(System.currentTimeMillis()));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);

    }

    @Test
    void findPlayerById() {
    }

    @Test
    void findBalanceByPlayerId() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void deposit() {
    }

    @Test
    void transactionIdExists() {
    }

    @Test
    void wager() {
    }
}