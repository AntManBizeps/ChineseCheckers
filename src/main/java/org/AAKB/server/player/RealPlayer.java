package org.AAKB.server.player;

import org.AAKB.constants.PlayerColor;
import org.AAKB.server.InputInterpeter;
import org.AAKB.server.board.Move;
import org.AAKB.server.main.Game;

public class RealPlayer extends AbstractPlayer implements Runnable {

    private final CommunicationManager communicationManager;

    private final Game game;

    public RealPlayer(Game game, Rookie rookie, PlayerColor color) throws Exception {
        setId(rookie.getId());
        setColor(color);
        this.communicationManager = rookie.getCommunicationManager();
        this.game = game;
    }

    @Override
    public void run() {

        sendStartBoard();
        try{

            do{
                String input = communicationManager.readLine();
                if (input == null) break;

                if(input.startsWith("SAVE")) {
                    String name = input.substring(5);
                    game.saveGame(name);
                }

                if(game.getCurrentPlayerTurn() == getId() && input.startsWith("SKIP")){
                    communicationManager.writeLine("GAME You skipped your turn.");
                    game.nextTurn();
                } else if(game.getCurrentPlayerTurn() == getId() && input.startsWith("MOVE")){
                    Move move = game.processMove(InputInterpeter.getMoveFromString(input));
                    game.getGameMaster().isWinner(getColor());
                    if(move == null){
                        communicationManager.writeLine("FALSE Invalid move. Try again.");
                    } else {
                        communicationManager.writeLine("GAME Moved.");
                    }
                } else if(game.getCurrentPlayerTurn() == getId()){
                    communicationManager.writeLine("FALSE Command not recognized. Try again.");
                }
                else {
                    communicationManager.writeLine("FALSE Not your turn. Please wait...");
                }
            }while(!game.hasWinner());
            AbstractPlayer winner = game.hosIsWinner();
            if(winner != null && winner != this){
                communicationManager.writeLine("LOST YOU LOST! Player " + getId() + " wins!");
            } else if(winner != null){
                communicationManager.writeLine("WIN YOU WON!");
            } else {
                communicationManager.writeLine("NONE Nobody wins.");
            }

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void sendStartBoard() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("GAME Welcome", getColor() + " "+ getId());
        sendCommand(commandBuilder.getCommand());

        commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("BOARD", game.getGameMaster().getBoardAsString());
        sendCommand(commandBuilder.getCommand());
    }


    @Override
    public void sendCommand(String command) {
        communicationManager.writeLine(command);
    }

    @Override
    public String readResponse() throws PlayerLeftException {
        try {
            return communicationManager.readLine();
        } catch (Exception e) {
            throw new PlayerLeftException(getColor().toString());
        }
    }
}
