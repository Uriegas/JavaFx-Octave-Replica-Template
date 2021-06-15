package com.spolancom;

import java.util.*;

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
                else if(arguments.get(0) instanceof ArrayList<?>){
                    Iterator<Exp> it = ((ArrayList)arguments.get(0)).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(it.hasNext())
                        results.add( Math.sin((Double)evaluate(it.next())) );
                    return results;
                }
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
                else if(arguments.get(0) instanceof ArrayList<?>){
                    Iterator<Exp> it = ((ArrayList)arguments.get(0)).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(it.hasNext())
                        results.add( Math.cos((Double)evaluate(it.next())) );
                    return results;
                }
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
                else if(arguments.get(0) instanceof ArrayList<?>){
                    Iterator<Exp> it = ((ArrayList)arguments.get(0)).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(it.hasNext())
                        results.add( Math.cos((Double)evaluate(it.next())) );
                    return results;
                }
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
                else if(arguments.get(0) instanceof ArrayList<?>){
                    Iterator<Exp> it = ((ArrayList)arguments.get(0)).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(it.hasNext())
                        results.add( Math.sqrt((Double)evaluate(it.next())) );
                    return results;
                }
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
                //if(arguments.get(0) instanceof FileNode){
                    try{
                        envmnt = read.readFile((String)arguments.get(0), interpreter);
                    }catch(Exception e){
                        throw new EnvironmentException("Could not read file " + arguments.get(0));
                    }
                //}
                //else
                //    throw new EnvironmentException("Not a file");
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
                    s.writetoFile((String)arguments.get(1), (String)arguments.get(0));
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
    public Object visitAssignExpr(Exp.AssignNode expr)throws EnvironmentException{
//        try{//Try to evaluate the expression, if it doesnt work it is a function
            Object result = evaluate(expr.value);
            envmnt.define(expr.name, result);
            return result;
//        }catch(EnvironmentException ex){//Save the expression and return a success note
//            envmnt.define(expr.name, expr.value);
//            FuncCallable f = new FuncCallable();
//            return "Success loading: " + expr.name;//evaluate(expr.value);
//            return "Could not evaluate this expression";
//        }
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
                            args.add(evaluate(arg));
                    }
                    return f.call(this, args);//Here in the future it is going to recive arrays
			//<Maybe I need to fix this
                }
            }
            //>What is this?
            else if(func instanceof String || func instanceof Double)
                return (Double)func;
            else if(func instanceof Exp)
                return evaluate((Exp)func);
            //<What is this?
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
//        String var_name = expr.name;//Get the name of the token
//        Object value = envmnt.get(var_name);
//        if(value instanceof String){
//            try{
//                return Double.parseDouble(value.toString());
//            }catch(Exception e){
//                throw new EnvironmentException("Cannot convert " + var_name + " to number");
//            }
//        }
//        else if(value instanceof Double){
//            return value;
//        }
//        else if(value instanceof Exp){
//            return evaluate(value);
//        }
//        throw new EnvironmentException("No match type for the variable " + var_name);
        return envmnt.get(expr.name) ;
    }
    /**
     * File handling, just for not mixing numeric variables with strings
     */
    @Override
    public String visitFileExpr(Exp.FileNode expr){
        return expr.name;
    }
    /**
     * Evaluate a function definition aka. save in environment
     */
    @Override
    public Object visitFunctionExpr(Exp.FunctionNode expr){
        FuncDeclaration function = new FuncDeclaration(expr.expression, expr.params);
        envmnt.define(expr.name.getValue(), function);
        return function;//Returns a pointer to the function
    }
    /**
     * This evalutes most of the mathematical functions
     */
    @Override
    public Object visitBinaryExpr(Exp.BinaryNode expr){
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        try{
        switch(expr.operator.getToken()){
            case Token.PLUS:{
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                else if (left instanceof ArrayList<?> && right instanceof ArrayList<?>) {
                    Iterator<Exp> l = ((ArrayList)left).iterator();
                    Iterator<Exp> r = ((ArrayList)right).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(l.hasNext() && r.hasNext()){
                        results.add((Double)evaluate(l.next()) + (Double)evaluate(r.next()));
                    }
                    return results;
                }
                else{break;}
            }
            case Token.MINUS:{
                if (left instanceof Double && right instanceof Double) {
                    return (double)left - (double)right;
                }
                else if (left instanceof ArrayList<?> && right instanceof ArrayList<?>) {
                    Iterator<Exp> l = ((ArrayList)left).iterator();
                    Iterator<Exp> r = ((ArrayList)right).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(l.hasNext() && r.hasNext()){
                        results.add((Double)evaluate(l.next()) - (Double)evaluate(r.next()));
                    }
                    return results;
                }
                else{break;}
            }
            case Token.MULT:{
                if (left instanceof Double && right instanceof Double) {
                    return (double)left * (double)right;
                }
                else if (left instanceof ArrayList<?> && right instanceof ArrayList<?>) {
                    Iterator<Exp> l = ((ArrayList)left).iterator();
                    Iterator<Exp> r = ((ArrayList)right).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(l.hasNext() && r.hasNext()){
                        results.add((Double)evaluate(l.next()) * (Double)evaluate(r.next()));
                    }
                    return results;
                }
                else{break;}
            }
            case Token.DIV:{
                if (left instanceof Double && right instanceof Double) {
                    return (double)left / (double)right;
                }
                else if (left instanceof ArrayList<?> && right instanceof ArrayList<?>) {
                    Iterator<Exp> l = ((ArrayList)left).iterator();
                    Iterator<Exp> r = ((ArrayList)right).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(l.hasNext() && r.hasNext()){
                        results.add((Double)evaluate(l.next()) / (Double)evaluate(r.next()));
                    }
                    return results;
                }
                else{break;}
            }
            case Token.POW:{
                if (left instanceof Double && right instanceof Double) {
                    return Math.pow((Double)left, (Double)right);
                }
                else if (left instanceof ArrayList<?> && right instanceof ArrayList<?>) {
                    Iterator<Exp> l = ((ArrayList)left).iterator();
                    Iterator<Exp> r = ((ArrayList)right).iterator();
                    ArrayList<Double> results = new ArrayList<Double>();
                    while(l.hasNext() && r.hasNext()){
                        results.add( Math.pow( (Double)evaluate(l.next()), (Double)evaluate(r.next())) );
                    }
                    return results;
                }
                else{break;}
            }
            default:
                throw new EnvironmentException("Not valid operation in " + left.toString() + " " + expr.operator.getValue() + " " + right.toString());
        }}catch(Exception exc){//If something doesn't works throw this error
            throw new EnvironmentException("Not valid operation in " + left.toString() + " " + expr.operator.getValue() + " " + right.toString());
        }
        throw new EnvironmentException("Not valid operation in " + left.toString() + " " + expr.operator.getValue() + " " + right.toString());
    }
    /**
     * Evaluate an array node, simply return the List<Exp>
     */
    @Override
    public Object visitArrayNode(Exp.ArrayNode expr){
        return expr.expression;//Return the list of expression
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
