package com.slots.game.service;

import com.slots.game.response.Response;

import java.math.BigDecimal;

public interface SlotGameService {


    Response deposit(String txanId, String playerId, BigDecimal amount);

}
