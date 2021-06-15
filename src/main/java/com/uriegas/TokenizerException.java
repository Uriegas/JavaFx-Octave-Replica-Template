package com.uriegas;
/**
 * Exceptions in Tokenizer
 */
public class TokenizerException extends RuntimeException{
    /**
     * Define the type of exception
     * @param m
     */
    public TokenizerException(String m){
        super(m);
    }
}
