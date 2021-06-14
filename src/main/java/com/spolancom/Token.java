package com.spolancom;

/**
 * Define in a readible way Token types
 */
enum Token_Type{
    ADD, SUB, MUL, DIV, POW, SQRT, SIN, COS, TAN, //Operators
    NUMBER, VARIABLE, //Operands
    ASSOCIATIVE_RIGTH, ASSOCIATIVE_LEFT,//Agrupators
    UNDEFINED //Undefined type aka ERROR
}

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
    private Token_Type type;
    private String value;
    
    //New attribute
    private int token_type;
    /**
     * Initializer for Numbers
     * @param v The value of the token
     */
    public Token(Double v){
        type = Token_Type.NUMBER;
        value = v.toString();
    }
    /**
     * Initializer for not numbers
     * @param v The value of the token
     */
    public Token(String v){
        value = v;
        switch(v){//Maybe here we need a try catch
            case "+": type = Token_Type.ADD; break;
            case "-": type = Token_Type.SUB; break;
            case "*": type = Token_Type.MUL; break;
            case "/": type = Token_Type.DIV; break;
            case "^": type = Token_Type.POW; break;
            case "sqrt": type = Token_Type.SQRT; break;
            case "sin": type = Token_Type.SIN; break;
            case "cos": type = Token_Type.COS; break;
            case "tan": type = Token_Type.TAN; break;
            case "(": type = Token_Type.ASSOCIATIVE_LEFT; break;
            case "[": type = Token_Type.ASSOCIATIVE_LEFT; break;
            case ")": type = Token_Type.ASSOCIATIVE_RIGTH; break;
            case "]": type = Token_Type.ASSOCIATIVE_RIGTH; break;
            case "x": type = Token_Type.VARIABLE; break;
            default: type = Token_Type.UNDEFINED; break;
        }
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

    public int getToken(){
        return token_type;
    }
    /**
     * Returns token's value
     * @return The value of the number in Double format
     */
    public Double getNumber(){
        if(type == Token_Type.NUMBER)
            return Double.valueOf(value.toString());
        else
            return null;
    }
    /**
     * Returns the token's value
     * @return Value in string format
     */
    public String getValue(){
        return value;
    }
    /**
     * Token type (ej. ADD, POW, NUMBER)
     * @return Token_type
     */
    public Token_Type getType(){return type;}
    /**
     * It's the token an operand?
     * @return bool
     */
    public boolean isOperand(){
        if( this.type == Token_Type.NUMBER ||
            this.type == Token_Type.VARIABLE )
            return true;
        else
            return false;
    }

    /**
     * It's the token an operator?
     * @return bool
     */
    public boolean isOperator(){
        if( this.type == Token_Type.ADD || this.type == Token_Type.SUB ||
            this.type == Token_Type.MUL || this.type == Token_Type.DIV ||
            this.type == Token_Type.POW )
            return true;
        else
            return false;
    }

    /**
     * It's the token a function?
     * @return bool
     */
    public boolean isFunction(){
        if( this.type == Token_Type.SIN || this.type == Token_Type.COS ||
            this.type == Token_Type.TAN || this.type == Token_Type.SQRT )
            return true;
        else
            return false;
    }
    /**
     * It's the token a binary operator?
     * @return bool
     */
    public boolean isBinary(){
        if( this.type == Token_Type.ADD || this.type == Token_Type.SUB ||
            this.type == Token_Type.MUL || this.type == Token_Type.DIV ||
            this.type == Token_Type.POW)
            return true;
        else
            return false;
    }
    /**
     * it's the token a unary operator?
     * @return bool
     */
    public boolean isUnary(){
        if( this.type == Token_Type.SIN || this.type == Token_Type.SQRT ||
            this.type == Token_Type.COS || this.type == Token_Type.TAN )
            return true;
        else
            return false;
    }
    /**
     * Precedence of operator tokens
     * @return int indicating precedence
     */
    public int precedence(){
        if(type == Token_Type.POW)
            return 3;
        else if(type == Token_Type.MUL || type == Token_Type.DIV)
            return 2;
        else if(type == Token_Type.ADD || type == Token_Type.SUB)
            return 1;
        else //Unary operators
            return 0;
    }
    /**
     * it's the token associative to the left?
     * @return bool
     */
    public boolean leftAssociative(){
        if( type == Token_Type.ADD || type == Token_Type.SUB ||
            type == Token_Type.MUL )
            return true;
        else
            return false;
    }
    /**
     * it's the token associative to the right?
     * @return bool
     */
    public boolean rigthAssociative(){
        return !this.leftAssociative();
    }
    /**
     * Compares wether 2 tokens are of the same type and value
     * @return bool
     */
    public boolean equals(Token t){
        if(this.type == t.type && this.value == t.value)
            return true;
        else
            return false;
    }
    /**
     * Boring toString
     */
    public String toString(){
        return token_type + ": " + value;
    }
}