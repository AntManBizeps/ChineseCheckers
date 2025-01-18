package org.AAKB.server.movement;

import org.AAKB.server.board.Board;
import org.AAKB.server.board.Move;

public interface IMovementStrategy {
    public int verifyMove(Board board, Move move, AdditionalVerifyCondition[] additionalVerifyCondition);
    public Board makeMove(Board board, Move move);
}
