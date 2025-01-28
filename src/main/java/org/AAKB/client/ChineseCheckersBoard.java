package org.AAKB.client;

import static org.AAKB.constants.ConstantProperties.SERVER_ADDRESS;
import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the Chinese Checkers application.
 * Sets up the GUI and initializes the controller.
 */
public class ChineseCheckersBoard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/board.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Controller controller = loader.getController();
        Client client = new Client(controller);


        primaryStage.setTitle("Chińskie Warcaby");
        primaryStage.setScene(scene);
        primaryStage.show();

        client.startClient();
        primaryStage.setOnCloseRequest(e -> {
            client.quit();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
