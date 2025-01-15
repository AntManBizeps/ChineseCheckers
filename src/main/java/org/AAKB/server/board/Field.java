package org.AAKB.server.board;


import org.AAKB.constants.PlayerColor;

/**
 * represents a single field and a pawn standing on it
 */
public class Field {

    /**
     * pawn's color
     */
    private PlayerColor currentColor;
    /**
     * start color
     */
    private PlayerColor nativeColor;
    /**
     * end color
     */
    private PlayerColor targetColor;
    private boolean playable;

    public Field(){}

    public Field(PlayerColor currentColor, PlayerColor nativeColor, PlayerColor targetColor, boolean playable)
    {
        this.currentColor = currentColor;
        this.nativeColor = nativeColor;
        this.targetColor = targetColor;
        this.playable = playable;
    }

    public PlayerColor getCurrentColor()
    {
        return currentColor;
    }

    void setCurrentColor(PlayerColor currentColor)
    {
        this.currentColor = currentColor;
    }

    PlayerColor getNativeColor()
    {
        return nativeColor;
    }

    public PlayerColor getTargetColor()
    {
        return targetColor;
    }

    public boolean isPlayable()
    {
        return playable;
    }
}
