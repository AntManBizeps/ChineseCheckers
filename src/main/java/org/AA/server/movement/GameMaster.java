package org.AA.server.movement;

import org.AA.constants.Coordinates;
import org.AA.constants.PlayerColor;
import org.AA.server.board.*;

import java.util.ArrayList;
import java.util.List;

public class GameMaster {
    private Board board;
    private IMovementStrategy movementStrategy;
    private IBoardFactory boardFactory;

    public GameMaster(IMovementStrategy movementStrategy, IBoardFactory boardFactory) {
        this.board = new ClassicBoard();
        this.movementStrategy = movementStrategy;
        this.boardFactory = boardFactory;
    }

    public void initializeBoard(int numberOfPlayers)
    {
        board = boardFactory.createBoard( numberOfPlayers );
    }

    public PlayerColor[] getPossibleColorsForPlayers(int numberOfPlayers )
    {
        switch( numberOfPlayers )
        {
            case 1:
                return new PlayerColor[]{PlayerColor.RED};
            case 2:
                return new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN};
            case 3:
                return new PlayerColor[]{PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW};
            case 4:
                return new PlayerColor[]{PlayerColor.BLUE, PlayerColor.YELLOW, PlayerColor.VIOLET, PlayerColor.ORANGE};
            case 6:
                return new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.ORANGE, PlayerColor.YELLOW, PlayerColor.VIOLET};
        }
        throw new RuntimeException( "Given wrong number of players: " + numberOfPlayers );
    }

    /**
     * weryfikuje poprawność ruchu z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
     */

    public int verifyMove(Move move, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        return movementStrategy.verifyMove(board, move, additionalVerifyConditions);
    }

    /**
     * wykonuje ruch pionkiem z pola (x1,y1) na pole (x2,y2) na podstawie podanych zasad movementstrategy
     */

    public void makeMove(Move move)
    {
        board = movementStrategy.makeMove(board, move);
    }

    /**
     * Zwraca kolor pola(x,y)
     */

    public PlayerColor getColorAtPos(int x, int y)
    {
        try
        {
            return board.getCurrentColor(x, y);
        } catch (UnplayableFieldException ufexc)
        {
            return null;
        }
    }

    public List<Coordinates> getPossibleMovesForPos(int x, int y, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        int result; // rezultat funkcji verifyMove (jeśli 0, to ruch niepoprawny)
        List<Coordinates> possibleMoves = new ArrayList<>();
        List<Coordinates> nearbyCoords = board.getNearbyCoords( x, y );
        for( Coordinates coord : nearbyCoords )
        {
            result = verifyMove(new Move(x, y, coord.getX(), coord.getY()), additionalVerifyConditions);
            if( result != 0 )
                possibleMoves.add( coord );
        }

        return possibleMoves;
    }


    /**
     sprawdza czy gracz o podanym kolorze jest zwycięzcą
     */
    public boolean isWinner(PlayerColor color)
    {
        return board.isWinner(color);
    }

    /**
     * Zwraca ustawienie pionków na planszy w postaci stringa
     */
    public String getBoardAsString()
    {
        return board.getAsString();
    }

    public Board getBoard()
    {
        return board;
    }
}
