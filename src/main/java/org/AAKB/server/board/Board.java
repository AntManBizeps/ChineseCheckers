package org.AAKB.server.board;

import org.AAKB.constants.Coordinates;
import org.AAKB.constants.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the board the game is taking place on
 */
public abstract class Board {

    int columns, rows;
    Field[][] fields;

    Board()
    {

    }

    /**
     * adds pawn of a given color on given field (x,y)
     */
    public void addPawn(int x, int y, PlayerColor color) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            fields[x][y].setCurrentColor(color);
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    /**
     * removes a pawn from a given field (x,y)
     */
    public void removePawn(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            fields[x][y].setCurrentColor(PlayerColor.NONE);
            //setField(x, y, new Field(PlayerColor.NONE, fields[x][y].getNativeColor(), fields[x][y].getTargetColor(), true));

        } else
        {
            throw new UnplayableFieldException();
        }
    }

    /**
     * return pawn's color on the given field (x,y)
     */
    public PlayerColor getCurrentColor(int x, int y) throws UnplayableFieldException
    {
        if (fields[x][y].isPlayable())
        {
            return fields[x][y].getCurrentColor();
        } else
        {
            throw new UnplayableFieldException();
        }
    }

    public Field getField(int x, int y)
    {
        if (x < 1 || y < 1 || x > columns || y > rows)
        {
            return null;
        } else
        {
            return fields[x][y];
        }
    }

    public void setField(int x, int y, Field field)
    {
        if (x < 1 || y < 1 || x > columns || y > rows)
        {
            throw new NullPointerException();
        } else
        {
            fields[x][y] = field;
        }
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }

    /**
     * return a list of all coordinates of fields in a range of 1 or 2
     * in straight line from a given field (x, y)
     */
    public List<Coordinates> getNearbyCoords(int x, int y )
    {
        List<Coordinates> coords = new ArrayList<>();

        // pola odległe o 1

        // na lewo
        coords.add( new Coordinates( x - 1, y ) );
        // na prawo
        coords.add( new Coordinates( x + 1, y ) );
        // góra lewo (dla wiersza nieparzystego, aby dojść do pola góra lewo trzeba zmniejszyć x o 1)
        coords.add( new Coordinates( ( y % 2 == 0 ? x : x - 1 ), y - 1 ) );
        // góra prawo (dla wiersza parzystego, aby dojść do pola góra prawo trzeba zwiększyć x o 1)
        coords.add( new Coordinates( ( y % 2 == 0 ? x + 1 : x ), y - 1 ) );
        // dół lewo (dla wiersza nieparzystego trzeba zmniejszyć x o 1)
        coords.add( new Coordinates( ( y % 2 == 0 ? x : x - 1 ), y + 1 ) );
        // dół prawo (dla wiersza parzystego trzeba zwiększyc x o 1)
        coords.add( new Coordinates( ( y % 2 == 0 ? x + 1 : x ), y + 1 ) );

        // pola odległe o 2

        // na lewo
        coords.add( new Coordinates( x - 2, y ) );
        // na prawo
        coords.add( new Coordinates( x + 2, y ) );
        // góra lewo
        coords.add( new Coordinates( x - 1, y - 2 ) );
        // góra prawo
        coords.add( new Coordinates( x + 1, y - 2 ) );
        // dół lewo
        coords.add( new Coordinates( x - 1, y + 2 ) );
        // dół prawo
        coords.add( new Coordinates( x + 1, y + 2 ) );

        return coords;
    }

    /**
     * checks if all pawns of a given color are already in their target fields
     */
    public abstract boolean isWinner(PlayerColor color);

    /**
     * returns board as string
     */
    public abstract String getAsString();





}
