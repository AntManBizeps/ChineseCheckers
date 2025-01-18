package org.AAKB.client;

import org.AAKB.client.board.Board;
import org.AAKB.constants.Coordinates;
import org.AAKB.constants.PlayerColor;
import org.AAKB.constants.Response;
import org.AAKB.constants.ResponseInterpreter;

import javafx.application.Platform;

import java.util.function.Consumer;

/**
 * Represents a player in the game and manages interactions with the board and server.
 */
public class Player {
    private Board board;
    private PlayerColor color;
    private CommunicationManager commManager;
    private Consumer<Boolean> toggleGUI;
    private Consumer<String> displaySuccess;
    private Consumer<String> displayAlert;
    private Consumer<String> displayError;

    private boolean isTurn;
    private boolean isGameOver;

    public Player(CommunicationManager commManager, Board board,
                  Consumer<Boolean> toggleGUI,
                  Consumer<String> displaySuccess,
                  Consumer<String> displayAlert,
                  Consumer<String> displayError) {
        this.commManager = commManager;
        this.board = board;
        this.color = PlayerColor.NONE; // Default value
        this.toggleGUI = toggleGUI;
        this.displaySuccess = displaySuccess;
        this.displayAlert = displayAlert;
        this.displayError = displayError;
    }

    /**
     * Starts the game session for the player.
     */
    public void startGame() {
        isTurn = false;
        isGameOver = false;
        displaySuccess.accept("Connected. Waiting for other players...");
        lockGUIAndWaitForResponses();
    }

    /**
     * Handles a field click on the board.
     *
     * @param x the x-coordinate of the clicked field.
     * @param y the y-coordinate of the clicked field.
     */
    public void handleFieldClick(int x, int y) {
        if (!board.isFieldEmpty(x, y) && board.getColor(x, y).equals(color)) {
            selectField(x, y);
        } else {
            handleEmptyFieldClick(x, y);
        }
    }

    private void selectField(int x, int y) {
        Coordinates selected = board.getCoordinatesOfSelectedField();
        boolean isSameField = selected != null && selected.getX() == x && selected.getY() == y;

        if (isSameField) {
            board.deselectAndUnmarkAllFields();
            sendSkipTurn();
        } else {
            board.selectField(x, y);
            requestCluesFromServer(x, y);
        }

        lockGUIAndWaitForResponses();
    }

    private void handleEmptyFieldClick(int x, int y) {
        if (board.isFieldEmpty(x, y) && board.getCoordinatesOfSelectedField() != null) {
            sendMove(x, y);
        } else {
            board.deselectAndUnmarkAllFields();
        }
    }

    private void sendMove(int x, int y) {
        Coordinates selected = board.getCoordinatesOfSelectedField();
        if (selected != null) {
            int fromX = selected.getX();
            int fromY = selected.getY();
            sendMoveRequest(fromX, fromY, x, y);
        }
        board.deselectAndUnmarkAllFields();
        lockGUIAndWaitForResponses();
    }

    private void sendMoveRequest(int fromX, int fromY, int toX, int toY) {
        String message = "MOVE " + fromX + " " + fromY + " " + toX + " " + toY;
        commManager.sendMessage(message);
    }

    private void sendSkipTurn() {
        commManager.sendMessage("SKIP");
    }

    private void lockGUIAndWaitForResponses() {
        toggleGUI.accept(true);
        Thread responseThread = new Thread(() -> {
            try {
                waitForResponses();
            } catch (Exception e) {
                displayError.accept("Error communicating with the server: " + e.getMessage());
            }
        });
        responseThread.setDaemon(true);
        responseThread.start();
    }

    private void waitForResponses() throws Exception {
        while (!isTurn) {
            String line = commManager.readMessage();
            Response[] responses = ResponseInterpreter.getResponses(line);
            processAllResponses(responses);
        }
        toggleGUI.accept(false);
    }

    private void processAllResponses(Response[] responses) {
        for (Response response : responses) {
            processResponse(response);
        }
    }

    private void processResponse(Response response) {
        switch (response.getCode()) {
            case "WELCOME" -> setupPlayerColor(response);
            case "YOU" -> {
                isTurn = true;
                displaySuccess.accept("(" + color + ") Your turn!");
            }
            case "BOARD" -> Platform.runLater(() -> loadBoard(response));
            case "CLUES" -> Platform.runLater(() -> highlightPossibleMoves(response));
            case "END" -> {
                isGameOver = true;
                displaySuccess.accept("Game over. You finished in " + response.getNumbers()[0] + " place.");
            }
            default -> displayError.accept("Unknown server response: " + response.getCode());
        }
    }

    private void setupPlayerColor(Response response) {
        color = PlayerColor.valueOf(response.getWords()[0]);
        displayAlert.accept("You are playing as: " + color);
    }

    private void loadBoard(Response response) {
        board.removeAllPawns();
        int index = 0;
        for (String word : response.getWords()) {
            PlayerColor pieceColor = PlayerColor.valueOf(word);
            int x = response.getNumbers()[index];
            int y = response.getNumbers()[index + 1];
            board.addPawn(x, y, pieceColor);
            index += 2;
        }
    }

    private void highlightPossibleMoves(Response response) {
        board.unmarkAllPossibleJumpTargets();
        int[] moves = response.getNumbers();
        for (int i = 0; i < moves.length - 1; i += 2) {
            board.markFieldAsPossibleJumpTarget(moves[i], moves[i + 1]);
        }
    }

    private void requestCluesFromServer(int x, int y) {
        commManager.sendMessage("CLUES " + x + " " + y);
    }
}
