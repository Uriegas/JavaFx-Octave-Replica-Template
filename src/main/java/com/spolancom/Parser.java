package com.spolancom;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Parser implementing a defined grammar and
 * the recursive descendent algorithm of type LL(2).
 * 
 * Grammar:
 * expression -> assignment | "func" function
 * function -> IDENTIFIER "(" parameters? ")" "=" term  //Esta cosa maybe muere
 * assignment -> IDENTIFIER "=" assignment | term
 * term -> factor ( ( "-" | "+" ) factor )*
 * factor -> pow ( ( "/" | "*" ) pow )*
 * pow -> call ^ call | call
 * call -> IDENTIFIER "(" arguments? ")"
 * primary -> NUMBER | IDENTIFIER | "(" expression ")"
 * 
 * This implementatino is based on:
 * https://www.craftinginterpreters.com/appendix-i.html
 */
public class Parser {
    LinkedList<Token> tokens;
    int current;
    Token currentToken;
    //int num_identifiers;//Use for function definitions
    /**
     * Create the object with the tokens
     * 
     * @param t
     */
    public Parser(LinkedList<Token> t) {
        this();
        tokens = (LinkedList<Token>) t.clone();
        currentToken = tokens.getFirst();
    }

    /**
     * Tokenize the string before parsing it
     * 
     * @param str The expression to tokenize
     */
    public Parser(String str) {
        this();
        Tokenizer t = new Tokenizer();
        t.tokenize(str);
        tokens = t.getTokens();
    }

    /**
     * Dummy constructor
     */
    public Parser() {
        current = 0;
        tokens = new LinkedList<Token>();
    }

