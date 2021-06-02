package com.uriegas;
//import java.net.URL;

import javafx.application.Application;
//import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import javafx.scene.control.Button;
//import javafx.scene.layout.Pane;
//import javafx.scene.text.Text;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/octave-template.fxml"));

//        System.out.println("Hola");
//        System.out.println(this.getClass().getResource("/octave-template.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("octave-template.fxml"));
        Parent root = (Parent) loader.load();
        primaryStage.setTitle("Octave");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        
/*
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:src/main/resources/octave-template.fxml"));
        Scene scene = loader.load();

        primaryStage.setTitle("Simple form example. Page 2");
        primaryStage.setScene(scene);
        primaryStage.onCloseRequestProperty().setValue(event -> {
        System.out.println("\nBye! See you later!");
            Platform.exit();
        });
        primaryStage.show();
*/

/*
        Text txt = new Text("Hello World!");
        txt.relocate(135, 40);

        Button btn = new Button("Exit");
        btn.relocate(155, 80);

        btn.setOnAction(actionEvent -> {
            System.out.println("Bye! See you later!");
            Platform.exit();
        });

        Pane pane = new Pane();
        pane.getChildren().addAll(txt, btn);

        primaryStage.setTitle("The primary stage (top-level container)");
        primaryStage.onCloseRequestProperty()
                .setValue(e -> System.out.println("Bye! See you later!"));
        primaryStage.setScene(new Scene(pane, 350, 150));
        primaryStage.show();
*/
    }

    public static void main(String[] args) {
        launch(args);
    }
/*
    @Override
    public void stop() {
        System.out.println("Doing what has to be done before closing");
    }
*/
}