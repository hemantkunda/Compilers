package ast;

import environment.Environment;

/**
 * The Writeln class encapsulates a single Expression object. Using this 
 * object, a Writeln object can emulate a command to print to stdout. A given
 * Writeln object, when executed, will print the value of its stored 
 * Expression to the console.  
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public class Writeln extends Statement
{
    private Expression exp;
    
    /**
     * Creates a new instance of the Writeln class with the specified 
     * Expression object.
     * 
     * @param exp the expression stored by the Writeln object
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }
    
    /**
     * Prints the value of the Writeln's Expression to stdout, using the 
     * Environment object to determine the value of any detected variables.
     * 
     * @param env the Environment object that stores known variables
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }
}
