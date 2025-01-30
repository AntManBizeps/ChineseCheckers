package org.AAKB.server.board;

public class Move {
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public Move(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
    }

    public Move getReverseMove(){
        return new Move(toX, toY, fromX, fromY);
    }

    public int getFromX() {
        return fromX;
    }

    public int getToX() {
        return toX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToY() {
        return toY;
    }
}
