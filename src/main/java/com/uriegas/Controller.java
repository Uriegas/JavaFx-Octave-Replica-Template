package com.uriegas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private TextArea cmdString;
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
}
