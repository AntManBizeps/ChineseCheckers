package org.AAKB.server.board;

public class Move {
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public Move(int fromX, int toX, int fromY, int toY) {
        this.fromX = fromX;
        this.toX = toX;
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
