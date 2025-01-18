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

    private CommunicationManager communicationManager;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if the GUI cannot be loaded or initialized.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/board.fxml"));
            Parent root = loader.load();

            // Get the controller and connect it to the server
            Controller controller = loader.getController();
            controller.connectAndStartMatch(SERVER_ADDRESS, SERVER_PORT);

            // Set up the primary stage
            Scene scene = new Scene(root);
            primaryStage.setTitle("Trylma - Chinese Checkers");
            primaryStage.setScene(scene);

            // Handle application close event
            primaryStage.setOnCloseRequest(event -> {
                controller.blockGUI(true); // Lock the GUI to prevent further interaction
                stop(); // Release resources
                Platform.exit();
            });

            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error initializing the application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Releases resources when the application is stopped.
     */
    @Override
    public void stop() {
        try {
            if (communicationManager != null) {
                communicationManager.sendMessage("DISCONNECT");
                communicationManager = null;
            }
        } catch (Exception e) {
            System.err.println("Error during application shutdown: " + e.getMessage());
        }
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
