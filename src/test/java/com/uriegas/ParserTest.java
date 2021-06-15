package com.uriegas;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 * Test the Parser
 */
public class ParserTest {
    private Parser p;
    private Interpreter evalVisit;
    private PrintTree printVisit;
    private Exp expression;
    /**
     * Initialize everything
     */
    public ParserTest(){
        p = new Parser();
        evalVisit = new Interpreter();
        printVisit = new PrintTree();
    }
    @Test
    public void testEvaluation(){
        expression = p.parse("3*2^(4+3)");
        Object result = expression.accept(evalVisit);

        System.out.println("Expression is: " + expression.accept(printVisit) + "\nValue is: " + String.valueOf(result));
    }
    @Test
    public void testVariables(){
        expression = p.parse("3*2^(x+3)");
        Object result = expression.accept(evalVisit);
        System.out.println("Expression is: " + expression.accept(printVisit) + "\nValue is: " + String.valueOf(result));
    }
    @Test
    public void testFunctionCall(){
       // Exp tree = p.parse("x = 10+10");
       // Object result = tree.accept(evalVisitor);
        expression = p.parse("print(x)");
        Object result = expression.accept(evalVisit);
        System.out.println("Expression is: " + expression.accept(printVisit) + "\nReturn value is: " + String.valueOf(result));
        expression = p.parse("sin(10+cos(x+2)-70)");
        result = expression.accept(evalVisit);
        
        System.out.println("Expression is: " + expression.accept(printVisit) + "\nReturn value is: " + String.valueOf(result));

        assertEquals(Math.sin((10.0+Math.cos(4+2))-70), Double.valueOf((Double)result), 0.01);
    }
//    @Test
//    public void TestREAD(){
//        expression = p.parse("read('archivo1.equ')");
//        expression.accept(evalVisit);
//        //expression = p.parse("data = read(archivo1.xlsx)");
//    }
//    @Test
//    public void testSAVE(){
//        expression = p.parse("f1 = 2*x+10");
//        expression.accept(evalVisit);
//        expression = p.parse("save(f1(x), 'output.txt')");
//        expression.accept(evalVisit);
//    }
}
