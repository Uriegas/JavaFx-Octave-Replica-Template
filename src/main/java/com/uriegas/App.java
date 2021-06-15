package com.uriegas;

import java.util.Optional;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
/**
 * JavaFX App
 */
public class App extends Application {
    /**
     * Starts the JavaFx Application
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/octave-template.fxml"));

        /**
         * Alert: Exit Confirmation Window
         */
        primaryStage.setOnCloseRequest(event -> {
            Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?"
            );
            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
            exitButton.setText("Exit");
            closeConfirmation.setHeaderText("Confirm Exit");
            closeConfirmation.initModality(Modality.APPLICATION_MODAL);
            closeConfirmation.initOwner(primaryStage);

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get()))
                event.consume();
        });

        Parent root = (Parent) loader.load();
        primaryStage.setTitle("Octave");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    /**
     * In a JavaFx project the main function is ignored
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Close the JavaFx App
     */
    @Override
    public void stop() {
        System.out.println("Doing what has to be done before closing");
    }
}