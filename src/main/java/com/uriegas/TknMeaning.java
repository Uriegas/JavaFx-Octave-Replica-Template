package com.uriegas;
/**
 * Represents the meaning of the tokens
 * it is used for generating the tokens
 * in the Tokenizer
 */
public class TknMeaning {
    private int tokenType;//For meanning
    private String regex;//For searching
    
    /**
     * Simple Constructor
     * @param t tokenType
     * @param r regex expression
     */
    public TknMeaning(int t, String r){
        tokenType = t;
        regex = "^(" + r + ")";
    }
    /**
     * Get the token type
     * @return token type aka. meaning
     */
    public int getTokenType(){
        return tokenType;
    }
    /**
     * Get the regex syntax for searching
     * @return regex ex. \\d+ (number)
     */
    public String getRegex(){
        return regex;
    }
}
