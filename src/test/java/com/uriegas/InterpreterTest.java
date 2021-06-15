package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * Test the correct printing of a node aka. tree
 */
@RunWith(Parameterized.class)
public class InterpreterTest{
    private Parser p;
    private Interpreter interpreter;

    private String input;
    private double expected;

    @Before
    public void setUp(){
        p = new Parser();
        interpreter = new Interpreter();

        String[] inputs = new String[]{
            "func f1(var, var2) = 2*var + var2",
            "x = 10",
            "y = 20",
//            "z = [x, y, 10, 45]",
//            "w = [10, 50, 32, 43]",
            "func gato(x, y) = sin(x) + cos(y)",
//            "gato(z,w)",
            "func perro() = sin(10) + cos(10)"
        };

        for(String s : inputs)
            p.parse(s).accept(interpreter);
    }

    //Inject Parameters via constructor
    public InterpreterTest(String input, double expected){
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData(){
        return Arrays.asList(new Object[][]{
            {"f1(x, y)", 2*10+20},
            { "gato(5, 3)", (Math.sin(5.0) + Math.cos(3.0)) },
            {"perro()", Math.sin(10) + Math.cos(10)}
        });
    }

    @Test
    public void testInterpreter(){//Evaluate the input and compare with expected value
        System.out.println(p.parse(input).accept(interpreter).toString()+ '\n' + expected);
        assertEquals(p.parse(input).accept(interpreter), expected);
    }
}