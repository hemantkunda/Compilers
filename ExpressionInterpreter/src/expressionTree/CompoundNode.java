package expressionTree;

/**
 * The CompoundNode class is used by the ExpressionParser to build abstract 
 * syntax trees; CompoundNodes feature as the branches of the tree, leading
 * to more branches formed by other CompoundNodes or leaves formed by 
 * IdentifierNodes and ConstantNodes.
 * 
 * Each CompoundNode contains references to two other ExpNodes that branch to
 * the left and the right, as well as an operator stored in a String that 
 * relates the two sides of the trees.
 * 
 * @author Hemant Kunda
 *
 */
public class CompoundNode implements ExpNode
{
    private ExpNode left;
    private ExpNode right;
    private String op;
    
    /**
     * Creates a new instance of a CompoundNode using two ExpNodes as its
     * children to the left and right and a String containing an operand
     * to relate the two sides.
     * 
     * @param left an ExpNode containing the left side of the tree formed
     * with the CompoundNode at its root
     * @param right an ExpNode containing the right side of the tree formed
     * with the CompoundNode at its root
     * @param op the operand that defines how the CompoundNode should combine
     * the evaluation of the left and right sides of the CompoundNode
     */
    public CompoundNode(ExpNode left, ExpNode right, String op)
    {
        this.left = left;
        this.right = right;
        this.op = op;
    }
    
    /**
     * Evaluates the CompoundNode based off of the value of op. If op 
     * is not a valid arithmetic operator, then an ArithmeticException is
     * thrown to signal that the CompoundNode cannot be evaluated.
     * 
     * If op is an equals sign, then only the right side of the equation 
     * is evaluated; this value will be stored in the value of the left
     * side of the CompoundNode in the provided EvalState, provided
     * that the left side of the CompoundNode is an IdentifierNode. 
     * Assignment of variable values is handled by the interpret method in 
     * the ExpressionParser class. 
     * 
     * If op is a different valid math operator, then the left and right
     * sides are evaluated accordingly; for example, if op is "-", then
     * the eval method returns the difference of the result of the 
     * left side evaluation and the right side evaluation.  
     * 
     * A special check is needed if division occurs; if the right side of 
     * the CompoundNode evaluates to 0, then an ArithmeticException is
     * thrown, as division by 0 is illegal.
     * 
     * @param state the EvalState that stores the values of the currently 
     * initialized variables - this is passed on to the rest of the tree
     * in the event that identifiers need to be processed
     * 
     * @return the integer value of the CompoundNode as defined by the 
     * combination of the left side evaluation, right side evaluation, and
     * the operator combining the two evaluations
     * 
     * @throws ArithmeticException if the evaluation results in a division by
     * 0
     */
    public int eval(EvalState state) throws ArithmeticException
    {
        if (op.equals("="))
        {
            return right.eval(state);
        }
        else if (op.equals("-"))
        {
            return left.eval(state) - right.eval(state);
        }
        else if (op.equals("+"))
        {
            return left.eval(state) + right.eval(state); 
        }
        else if (op.equals("*"))
        {
            return left.eval(state) * right.eval(state);
        }
        else if (op.equals("/"))
        {
            int rightSide = right.eval(state);
            if (rightSide == 0)
            {
                throw new ArithmeticException("ERROR: DIVIDE BY ZERO");
            }
            return left.eval(state) / rightSide;
        }
        else if (op.equals("%"))
        {
            return left.eval(state) % right.eval(state);
        }
        else
        {
            throw new ArithmeticException("ERROR: INVALID OPERAND: " + op);
        }
    }
    
    /**
     * Sets the left side of the tree to the specified ExpNode.
     * 
     * Postcondition: getLeft() returns a reference to newLeft.
     * 
     * @param newLeft the new left branch of the CompoundNode
     */
    public void setLeft(ExpNode newLeft)
    {
        this.left = newLeft;
    }
    
    /**
     * Sets the right side of the tree to the specified ExpNode.
     * 
     * Postcondition: getRight() returns a reference to newRight.
     * 
     * @param newLeft the new right branch of the CompoundNode
     */
    public void setRight(ExpNode newRight)
    {
        this.right = newRight;
    }
    
    /**
     * Returns a reference to the left side of the node.
     * 
     * Postcondition: left has not been modified.
     * 
     * @return a reference to the left side of the node
     */
    public ExpNode getLeft()
    {
        return left;
    }
      
    /**
     * Returns a reference to the right side of the node.
     * 
     * Postcondition: right has not been modified.
     * 
     * @return a reference to the right side of the node
     */
    public ExpNode getRight()
    {
        return right;
    }
    
    /**
     * Returns the type of node (compoundType) as defined by the expTypeT
     * enum in the ExpNode interface.
     * 
     * @return the compoundType constant of the ExpTypeT enum
     */
    public expTypeT getType()
    {
        return expTypeT.compoundType;
    }

    /**
     * Returns the operand stored by the CompoundNode.
     * 
     * @return the String that represents the CompoundNode's operand
     */
    public String getOperand()
    {
        return op;
    }
    
    /**
     * Returns a String representation of the CompoundNode, including the
     * information of each side as well as the conjoining operand.
     * 
     * @return the information of the CompoundNode in the form of a String
     */
    public String toString()
    {
        return "CompoundNode(" + left.toString() + op + right.toString() +")";
    }
}
