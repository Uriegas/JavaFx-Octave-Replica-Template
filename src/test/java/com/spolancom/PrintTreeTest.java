package com.spolancom;

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
public class PrintTreeTest{
    private Parser p;
    private PrintTree printVisit;

    private String input;
    private String expected;

    @Before
    public void setUp(){
        p = new Parser();
        printVisit = new PrintTree();
    }

    //Inject Parameters via constructor
    public PrintTreeTest(String input, String expected){
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData(){
        return Arrays.asList(new Object[][]{
            {"x = 10", "ans = x = 10"},
            {"func f1(var, var_2) = 10*var + var_2", "func f1(var, var_2) = ((10*var)+var_2)"},
            {"func f2(data, cachorro, x, y, a) = data + cachorro + x + y + a", "func f2(data, cachorro, x, y, a) = ((((data+cachorro)+x)+y)+a)"},
            {"func f_3() = x + y", "func f_3() = (x+y)"}
        });
    }

    @Test
    public void testPrinting(){//Evaluate the input and compare with expected value
        System.out.println(printVisit.print( p.parse(input) ) + '\n' + expected);
        assertEquals(printVisit.print( p.parse(input) ), expected);
    }
}