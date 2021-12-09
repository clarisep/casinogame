package com.slots.game.service;

import com.slots.game.model.Player;
import com.slots.game.model.PlayerSession;
import com.slots.game.model.PlayerStats;
import com.slots.game.model.Promotion;
import com.slots.game.repo.PlayerRepository;
import com.slots.game.repo.PlayerSessionRepository;
import com.slots.game.repo.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotGameServiceImpl  {

    @Value(value= "${spring.transactions.counter}")
    private String counter;
    private final PlayerRepository playerRepository;
    private final PlayerSessionRepository playerSessionRepository;
    private final PromotionRepository promotionRepository;
    private Promotion promotion;

    private static final String PLAYER_DOES_NOT_EXIST = "Player with ID %s does not exist";
    private static final String TRANSACTION_ID_EXISTS = "Transaction ID %s already exists";

    public Player findPlayerById(final String playerId) {
        return playerRepository.findByPlayerId(playerId);
    }

    public BigDecimal findBalanceByPlayerId(final String playerId) {
        return playerSessionRepository.getBalanceByPlayerId(playerId);
    }

    public ResponseEntity<String> getBalance(final String playerId)  {
        Player player = findPlayerById(playerId);
        if (player != null) {
            BigDecimal balance = findBalanceByPlayerId(playerId);
            if (balance != null) {
                return ResponseEntity.ok(String.format("Player with ID %s has %s balance", playerId, balance));
            }
            return ResponseEntity.ok(String.format("Player with ID %s has zero balance", playerId));
        }
        return ResponseEntity.badRequest().body(String.format(PLAYER_DOES_NOT_EXIST, playerId));
    }

    public ResponseEntity<String> deposit(final String transactionId, final String playerId, final String amount) {
        if (!transactionIdExists(transactionId)) {
            Player player = findPlayerById(playerId);
            if (player != null) {
                PlayerSession playerSession = new PlayerSession(transactionId, player, new BigDecimal(amount), new Timestamp(System.currentTimeMillis()));
                playerSessionRepository.save(playerSession);
                return ResponseEntity.ok().body(String.format("Player with ID %s has won %s", playerId, amount));
            }
            return ResponseEntity.badRequest().body(String.format(PLAYER_DOES_NOT_EXIST, playerId));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(TRANSACTION_ID_EXISTS, transactionId));
    }

    public boolean transactionIdExists(final String transactionId) {
        return playerSessionRepository.findById(transactionId).isPresent();
    }

    public ResponseEntity<String> wager(final String transactionId, final String playerId, String amount) {
        if (!transactionIdExists(transactionId)) {
            Player player = findPlayerById(playerId);
            if (player != null) {
                //if a promotion exists, check if the promo code has already been used, and still is valid
                //and deduct one count from the promotion
                if (promotion != null  &&  (promotion.getCounter() > 0 && promotion.getCounter() <= 5 )) {
                    promotion.setCounter(promotion.getCounter() - 1);
                    promotionRepository.save(promotion);
                    amount = "0";
                }
                BigDecimal balance = findBalanceByPlayerId(playerId);
                if (balance == null || balance.intValue() < 0 || balance.intValue() <  new BigDecimal(amount).intValue()) {
                    return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(HttpStatus.I_AM_A_TEAPOT.toString());
                }
                PlayerSession playerSession = new PlayerSession(transactionId, player, new BigDecimal(amount).negate(),
                        new Timestamp(System.currentTimeMillis()));
                playerSessionRepository.save(playerSession);
                return ResponseEntity.ok().body(String.format("Player with ID %s deducted %s", playerId, amount));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(PLAYER_DOES_NOT_EXIST, playerId));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(TRANSACTION_ID_EXISTS, transactionId));
    }

    //Pam's requirement - last 10 wins/wagers
    public ResponseEntity<String> getTransactionStats(final PlayerStats playerStats) {
        StringBuilder stringBuilder = new StringBuilder();
        if (playerStats.getPassword().equals("swordfish")) {
            Player player = playerRepository.findByPlayerName(playerStats.getPlayerName());
            if (player != null) {
                int cnt = Integer.valueOf(counter);
                List<PlayerSession> playerSessionList = playerSessionRepository.findAllByPlayerOrderByActionTimestampDesc(
                        player, PageRequest.of(0, cnt));
                if (!CollectionUtils.isEmpty(playerSessionList)) {
                    for (PlayerSession session : playerSessionList) {
                        stringBuilder.append(session.toString() + "\n");
                    }
                    return ResponseEntity.ok().body(String.format("Last %s wagers/deposits for player %s: %n",
                            counter, playerStats.getPlayerName()) + stringBuilder);
                }
                return ResponseEntity.ok().body(String.format("No transactions found for player with id %s", player.getPlayerId()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(PLAYER_DOES_NOT_EXIST, playerStats.getPlayerName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Operation is not authorized");
    }

    // Promotion code
    public ResponseEntity<String> wager(final String transactionId, final String playerId,
                                        final String amount, final String promotionCode) {

        if (!transactionIdExists(transactionId)) {

            if (promotionCode.equals("paper")) {
                Player player = findPlayerById(playerId);
                if (player != null) {
                    promotion = promotionRepository.findById(1);
                    return wager(transactionId, playerId, amount);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(PLAYER_DOES_NOT_EXIST, playerId));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid promotion code %s" , promotionCode));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(TRANSACTION_ID_EXISTS, transactionId));
    }


}

