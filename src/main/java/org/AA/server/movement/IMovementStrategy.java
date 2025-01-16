package org.AA.server.movement;

import org.AA.server.board.Board;
import org.AA.server.board.Move;

public interface IMovementStrategy {
    public int verifyMove(Board board, Move move, AdditionalVerifyCondition[] additionalVerifyCondition);
    public Board makeMove(Board board, Move move);
}
