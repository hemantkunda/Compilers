package ast;

import environment.Environment;

/**
 * The Condition class encapsulates a String and two Expression objects.
 * Unlike the BinOp class, the Condition class performs a logical comparison
 * between its two Expressions.  The type of logical comparison is dependent
 * on the String.
 * 
 * @author hkunda
 * @date March 18, 2015
 */
public class Condition extends Expression
{
    private String logicOp;
    private Expression left;
    private Expression right;
    
    /**
     * Creates a new instance of the Condition class with the specified
     * left and right expressions and logic operand.
     * @param logicOp
     * @param left
     * @param right
     */
    public Condition(String logicOp, Expression left, Expression right)
    {
        this.logicOp = logicOp;
        this.left = left;
        this.right = right;
    }
    
    /**
     * Evaluates the Condition by returning either 0 or 1 depending on 
     * how the left and right Expressions compare to one another. This
     * comparison is dependant on what logicOp is; for example, if 
     * logicOp equals "<>", then eval will return 1 if the left expression
     * is not equivalent to the right expression. Because the Condition should
     * evaluate to true or false and the eval method only returns an int,
     * 1 is used to represent true and 0 to represent false.
     * 
     * @param env the Environment object that stores known variables
     * @return 1 if the Condition evaluates to true
     *         0 if the Condition evaluates to false
     */
    public int eval(Environment env)
    {
        int leftExp = left.eval(env);
        int rightExp = right.eval(env);
        int cond = 0;
        if (logicOp.equals("<>"))
        {
            if (rightExp != leftExp)
            {
                cond = 1;
            }
        }
        if (logicOp.equals("<"))
        {
            if (leftExp < rightExp)
            {
                cond = 1;
            }
        }
        if (logicOp.equals("<="))
        {
            if (leftExp <= rightExp)
            {
                cond = 1;
            }
        }
        if (logicOp.equals(">"))
        {
            if (leftExp > rightExp)
            {
                cond = 1;
            }
        }
        if (logicOp.equals(">="))
        {
            if (leftExp >= rightExp)
            {
                cond = 1;
            }
        }
        if (logicOp.equals("="))
        {
            if (leftExp == rightExp)
            {
                cond = 1;
            }
        }
        return cond;
    }
    
    /**
     * Returns a String representation of the Condition object.
     * 
     * @param a String containing the left side of the Condition, the logic 
     * operation, and the right side of the Condition, in sequential order.
     */
    public String toString()
    {
        return left + " " +  logicOp + " " + right;
    }
}

