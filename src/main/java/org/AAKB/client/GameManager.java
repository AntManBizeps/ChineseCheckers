package org.AAKB.client;

import java.util.ArrayList;
import java.util.List;

import org.AAKB.client.board.Board;
import org.AAKB.client.board.Field;
import org.AAKB.constants.PlayerColor;
import org.AAKB.constants.Response;
import org.AAKB.constants.ResponseInterpreter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameManager {
    Client client;
    Controller controller;
    Pane boardPane;
    Label infoBar;
    List<Field> fields = new ArrayList<>();
    Board board;
    private Field selectedPiece; // Przechowuje wybrany pionek
    private Field targetField; // Przechowuje pole docelowe
    private PlayerColor currentPlayerColor; // Kolor aktualnego gracza

    public GameManager(Client client, Controller controller) {
        this.client = client;
        this.controller = controller;
        boardPane = controller.getBoardPane();
        infoBar = controller.getInfoBar();

        controller.getAboutOption().setOnAction(null);
        controller.getQuitOption().setOnAction(null);
        controller.getUndoOption().setOnAction(this::onUndo);
        controller.getRedoOption().setOnAction(this::onRedo);
        controller.getSkipOption().setOnAction(this::onSkip);
    }

    public void handleServerMessage(String serverMessage) {
        Response[] responses = ResponseInterpreter.getResponses(serverMessage);

        for (Response response : responses) {
            switch (response.getCode()) {
                case "LOBBY":
                    handleLobbyMessage(response);
                    break;
                case "CHOOSE":
                    handleChooseMessage();
                    break;
                case "GAME":
                    handleGameMessage(response);
                    break;
                case "BOARD":
                    handleBoardMessage(response);
                    break;
                case "FALSE":
                    if (serverMessage.contains("Invalid move")) {
                        handleInvalidMove();
                    }
                    break;
                default:
                    System.err.println("Nieznany komunikat od serwera: " + response.getCode());
            }
        }
    }

    private void handleLobbyMessage(Response response) {
        StringBuilder message = new StringBuilder();
        for (String word : response.getWords()) {
            message.append(word).append(" ");
        }
        String finalMessage = message.toString().trim();
    
        // Użyj Platform.runLater, aby zaktualizować UI na wątku JavaFX
        Platform.runLater(() -> {
            controller.getInfoBar().setText(finalMessage); // Ustaw tekst w infoBar
        });
    }

    private void handleGameMessage(Response response) {
        StringBuilder message = new StringBuilder();
        for (String word : response.getWords()) {
            message.append(word).append(" ");
        }
        String finalMessage = message.toString().trim();
        createBoard();

        // Ustaw aktualny kolor gracza
        for (String word : response.getWords()) {
            if (word.matches("RED|GREEN|BLUE|YELLOW|ORANGE|VIOLET")) {
                currentPlayerColor = PlayerColor.valueOf(word); // Odczytanie koloru gracza
                System.out.println("Aktualny kolor gracza: " + currentPlayerColor);
                break;
            }
        }

        // Platform.runLater(() -> {
        //     Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //     alert.setTitle("Gra");
        //     alert.setHeaderText("Informacje o grze");
        //     alert.setContentText(finalMessage);
        //     alert.showAndWait();
        // });

        System.out.println("GAME: " + finalMessage);
    }

    private void handleBoardMessage(Response response) {
        loadBoard(response);
    }

    private void handleInvalidMove() {
        if (selectedPiece != null) {
            selectedPiece.highlightField(false); // Odznacz podświetlenie
            selectedPiece = null;               // Resetuj zaznaczenie
            targetField = null;                 // Usuń pole docelowe
            System.out.println("Invalid move. Zaznaczenie pionka zostało usunięte.");
        }
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

    private void createBoard() {
        intiBoard();
        board = new Board(fields);
    }

    private void intiBoard() {
        int x = 1;
        int y = 1;

        for (Node node : boardPane.getChildren()) {
            convertNodeToField(node, x, y);

            if (x == 13) {
                x = 1;
                y++;
                continue;
            }

            x++;
        }
    }

    private void convertNodeToField(Node node, int x, int y) {
        if (node instanceof Circle) {
            Field field = new Field(x, y, (Circle) node);
            fields.add(field);
            node.setOnMouseClicked(e -> onFieldClick(field));
        }
    }

    private void onFieldClick(Field field) {
        if (selectedPiece == null) {
            // Sprawdzamy, czy pole zawiera pionek gracza
            if (board.getColor(field.getX(), field.getY()) == currentPlayerColor) {
                selectedPiece = field;
                field.highlightField(true); // Podświetlenie wybranego pionka
                System.out.println("Wybrano pionek na polu: " + field.getX() + ", " + field.getY());
            } else {
                System.out.println("Nie możesz wybrać tego pola. To nie Twój pionek!");
            }
        } else {
            targetField = field;
            System.out.println("Wybrano pole docelowe: " + field.getX() + ", " + field.getY());
            sendMoveToServer();
            selectedPiece.highlightField(false); // Wyłącz podświetlenie pionka
            selectedPiece = null;
            targetField = null;
        }
    }

    private Field getFieldByCircleReference(Circle circle) {
        for (Field field : fields) {
            if (field.circleEquals(circle))
                return field;
        }
        return null;
    }

    private void onUndo(ActionEvent event) {

    }

    private void onRedo(ActionEvent event) {

    }

    private void onSkip(ActionEvent event) {
        client.send("SKIP");
    }


    private void sendMoveToServer() {
        if (selectedPiece != null && targetField != null) {
            String moveCommand = String.format(
                "MOVE %d,%d,%d,%d",
                selectedPiece.getX(), selectedPiece.getY(),
                targetField.getX(), targetField.getY()
            );
            client.send(moveCommand);
            System.out.println("Wysłano ruch: " + moveCommand);
        } else {
            System.err.println("Błąd: Nie można wysłać ruchu, ponieważ brakuje danych.");
        }
    }

    private void handleChooseMessage() {
        Platform.runLater(() -> {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Settings");
    
            // Dropdown for number of players
            ComboBox<Integer> playersDropdown = new ComboBox<>();
            playersDropdown.getItems().addAll(2, 3, 4, 6);
            playersDropdown.setPromptText("Select number of players");
    
            // Dropdown for number of bots
            ComboBox<Integer> botsDropdown = new ComboBox<>();
            botsDropdown.setPromptText("Select number of bots");
            botsDropdown.setDisable(true);
    
            // RadioButton for load game
            RadioButton loadGameRadio = new RadioButton("Load Game");
            loadGameRadio.setOnAction(e -> {
                boolean selected = loadGameRadio.isSelected();
                playersDropdown.setDisable(selected);
                botsDropdown.setDisable(selected);
            });
    
            // Submit button
            Button submitButton = new Button("Submit");
            submitButton.setOnAction(e -> {
                if (loadGameRadio.isSelected()) {
                    client.send("LOAD");
                } else {
                    int players = playersDropdown.getValue();
                    int bots;
                    if (botsDropdown.getValue() != null) {
                        bots = botsDropdown.getValue();
                    } else {
                        bots = 0;
                    }
                    String command = String.format("START %d,%d", players, bots);
                    client.send(command);
                    System.out.println("Players: " + players + ", Bots: " + bots);
                }
                popupStage.close();
            });
    
            // Update bots dropdown based on players selection
            playersDropdown.setOnAction(e -> {
                int players = playersDropdown.getValue();
                botsDropdown.getItems().clear();
                for (int i = 1; i < players; i++) {
                    botsDropdown.getItems().add(i);
                }
                botsDropdown.setDisable(false);
            });
    
            VBox popupLayout = new VBox(10, playersDropdown, botsDropdown, loadGameRadio, submitButton);
            popupLayout.setPadding(new Insets(20));
    
            Scene popupScene = new Scene(popupLayout, 300, 200);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        });
    }

}
