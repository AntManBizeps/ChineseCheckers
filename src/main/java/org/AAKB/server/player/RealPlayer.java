package org.AAKB.server.player;

import org.AAKB.constants.PlayerColor;
import org.AAKB.server.Game;
import org.AAKB.server.Rookie;

import java.net.Socket;

public class RealPlayer extends AbstractPlayer implements Runnable {

    private final CommunicationManager communicationManager;

    private final Game game;

    public RealPlayer(Game game, Rookie rookie, PlayerColor color) throws Exception {
        setId(rookie.getId());
        setColor(color);
        this.communicationManager = new CommunicationManager(rookie.getSocket(), rookie.getIn(), rookie.getOut());
        this.game = game;
    }

    @Override
    public void run() {
        sendStartBoard();
    }

    public void sendStartBoard() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("WELCOME", getColor() + " "+ getId());
        sendCommand(commandBuilder.getCommand());

        commandBuilder = new CommandBuilder();
        commandBuilder.addCommand("START");
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
