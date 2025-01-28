package org.AAKB.client.board;

import org.AAKB.constants.Coordinates;
import org.AAKB.constants.PlayerColor;

import java.util.List;

public class Board {
    private List<Field> fields;
    private Field selectedField;

    public Board(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * Removes all pawns from enabled fields
     */
    public void removeAllPawns() {
        for (Field field : fields) {
            field.setColor(PlayerColor.NONE); // Reset koloru dla wszystkich pól
        }
    }

    /**
     * Gets the coordinates of the currently selected field
     */
    public Coordinates getCoordinatesOfSelectedField() {
        if (selectedField != null) {
            int x = selectedField.getX();
            int y = selectedField.getY();
            return new Coordinates(x, y);
        }
        return null;
    }

    /**
     * Selects a field at the given coordinates (x, y).
     * Also deselects any previously selected field.
     */
    // public void selectField(int x, int y) {
    //     deselectAllFields();
    //     selectedField = getField(x, y);

    //     if (selectedField != null) {
    //         selectedField.setSelected(true);
    //     }
    // }

    // /**
    //  * Deselects all fields and resets the selected field.
    //  */
    // public void deselectAllFields() {
    //     for (Field field : fields) {
    //         field.setSelected(false);
    //     }
    //     selectedField = null;
    // }

    /**
     * Adds a pawn to the field at the given coordinates (x, y) with the specified color.
     */
    public void addPawn(int x, int y, PlayerColor color) {
        Field field = getField(x, y);
        if (field != null) {
            field.setColor(color);
        }
    }

    /**
     * Checks if the field at the given coordinates (x, y) is empty.
     */
    public boolean isFieldEmpty(int x, int y) {
        Field field = getField(x, y);
        return field != null && !field.hasPiece();
    }

    /**
     * Returns the color of a pawn on the field at the given coordinates (x, y).
     */
    public PlayerColor getColor(int x, int y) {
        Field field = getField(x, y);
        return field != null ? field.getColor() : PlayerColor.NONE;
    }

    /**
     * Returns the field at the given coordinates (x, y) if it exists, otherwise null.
     */
    private Field getField(int x, int y) {
        for (Field field : fields) {
            if (field.getX() == x && field.getY() == y) {
                return field;
            }
        }
        return null;
    }
}
