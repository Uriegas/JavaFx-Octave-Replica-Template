package com.uriegas;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.stage.*;
/**
 * Controller of the view (fxml)
 */
public class Controller {
    //Model (logical part of the program): the calcuator
    private Calculator calculator;
    @FXML
    private TabPane tabs;
    @FXML
    private TextArea cmdArea;
    @FXML
    private TextArea scriptArea;
    @FXML
    private TextField currentDir;
    @FXML
    private Button fileBtn;
    @FXML
    private Button copyBtn;
    @FXML
    private Button pasteBtn;
    @FXML
    private Button backBtn;
    @FXML
    private TreeView<File> files;
    @FXML
    private ListView<String> commandHistory;
    @FXML
    private TableView<Map.Entry<String, Object>> environment = new TableView<>();
    private ObservableList<String> listCommands;
    private String oldText;//Text prior user input
    private File currentScript;
    private String rootPath;
    final Clipboard clip = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    private HashMap<String, Object> table;

    /**
     * Initializer to create the treeviewer
     * This method is special,
     * It's like a pos-constructor:
     * executed after constructing everything
     */
    public void initialize(){
        //Init of vars
        calculator = new Calculator();
        table = calculator.getEnvironment();
        rootPath = System.getProperty("user.dir");
        setTreeDir(rootPath);
        /**
         * Environment implementation
         */
        TableColumn<Map.Entry<String,Object>,String> names = new TableColumn<>("Key");
        TableColumn<Map.Entry<String,Object>,Object> values = new TableColumn<>("Values");
        names.setCellValueFactory(new PropertyValueFactory<>("names"));
//        names.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Object>, String>, ObservableValue<String>>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Object>, String> p) {
//                // this callback returns property for just one cell, you can't use a loop here
//                // for first column we use key
//                return new SimpleStringProperty(p.getValue().getKey());
//            }
//        });
        values.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Object>, Object>, ObservableValue<Object>>() {

            @Override
            public ObservableValue<Object> call(TableColumn.CellDataFeatures<Map.Entry<String, Object>, Object> p) {
                // for second column we use value
                return new SimpleObjectProperty( p.getValue().getValue() ) ;
            }
        });

        ObservableList<Map.Entry<String, Object>> items = FXCollections.observableArrayList(table.entrySet());
        environment = new TableView<>(items);
        environment.getColumns().addAll(names, values);

        /**
         * Adds labels to folders and files, just their names 
         * without full path. Ex: /usr/bin/archivo.xlsx -> archivo.xlsx
         */
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
//        files.setRoot(root);
        files.setShowRoot(false);
        /**
         * When a doubleClick over File
         * Evaluate if it is an .equ file or a .xlsx to load
         * If not then print an error message to terminal
         */
        files.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e){
                if(e.getButton().equals(MouseButton.PRIMARY)){
                    if(e.getClickCount() == 2){
                        if(files.getSelectionModel().getSelectedItem().getValue().isDirectory()){
                            rootPath = files.getSelectionModel().getSelectedItem().getValue().toString();
                            setTreeDir(files.getSelectionModel().getSelectedItem().getValue().toString());
                        }
                    }
                }
            } 
        });
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
                    cmdArea.appendText("\n");
                    calcExec("read('" + newValue.getValue().getPath().toString() + "')");
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
    protected void pasteClicked(ActionEvent e){
        if(tabs.getSelectionModel().getSelectedIndex() == 1)
            scriptArea.appendText(clip.getString() + '\n');
        else
            printToTerminal("\nPlease paste command history only to Script tab");
    }
    @FXML
    protected void backClicked(ActionEvent e){
        rootPath = rootPath.substring(0, rootPath.lastIndexOf('/'));
        setTreeDir(rootPath);
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
                if(input.equals("!exit")){//Request close app(stage)
                    Node source = (Node)event.getSource();
                    Stage stage = (Stage)source.getScene().getWindow();
                    printToTerminal("Trying to close...");
                    stage.getOnCloseRequest().handle(null);
                    stage.close();
                }
                else{//Parse the input
                    calcExec(input);
                }
                //printToTerminal("You entered: " + input);
                    //Code with testing purposes
//                    if(input.equals("circle(x,y)")){
//                        ArrayList<Float> x = new ArrayList<Float>();
//                        ArrayList<Float> y = new ArrayList<Float>();
//                        for(int i = 0; i < 10; i++){
//                            x.add(new Float(i+2.1));
//                            y.add(new Float(i+1.1));
//                        }
//                        plotGraph(x, y);
//                    }
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
    /**
     * Set the root directory
     * of the TreeView
     */
    private void setTreeDir(String path){
        currentDir.clear();
        currentDir.appendText( path.substring(path.lastIndexOf('/'), path.length()) );
        files.setRoot(createFilesTree(new File(path)));
    }
    /**
     * Plot PieChart graph
     * TODO: Implement this as a Scene and add a copy button to copy the image
     * @param x
     * @param y
     */
    private void plotGraph(ArrayList<Float> x, ArrayList<Float> y){
        Text txt = new Text("Circle(x,y)");
        PieChart p = new PieChart();
        float xTotal=0, yTotal=0;
        for(Float data : x)
            xTotal += data;
        for(Float data : y)
            yTotal += data;
         
        p.getData().add(new PieChart.Data("x", xTotal));
        p.getData().add(new PieChart.Data("y", yTotal));
        VBox vb = new VBox(txt, p);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Stage s = new Stage();
        s.setTitle("Circle graph");
        s.setScene(new Scene(vb, 400, 400));
        s.show();
    }
    /**
     * Executes the input, just for modularity
     * @param in user input or clickable input
     */
    private void calcExec(String input){
        try{
            printToTerminal(String.valueOf(calculator.calculate(input)));
            listCommands.add(input);
            table = calculator.getEnvironment();
        }catch(Exception exception){
            printToTerminal(exception.getMessage());
        }
    }
}
