package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ReadTest {
    private Parser p;
    private Interpreter eval;

    private String input;
    private double expected;

    /**
     * Load expressions from file
     */
    @Before
    public void setUp(){
        p = new Parser();
        eval = new Interpreter();
        String[] inputs = new String[]{
            "read('archivo1.equ')"
        };

        for(String s : inputs)
            p.parse(s).accept(eval);
    }

    //Inject Parameters via constructor
    public ReadTest(String input, double expected){
        this.input = input;
        this.expected = expected;
    }
    /**
     * Initializater data injector to class ReadTest
     * @return collection of objects: [input, expected]
     */
    @Parameterized.Parameters
    public static Collection<Object[]> getTestData(){
        return Arrays.asList(new Object[][]{
//            {"x = 10", "ans = x = 10"},
//            {"func f1(var, var_2) = 10*var + var_2", "func f1(var, var_2) = ((10*var)+var_2)"},
//            {"func f2(data, cachorro, x, y, a) = data + cachorro + x + y + a", "func f2(data, cachorro, x, y, a) = ((((data+cachorro)+x)+y)+a)"},
//            {"func f_3() = x + y", "func f_3() = (x+y)"}
            //{"read('src/main/resources/archivo1.equ')", "Succesfully loaded file"}
            {"f1(y)", Math.pow(10.0, 2) + 12 * Math.sin(21*32)}
        });
    }

    /**
     * Test: evalaute a function from the read file and compare with expected value
     */
    @Test
    public void testReadFunction(){//Evaluate the input and compare with expected value
        //System.out.println(p.parse(input).accept(eval) ) + '\n' + expected);
        System.out.println(p.parse(input).accept(eval));
        assertEquals(p.parse(input).accept(eval), expected);
    }
}
