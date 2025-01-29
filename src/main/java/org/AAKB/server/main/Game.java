package org.AAKB.server.main;

import org.AAKB.constants.Coordinates;
import org.AAKB.constants.PlayerColor;
import org.AAKB.server.board.Move;
import org.AAKB.server.board.UnplayableFieldException;
import org.AAKB.server.movement.*;
import org.AAKB.server.board.ClassicBoardFactory;
import org.AAKB.server.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Game {
    private final ArrayList<Rookie> rookies;

    private final int totalNumberOfPlayers;

    private final int realPlayers;

    private final int botPlayers;

    private final AtomicInteger numberOfCurrentPlayers = new AtomicInteger(0);

    private PlayerColor[] playerColors;

    private final GameMaster gameMaster;

    private final AtomicInteger currentPlayerTurn = new AtomicInteger(0);

    private final ArrayList<AbstractPlayer> players;

    private final ArrayList<Thread> playerThreads;

    private JumpStatusVerifyCondition jumpStatus;

    private PreviousPawnVerifyCondition previousPawn;

    private AdditionalVerifyCondition[] conditions;


    public Game(ArrayList<Rookie> rookies, int botPlayers) throws Exception {
        this.totalNumberOfPlayers = rookies.size()+botPlayers;
        this.numberOfCurrentPlayers.set(rookies.size());
        this.botPlayers = botPlayers;
        this.realPlayers = rookies.size();
        this.rookies = rookies;

        players = new ArrayList<>();
        playerThreads = new ArrayList<>();

        gameMaster = new GameMaster(new ClassicMovementStrategy(), new ClassicBoardFactory());
        gameMaster.initializeBoard(totalNumberOfPlayers);
        playerColors = gameMaster.getPossibleColorsForPlayers(totalNumberOfPlayers);

        addRealPlayers();
        addBotPlayers();
        sleep(2000);
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

    private void addBotPlayers() throws Exception {
        for(int i=0; i<botPlayers; i++){
            int id = numberOfCurrentPlayers.incrementAndGet();
            Cobot botPlayer = new Cobot(this, id, playerColors[id-1]);
            players.add(botPlayer);
            Thread thread = new Thread(botPlayer);
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

    public Move processMove(Move input){
        if(input == null){
            return null;
        }
        int outcome = gameMaster.verifyMove(input, conditions);
        jumpStatus.setStatus(outcome);
        previousPawn.setCurrentXY(input.getFromX(), input.getFromY());
        if(outcome == 1){
            gameMaster.makeMove(input);
            propagateMove();
            nextTurn();
            return input;
        } else if(outcome == 2) {
            gameMaster.makeMove(input);
            propagateMove();
            return input;
        }
        return null;
    }

    public void propagateMove(){
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
        broadcast(commandBuilder.getCommand());
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

    public Move chooseBestMove(PlayerColor color) throws UnplayableFieldException {
        List<Coordinates> myPawns = gameMaster.getBoard().getCoordinatesByCurrentColor(color);
        List<Coordinates> targetFields = gameMaster.getBoard().getCoordinatesByTargetColor(color);
        if (myPawns != null && !myPawns.isEmpty() && targetFields != null && !targetFields.isEmpty()) {
            Coordinates targetMove = Coordinates.getAverageCoordinates(targetFields);
            for (int i = 0; i < myPawns.size(); i++) {
                Coordinates bestStartMove = Coordinates.getClosestCoordinate(myPawns, targetMove);
                if (bestStartMove != null) {
                    PlayerColor kolor = gameMaster.getBoard().getNativeColor(bestStartMove.getX(), bestStartMove.getY());
                    if (gameMaster.getBoard().getCurrentColor(bestStartMove.getX(), bestStartMove.getY()) != gameMaster.getBoard().getTargetColor(bestStartMove.getX(), bestStartMove.getY())) {
                        List<Coordinates> possiblePawns = gameMaster.getPossibleMovesForPos(bestStartMove.getX(), bestStartMove.getY(), conditions);
                        if (!possiblePawns.isEmpty()) {
                            Coordinates bestEndMove = Coordinates.getClosestCoordinate(possiblePawns, targetMove);
                            if (bestEndMove != null) {
                                return new Move(bestStartMove.getX(), bestStartMove.getY(), bestEndMove.getX(), bestEndMove.getY());
                            } else {
                                myPawns.remove(bestStartMove);
                            }
                        } else myPawns.remove(bestStartMove);
                    } else myPawns.remove(bestStartMove);
                }
            }
        }
        return null;
    }
}

