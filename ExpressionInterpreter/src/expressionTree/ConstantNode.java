package expressionTree;

import expressionTree.ExpNode.expTypeT;

/**
 * The ConstantNode class is used by the ExpressionParser class to generate
 * abstract syntax trees; ConstantNodes (and IdentifierNodes) are used as
 * the leaves of the trees.
 * 
 * Each ConstantNode stores a single int value that it returns when evaluated.
 * @author Hemant Kunda
 *
 */
public class ConstantNode implements ExpNode
{
    private int value;
    
    /**
     * Creates a new instance of the ConstantNode class that encapsulates 
     * an integer value.
     * 
     * @param value the value stored by the ConstantNode
     */
    public ConstantNode(int value)
    {
        this.value = value;
    }
    
    /**
     * Evaluates the given node by returning the value it stores.
     * 
     * @param state an EvalState object containing information on recognized
     * identifiers and their corresponding values
     * 
     * @return the integer value stored by the ConstantNode
     */
    public int eval(EvalState state)
    {
        return value;
    }
    
    /**
     * Returns the ConstantNode's node type, which is the constantType constant
     * of the expTypeT enum.
     * 
     * @return the constantType constant of the expTypeT enum
     */
    public expTypeT getType()
    {
        return expTypeT.constantType;
    }
    
    /**
     * Returns a String representation of the ConstantNode that contains its 
     * type (ConstantNode), a colon, and the value it stores.
     * 
     * @return a String containing the ConstantNode's information
     */
    public String toString()
    {
        return "ConstantNode: " + value;
    }
}
