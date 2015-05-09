package ast;

import environment.Environment;

/**
 * The Number class serves as a wrapper for a single integer value. Numbers, 
 * along with Variables, are the most basic forms of an Expression - any 
 * Expression can be built using Numbers and Variables.
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public class Number extends Expression
{
    private int value;
    
    /**
     * Creates a new instance of the Number class that encapsulates a single
     * integer value.
     * @param value the value stored by the Number
     */
    public Number(int value)
    {
        this.value = value;
    }
    
    /**
     * Evaluates the Number by returning the number stored by the object.
     * 
     * @param env the Environment object that stores known variables
     * 
     * @return the Number's value
     */
    public int eval(Environment env)
    {
        return value;
    }
    
    /**
     * Returns a String representation of the Number.
     * 
     * @return a String containing the Number's stored integer
     */
    public String toString()
    {
        return "" + value;
    }
}
