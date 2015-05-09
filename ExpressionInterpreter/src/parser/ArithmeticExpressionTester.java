package parser;

import parser.ExpressionParser;
import scanner.ScanErrorException;
import expressionTree.*;
/**
 * Tester for the ArithmeticExpressions lab
 * Compilers and Interpreters 2014-2015
 * @author Mr. Page
 *
 */
public class ArithmeticExpressionTester
{
    EvalState state;
    ExpressionParser parser = new ExpressionParser();

    public static void main(String[] args)
    {
        ArithmeticExpressionTester tester = new ArithmeticExpressionTester();
        tester.tests();
    }
    
    public ArithmeticExpressionTester()
    {
        state = parser.evalState();
    }
    
    public void tests()
    {
        test1();
        test2();
        test3();
    }
    
    private void test1()
    {
        // test 1 - illegal operation should cause an ArithmeticException
        ExpNode test1 = new CompoundNode(
                new ConstantNode(2),
                new ConstantNode(3),
                "^");
        try
        {
            int result = test1.eval(state);
            System.out.println("test 1 failed ");
        }
        catch (ArithmeticException e)
        {
            System.out.println(e);
            System.out.println("test 1 passed");
        } 
    }
    
    private void test2()
    {
        System.out.println("test2");
        // test2 - test all arithmetic operations
        int c1 = 25;
        int c2 = 5;
        ExpNode constant1 = new ConstantNode(c1);
        ExpNode constant2 = new ConstantNode(c2);
        ExpNode test = new CompoundNode(constant1, constant2, "+");
        if(test.eval(state) != c1 + c2) System.out.println("Addition failed");
        
        test = new CompoundNode(constant1, constant2, "-");
        if(test.eval(state) != c1 - c2) System.out.println("Subtraction failed");
        
        test = new CompoundNode(constant1, constant2, "*");
        if(test.eval(state) != c1 * c2) System.out.println("Multiplication failed");
        
        test = new CompoundNode(constant1, constant2, "/");
        if(test.eval(state) != c1 / c2) System.out.println("Division failed");
        
        test = new CompoundNode(constant1, constant2, "%");
        if(test.eval(state) != c1 % c2) System.out.println("Modulus failed");
        
        try
        {
            // division by zero
            constant2 = new ConstantNode(0);
            test = new CompoundNode(constant1, constant2, "/");
            System.out.println("test failed - division by zero " + test.eval(state));
        }
        catch (ArithmeticException e)
        {
            System.out.println("should be a custom message - check code");
            System.out.println(e);
        }
        
    }
    
    public void test3()
    {
        try
        {
            System.out.println("Test 3");
            ExpNode root = parser.parse("y = 3");
            System.out.println(root.eval(state));
            root = parser.parse("x = ((5))");
            System.out.println(root.eval(state));
            
        }
        catch (Exception e)  // valid in java 7
        {
            
        }

        
        
    }
}