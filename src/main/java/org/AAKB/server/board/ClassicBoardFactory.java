package org.AAKB.server.board;

import org.AAKB.constants.PlayerColor;
/**
 * creates objects of ClassicBoard
 */
public class ClassicBoardFactory implements IBoardFactory {

    private ClassicBoard board = new ClassicBoard();

    /**
     * creates a classic board for a given number of players
     */
    @Override
    public Board createBoard(int numberOfPlayers)
    {

        setBluePlayer(false);
        setGreenPlayer(false);
        setOrangePlayer(false);
        setRedPlayer(false);
        setVioletPlayer(false);
        setYellowPlayer(false);

        switch (numberOfPlayers)
        {
            case 1: // plansza dla 1 gracza
            {
                setRedPlayer( true );
                break;
            }
            case 2: // plansza dla 2 graczy
            {
                setRedPlayer(true);
                setGreenPlayer(true);
                break;
            }

            case 3: // plansza dla 3 graczy
            {
                setRedPlayer(true);
                setBluePlayer(true);
                setYellowPlayer(true);
                break;
            }
            case 4: //plansza dla 4 graczy
            {
                setYellowPlayer(true);
                setOrangePlayer(true);
                setVioletPlayer(true);
                setBluePlayer(true);
                break;
            }
            case 6: //plansza dla 6 graczy
            {
                setRedPlayer(true);
                setGreenPlayer(true);
                setYellowPlayer(true);
                setOrangePlayer(true);
                setVioletPlayer(true);
                setBluePlayer(true);
                break;
            }
            default:
            {
                throw new RuntimeException( "Nieobsługiwana liczba graczy" );
            }

        }

        // Wypełnianie środkowej części planszy

        //zmienne pomocnicze do inicjalizacji pól
        int[] beginnings = {5, 4, 4, 3, 3, 3, 4, 4, 5};
        int[] endings = {9, 9, 10, 10, 11, 10, 10, 9, 9};
        //iteracja po j - rzędach
        for (int j = 5; j <= 13; j++)
        {
            //iteracja po i - kolumnach
            for (int i = beginnings[j - 5]; i <= endings[j - 5]; i++)
            {
                board.setField(i, j, new Field(PlayerColor.NONE, PlayerColor.NONE, PlayerColor.NONE, true));
            }
        }

        return board;
    }

    /**
     * creates fields in a corner of a player of color green
     * if the color is playable, the variable currentColor of field is set to GREEN, otherwise NONE
     */
    private void setGreenPlayer(boolean isColorInGame)
    {
        board.setField(7, 1, getGreenField(isColorInGame));
        board.setField(6, 2, getGreenField(isColorInGame));
        board.setField(7, 2, getGreenField(isColorInGame));
        board.setField(6, 3, getGreenField(isColorInGame));
        board.setField(8, 3, getGreenField(isColorInGame));
        board.setField(7, 3, getGreenField(isColorInGame));
        board.setField(5, 4, getGreenField(isColorInGame));
        board.setField(6, 4, getGreenField(isColorInGame));
        board.setField(7, 4, getGreenField(isColorInGame));
        board.setField(8, 4, getGreenField(isColorInGame));
    }

    private Field getGreenField( boolean isColorInGame )
    {
        return isColorInGame ? new Field(PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.RED, true) : new Field(PlayerColor.NONE, PlayerColor.GREEN, PlayerColor.RED, true);
    }

    /**
     * creates fields in a corner of a player of color red
     * if the color is taking part in the game, the variable currentColor of field is set to RED, otherwise NONE
    */
    private void setRedPlayer(boolean isColorInGame)
    {
        board.setField(7, 17, getRedField(isColorInGame));
        board.setField(6, 16, getRedField(isColorInGame));
        board.setField(7, 16, getRedField(isColorInGame));
        board.setField(6, 15, getRedField(isColorInGame));
        board.setField(7, 15, getRedField(isColorInGame));
        board.setField(8, 15, getRedField(isColorInGame));
        board.setField(5, 14, getRedField(isColorInGame));
        board.setField(6, 14, getRedField(isColorInGame));
        board.setField(7, 14, getRedField(isColorInGame));
        board.setField(8, 14, getRedField(isColorInGame));
    }

    private Field getRedField(boolean isColorInGame)
    {
        return isColorInGame ? new Field(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, true) : new Field(PlayerColor.NONE, PlayerColor.RED, PlayerColor.GREEN, true);
    }

