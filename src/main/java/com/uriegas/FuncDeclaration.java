package com.uriegas;

import java.util.*;
/**
 * Class to handle function declarations Ex. func f1(x,y) = x + y
 * This functions saves data like arity, the expression (operation) and a call method
 */
public class FuncDeclaration implements FuncCallable{
	private Exp expression;
	private List<Token> parameters;
	/**
	 * Instantiate Function with the Expression
	 * and the list of parameters, the name of the
	 * function is handle in the environment definition of it
	 * @param e expression
	 * @param param function parameters
	 */
	public FuncDeclaration(Exp e, List<Token> param){
		this.expression = e;
		this.parameters = param;
	}
	/**
	 * Arity: Number of parameters of the function
	 * @return int number of parameters
	 */
	@Override
	public int arity(){return parameters.size();}
	/**
	 * Call method, when used the function is executed with the given
	 * arguments and an instance of the interpreter as environment
	 * This function also handles array variables
	 * @return the result of the operation.
	 */
	@Override
	public Object call(Interpreter interpreter, ArrayList<Object> arguments){
            for( int i = 0; i < arguments.size(); i++)//Add to the envmnt temporal vars
                interpreter.envmnt.define(parameters.get(i).getValue(), arguments.get(i));
			return interpreter.evaluate(expression);
	}
}
