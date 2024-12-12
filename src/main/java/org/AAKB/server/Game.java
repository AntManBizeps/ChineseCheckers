package org.AAKB.server;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameStarted;

    public Game() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameStarted = false;
    }

    public void addPlayer(Player player) {
        if (!gameStarted) {
            players.add(player);
        } else {
            throw new IllegalStateException("Cannot add players after the game has started.");
        }
    }

    public void startGame() {
        if (players.size() < 2) {
            throw new IllegalStateException("Not enough players to start the game.");
        }
        gameStarted = true;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
