package org.AAKB.server;

import org.AAKB.server.board.Move;

import java.util.Arrays;

public class InputInterpeter {
    public static Move getMoveFromString(String input) {
        String moveCords = input.substring(6);
        int[] numbers = Arrays.stream(moveCords.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        if (numbers.length == 4) {
            return new Move(numbers[0], numbers[1], numbers[2], numbers[3]);
        }

        return null;
    }
}
