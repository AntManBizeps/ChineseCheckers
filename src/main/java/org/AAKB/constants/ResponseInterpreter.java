package org.AAKB.constants;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;

public class ResponseInterpreter {
    public static Response[] getResponses(String line) {
        // Usuwanie znaków nowej linii z końca (jeśli występuje)
        line = line.replace("\n", "");

        // Dzielenie linii na poszczególne komunikaty (rozdzielone @)
        String[] strResponses = line.split("@");

        // Wczytywanie poszczególnych komunikatów
        List<Response> responses = new ArrayList<>();
        for (String strResponse : strResponses) {
            // Rozdzielanie komunikatu względem spacji
            String[] parts = strResponse.split(" ");

            // Komunikat powinien zawierać co najmniej jeden element
            if (parts.length < 1)
                throw new RuntimeException("Otrzymano niepoprawną odpowiedź od serwera: '" + line + "'");

            // Pierwsze słowo komunikatu to kod (np. "START", "LOBBY", "BOARD")
            String code = parts[0];

            // Wczytywanie kolejnych liczb i słów
            List<Integer> numbers = new ArrayList<>();
            List<String> words = new ArrayList<>();

            for (int i = 1; i < parts.length; i++) {
                // Rozdziel liczby, jeśli występują przecinki (np. "2,1")
                String[] subParts = parts[i].split(",");

                for (String subPart : subParts) {
                    subPart = subPart.trim(); // Usuwanie spacji

                    if (!subPart.isEmpty() && isNumber(subPart)) {
                        numbers.add(Integer.valueOf(subPart));
                    } else if (!subPart.isEmpty()) {
                        words.add(subPart); // Jeśli nie jest liczbą, dodaj do słów
                    }
                }
            }

            // Dodawanie sparsowanego komunikatu
            int[] numbersArray = numbers.stream().mapToInt(i -> i).toArray();
            String[] wordsArray = words.toArray(new String[0]);
            Response response = new Response(code, numbersArray, wordsArray);

            responses.add(response);
        }

        return responses.toArray(new Response[0]);
    }

    /**
     * Sprawdza, czy podany ciąg znaków jest liczbą (obsługuje liczby ujemne).
     */
    private static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        if (str.matches("-?\\d+")) return true; // Obsługa liczb całkowitych, także ujemnych
        return false;
    }
}
