package org.AA.server.movement;

import org.AA.constants.PlayerColor;
import org.AA.server.board.Board;
import org.AA.server.board.Move;
import org.AA.server.board.UnplayableFieldException;

public class ClassicMovementStrategy implements IMovementStrategy {

    @Override
    public int verifyMove(Board board, Move move, AdditionalVerifyCondition[] additionalVerifyConditions) {
        int x1 = move.getFromX();
        int y1 = move.getFromY();
        int x2 = move.getToX();
        int y2 = move.getToY();

        if (additionalVerifyConditions.length != 2)
        {
            throw new RuntimeException("not enough verify conditions");
        }
        //zmienne pomocnicze
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (fieldsNotPlayable(board, x1, y1, x2, y2))
        {
            return 0;
        }
        if (isWrongPawnState(board, x1, y1, x2, y2))
        {
            return 0;
        }
        if (isNotPreviousPawn(additionalVerifyConditions))
        {
            return 0;
        }

        switch (Math.abs(dy))
        {
            //Pola leżą w tym samym rzędzie
            case 0:
            {
                //Sprawdza odległość na osi x
                switch (Math.abs(dx))
                {
                    //Jeżeli 0 to znaczy że (x1,y1)==(x2,y2) czyli jest to nieprawidłowy ruch
                    case 0:
                    {
                        return 0;
                    }
                    //Jeżeli odległość == 1 to pola są bezpośrednimi sąsiadami czyli wykonujemy ruch krótki
                    case 1:
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    }
                    //Jeżeli odległość == 2 to sprawdzamy ruch przeskakujący
                    case 2:
                    {
                        //Sprawdza czy pomiędzy polami znajduje się jakiś pionek którego można przeskoczyć
                        if (!board.getField((x1 + x2) / 2, y1).getCurrentColor().equals(PlayerColor.NONE))
                        {
                            return 2;
                        }
                        //Jeżeli nie ma to ruch nieprawidłowy
                        else
                        {
                            return 0;
                        }
                    }
                }
            }
            //Pola leżą w sąsiednich rzędach
            case 1:
            {
                //Sprawdza czy numer rzędu jest parzysty ( ze względu na przesunięcie współrzędne x sąsiadów są inne dla różnych parzystości rzędów)
                if (y1 % 2 == 0)
                {
                    //Dla rzędów parzystych sprawdza x2==x1 i x2==x1+1
                    if (dx == 0 || dx == 1)
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    } else
                    {
                        return 0;
                    }
                } else
                {
                    //Dla rzędów nieparzystych sprawdza x2==x1 i x2==x1-1
                    if (dx == 0 || dx == -1)
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    } else
                    {
                        return 0;
                    }
                }
            }
            //Numery rzędów pól różnią się o 2 (dalsi sąsiedzi)
            case 2:
            {
                if (Math.abs(dx) != 1)
                {
                    return 0;
                }
                //Trzeba rozważyć osobno dla rzędów parzystych i niepazystych
                //Dla rzędów parzystych
                if (y1 % 2 == 0)
                {
                    //Jeżeli skacze w lewo to pole pomiędzy ma współrzędne x==x1 , y==|y2-y1/2|
                    if (x2 < x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(PlayerColor.NONE))
                    {
                        return 2;
                    }
                    //Jeżeli skacze w prawo to pole pomiędzy ma współrzędne x==x1+1 , y==|y2-y1/2|
                    else if (x2 > x1 && !board.getField(x1 + 1, (y2 + y1) / 2).getCurrentColor().equals(PlayerColor.NONE))
                    {
                        return 2;
                    } else
                    {
                        return 0;
                    }
                } else
                {
                    //Jeżeli skacze w lewo to pole pomiędzy ma współrzędne x==x1-1 , y==|y2-y1/2|
                    if (x2 < x1 && !board.getField(x1 - 1, (y1 + y2) / 2).getCurrentColor().equals(PlayerColor.NONE))
                    {
                        return 2;
                    }
                    //Jeżeli skacze w prawo to pole pomiędzy ma współrzędne x==x1 , y==|y2-y1/2|
                    else if (x2 > x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(PlayerColor.NONE))
                    {
                        return 0;
                    } else
                    {
                        return 0;
                    }
                }

            }
            //dla różnicy rzędów >2 ruch jest niemożliwy
            default:
            {
                return 0;
            }
        }
    }


    /**
     * zwraca true jeżeli któreś z pól jest nullem lub wypełniaczem
     */
    private boolean fieldsNotPlayable(Board board, int x1, int y1, int x2, int y2)
    {
        return board.getField(x1, y1) == null || board.getField(x2, y2) == null || !board.getField(x1, y1).isPlayable() || !board.getField(x2, y2).isPlayable();
    }

    /**
     * Zwraca true gdy próbujemy ruszyć z pustego pola lub na zajęte pole
     */
    private boolean isWrongPawnState(Board board, int x1, int y1, int x2, int y2)
    {
        return !board.getField(x2, y2).getCurrentColor().equals(PlayerColor.NONE) || board.getField(x1, y1).getCurrentColor().equals(PlayerColor.NONE);
    }

    /**
     * Zwraca true gdy po wykonaniu skoku próbujemy wykonać ruch innym pionkiem
     */
    private boolean isNotPreviousPawn(AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        return !additionalVerifyConditions[0].verify() && additionalVerifyConditions[1].verify();
    }

    /**
     * Wykonuje ruch pionkiem z pola (x1,y1) na (x2,y2) na danej planszy. Zwraca planszę po wykonaniu ruchu
     * Funkcja powinna być wykonana po weryfikacji poprawności ruchu
     */
    @Override
    public Board makeMove(Board board, Move move)
    {
        try
        {
            board.addPawn(move.getToX(), move.getToY(), board.getCurrentColor(move.getFromX(), move.getFromY()));
            board.removePawn(move.getFromX(), move.getFromY());
            return board;
        } catch (UnplayableFieldException ufexc)
        {
            return board;
        }

    }
}
