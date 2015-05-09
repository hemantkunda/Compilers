package expressionTree;

/**
 * The ExpNode interface defines three methods that are universal to 
 * nodes that are used to build abstract syntax trees. The interface defines
 * the eval method, which is used to evaluate the ExpNode according to how
 * it is defined; the getType method, which returns an expTypeT object defining
 * the node's type, and the toString method, which returns a String
 * representation of the ExpNode.
 * 
 * The getType method returns constants of the enum expTypeT, which is defined
 * at the end of this class. As of February 26, 2015, there are three types
 * of recognizable nodes: constantType, identifierType, and compoundType.
 * 
 * @author Hemant Kunda
 *
 */
public interface ExpNode
{
    /**
     * Evaluates the node based on the provided symbol table.  If the node
     * is a compoundType node, evaluation will require a post-order walk of 
     * the tree, starting at the root.
     * 
     * @param state the symbol table that maps symbols to their values
     * @return an int?
     */
    int eval(EvalState state);
    
    /**
     * Gets the type of the node.  The type is explicitly defined to be either
     * constantType, identifierType, or compoundType.  
     * 
     * A node that is constantType deals with constants (i.e. numbers).
     * A node that is identifierType deals with identifiers (e.g. variable
     * names or keywords).
     * A node that is compoundType contains an operator and branches down
     * to two different nodes that could be any type.
     * 
     * @return the node's type
     */
    expTypeT getType();
    
    /**
     * Returns a String representation of the node.
     * @return a String containing the node's information
     */
    String toString();
    
    enum expTypeT{constantType, identifierType, compoundType};
}
