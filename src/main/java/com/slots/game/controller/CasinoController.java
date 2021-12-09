package com.slots.game.controller;

import com.slots.game.model.PlayerStats;
import com.slots.game.service.SlotGameServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;


@Controller
@Slf4j
@RequiredArgsConstructor
public  class CasinoController {

    private final SlotGameServiceImpl slotGameService;

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(@RequestParam(value = "playerId") final String playerId) {
        return slotGameService.getBalance(playerId);
    }

    @PostMapping("/wager")
    public ResponseEntity<String> deductMoney(@RequestParam(value = "transactionId") final String transactionId,
                                              @RequestParam(value = "playerId") final String playerId,
                                              @RequestParam(value = "amount") final String amount,
                                              @RequestParam(value = "promotionCode", required = false) final String promotionCode)
    {
        if (StringUtils.isEmpty(promotionCode)) {
            return slotGameService.wager(transactionId, playerId, amount);
        }
        return slotGameService.wager(transactionId, playerId, amount, promotionCode);

    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositMoney(@RequestParam(value="transactionId") final String transactionId,
                                               @RequestParam(value="playerId") final String playerId,
                                               @RequestParam(value="amount") final String amount)
    {
        return slotGameService.deposit(transactionId, playerId, amount);
    }

    @GetMapping(value = "/gettransactions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTransactions(@RequestBody final PlayerStats playerstats) {
        return slotGameService.getTransactionStats(playerstats);
    }

}

