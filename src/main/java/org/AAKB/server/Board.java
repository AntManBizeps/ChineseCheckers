package org.AAKB.server;


import java.util.Arrays;

public class Board {
    private static final int SIZE = 121; // Example size for Chinese Checkers
    private char[] board;

    public Board() {
        board = new char[SIZE];
        Arrays.fill(board, '.'); // '.' represents an empty field
    }

    public void display() {
        for (char cell : board) {
                System.out.print(cell + " ");
            System.out.println();
        }
    }

    public boolean validateMove(String start, String end) {
        // Add basic validation logic (e.g., check bounds, valid moves)
        return true;
    }

    public void applyMove(String start, String end) {
        // Update board state based on the move
    }
}
