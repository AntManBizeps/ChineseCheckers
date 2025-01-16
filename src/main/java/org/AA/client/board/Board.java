//package org.AAKB.client.board;
//
//import org.AAKB.constants.Coordinates;
//import org.AAKB.constants.PlayerColor;
//
//import java.util.List;
//
//public class Board {
//    private List<Field> fields;
//    private Field selectedField;
//
//    public Board( List<Field> fields )
//    {
//        this.fields = fields;
//    }
//
//    /**
//     *  removes all enabled pawns
//     */
//    public void removeAllPawns()
//    {
//        for( Field field : fields )
//        {
//            if( !field.isDisabled() )
//                field.setColor( PlayerColor.NONE );
//        }
//    }
//
//    public Coordinates getCoordinatesOfSelectedField()
//    {
//        if( selectedField != null )
//        {
//            int x = selectedField.getX();
//            int y = selectedField.getY();
//            return new Coordinates( x, y );
//        }
//        else
//        {
//            return null;
//        }
//    }
//
//    /**
//     * selects pawn on field of given coordinates (x, y)
//     * Funkcja odznacza również uprzednio zaznaczone pole.
//     */
//    public void selectField( int x, int y )
//    {
//        deselectAllFields();
//        selectedField = getField( x, y );
//
//        if( selectedField != null )
//        {
//            selectedField.setSelected( true );
//        }
//    }
//
//    public void deselectAndUnmarkAllFields()
//    {
//        deselectAllFields();
//        unmarkAllPossibleJumpTargets();
//        selectedField = null;
//    }
//
//    private void deselectAllFields()
//    {
//        for( Field field : fields )
//            field.setSelected( false );
//    }
//
//    public void unmarkAllPossibleJumpTargets()
//    {
//        for( Field field : fields )
//        {
//            field.markAsPossibleJumpTarget( false );
//        }
//    }
//
//    public void addPawn( int x, int y, PlayerColor color )
//    {
//        Field field = getField( x, y );
//        if( field != null )
//            field.setColor( color );
//    }
//
//    public boolean isFieldEmpty( int x, int y )
//    {
//        Field field = getField( x, y );
//        if( field != null )
//            return field.getColor() == PlayerColor.NONE;
//        else
//            return true;
//    }
//
//    /**
//     * retruns color of a pawn on a field of given coordinates (x, y)
//     */
//    public PlayerColor getColor( int x, int y )
//    {
//        Field field = getField( x, y );
//        if( field != null )
//            return field.getColor();
//        else
//            return PlayerColor.NONE;
//    }
//
//    /**
//     * Podświetla pole (x, y)
//     * Używane do podświetlania pól, na które można skoczyć.
//     */
//    public void markFieldAsPossibleJumpTarget( int x, int y )
//    {
//        Field field = getField( x, y );
//        if( field != null )
//        {
//            field.markAsPossibleJumpTarget( true );
//        }
//    }
//
//    /**
//     * returns a field of given coordinates if exists, otherwise null
//     *
//     */
//    private Field getField( int x, int y )
//    {
//        for( Field field : fields )
//        {
//            if( field.getX() == x && field.getY() == y )
//                return field;
//        }
//        return null;
//    }
//}
