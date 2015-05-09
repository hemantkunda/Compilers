package environment;

import java.util.HashMap;

/**
 * The Environment class serves as an environment for statements to be 
 * executed within.  To this end, an Environment object stores known variables
 * and their corresponding values, which can be accessed by Expressions and
 * Statements to facilitate evaluations and executions respectively.
 * 
 * @author hkunda
 * @date March 18, 2015
 */
public class Environment
{
    private HashMap<String, Integer> variableTable;
    
    /**
     * Creates a new instance of the Environment class.
     */
    public Environment()
    {
        variableTable = new HashMap<String, Integer>();
    }
    
    /**
     * Declares the specified variable with the specified value within the
     * Environment object by adding the variable and value as a key-value
     * pair to the HashMap instance field.
     * 
     * @param variable the name of the variable to be instantiated
     * @param value the value of the instantiated variable
     */
    public void setVariable(String variable, int value)
    {
        variableTable.put(variable, value);
    }
    
    /**
     * Retrieves the integer value of the specified variable. If the 
     * Environment has not encountered the specified variable (if it doesn't
     * appear in the variable table), then an IllegalArgumentException is
     * thrown, specifying the invalid variable name.
     * 
     * @param variable the name of the variable whose value is to be returned
     * @return the value stored within the specified variable
     */
    public int getVariable(String variable)
    {
        if (variableTable.containsKey(variable))
        {
            return variableTable.get(variable);
        }
        throw new IllegalArgumentException("Variable " + variable + " has not"
                + " been initialized.");
    }
}
