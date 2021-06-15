package com.uriegas;

import static org.junit.Assert.assertTrue;

import java.util.*;
import org.junit.*;

public class ReadExcelTest {
    ReadFunction read;
    Environment environment;
    Environment expected;
    Interpreter interpreter;

    @Before
    public void setUp(){
        read = new ReadFunction();
        environment = new Environment();
        interpreter = new Interpreter();
        expected = interpreter.getEnv();

        List<List<Exp>> values = new ArrayList<List<Exp>>();
        values.add(new ArrayList<Exp>());
        values.add(new ArrayList<Exp>());
        values.get(0).add(new Exp.NumberNode("0.0"));
        values.get(0).add(new Exp.NumberNode("1.0"));
        values.get(0).add(new Exp.NumberNode("2.0"));
        values.get(1).add(new Exp.NumberNode("10.0"));
        values.get(1).add(new Exp.NumberNode("9.0"));
        values.get(1).add(new Exp.NumberNode("8.0"));

        expected.define("x_1", values.get(0));
        expected.define("x_2", values.get(1));
    }
    public ReadExcelTest(){
    }

    /**
     * Test load of variables into an environment
     * @throws Exception
     */
    @Test
    public void testExcel() throws Exception{
        environment = read.readFile("archivo1.xlsx", interpreter);
        assertTrue("Error", environment.equals(expected));
    }
}
