package parser;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import scanner.ScanErrorException;

/**
 * The ExpressionInterpreterMain allows the user to input arithmetic 
 * expressions, using the ExpressionParser class to evaluate them.  
 * Information on the grammar used by the ExpressionParser to define its
 * language can be found in the ExpressionParser class documentation.
 * 
 * @author Hemant Kunda
 *
 */
public class ExpressionInterpreterMain
{
    /**
     * Allows the user to enter arithmetic expressions.  These expressions
     * are parsed and interpreted by the ExpressionParser class, with the
     * result being printed to the console. If an error occurs during the 
     * scanning or parsing process, a suitable error message will be printed
     * to the console.
     * 
     * Once the ExpressionParser successfully interprets the input or
     * throws an error due to illegal inputs, a feedback mechanism checks to
     * see if the user wishes to continue inputting arithmetic expressions.
     * If the user enters "no", the method terminates; any other expression
     * will cause the method to loop back to the beginning.
     * 
     * @param args arguments entered from the command line
     */
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        ExpressionParser parser = new ExpressionParser();
        boolean done = false;
        while (!done)
        {
            System.out.println("Enter a line to be processed.");
            String input = in.nextLine();
            try
            {
                System.out.println("The interpreted value is: " + 
                                    parser.interpret(input));
            }
            catch (ArithmeticException a)
            {
                System.err.println(a.getMessage());
            }
            catch (ScanErrorException s)
            {
                System.err.println(s.getMessage());
            }
            catch (InputMismatchException i)
            {
                System.err.println(i.getMessage());
            }
            catch (ParseException p)
            {
                System.err.println(p.getMessage());
            }
            catch (IllegalArgumentException i)
            {
                System.err.println(i.getMessage());
            }
            catch (NullPointerException n)
            {
                System.err.println(n.getMessage());
            }
            
            System.out.println("Would you like to add more input?");
            String answer = in.nextLine();
            if (answer.equals("no"))
            {
                done = true;
            }
        }
    }
}
