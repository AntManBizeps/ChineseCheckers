package org.AAKB.server.main;

import org.AAKB.constants.PlayerColor;
import org.AAKB.server.InputInterpeter;
import org.AAKB.server.board.Move;
import org.AAKB.server.movement.*;
import org.AAKB.server.board.ClassicBoardFactory;
import org.AAKB.server.player.AbstractPlayer;
import org.AAKB.server.player.CommandBuilder;
import org.AAKB.server.player.RealPlayer;
import org.AAKB.server.player.Rookie;

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

    private JumpStatusVerifyCondition jumpStatus;

    private PreviousPawnVerifyCondition previousPawn;

    private AdditionalVerifyCondition[] conditions;


    public Game(ArrayList<Rookie> rookies, int totalPlayers) throws Exception {
        this.totalNumberOfPlayers = totalPlayers;
        this.numberOfCurrentPlayers.set(rookies.size());
        this.rookies = rookies;

        players = new ArrayList<>();
        playerThreads = new ArrayList<>();

        gameMaster = new GameMaster(new ClassicMovementStrategy(), new ClassicBoardFactory());
        gameMaster.initializeBoard(totalPlayers);
        playerColors = gameMaster.getPossibleColorsForPlayers(totalPlayers);

        addRealPlayers();
        nextTurn();
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

    public void nextTurn() {
        setDefaultVerificationConditions();
        currentPlayerTurn.updateAndGet(current -> ((current % totalNumberOfPlayers)+1));
        AbstractPlayer foundPlayer = players.stream()
                .filter(player -> player.getId() == currentPlayerTurn.get())
                .findFirst()
                .orElse(null);
        if(foundPlayer != null) {
            foundPlayer.sendCommand("GAME Your turn Player " + currentPlayerTurn.get());
        } else {
            nextTurn();
        }
    }

    private void setDefaultVerificationConditions() {
        jumpStatus = new JumpStatusVerifyCondition( 0 );
        previousPawn = new PreviousPawnVerifyCondition();
        conditions = new AdditionalVerifyCondition[]{ jumpStatus, previousPawn };
    }

    public Move processMove(String input){
        Move move = InputInterpeter.getMoveFromString(input);
        if(move == null){
            System.out.println("kupa");
            return null;
        }
        int outcome = gameMaster.verifyMove(move, conditions);
        jumpStatus.setStatus(outcome);
        previousPawn.setCurrentXY(move.getFromX(), move.getFromY());
        if(outcome == 1){
            gameMaster.makeMove(move);
            propagateMove();
            nextTurn();
            return move;
        } else if(outcome == 2) {
            gameMaster.makeMove(move);
            propagateMove();
            return move;
        }
        return null;
    }

    public void propagateMove(){
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
    }

    public GameMaster getGameMaster() {
        return gameMaster;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn.get();
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn.set(currentPlayerTurn);
    }

    public boolean hasWinner() {
        for(AbstractPlayer player : players){
            if(player.isWinner()) return true;
        }
        return false;
    }

    public void broadcast(String message) {
        for(AbstractPlayer player : players){
            player.sendCommand(message);
        }
    }

    public AbstractPlayer hosIsWinner() {
        for(AbstractPlayer player : players){
            if(player.isWinner()) return player;
        }
        return null;
    }

}