    /**
     * Just get the next token in the tokens
     * Although we define an EPSILON the parser is
     * design to handle null tokens just returning the valid one
     * @return The newer token
     */
    public Token nextToken() {
        try {
            return tokens.get(current + 1);
        } catch (IndexOutOfBoundsException e) {// Maybe just throw a new TokenizerException
            return currentToken = new Token(Token.EPSILON, null);// EOT:END OF TOKENS
        }
    }
    /**
     * Get the last token
     * DOESN'T move the currenToken
     * @return previous token
     */
    public Token previousToken() {
        try {
            return tokens.get(current - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new TokenizerException("Yo, you tried access a non existen previous token");
        }
    }

    /**
     * Advance to the next
     * MOVES the currentToken to the next token
     * The program handles null tokens
     * @return The previous token before advancing
     */
    public Token advance() {
        current++;
        currentToken = tokens.get(current);
        return previousToken();
    }

    /**
     * Actual method that parses the tokens 
     * Implements recursive descendent parsing
     * Parse from token list
     * @return The root node
     */
    public Exp parse(LinkedList<Token> t) {
        tokens = (LinkedList<Token>) t.clone();
        currentToken = tokens.getFirst();
        // Recursive descendent parsing implementation
        Exp e = expression();
        // Error if we havent get EOT at the end of parsing
        if (currentToken.getToken() != Token.EPSILON)
            throw new ParserException("Syntax Error");
        return e;
    }

    /**
     * Implements recursive descendent parsing
     * Parse from string, use tokenizer
     * @return root node
     */
    public Exp parse(String str) {
        // Reinit the current token just in case
        current = 0;
        Tokenizer tok = new Tokenizer();
        tokens = tok.tokenize(str);
        currentToken = tokens.get(current);
        // Recursive descendent parsing implementation
        Exp e = expression();
        if(currentToken.getType() == null || currentToken.getToken() == Token.EPSILON)
            return e;
        throw new ParserException("Invalid token: " + currentToken.toString());
    }

    /**
     * Here starts the Parser aka grammar rules
     * Everything the user enters is an expression
     * Precedence is implemented backwards
     * Lower precendes in the first rule
     * Higher precende is the lowest rule
     * @return root node
     */
    private Exp expression() {//expression -> assignment | "func" function
        if(currentToken.getToken() == Token.FUNC){
            advance();
            return function();
        }
        else{
            tokens.addFirst(new Token(Token.EQUALS, "="));
            tokens.addFirst(new Token(Token.IDENTIFIER, "ans"));
            current = 0;
            currentToken = tokens.get(current);
            return assignment();
        }
    }
    /**
     * Grammar rule to handle functions declarations
     * @return
     */
    private Exp function(){//function -> IDENTIFIER "(" parameters ")" "=" term
        Token name = advance();//The function name
        //Save list of arguments
        ArrayList<Token> params = new ArrayList<Token>();
        Exp expression;
        if(advance().getToken() == Token.OPEN_PARENTHESIS){
            if(currentToken.getToken() == Token.CLOSE_PARENTHESIS){
                advance();
                advance();
                expression = term();
            }
            else{
                do{
                    params.add(advance());
                }while(advance().getToken() == Token.COMMA);
                if(previousToken().getToken() == Token.CLOSE_PARENTHESIS){
                    advance();
                    expression = term();
                }
                else
                    throw new ParserException("Expected ')' after parameters");
            }

        }
        else
            throw new ParserException("Expected '(' after func name ");
        return new Exp.FunctionNode(name, params, expression);
    }
    /**
     * Grammar rule to handle assignment
     * Or pass to the next rule
     * @return Expression node
     */
    private Exp assignment() {// assignment -> IDENTIFIER "=" assignment | term
        if ((currentToken.getToken() == Token.IDENTIFIER) && (nextToken().getToken() == Token.EQUALS)) {
            Token name = currentToken;// Point to the name of the token
            advance();// The EQUAL sign "="
            advance();// Point to next token to parse expression
            return new Exp.AssignNode(name, assignment());
        } else {//If not an assignment append at begin: "ans =" (default environment variable: ans)
            return term();
        }
    }
    /**
     * Grammar rule to handle terms
     * Or pass to the next rule
     * @return Expression node
     */
    private Exp term() {// term -> factor ( ( "-" | "+" ) factor )*
        Exp exp = factor();
        while (currentToken.getToken() == Token.PLUS || currentToken.getToken() == Token.MINUS) {
            Token operator = currentToken;
            advance();
            Exp right = factor();
            exp = new Exp.BinaryNode(exp, operator, right);
        }
        return exp;
    }

    /**
     * Grammar rule to handle factors
     * Or pass to the next rule
     * @return Expression node
     */
    private Exp factor() {// factor -> pow ( ( "/" | "*" ) pow )*
        Exp exp = pow();
        while (currentToken.getToken() == Token.MULT || currentToken.getToken() == Token.DIV) {
            Token operator = currentToken;
            advance();
            Exp right = pow();// call or maybe we should redifine unary to extend to call
            exp = new Exp.BinaryNode(exp, operator, right);
        }
        return exp;
    }

    /**
     * Grammar rule to handle pow (^)
     * Or pass to the next rule
     * @return Expression node
     */
    private Exp pow() {// pow -> primary "^" primary
        Exp exp = call();
        if (currentToken.getToken() == Token.POW) {
            Token operator = currentToken;
            advance();
            Exp right = call();
            exp = new Exp.BinaryNode(exp, operator, right);
        }
        return exp;
    }

    /**
     * Grammar rule for a function call
     * In the future we need to change the distintion between variable and function
     * To abstract it to only an identifier
     * @return
     */
    private Exp call(){//call -> primary ( "(" arguments? ")" )*
        Exp exp = primary();
        if(previousToken().getToken() == Token.IDENTIFIER && currentToken.getToken() == Token.OPEN_PARENTHESIS){//If the previous token is not a number or expression and the current token is a prenthesis
            Token fun_name = previousToken();
            ArrayList<Exp> arguments = new ArrayList<>();
            while(currentToken.getToken() != Token.CLOSE_PARENTHESIS){
                do{
                    advance();
                    arguments.add(assignment());
                }while(currentToken.getToken() == Token.COMMA);
                Exp call = new Exp.CallNode(fun_name, arguments);
                advance();
                return call;
            }
            throw new ParserException("Cannot call expressions: " + previousToken());
        }
        return exp;
    }
    /**
     * Grammar rule to handle terminals
     * (grammar tokens or leaf nodes)
     * This is the last rule
     * Highest precedence
     * Recursivity is seen in: "(" expression ")"
     * @return Expression node 
     */
    private Exp primary() {// primary -> NUMBER | IDENTIFIER | "(" assignment ")"
        if (currentToken.getToken() == Token.IDENTIFIER) {
            advance();
            return new Exp.Variable(previousToken());
        }
        else if (currentToken.getToken() == Token.NUMBER) {
            advance();
            return new Exp.NumberNode(previousToken().getValue());
        }
        else if (currentToken.getToken() == Token.OPEN_PARENTHESIS) {
            advance();
            Exp exp = assignment();
            if (currentToken.getToken() != Token.CLOSE_PARENTHESIS) {
                throw new ParserException("Close parenthesis not found");
            }
            advance();
            return new Exp.GroupingNode(exp);
        }
        else if(currentToken.getToken() == Token.FILE){
            advance();
            return new Exp.FileNode(previousToken());
        }
        throw new ParserException("Missing token");
    }
}
