package org.AA.server.board;


import org.AA.constants.PlayerColor;

/**
 * represents a classic board: 6-pointed star with 10 fields for every horn and 61 fields for inner hexagon
 */
public class ClassicBoard extends Board {

    public ClassicBoard() {
        columns = 13;
        rows = 17;
        //wypełnienie boarda polami
        fields = new Field[columns + 1][rows + 1];
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                setField(i, j, new Field());
            }
        }
    }

    /**
     * checks if the player of a given color has already won
     */
    @Override
    public boolean isWinner(PlayerColor color) {        Field tempField;
        for (int i = 1; i <= columns; i++)
        {
            for (int j = 1; j <= rows; j++)
            {
                tempField = fields[i][j];
                if (tempField.isPlayable() && tempField.getCurrentColor().equals(color) && !tempField.getCurrentColor().equals(tempField.getTargetColor()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                if (fields[i][j].isPlayable()) {
                    if (!fields[i][j].getCurrentColor().equals(PlayerColor.NONE)) {
                        //jezeli string jest niepusty to dodaj spacje
                        if (!stringBuilder.toString().equals("")) {
                            stringBuilder.append(" ");
                        }
                        stringBuilder.append(fields[i][j].getCurrentColor().toString());
                        stringBuilder.append(" ");
                        stringBuilder.append(i);
                        stringBuilder.append(" ");
                        stringBuilder.append(j);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

}
