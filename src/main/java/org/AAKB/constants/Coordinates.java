package org.AAKB.constants;

import java.util.List;
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

    public static Coordinates getAverageCoordinates(List<Coordinates> coordinatesList) {
        if (coordinatesList.isEmpty()) {
            return null;
        }

        int sumX = 0, sumY = 0;
        for (Coordinates coord : coordinatesList) {
            sumX += coord.getX();
            sumY += coord.getY();
        }

        int avgX = sumX / coordinatesList.size();
        int avgY = sumY / coordinatesList.size();

        return new Coordinates(avgX, avgY);
    }

    public static Coordinates getClosestCoordinate(List<Coordinates> possibleCoordinates, Coordinates averageTarget) {
        if (possibleCoordinates.isEmpty() || averageTarget == null) {
            return null;
        }

        Coordinates closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Coordinates coord : possibleCoordinates) {
            double distance = Math.sqrt(Math.pow(coord.getX() - averageTarget.getX(), 2) +
                    Math.pow(coord.getY() - averageTarget.getY(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = coord;
            }
        }

        return closest;
    }

    public boolean isEqual(Coordinates coordinates){
        return x == coordinates.getX() && y == coordinates.getY();
    }
}
