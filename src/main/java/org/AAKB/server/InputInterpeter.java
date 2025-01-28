package org.AAKB.server;

import org.AAKB.server.board.Move;

import java.util.Arrays;

public class InputInterpeter {
    public static Move getMoveFromString(String input) {
        System.out.println(input);
        String moveCords = input.substring(5);
        try{
            System.out.println(moveCords);
            int[] numbers = Arrays.stream(moveCords.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            System.out.println(numbers);
            if (numbers.length == 4) {
                return new Move(numbers[0], numbers[1], numbers[2], numbers[3]);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
