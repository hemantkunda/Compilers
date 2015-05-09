package ast;

import parser.Emitter;
import environment.Environment;

/**
 * The BinOp class encapsulates a String and two Expression objects.  Using 
 * these objects, a BinOp object can emulate the mathematical combination of
 * its two expressions, whether it be addition, subtraction, multiplication,
 * etc.  The type of mathematical combination is dependent on the BinOp's
 * String.
 * @author hkunda
 * @date March 17, 2015
 *
 */
public class BinOp extends Expression
{
    private String op;
    private Expression left;
    private Expression right;
    
    /**
     * Creates a new instance of the BinOp class with the specified
     * operator and left and right expressions.
     * 
     * @param op the operand relating the left and right expressions
     * @param left the expression preceding the operand
     * @param right the expression following the operand
     */
    public BinOp(String op, Expression left, Expression right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }
    
    /**
     * Evaluates the BinOp object by first evaluating the left and right hand
     * Expressions stored by the BinOp.  These two values are then related
     * based on what the value of op is; for example, if op.equals("+"), then
     * the two values are added together.  If, instead, op is a division sign
     * "/", then the first value is divided by the second value.  In the case
     * of division, if the right hand Expression evaluates to 0, then an 
     * ArithmeticException is thrown, to avoid division by 0.
     * 
     * @param env the Environment object that stores known variables
     * 
     * @return the integer value of the BinOp
     */
    public int eval(Environment env)
    {
        int leftSide = left.eval(env);
        int rightSide = right.eval(env);
        if (op.equals("+"))
        {
            return leftSide + rightSide;
        }
        else if (op.equals("-"))
        {
            return leftSide - rightSide;
        }
        else if (op.equals("*"))
        {
            return leftSide * rightSide;
        }
        else if (op.equals("/"))
        {
            if (rightSide == 0)
            {
                throw new ArithmeticException("ERROR: DIVIDE BY ZERO");
            }
            return leftSide / rightSide;
        }
        else
        {
            return leftSide % rightSide;
        }
    }
    
    /**
     * Returns a String representation of the BinOp object.
     * 
     * @param a String containing the left side of the BinOp, the joining 
     * operation, and the right side of the BinOp, in sequential order.
     */
    public String toString()
    {
        return left + " " + op + " " + right;
    }
    
    /**
     * Compiles the BinOp object to MIPS code.  The left side is first compiled and then
     * pushed onto the stack, allowing space for the right side to be compiled.  The
     * left side is then popped off the stack into the register $t0.  The appropriate
     * operation is then called based on the value of the BinOp's op object. 
     * 
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	left.compile(e);
    	e.emitPush("$v0");
    	right.compile(e);
    	e.emitPop("$t0");
    	e.emit("# left in $t0 and right in $v0");
    	
    	if (op.equals("+"))
    	{
    		e.emit("addu $v0, $t0, $v0 # adds the values and stores in $v0");
    	}
    	else if (op.equals("-"))
    	{
    		e.emit("subu $v0, $t0, $v0 # subtracts $v0 from $t0 and stores in $v0");
    	}
    	else if (op.equals("*"))
    	{
    		e.emit("mult $t0, $v0");
    		e.emit("mflo $v0 # stores product in $v0");
    	}
    	else if (op.equals("/"))
    	{
    		e.emit("div $t0, $v0");
    		e.emit("mflo $v0 # stores quotient in $v0");
    	}
    	else if (op.equals("mod"))
    	{
    		e.emit("div $t0, $v0");
    		e.emit("mfhi $v0 # stores remainder in $v0");
    	}
    }
}
