package com.uriegas;

import java.util.*;
public interface FuncCallable {
    public int arity();
    public Object call(Interpreter interpreter, ArrayList<Object> arguments);
}
