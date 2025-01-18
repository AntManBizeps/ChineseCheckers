package org.AAKB.client;

import org.AAKB.client.board.Board;
import org.AAKB.client.board.Field;
import org.AAKB.constants.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private Pane boardPane; // Pane containing all Circle nodes representing fields

    @FXML
    private Label infoBar; // Label displaying messages and alerts to the user

    private List<Field> fields = new ArrayList<>();
    private CommunicationManager communicationManager;
    private Board board;
    private Player player;
    private boolean guiBlocked = true;

    /**
     * Initializes the controller when the FXML is loaded.
     */
    @FXML
    private void initialize() {
        loadAllFieldsFromBoard();
        showAlert("Connect to the server to start the game.");
    }

    /**
     * Loads all field nodes (Circle) from the boardPane into the `fields` list.
     */
    private void loadAllFieldsFromBoard() {
        int x = 1; // Field column coordinate
        int y = 1; // Field row coordinate

        for (Node node : boardPane.getChildren()) {
            if (node instanceof Circle) {
                fields.add(new Field(x, y, (Circle) node));
                node.setOnMouseClicked(this::onFieldClick);
                if (x == 13) { // Adjust coordinates
                    x = 1;
                    y++;
                } else {
                    x++;
                }
            } else {
                throw new RuntimeException("Invalid node type in boardPane. Only Circle is allowed.");
            }
        }
    }

    /**
     * Handles the event when a field (Circle) is clicked.
     */
    private void onFieldClick(MouseEvent event) {
        if (!guiBlocked && event.getSource() instanceof Circle) {
            Circle clickedCircle = (Circle) event.getSource();
            Field clickedField = fields.stream()
                .filter(field -> field.circleEquals(clickedCircle))
                .findFirst()
                .orElse(null);

            if (clickedField != null) {
                player.handleFieldClick(clickedField.getX(), clickedField.getY());
            }
        }
    }

    /**
     * Starts a new game with 2 players.
     */
    @FXML
    private void onNewGame2Players() {
        startNewGame(2);
    }

    /**
     * Starts a new game with 3 players.
     */
    @FXML
    private void onNewGame3Players() {
        startNewGame(3);
    }

    /**
     * Starts a new game with 4 players.
     */
    @FXML
    private void onNewGame4Players() {
        startNewGame(4);
    }

    /**
     * Starts a new game with 6 players.
     */
    @FXML
    private void onNewGame6Players() {
        startNewGame(6);
    }

    /**
     * Starts a new game with the specified number of players.
     *
     * @param numberOfPlayers the number of players for the new game
     */
    private void startNewGame(int numberOfPlayers) {
        showAlert("Starting a new game with " + numberOfPlayers + " players.");
        // Additional logic to initialize the game
    }

    /**
     * Handles the "Join Game" menu action.
     */
    @FXML
    private void onJoinGame() {
        showAlert("Joining a game...");
        // Additional logic to join a game
    }

    /**
     * Handles the "Quit Game" menu action.
     */
    @FXML
    private void onQuitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * Displays information about the application.
     */
    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Chinese Checkers Application");
        alert.setContentText("This application was developed for educational purposes.\n\nAuthors: Sebastian Fojcik & Maciej Król.");
        alert.showAndWait();
    }

    void connectAndStartMatch(String host, int port) {
        try {
            communicationManager = new CommunicationManager(host, port);
            board = new Board(fields);
            player = new Player(communicationManager, board, this::blockGUI, this::showSuccess, this::showAlert, this::showError);
            player.startGame();
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    void blockGUI(boolean state) {
        guiBlocked = state;
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-alignment: center;");
            infoBar.setText(message);
        });
    }

    private void showSuccess(String message) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-alignment: center;");
            infoBar.setText(message);
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-alignment: center;");
            infoBar.setText(message);
        });
    }
}
