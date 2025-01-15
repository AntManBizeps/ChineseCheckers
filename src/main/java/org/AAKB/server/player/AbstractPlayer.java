package org.AAKB.server.player;

import org.AAKB.constants.PlayerColor;

public abstract class AbstractPlayer {

    private int id;

    private PlayerColor color;

    private boolean finished;

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

    public void setFinished(boolean status)
    {
        this.finished = status;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

}
