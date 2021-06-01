package com.spolancom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to Read variables or functions
 */
public class ReadFunction {
    private ArrayList<String> expressions = new ArrayList<>();
    /**
     * Dummy constructor
     */
    public ReadFunction(){
    }
    /**
     * ReadFile method
     */
    public Environment ReadFile(String path, Interpreter i) throws Exception{
        path = path.substring(1, path.length()-1);
        if(!(path.endsWith(".equ") || path.endsWith(".xslx")))
            throw new EnvironmentException("Not valid file extension in path " + path);
        
        if(path.endsWith(".equ")){
            System.out.println(path);
            BufferedReader s = new BufferedReader(new FileReader(path));
            String line = "";

            while((line = s.readLine()) != null){
                expressions.add(line);
            }
            //Pass expressions to parser and put them into the environment
            Parser p = new Parser();
            Exp result;
            for(String exp : expressions){
                result = p.parse(exp);
                result.accept(i);
            }
            s.close();
        }
        else if(path.endsWith(".xlsx")){
        }
        return i.getEnv();
    }
}
