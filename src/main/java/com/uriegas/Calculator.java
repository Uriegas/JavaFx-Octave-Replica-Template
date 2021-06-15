package com.uriegas;

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
     * Allows other programs to interact with the calculator
     * This is the main method of the Calculator
     * @param in an expression in String format
     * @return Object representing the evaluated expression
     * @throws Exception whatever expression that the program throws
     */
    public Object calculate(String in) throws Exception{
        return( parser.parse(in).accept(evalVisit) ) ;
    }
    /**
     * Start calculator, simple REPL
     * Just runs the calculator in CLI
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
                System.out.println(parser.parse(input).accept(evalVisit));
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