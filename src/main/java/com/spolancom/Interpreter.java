package com.spolancom;

import java.beans.Expression;
import java.util.ArrayList;

public class Interpreter implements Exp.Visitor<Object>{
    //We dont implement function scope, just global variables
    public Environment envmnt;
    /**
     * Just initialize the environment (map of values)
     * Here we can load all program defined functions
     * Or variables that we want. Ex. PI.
     */
    public Interpreter(){
        envmnt = new Environment();
        envmnt.define("x", "4.0");//Test variable
        //Defin a print function
        envmnt.define("print", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                try{
                    System.out.println(arguments.get(0));
                }
                catch(Exception e){
                    throw new EnvironmentException("Cannot use: " + arguments.get(0) + "in print function");
                }
                return 0.0;
            }
        });
        /**
         * Define sin function
         */
        envmnt.define("sin", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                if(arguments.get(0) instanceof Double)
                    return Math.sin((double)arguments.get(0));
                else
                    throw new EnvironmentException("Could not convert " + arguments.get(0).toString() + "to number");
            }
        });
        /**
         * Define cos function
         */
        envmnt.define("cos", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                if(arguments.get(0) instanceof Double)
                    return Math.cos((double)arguments.get(0));
                else
                    throw new EnvironmentException("Could not convert " + arguments.get(0).toString() + "to number");
            }
        });
        /**
         * Define tan function
         */
        envmnt.define("tan", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                if(arguments.get(0) instanceof Double)
                    return Math.tan((double)arguments.get(0));
                else
                    throw new EnvironmentException("Could not convert " + arguments.get(0).toString() + "to number");
            }
        });
        /**
         * Define sqrt function
         */
        envmnt.define("sqrt", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                if(arguments.get(0) instanceof Double)
                    return Math.sqrt((double)arguments.get(0));
                else
                    throw new EnvironmentException("Could not convert " + arguments.get(0).toString() + "to number");
            }
        });
        /**
         * Define read function: Handles 2 types of files
         * Xlsx and .equ, to load data and functions
         */
        envmnt.define("read", new FuncCallable(){
            @Override
            public int arity(){return 1;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                ReadFunction read = new ReadFunction();
                try{
                    envmnt = read.ReadFile(arguments.get(0), interpreter);
                }catch(Exception e){
                    throw new EnvironmentException("Could'nt read file " + arguments.get(0));
                }
                return "Succesful file reading in " + arguments.get(0);
            }
        });
        /**
         * Define save function
         */
        envmnt.define("save", new FuncCallable(){
            @Override
            public int arity(){return 2;}
            @Override
            public Object call(Interpreter interpreter, ArrayList<Object> arguments){
                SaveFunction s = new SaveFunction();
                try{
                    s.writetoFile(arguments.get(1), arguments.get(0));
                }catch(Exception e){
                    throw new EnvironmentException("Couldn't write to file " + arguments.get(1));
                }
                return "Succesful file writin in " + arguments.get(1);
            }
        });
    }
    /**
     * Add the value and expression to the environment
     * Here we don't execute the expression, just save it
     * We only execute it when we call a function
     */
    @Override
    public Object visitAssignExpr(Exp.AssignNode expr){
        try{//Try to evaluate the expression, if it doesnt work it is a function
            Object result = evaluate(expr.value);
            envmnt.define(expr.name, result);
            return result;
        }catch(EnvironmentException ex){//Save the expression and return a success note
//            envmnt.define(expr.name, expr.value);
//            FuncCallable f = new FuncCallable();
//            return "Success loading: " + expr.name;//evaluate(expr.value);
            return "Could not evaluate this expression";
        }
    }
    /**
     * Search for the function in the environment
     * and execute the expression, it will search
     * for the parameters of the expression recursevly
     */
    @Override
    public Object visitCallExpr(Exp.CallNode expr){
        try{
            Object func = envmnt.get(expr.name);
            if(func instanceof FuncCallable){
                FuncCallable f = (FuncCallable)func;
                if(f.arity() != expr.arguments.size())
                    throw new EnvironmentException("Incorrect number of parameters for function" + expr.name);
                else{
			//>Maybe I need to fix this
                    ArrayList<Object> args = new ArrayList<Object>();
                    for(Exp arg : expr.arguments){
                        if(arg instanceof Exp.FileNode)
                            args.add(arg.toString());
                        else//Add the evaluated arguments
                            args.add(evaluate(arg));
                    }
                    return (Double)f.call(this, args);//Here in the future it is going to recive arrays
			//<Maybe I need to fix this
                }
            }
            else if(func instanceof String || func instanceof Double)
                return (Double)func;
            else if(func instanceof Exp)
                return evaluate((Exp)func);
            else{
                throw new EnvironmentException("Incorrect nomber of parameters for function" + expr.name);
            }
            
        }catch(Exception e){
            throw new EnvironmentException("This function doesn't exist");
        }
    }
    /**
     * Evaluate inner expression an return result
     */
    @Override
    public Object visitGroupingExpr(Exp.GroupingNode expr){
        return evaluate(expr.expression);
    }
    /**
     * Just get the number inside this node
     */
    @Override
    public Double visitNumberExpr(Exp.NumberNode e){
        return Double.parseDouble(e.value);
    }
    /**
     * If we are executing an expression and found a variable
     * Example: expression -> 2x+ sqrt(13+x)
     * we want to find the actual value of that variable
     * Here we search it in the environment
     */
    @Override
    public Object visitVariableExpr(Exp.Variable expr){//We are searching for variables when their value is just their string, this are strings we can change the parser to accept arguments in '' as strings its var_name = value
        String var_name = expr.name;//Get the name of the token
        Object value = envmnt.get(var_name);
        if(value instanceof String){
            try{
                return Double.parseDouble(value.toString());
            }catch(Exception e){
                throw new EnvironmentException("Cannot convert " + var_name + " to number");
            }
        }
        else if(value instanceof Double){
            return value;
        }
        else if(value instanceof Exp){
            return evaluate((Exp) value);
        }
        throw new EnvironmentException("No match type for the variable " + var_name);
    }
    /**
     * Im not gonna use this
     */
    @Override
    public Double visitFileExpr(Exp.FileNode expr){
        return 0.0;
    }
    /**
     * Evaluate a function definition aka. save in environment
     */
    @Override
    public Object visitFunctionExpr(Exp.FunctionNode expr){
        FuncDeclaration function = new FuncDeclaration(expr.expression, expr.params);
        envmnt.define(expr.name.getValue(), function);
        return this;
    }
    /**
     * This evalutes most of the mathematical functions
     */
    @Override
    public Object visitBinaryExpr(Exp.BinaryNode expr){
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        switch(expr.operator.getToken()){
            case Token.PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
            case Token.MINUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left - (double)right;
                }
            case Token.MULT:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left * (double)right;
                }
            case Token.DIV:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left / (double)right;
                }
            case Token.POW:
                if (left instanceof Double && right instanceof Double) {
                    return Math.pow((Double)left, (Double)right);
                }
            default:
                throw new EnvironmentException("Not valid operation in " + left.toString() + " " + expr.operator.getValue() + " " + right.toString());
        }
    }

    /**
     * Evalute with the current environment
     * @param e node to evalute
     * @return The result of the evaluation
     */
    public Object evaluate(Exp e){
        return e.accept(this);
    }

    /**
     * Get the environment
     */
    public Environment getEnv(){
        return this.envmnt;
    }
}
