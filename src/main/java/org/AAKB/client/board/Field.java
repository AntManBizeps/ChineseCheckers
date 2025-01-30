package org.AAKB.client.board;

import org.AAKB.constants.PlayerColor;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;

public class Field {
    private int x;
    private int y;
    private Circle circle;
    private PlayerColor color = PlayerColor.NONE;
    private Paint previousStroke; // Przechowuje poprzedni kolor obramowania

    public Field(int x, int y, Circle circle) {
        this.x = x;
        this.y = y;
        this.circle = circle;
        this.previousStroke = circle.getStroke(); // Zapamiętanie domyślnego bordera
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public PlayerColor getColor() { return color; }

    public void setColor(PlayerColor color) {
        this.color = color;
        updateCircleColor(color);
    }

    public boolean hasPiece() {
        return color != PlayerColor.NONE;
    }

    private void updateCircleColor(PlayerColor color) {
        Stop[] stops = {new Stop(0, Color.WHITE), new Stop(1, (Color) getColorAsPaint(color))};
        RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
        circle.setFill(gradient);
    }

    private Paint getColorAsPaint(PlayerColor color) {
        switch (color) {
            case NONE: return Color.WHITE;
            case RED: return Color.RED;
            case GREEN: return Color.GREEN;
            case YELLOW: return Color.YELLOW;
            case BLUE: return Color.BLUE;
            case ORANGE: return Color.ORANGE;
            case VIOLET: return Color.VIOLET;
            default: throw new RuntimeException("Nieznany kolor: " + color);
        }
    }

    public void highlightField(boolean highlight) {
        if (highlight) {
            previousStroke = circle.getStroke(); // Zapisz poprzednie obramowanie
            circle.setStroke(Color.YELLOW); // Ustawienie żółtej obwódki
            // circle.setStrokeWidth(3);
        } else {
            circle.setStroke(previousStroke);
             // Przywrócenie poprzedniego obramowania
        }
    }

    public boolean circleEquals(Circle circle) {
        return this.circle == circle;
    }
}
