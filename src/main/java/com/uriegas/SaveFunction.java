package com.uriegas;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * SaveFunction added in environment
 * Has only one function to handle files
 */
public class SaveFunction{
    ArrayList<String> object_to_save;
    String path;
    /**
     * Dummy constructor
     */
    public SaveFunction(){
    }
    /**
     * Method to save the full environment (current vars in program)
     * @param path where to save
     * @param e environment of vars
     * @param i Interpreter for evaluate expressions recursively
     * @throws Exception
     */
    public void writetoFile(String path, Environment e, Interpreter i) throws Exception{
        if(!path.endsWith(".txt"))
            path += ".txt";
        File file = new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(Exception ex){
                throw new FileException("Cannot open this file");
            }
        }

        ArrayList<Object> writing = e.getAll();

        FileWriter myWriter = new FileWriter(path);
        for(Object w : writing){
            if(w instanceof Exp)
                myWriter.write(w.toString());
        }
        myWriter.close();
    }
    /**
     * Write a function to specifed file path
     * @param path the file
     * @param evaluated_func the result of the function well formatted (Ready to write)
     * @throws Exception
     */
    public void writetoFile(String path, String evaluated_func) throws Exception{
        if(!path.endsWith(".txt"))
            path += ".txt";
        File file = new File(path);
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(Exception ex){
                throw new FileException("Cannot open this file");
            }
        }

        FileWriter myWriter = new FileWriter(path);
        myWriter.write(evaluated_func);
        myWriter.close();
    }
}