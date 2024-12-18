package org.AAKB.server;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandlerHelper {
    private static final ArrayList<Integer> allowedNumberOfPlayers = new ArrayList<>(Arrays.asList(2,3,4,6));

    private ClientHandlerHelper() {}

    public static boolean validateNumberOfPlayers(int number){
        return allowedNumberOfPlayers.contains(number);
    }
}
