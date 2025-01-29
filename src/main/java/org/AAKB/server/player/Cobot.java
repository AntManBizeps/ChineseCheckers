package org.AAKB.server.player;

import org.AAKB.constants.PlayerColor;
import org.AAKB.server.board.Move;
import org.AAKB.server.main.Game;


public class Cobot extends AbstractPlayer implements Runnable {

    private final Game game;

    public Cobot(Game game, int id, PlayerColor color) {
        setId(id);
        setColor(color);
        this.game = game;
    }

    @Override
    public void run() {

        do{
            if(game.getCurrentPlayerTurn() == getId()){
                makeMove();
            }

        }while(!game.hasWinner());

    }


    public void makeMove(){
        try{
            Move move = game.chooseBestMove(getColor());
            Move outcome = game.processMove(move);
        } catch (Exception e) {
            e.printStackTrace();
            game.nextTurn();
        }



    }

    @Override
    public void sendCommand(String command) {

    }

    @Override
    public String readResponse() throws PlayerLeftException {
        return "";
    }
}
