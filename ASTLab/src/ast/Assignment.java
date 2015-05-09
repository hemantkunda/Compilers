package ast;

import environment.Environment;

/**
 * The Assignment class encapsulates a String object and an Expression object.
 * Using these two objects, an Assignment object can emulate a variable
 * declaration statement by storing within an Environment variable a 
 * variable name and its corresponding value.
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;
    
    /**
     * Creates a new instance of the Assignment object that stores a variable
     * name and the unsimplified expression to be stored in that variable.
     * 
     * @param var the name of the variable
     * @param exp the expression that, when evaluated, yields the value that
     * should be stored in var
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }
    
    /**
     * Executes the assignment statement by associating the integer value
     * stored by the Expression and the variable name stored by the 
     * Assignment object.  This association is created in the form of a
     * key, pair entry in the Environment object's variableTable.
     * 
     * @param env env the Environment object that stores known variables 
     */
    public void exec(Environment env)
    {
        int value = exp.eval(env);
        env.setVariable(var, value);
    }
}
