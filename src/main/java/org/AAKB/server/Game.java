package org.AAKB.server;

import org.AAKB.constants.PlayerColor;
import org.AAKB.server.board.ClassicBoardFactory;
import org.AAKB.server.player.AbstractPlayer;
import org.AAKB.server.player.RealPlayer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    private final ArrayList<Rookie> rookies;

    private final int totalNumberOfPlayers;

    private final AtomicInteger numberOfCurrentPlayers = new AtomicInteger(0);

    private PlayerColor[] playerColors;

    private final GameMaster gameMaster;

    private final AtomicInteger currentPlayerTurn = new AtomicInteger(0);

    private final ArrayList<AbstractPlayer> players;

    private final ArrayList<Thread> playerThreads;

    private volatile boolean turnFinished;

    public Game(ArrayList<Rookie> rookies, int totalPlayers) throws Exception {
        this.totalNumberOfPlayers = totalPlayers;
        this.numberOfCurrentPlayers.set(rookies.size());
        this.rookies = rookies;

        players = new ArrayList<>();
        playerThreads = new ArrayList<>();

        gameMaster = new GameMaster(new ClassicMovementStrategy(), new ClassicBoardFactory());
        gameMaster.initializeBoard(totalPlayers);
        playerColors = gameMaster.getPossibleColorsForPlayers(totalPlayers);

        turnFinished = true;

        addRealPlayers();
    }

    private void addRealPlayers() throws Exception {
        for(Rookie rookie : rookies){
            RealPlayer realPlayer = new RealPlayer(this, rookie, playerColors[rookie.getId()-1]);
            players.add(realPlayer);
            Thread thread = new Thread(realPlayer);
            playerThreads.add(thread);
            thread.start();
        }
    }

    public GameMaster getGameMaster() {
        return gameMaster;
    }

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void setIsTurnFinished(boolean turnFinished) {
        this.turnFinished = turnFinished;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn.get();
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn.set(currentPlayerTurn);
    }

    public void broadcast(String message) {
        for(AbstractPlayer player : players){
            player.sendCommand(message);
        }
    }

}

