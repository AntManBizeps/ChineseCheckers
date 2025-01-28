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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class GameManager {
    Client client;
    Controller controller;
    Pane boardPane;
    List<Field> fields = new ArrayList<>();
    Board board;
    private Field selectedPiece; // Przechowuje wybrany pionek
    private Field targetField; // Przechowuje pole docelowe
    private PlayerColor currentPlayerColor; // Kolor aktualnego gracza

    public GameManager(Client client, Controller controller) {
        this.client = client;
        this.controller = controller;
        boardPane = controller.getBoardPane();

        controller.getAboutOption().setOnAction(null);
        controller.getQuitOption().setOnAction(null);
        controller.getPlayers2Option().setOnAction(this::on2PlayersOption);
        controller.getPlayers3Option().setOnAction(this::on3PlayersOption);
        controller.getPlayers4Option().setOnAction(this::on4PlayersOption);
        controller.getPlayers6Option().setOnAction(this::on6PlayersOption);
    }

    public void handleServerMessage(String serverMessage) {
        Response[] responses = ResponseInterpreter.getResponses(serverMessage);

        for (Response response : responses) {
            switch (response.getCode()) {
                case "LOBBY":
                    handleLobbyMessage(response);
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
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lobby");
            alert.setHeaderText("Informacja od serwera");
            alert.setContentText(finalMessage);
            alert.showAndWait();
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

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gra");
            alert.setHeaderText("Informacje o grze");
            alert.setContentText(finalMessage);
            alert.showAndWait();
        });

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

    private void on2PlayersOption(ActionEvent event) {
        client.send("2");
    }

    private void on3PlayersOption(ActionEvent event) {
        client.send("3");
    }

    private void on4PlayersOption(ActionEvent event) {
        client.send("4");
    }

    private void on6PlayersOption(ActionEvent event) {
        client.send("6");
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
}
