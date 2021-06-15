package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Stack;

import com.uriegas.Calculator;

import org.junit.Test;

/**
 * Test for the calcuator
 * passing string and comparing with result
 */
public class CalculatorTest {
    Calculator c = new Calculator();
    Stack<Double> result = new Stack<>();
    Stack<String> input = new Stack<>();
    /**
     * Init inputs(string) and results(double)
     */
    @Test
    public void CalcTest()throws Exception{//Initialize values to evaluate
        input.push("3*5+2");
        result.push((double)3*5+2);
        input.push("3*4 + sin(2) - 2^2 + sqrt(4)");
        result.push( 3 * 4 + Math.sin(2) - Math.pow(2, 2) + Math.sqrt(4) );
        input.push("2*5+5+0-4");//Doesnt supported by ShuntingYardMethod
        result.push((double)2*5+5+(-4));
        while(!input.isEmpty())
            assertEquals(c.calculate(input.pop()), result.pop() );
        //catch(Exception e){System.out.print(e.toString());}
    }

}
