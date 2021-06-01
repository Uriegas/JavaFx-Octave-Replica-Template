package com.spolancom;

import java.util.*;

/**
 * <h1>Calculator<h1/>
 * A scientific calculator from terminal
 * programmed in Java
 * @author Eduardo Uriegas
 * @version 1.0
 * @since 2021-06-06
 */
public class Calculator{
    private String input;
    private Parser parser;
    private Interpreter evalVisit;
    /**
     * Dummy constructor
     */
    public Calculator(){
        input = "";
        parser = new Parser();//Evalute input
        evalVisit = new Interpreter();//Computation of input
    }
    /**
     * Start calculator
     * Just runs the calculator
     * @return Exit state
     */
    public int run(){
        Scanner s = new Scanner(System.in);
        System.out.println("Scientific Calculator");

        System.out.print("> ");
        //Save user input to in
        input = s.nextLine();
        while(!input.equals("!exit")){
            try{
                Exp result = parser.parse(input);
//                System.out.println("Procceded input maps to: " + result.accept(printVisit));
                System.out.println(result.accept(evalVisit));
            }
            catch(TokenizerException e){
                System.out.println(e.getMessage());
            }
            catch(ParserException ex){
                System.out.println(ex.getMessage());
            }
            catch(EvaluationException ex1){
                System.out.println(ex1.getMessage());
            }
            catch(EnvironmentException ex2){
                System.out.println(ex2.getMessage());
            }
            System.out.print("> ");
            //Save user input to in
            input = s.nextLine();
        }
        s.close();
        return 0;
    }
}