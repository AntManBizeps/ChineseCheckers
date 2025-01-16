package org.AA.constants;

import java.util.Objects;

public class Coordinates {
    private int x;
    private int y;
    public Coordinates( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public boolean equals( Object o )
    {
        if( o instanceof Coordinates )
        {
            Coordinates c = (Coordinates)o;
            return x == c.x && y == c.y;
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( x, y );
    }
}
