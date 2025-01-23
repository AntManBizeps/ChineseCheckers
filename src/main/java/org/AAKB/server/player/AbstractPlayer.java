package org.AAKB.server.player;

import org.AAKB.constants.PlayerColor;

public abstract class AbstractPlayer {

    private int id;

    private PlayerColor color;

    private boolean winner;

    AbstractPlayer() {}

    /**
     * Wysyła komendę do gracza
     */
    public abstract void sendCommand(String command);

    /**
     * Odczytuje komendę wysłaną przez gracza
     */
    public abstract String readResponse() throws PlayerLeftException;

    public PlayerColor getColor()
    {
        return color;
    }

    public void setColor(PlayerColor color){
        this.color = color;
    }

    public void setWinner(boolean status)
    {
        this.winner = status;
    }

    public boolean isWinner()
    {
        return winner;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

}
