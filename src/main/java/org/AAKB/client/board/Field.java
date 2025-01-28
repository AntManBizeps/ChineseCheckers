package org.AAKB.client.board;

import org.AAKB.constants.PlayerColor;

import javafx.scene.paint.*;
import javafx.scene.shape.Circle;

public class Field {
    private int x;                                  // Współrzędna X pola (kolumna)
    private int y;                                  // Współrzędna Y pola (wiersz)
    private Circle circle;                          // Referencja do odpowiadającego Circle w GUI
    private PlayerColor color = PlayerColor.NONE;   // Kolor pionka na danym polu

    public Field(int x, int y, Circle circle) {
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    // Getter współrzędnych
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // Getter i setter koloru pionka
    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
        updateCircleColor(color);
    }

    // Aktualizacja koloru pola (dla pionka lub pustego pola)
    private void updateCircleColor(PlayerColor color) {
        Stop[] stops = {new Stop(0, Color.WHITE), new Stop(1, (Color) getColorAsPaint(color))};
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops
        );
        circle.setFill(gradient);
    }

    // Konwersja koloru PlayerColor na Paint
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

    // Metoda podświetlająca pole
    public void highlightField(boolean highlight) {
        if (highlight) {
            circle.setStroke(Color.YELLOW); // Ustawienie żółtej obwódki
            circle.setStrokeWidth(3);      // Grubość obwódki
        } else {
            circle.setStroke(null);        // Usunięcie obwódki
        }
    }

    public boolean hasPiece() {
        return color != PlayerColor.NONE; // Jeśli kolor nie jest NONE, pole jest zajęte
    }

    // Porównanie Circle
    public boolean circleEquals(Circle circle) {
        return this.circle == circle;
    }
}
