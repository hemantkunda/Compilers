package ast;

import parser.Emitter;
import environment.Environment;

/**
 * The Variable class encapsulates a single String value.  With this value,
 * a Variable object can emulate a single undeclared variable.  The Variable's
 * eval method declares the variable by adding it to the provided Environment
 * object.
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public class Variable extends Expression
{
    private String name;
    
    /**
     * Creates a new instance of the Variable class that encapsulates a
     * single String value.
     * @param name the name of the variable that this object represents
     */
    public Variable(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the integer value stored within the variable.  The integer value
     * is accessed through the provided Environment object.
     * 
     * @param env the Environment object that stores known variables
     * 
     * @return the value stored within the variable (as determined by the
     * Environment)
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }
    
    /**
     * Returns a String representation of the Variable.
     * 
     * @return a String containing the Variable's name as well as an indication
     * of the Expression's type (in case it's ambiguous when called)
     */
    public String toString()
    {
        return "Var: " + name;
    }
    
    /**
     * Compiles the Variable to MIPS code by associating the value stored in $v0 with
     * the variable name's memory address.
     * 
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	e.emit("la $t0, var" + name);
    	e.emit("lw $v0, ($t0)");
    }
}
