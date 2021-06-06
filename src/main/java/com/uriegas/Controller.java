package com.uriegas;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class Controller {
    @FXML
    private TextArea cmdArea;
    @FXML
    private Button fileBtn;
    @FXML
    private Button pasteBtn;
    @FXML
    private Button currentDirBtn;

    @FXML
    protected void fileClicked(ActionEvent e){
        System.out.println("You pressed the file button");
    }
    @FXML
    protected void pasteClicked(ActionEvent e){
        System.out.println("You pressed the paste button");
    }
    @FXML
    protected void backClicked(ActionEvent e){
        System.out.println("You pressed the back button");
    }
    @FXML
    protected void selectDirClicked(ActionEvent e){
        System.out.println("You pressed the selectDir button");
    }
    @FXML
    protected void inputStatement(){
        cmdArea.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.ENTER){
                String text = cmdArea.getText();
                System.out.println("You entered: " + text);
            }
        }
        );
    }
//    cmdArea.setOnKeyPressed( new EventHandler<KeyEvent>(){
//        public void handle(KeyEvent keyEvent) {
//            if (keyEvent.getCode() == KeyCode.ENTER)  {
//                String text = cmdArea.getText();
//                System.out.println(text);
//                cmdArea.setText("");
//            }
//        }
//    });
    
}
