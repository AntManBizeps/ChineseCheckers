package org.AAKB.server.board;

/**
 * factory of Board class
 */

public interface IBoardFactory {
    Board createBoard(int numberOfPlayers);
}