    /**
     * creates fields in a corner of a player of color blue
     * if the color is playable, the variable currentColor of field is set to BLUE, otherwise NONE
     */
    private void setBluePlayer(boolean isColorInGame)
    {
        board.setField(1, 5, getBlueField(isColorInGame));
        board.setField(2, 5, getBlueField(isColorInGame));
        board.setField(3, 5, getBlueField(isColorInGame));
        board.setField(4, 5, getBlueField(isColorInGame));
        board.setField(1, 6, getBlueField(isColorInGame));
        board.setField(2, 6, getBlueField(isColorInGame));
        board.setField(3, 6, getBlueField(isColorInGame));
        board.setField(2, 7, getBlueField(isColorInGame));
        board.setField(3, 7, getBlueField(isColorInGame));
        board.setField(2, 8, getBlueField(isColorInGame));
    }

    private Field getBlueField(boolean isColorInGame)
    {
        return isColorInGame ? new Field(PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.ORANGE, true) : new Field(PlayerColor.NONE, PlayerColor.BLUE, PlayerColor.ORANGE, true);
    }

    /**
     * creates fields in a corner of a player of color violet
     * if the color is playable, the variable currentColor of field is set to VIOLET, otherwise NONE
     * */
    private void setVioletPlayer(boolean isColorInGame)
    {
        board.setField(2, 10, getVioletField(isColorInGame));
        board.setField(2, 11, getVioletField(isColorInGame));
        board.setField(3, 11, getVioletField(isColorInGame));
        board.setField(1, 12, getVioletField(isColorInGame));
        board.setField(2, 12, getVioletField(isColorInGame));
        board.setField(3, 12, getVioletField(isColorInGame));
        board.setField(1, 13, getVioletField(isColorInGame));
        board.setField(2, 13, getVioletField(isColorInGame));
        board.setField(3, 13, getVioletField(isColorInGame));
        board.setField(4, 13, getVioletField(isColorInGame));
    }

    private Field getVioletField(boolean isColorInGame)
    {
        return isColorInGame ? new Field(PlayerColor.VIOLET, PlayerColor.VIOLET, PlayerColor.YELLOW, true) : new Field(PlayerColor.NONE, PlayerColor.VIOLET, PlayerColor.YELLOW, true);
    }

    /**
     * creates fields in a corner of a player of color yellow
     * if the color is playable, the variable currentColor of field is set to YELLOW, otherwise NONE
     */
    private void setYellowPlayer(boolean isColorInGame)
    {
        board.setField(10, 5, getYellowField(isColorInGame));
        board.setField(11, 5, getYellowField(isColorInGame));
        board.setField(12, 5, getYellowField(isColorInGame));
        board.setField(13, 5, getYellowField(isColorInGame));
        board.setField(10, 6, getYellowField(isColorInGame));
        board.setField(11, 6, getYellowField(isColorInGame));
        board.setField(12, 6, getYellowField(isColorInGame));
        board.setField(11, 7, getYellowField(isColorInGame));
        board.setField(12, 7, getYellowField(isColorInGame));
        board.setField(11, 8, getYellowField(isColorInGame));
    }

    private Field getYellowField(boolean isColorInGame)
    {
        return isColorInGame ? new Field(PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.VIOLET, true) : new Field(PlayerColor.NONE, PlayerColor.YELLOW, PlayerColor.VIOLET, true);
    }

    /**
     * creates fields in a corner of a player of color orange
     * if the color is playable, the variable currentColor of field is set to ORANGE, otherwise NONE
     */
    private void setOrangePlayer(boolean isColorInGame)
    {
        board.setField(11, 10, getOrangeField(isColorInGame));
        board.setField(11, 11, getOrangeField(isColorInGame));
        board.setField(12, 11, getOrangeField(isColorInGame));
        board.setField(10, 12, getOrangeField(isColorInGame));
        board.setField(11, 12, getOrangeField(isColorInGame));
        board.setField(12, 12, getOrangeField(isColorInGame));
        board.setField(10, 13, getOrangeField(isColorInGame));
        board.setField(11, 13, getOrangeField(isColorInGame));
        board.setField(12, 13, getOrangeField(isColorInGame));
        board.setField(13, 13, getOrangeField(isColorInGame));
    }

    private Field getOrangeField(boolean isColorInGame)
    {
        return isColorInGame ? new Field(PlayerColor.ORANGE, PlayerColor.GREEN, PlayerColor.BLUE, true) : new Field(PlayerColor.NONE, PlayerColor.ORANGE, PlayerColor.BLUE, true);
    }



}
