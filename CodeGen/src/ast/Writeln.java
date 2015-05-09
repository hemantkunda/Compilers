package ast;

import parser.Emitter;
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
    
    /**
     * Compiles the Writeln object to MIPS code by compiling the Writeln's Expression, 
     * storing that value in $a0, and printing it out via MIPS's syscall function.
     * A newline character is then printed to visually advance the cursor one line on the
     * console.  
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	exp.compile(e);
    	e.emit("move $a0, $v0");
    	e.emit("li $v0, 1");
    	e.emit("syscall");
    	e.emit("li $v0, 4");
    	e.emit("la $a0, nl");
    	e.emit("syscall");
    }
}
