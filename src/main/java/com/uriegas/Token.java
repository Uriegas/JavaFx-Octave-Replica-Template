package com.uriegas;

/**
 * Represents Operators, Operands and Agrupation tokens.
 * Applying the type and the value (string)
 */
public class Token {//This vars are ids used in Tokenizer and Parser
    public static final int EPSILON = 0;
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MULT = 3;
    public static final int DIV = 4;
    public static final int POW = 5;
    public static final int IDENTIFIER = 6;
    public static final int OPEN_PARENTHESIS = 7;
    public static final int CLOSE_PARENTHESIS = 8;
    public static final int NUMBER = 9;
    //public static final int VARIABLE = 10;
    public static final int EQUALS = 10;
    public static final int FILE = 11;//Used in read_func
    public static final int COMMA = 12;//Used in save_func
    public static final int FUNC = 13;//Func keyword

    private String value;
    private int token_type;
    /**
     * Initializer for Numbers
     * @param v The value of the token
     */
    public Token(Double v){
        value = v.toString();
    }
    /**
     * Simple constructor used in the Tokenizer
     * @param t
     * @param v
     */
    public Token(int t, String v){
        token_type = t;
        value = v;
    }
    /**
     * Get the current Token type
     * Ex. IDENTIFIER, PLUS, etc.
     * @return
     */
    public int getToken(){
        return token_type;
    }
    /**
     * Returns the token's value.
     * Ex. 12, f1, x, )
     * @return Value in string format
     */
    public String getValue(){
        return value;
    }
    /**
     * Boring toString
     */
    public String toString(){
        return token_type + ": " + value;
    }
}