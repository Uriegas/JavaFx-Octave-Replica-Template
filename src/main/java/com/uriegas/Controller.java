package com.uriegas;
import java.io.*;
import java.nio.file.Paths;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.stage.*;

public class Controller {
    @FXML
    private TabPane tabs;
    @FXML
    private TextArea cmdArea;
    @FXML
    private TextArea scriptArea;
    @FXML
    private Button fileBtn;
    @FXML
    private Button pasteBtn;
    @FXML
    private Button currentDirBtn;
    @FXML
    private TreeView<File> files;
    @FXML
    private ListView<String> commandHistory;
    private ObservableList<String> listCommands;
    private String oldText;//Text prior user input
    private File currentScript;
    final Clipboard clip = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();


    /**
     * Initializer to create the treeviewer
     * This method is special,
     * It's like a pos-constructor:
     * executed after constructing everything
     */
    public void initialize(){
        currentScript = null;
        TreeItem<File> root = createFilesTree(new File(
            Paths.get(System.getProperty("user.dir")).toString()));

        files.setCellFactory(new Callback<TreeView<File>,TreeCell<File>>(){
            public TreeCell<File> call(TreeView<File> t){
                return new TreeCell<File>(){
                    @Override
                    protected void updateItem(File item, boolean empty){
                        super.updateItem(item ,empty);
                        setText((empty || item == null) ? "" : item.getName());
                    }
                };
            }
        });
        files.setRoot(root);
        files.setShowRoot(false);
        /**
         * When a doubleClick over File
         * Evaluate if it is an .equ file or a .xlsx to load
         * If not then print an error message to terminal
         */
//        files.setOnMouseClicked(new EventHandler<MouseEvent>(){
//            @Override
//            public void handle(MouseEvent e){
//                if(e.getButton().equals(MouseButton.PRIMARY))
//                    if(e.getClickCount() == 2)
//                        System.out.println("Double clicked");
//            } 
//        });
        /**
         * Adds a listener to the click file in the Treeview
         * When a file is clicked then execute anonymous function
         * If clicked evaluate if it is a .equ or a .xlsx
         */
        oldText = cmdArea.getText();
        files.getSelectionModel().selectedItemProperty()//todo: double click
            .addListener((v, oldValue, newValue)->{
                if(newValue != null && (newValue.getValue().getPath().endsWith(".equ"))){
                    //Ugly syntax but only gets the file name of a path. Ex: /usr/hermoso/file1.txt -> file1.txt
                    printToTerminal( "\nOpening file: " + getFileName(newValue.getValue()));
                    renderFile(newValue.getValue());
                    currentScript = newValue.getValue();
                    tabs.getSelectionModel().select(1);
                }
                else if(newValue != null && newValue.getValue().getPath().endsWith(".xlsx")){
                    printToTerminal("\nHere you ought to load the file");
                }
        });
    /**
     * The user cannot delete anything before the: '>>'
         */
        cmdArea.setTextFormatter(new TextFormatter<>(change ->{
            if(change.getCaretPosition() < oldText.length() || change.getAnchor() < oldText.length())
                return null;
            return change;
        }));

        listCommands = FXCollections.observableArrayList();
        commandHistory.setItems(listCommands);
        commandHistory.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    cmdArea.appendText(newValue);
                }});
    }

    @FXML//Todo: new file window
    protected void fileClicked(ActionEvent e){//Just go to the script tab
//        System.out.println("You pressed the file button");
        //Cannot create file just open it
//        FileChooser choose = new FileChooser();
//        choose.setTitle("Create File as");
//        currentScript = choose.showOpenDialog(new Stage());
        //This is for extension of file validation
        currentScript = null;
        scriptArea.clear();
        tabs.getSelectionModel().select(1);
    }
    @FXML
    protected void copyClicked(ActionEvent e){
        //System.out.println("You pressed the copy button");
        //Copy current content to clipboard
        if(tabs.getSelectionModel().getSelectedIndex() == 0)
            content.putString(commandHistory.getItems().toString().replace(", ", "\n").replace("[", "").replace("]", ""));
        else
            content.putString(scriptArea.getText());
        clip.setContent(content);
    }
    @FXML
    protected void saveClicked(ActionEvent e){
        //System.out.println("You pressed the paste button");
        if(tabs.getSelectionModel().getSelectedIndex() != 1){
            tabs.getSelectionModel().select(0);
            printToTerminal("\n You can only save the Script");
        }
        else{//Wants to save the current script
            if(currentScript != null && currentScript.exists()){
                try{
                    PrintWriter savedText = new PrintWriter(currentScript);
                    BufferedWriter out = new BufferedWriter(savedText);
                    out.write(scriptArea.getText());
                    out.close();
                    tabs.getSelectionModel().select(0);
                    printToTerminal("\nSuccess saving file: " + getFileName(currentScript));
                }catch(IOException ex){
                    tabs.getSelectionModel().select(0);
                    printToTerminal("\nCouldn't write to file: " + getFileName(currentScript));
                }
            }
            else{//This means that the user wants to save a new file script
                FileChooser choose = new FileChooser();
                choose.setTitle("Save as");
                currentScript = choose.showSaveDialog(new Stage());
                //This is for extension of file validation
                if(currentScript.getPath().substring(currentScript.getPath().length() -4) != "equ")
                    currentScript.renameTo(new File(currentScript.getPath() + ".equ"));
                try{
                    if(currentScript != null){
                        PrintWriter savedText = new PrintWriter(currentScript);
                        BufferedWriter out = new BufferedWriter(savedText);
                        out.write(scriptArea.getText());
                        out.close();
                        tabs.getSelectionModel().select(0);
                        printToTerminal("\nSuccess saving file: " + getFileName(currentScript));
                    }
                }catch(IOException ex){
                    tabs.getSelectionModel().select(0);
                    printToTerminal("\nCouldn't write to file: " + getFileName(currentScript));
                }
            }
        }
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
                //This if doesn't work, don't know why
                //if(input != "" || input != null || input != "\n"){
                    printToTerminal("You entered: " + input);
                    listCommands.add(input);
                //}
            }
        }
        );
    }
    /**
     * Creates the node for the Files Tree
     * @param f the root File
     * @return the TreeItem with all the Files
     * Source: JavaFX docs
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
     * @param text to print
     */
    private void printToTerminal(String str){
        str = str + "\n>>";
        cmdArea.appendText(str);
        oldText = cmdArea.getText();
    }
    /**
     * Helper function to get the name of a full path File
     * @param file to get name of
     * @return
     */
    private String getFileName(File f){
        return f.getPath().substring(f.getPath().lastIndexOf('/')+1);
    }
    /**
     * Loads the file into the Script TextArea
     * @param file to render
     */
    private void renderFile(File f){
        String buff;
        try(BufferedReader reader = new BufferedReader(new FileReader(f))){
            scriptArea.clear();
            while((buff = reader.readLine()) != null)
                scriptArea.appendText(buff + '\n');
        }catch(IOException e){
            printToTerminal("\nCouldn't read file: " + getFileName(f));
        }
    }
}
