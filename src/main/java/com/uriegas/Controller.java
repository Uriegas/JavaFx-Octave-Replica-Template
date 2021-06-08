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
    /**
     * Gets the text entered by the user in the command line area
     * Can be improved by the use of an emulator, as in the code in:
     * https://github.com/javaterminal/TerminalFX
     * Pending Functionality:
     * "Add the !exit command as a close of the program"
     */
    @FXML
    protected void inputStatement(){
        cmdArea.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.ENTER){
                String[] text = cmdArea.getText().split("\n");
                String input = text[text.length - 1];
                input = input.substring(2, input.length());
                System.out.println("You entered: " + input);
                cmdArea.appendText("You entered: " + input + '\n');
                cmdArea.appendText(">>");
            }
        }
        );
    }
}
