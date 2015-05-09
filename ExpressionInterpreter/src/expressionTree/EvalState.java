package expressionTree;

import java.util.HashMap;

/**
 * The EvalState class stores information on variables defined by an
 * arithmetic expression and allows modification and retrieval of these
 * definitions. The only variables that can be defined are primitive int
 * variables.  
 * 
 * @author Hemant Kunda
 *
 */
public class EvalState
{
    private HashMap<String, Integer> symbolTable; 
           
    /**
     * Creates a new instance of the EvalState class.
     */
    public EvalState()
    {
        symbolTable = new HashMap<String, Integer>();
    }
    
    /**
     * Checks to see if the HashMap contains the specified key, returning true
     * if it does and false otherwise.
     * 
     * @param var the key whose presence in the HashMap is to be verified
     * @return true if the HashMap contains a key-value pair with that key;
     *         false otherwise
     */
    public boolean isDefined(String var)
    {
        return symbolTable.containsKey(var);
    }
    
    /**
     * Gets the value corresponding to the specified key from the HashMap via
     * the HashMap's provided functionality.
     * 
     * @param var the key whose value is to be returned
     * @return the value attached to the specific key
     */
    public int getValue(String var)
    {
        return symbolTable.get(var);
    }
    
    /**
     * Given a String key and an int value, associates the value with the
     * key within the HashMap via the HashMap's provided functionality.
     * @param var the key of the key-value pair
     * @param value the value of the key-value pair
     */
    public void setValue(String var, int value)
    {
        symbolTable.put(var, value);
    }
}
