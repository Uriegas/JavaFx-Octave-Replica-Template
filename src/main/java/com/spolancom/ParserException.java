package com.spolancom;
/**
 * Extend the RuntimeException
 * Exception used in the Parser class
 */
public class ParserException extends RuntimeException {
    /**
     * Define the type of exception
     * @param m
     */
    public ParserException(String m){
        super(m);
    }
}
