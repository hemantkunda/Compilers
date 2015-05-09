package expressionTree;

/**
 * The IdentifierNode class is used by the ExpressionParser class to generate
 * abstract syntax trees. IdentifierNodes (and ConstantNodes) are used as the
 * leaves of the trees.
 * 
 * Each IdentifierNode contains a String reference to a variable name.  When
 * evaluated, the IdentifierNode returns the value stored in that variable,
 * using the provided EvalState object as a reference.
 * 
 * @author Hemant Kunda
 *
 */
public class IdentifierNode implements ExpNode
{
    private String name;
    
    /**
     * Creates a new instance of the IdentifierNode class that encapsulates 
     * a String value.
     * 
     * @param value the value stored by the IdentifierNode
     */
    public IdentifierNode(String name)
    {
        this.name = name;
    }
    
    /**
     * Evaluates the IdentifierNode by inputting the stored String into the 
     * provided EvalState object and returning the EvalState's output; the
     * EvalState object serves as a dictionary, mapping the IdentifierNode's
     * instance field (essentially a variable name) to the value that it
     * corresponds to.
     * 
     * If the EvalState does contain a key identical to the IdentifierNode's
     * stored variable name, a NullPointerException will be thrown.
     * 
     * @param state an EvalState object containing information on recognized
     * identifiers and their corresponding values
     * 
     * @return the integer value that corresponds to the String value stored
     * by the IdentifierNode
     * 
     * @throws NullPointerException if the IdentifierNode's variable is not
     * defined in the EvalState object
     */
    public int eval(EvalState state) throws NullPointerException
    {
        if (!state.isDefined(name))
        {
            throw new NullPointerException("ERROR: " + name + " is undefined.");
        }
        if (name.equals("END"))
        {
            return 0;
        }
        return state.getValue(name);
    }
    
    /**
     * Returns the String stored by the IdentifierNode.
     * 
     * @return the String encapsulated by the IdentifierNode object
     */
    public String getValue()
    {
        return name;
    }
    
    /**
     * Returns the IdentifierNode's node type, which is the identifierType 
     * constant of the expTypeT enum.
     * 
     * @return the identifierType constant of the expTypeT enum
     */
    public expTypeT getType()
    {
        return expTypeT.identifierType;
    }
    
    /**
     * Returns a String representation of the IdentifierNode that contains its 
     * type (IdentifierNode), a colon, and the value it stores.
     * 
     * @return a String containing the IdentifierNode's information
     */
    public String toString()
    {
        return "IdentifierNode: " + name;
    }
}
