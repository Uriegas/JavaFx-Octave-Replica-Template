package com.uriegas;
import java.io.*;
import java.util.function.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.collections.*;

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
    private TreeView<File> files;
    private String oldText;//Text prior user input

    /**
     * Initializer to create the treeviewer
     * This method is special,
     * It's like a pos-constructor:
     * executed after constructing everything
     */
    public void initialize(){
        TreeItem<File> root = createFilesTree(new File("/"));
        files.setRoot(root);
        files.setShowRoot(false);
        /**
         * Adds a listener to the click file in the Treeview
         * When a file is clicked then execute anonymous function
         */
        oldText = cmdArea.getText();
        files.getSelectionModel().selectedItemProperty()
            .addListener((v, oldValue, newValue)->{
                if(newValue != null)
                    printToTerminal(newValue.getValue().toString());
            });
        
        cmdArea.setTextFormatter(new TextFormatter<>(change ->{
            if(change.getCaretPosition() < oldText.length() || change.getAnchor() < oldText.length())
                return null;
            return change;
        }));
    }

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
                printToTerminal("You entered: " + input);
            }
        }
        );
    }
    /**
     * Creates the node for the Files Tree
     * @param f the root File
     * @return the TreeItem with all the Files
     */
    private TreeItem<File> createFilesTree(final File f) {
    return new TreeItem<File>(f) {
        private boolean isLeaf;
        private boolean isFirstTimeChildren = true;
        private boolean isFirstTimeLeaf = true;

        @Override
        public ObservableList<TreeItem<File>> getChildren() {
            if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
            }
            return super.getChildren();
        }

        @Override
        public boolean isLeaf() {
            if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            File f = (File) getValue();
            isLeaf = f.isFile();
            }
            return isLeaf;
        }

        private ObservableList<TreeItem<File>> buildChildren(
            TreeItem<File> TreeItem) {
            File f = TreeItem.getValue();
            if (f == null) {
            return FXCollections.emptyObservableList();
            }
            if (f.isFile()) {
            return FXCollections.emptyObservableList();
            }
            File[] files = f.listFiles();
            if (files != null) {
            ObservableList<TreeItem<File>> children = FXCollections
                .observableArrayList();
            for (File childFile : files) {
                children.add(createFilesTree(childFile));
            }
            return children;
            }
            return FXCollections.emptyObservableList();
        }
        };
    }
    /**
     * Println for the Emulated Terminal
     */
    private void printToTerminal(String str){
        str = str + "\n>>";
        cmdArea.appendText(str);
        oldText = cmdArea.getText();
    }
}
