package com.spolancom;

import java.util.*;
public interface FuncCallable {
    public int arity();
    public Object call(Interpreter interpreter, ArrayList<String> arguments);
}
