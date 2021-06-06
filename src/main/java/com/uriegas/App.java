package com.uriegas;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.stage.*;
/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/octave-template.fxml"));

        Parent root = (Parent) loader.load();
        primaryStage.setTitle("Octave");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void stop() {
        System.out.println("Doing what has to be done before closing");
    }
}