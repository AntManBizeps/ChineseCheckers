package org.AAKB.server.main;

import org.AAKB.constants.ConstantProperties;
import org.AAKB.server.board.ClassicBoardFactory;
import org.AAKB.server.board.Move;
import org.AAKB.server.movement.ClassicMovementStrategy;
import org.AAKB.server.movement.GameMaster;
import org.AAKB.server.player.CommandBuilder;
import org.AAKB.server.player.Rookie;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Replay {
    ArrayList<Rookie> rookieList;

    Map<Integer, String> gameNames = new HashMap<>();

    Map<Integer,String> movesHistory = new HashMap<>();
    int numberOfPlayers = 0;
    int numberOfMoves = 0;

    private AtomicInteger chosenGameId = new AtomicInteger(0);

    public Replay(ArrayList<Rookie> rookieList) throws Exception {
        this.rookieList = rookieList;
        choosegame();
        loadData(chosenGameId.get());
        rewatch();
    }

    public void choosegame() throws Exception {
        getGameNames();
        while(chosenGameId.get() == 0){
            rookieList.getFirst().getCommunicationManager().writeLine(getGameNamesAsString());
            String input = rookieList.getFirst().getCommunicationManager().readLine();
            if(input.startsWith("DB")){
                chosenGameId.set(Integer.parseInt(input.split(" ")[1]));
            }
        }
    }

    public void getGameNames() {
        try {

            URI uri = URI.create(ConstantProperties.DATABASE_SERVER_URL);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");


            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray gamesArray = new JSONArray(response.toString());

                for (int i = 0; i < gamesArray.length(); i++) {
                    JSONObject game = gamesArray.getJSONObject(i);
                    int gameId = game.getInt("game_id");
                    String gameName = game.getString("game_name");
                    gameNames.put(gameId, gameName);
                }
            } else {
                System.out.println("GAME: Couldn't load data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(int id) throws IOException {
        URI uri = URI.create(ConstantProperties.DATABASE_SERVER_URL + "/" + id);
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            numberOfPlayers = jsonResponse.getInt("num_of_players");
            numberOfMoves = jsonResponse.getInt("num_of_moves");

            JSONObject movesJson = jsonResponse.getJSONObject("moves");

            System.out.println(movesJson.get("1"));

            Iterator<String> keys = movesJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int moveId = Integer.parseInt(key);
                String moveValue = movesJson.getString(key);
                movesHistory.put(moveId, moveValue);
            }

        }
    }


    public void rewatch(){

        if(movesHistory == null){
            throw new ArrayStoreException("Move list is empty");
        }
        for(Rookie rookie: rookieList){
            rookie.setBoardStates(movesHistory);
            Thread thread = new Thread(rookie);
            thread.start();
        }
    }

    public String getGameNamesAsString(){
        StringBuilder gameNamesOptions = new StringBuilder();
        gameNamesOptions.append("DB ");
        gameNames.forEach((moveId, moveValue) -> {
            gameNamesOptions.append(moveId);
            gameNamesOptions.append(" ");
            gameNamesOptions.append(moveValue);
            gameNamesOptions.append(" ");
        });
        return gameNamesOptions.toString();
    }



}
