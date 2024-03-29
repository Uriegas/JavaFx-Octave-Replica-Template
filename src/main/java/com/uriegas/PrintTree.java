package com.uriegas;
/**
 * Visitor to the Exp classes that instead of perform
 * operations performs printing of AST's.
 * It is used only to visually verify correctness of parsing
 */
public class PrintTree implements Exp.Visitor<String> {
    String print(Exp expr) {
        return expr.accept(this);
    }
    /**
     * Add the value and expression to the environment
     * map<value, expression>
     */
    @Override
    public String visitAssignExpr(Exp.AssignNode expr){
        return expr.name + " = " + expr.value.accept(this);
    }
    /**
     * Search for the function in the environment
     * and execute it expression
     */
    @Override
    public String visitCallExpr(Exp.CallNode expr){
        String s = "";
        s += expr.name + "(";
        for(Exp arg : expr.arguments)
            s += arg.accept(this) + ", ";
        s = s.substring(0, s.length()-1);
        s = s.substring(0, s.length()-1);
        return s += ")";
    }
    /**
     * Evaluate inner expression an return result
     */
    @Override
    public String visitGroupingExpr(Exp.GroupingNode expr){
        return "(" + expr.expression.accept(this) + ")";
    }
    /**
     * Just get the number inside this node
     */
    @Override
    public String visitNumberExpr(Exp.NumberNode e){
        return (String)e.value;
    }
    @Override
    public String visitVariableExpr(Exp.Variable expr){
        return expr.name;//Get the name of the token
        //return lookupVariable(expr.name, expr);
    }
    /**
     * This evalutes most of the mathematical functions
     */
    @Override
    public String visitBinaryExpr(Exp.BinaryNode expr){
        return "(" + expr.left.accept(this) + expr.operator.getValue() + expr.right.accept(this) + ")";
    }
    /**
     * Get the name of the current file
     */
    @Override
    public String visitFileExpr(Exp.FileNode expr){
        return "'" + expr.name + "'";//Get the name of the token
    }
    /**
     * Evaluate a function declaration
     */
    @Override
    public String visitFunctionExpr(Exp.FunctionNode expr){
        String s = "func " + expr.name.getValue() + "(";
        if(!expr.params.isEmpty()){
        for(Token t : expr.params)
            s += t.getValue() + ", ";
        s = s.substring(0, s.lastIndexOf(","));
        }
        s += ") = " + expr.expression.accept(this);
        return s;
    }
    /**
     * Evaluate an array node, simply return the List<Exp>
     */
    @Override
    public String visitArrayNode(Exp.ArrayNode expr){
        String s = "[ ";
        if(!expr.expression.isEmpty()){
            for(Exp t : expr.expression)
                s += t.accept(this) + ", ";
            s = s.substring(0, s.lastIndexOf(","));
        }
        s += ']';
        return s;
    }
}
