package com.spolancom;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.apache.poi.util.ArrayUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ArrayTest {
    private Parser p;
    private Interpreter eval;
    private PrintTree printer;

    private String input;
    private ArrayList<Exp> expected;

    /**
     * Load expressions from file
     */
    @Before
    public void setUp(){
        p = new Parser();
        printer = new PrintTree();
        eval = new Interpreter();
        String[] inputs = new String[]{
            "read('archivo1.equ')",
            "read('archivo1.xlsx')"
        };

        for(String s : inputs){
            p.parse(s).accept(eval);
            System.out.println(p.parse(input).accept(printer));
        }
    }

    //Inject Parameters via constructor
    public ArrayTest(String input, ArrayList<Exp> expected){
        this.input = input;
        this.expected = expected;
    }
    /**
     * Initializater data injector to class ArrayTest
     * @return collection of objects: [input, expected]
     */
    @Parameterized.Parameters
    public static Collection<Object[]> getTestData(){
        return Arrays.asList( new Object[][]{
            {"x_2 = x_1", new ArrayList<Exp>(Arrays.asList(
                new Exp.NumberNode("0.0"),
                new Exp.NumberNode("1.0"),
                new Exp.NumberNode("2.0")
            )),
            },
            {"x_2 + x_1",  new ArrayList<Exp>(Arrays.asList(
                new Exp.NumberNode("0.0"),
                new Exp.NumberNode("1.0"),
                new Exp.NumberNode("2.0")
            ))
            }}//Commented because the function doesn't exist due to not updatable interpreter
//            {"f1(x_1)", Arrays.asList(new Double[]{
//                (Math.pow(0.0, 2) + 12 * Math.sin(21*32)),
//                (Math.pow(1.0, 2) + 12 * Math.sin(21*32)),
//                (Math.pow(2.0, 2) + 12 * Math.sin(21*32))
//                })
//            }
            );
        }
    /**
     * Test: evalaute a function from the read file and compare with expected value
     */
    @Test
    public void testReadFunction(){//Evaluate the input and compare with expected value
        //System.out.println(p.parse(input).accept(eval) ) + '\n' + expected);
        System.out.println(p.parse(input).accept(eval));
        System.out.println(p.parse(input).accept(eval).toString() + '\n' + expected.toString());
    }
}
