package ast;

import environment.Environment;

/**
 * The Expression class generalizes algebraic expressions.  Any subclass must
 * implement the eval method, which should evaluate the expression it 
 * represents with the assistance of the provided Environment object.
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public abstract class Expression
{
    /**
     * Returns the value stored by the Expression.
     * 
     * @param env the Environment object that stores known variables
     * 
     * @return the value of the Expression when evaluated according to PEMDAS
     */
    public abstract int eval(Environment env);
    
    /**
     * Returns a String representation of the Expression.
     * 
     * @param a String containing the Expression's stored information
     */
    public abstract String toString();
}
