package org.AAKB.server.main;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameData {
        private String game_name;
        private int num_of_players;
        private int num_of_moves;
        private Map<Integer, String> moves;

        public GameData(String game_name, int num_of_players, Map<Integer, String> moves) {
            this.game_name = game_name;
            this.num_of_players = num_of_players;
            this.moves = moves;
            int num_of_moves = moves.size();
        }

        public String getGame_name() { return game_name; }
        public int getNum_of_players() { return num_of_players; }
        public int getNum_of_moves() { return num_of_moves; }
        public Map<Integer, String> getMoves() { return moves; }
}
