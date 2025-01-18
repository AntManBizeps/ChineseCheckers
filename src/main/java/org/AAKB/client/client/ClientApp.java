package org.AAKB.client.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        Button connectButton = new Button("Connect");

        connectButton.setOnAction(e -> {
            try {
                // Połącz z serwerem
                Client client = new Client(); // Host i port serwera
                messageArea.appendText("Connected to server\n");
            } catch (Exception ex) {
                messageArea.appendText("Failed to connect to server: " + ex.getMessage() + "\n");
            }
        });


        VBox layout = new VBox(10, connectButton, messageArea);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.setTitle("Chinese Checkers - Lobby");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        if (client != null) {
//            client.close();
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
