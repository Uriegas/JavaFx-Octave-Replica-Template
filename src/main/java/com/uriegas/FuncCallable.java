package com.uriegas;
import java.util.*;
/**
 * Interface to create a Function
 * Define: arity = number of parameters
 *         call  = code to run when function is called
 */
public interface FuncCallable {
    public int arity();
    public Object call(Interpreter interpreter, ArrayList<Object> arguments);
}
