package org.AAKB.server;

import org.AAKB.server.board.Board;
import org.AAKB.server.board.Move;

public interface IMovementStrategy {
    public Move verifyMove(Board board, Move move);

    public Board makeMove(Board board, Move move);
}
