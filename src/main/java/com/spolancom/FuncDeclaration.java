package com.spolancom;

import java.util.*;

public class FuncDeclaration implements FuncCallable{
	private Exp expression;
	private List<Token> parameters;
	public FuncDeclaration(Exp e, List<Token> param){
		this.expression = e;
		this.parameters = param;
	}
	@Override
	public int arity(){return parameters.size();}
	@Override
	public Object call(Interpreter interpreter, ArrayList<Object> arguments){
        for( int i = 0; i < arguments.size(); i++)//Add to the envmnt temporal vars
            interpreter.envmnt.define(parameters.get(i).getValue(), arguments.get(i));
        return interpreter.evaluate(expression);
	}
}
