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
//		boolean isArray = false;
//		int numberofArrays = 0;
//		for(Object arg : arguments)
//			if(arg instanceof ArrayList){//If some of the arguments is an array
//				numberofArrays++;
//			}
//		isArray = (numberofArrays != 0) ? true : false;
//		
//		if(isArray){//Code to execute when we find an array
//			if(numberofArrays != arguments.size())//Mismatch in arguments types
//				throw new EnvironmentException("Mismatch in parameters types: Cannot calculate arrays with numbers");
//			else{//Same types, calcuate until we reach one of the parameters end
//				try{
//				ArrayList<Exp> result = new ArrayList<Exp>();
//				int max_arg_size = 0;
//				for(Object arg : arguments)//Find the minor array
//					if(arg instanceof ArrayList<?>)
//						if((((ArrayList)arg).size() > 0) && ((ArrayList)arg).get(0) instanceof Exp)//If this object is a non empty ArrayList<Exp>
//							if( ((ArrayList)arg).size() > max_arg_size )
//								max_arg_size = ((ArrayList)arg).size();
//				
//				for(int i = 0; i < max_arg_size; i++){
//					for(int j = 0; j < arguments.size(); j++){
//						if((((ArrayList)arguments.get(j)).size() > 0) && ((ArrayList)arguments.get(j)).get(0) instanceof Exp)
//							interpreter.envmnt.define(parameters.get(j).getValue(), ((ArrayList<Exp>)arguments.get(j)).get(i));
//					}
//					result.add((Exp)interpreter.evaluate(expression));
//				}
//				return result;
//				}catch(Exception exception){
//					throw new EnvironmentException("Could not execute this function with given parameters");
//				}
//			}
//		}
//		if(!isArray){//Code to execute when it normal arguments
//			for( int i = 0; i < arguments.size(); i++)//Add to the envmnt temporal vars
//				interpreter.envmnt.define(parameters.get(i).getValue(), arguments.get(i));
//			return interpreter.evaluate(expression);
//		}
//		else{
//			throw new EnvironmentException("Uknown error");
//		}
//        int validation = 0;
//        List<ArrayList<Object>> arrayargs = new ArrayList<ArrayList<Object>>();
//		for(Object arg : arguments)//Check all args are arrays
//            if(arg instanceof List<?>)
//                arrayargs.add(arg);
//        if(validation == arguments.size())//If all args are arrays then:
            for( int i = 0; i < arguments.size(); i++)//Add to the envmnt temporal vars
                interpreter.envmnt.define(parameters.get(i).getValue(), arguments.get(i));
			return interpreter.evaluate(expression);
	}
}
