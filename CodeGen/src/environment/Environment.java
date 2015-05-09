package environment;

import java.util.HashMap;

import ast.ProcedureCall;
import ast.ProcedureDeclaration;

/**
 * The Environment class serves as an environment for statements to be 
 * executed within.  To this end, an Environment object stores known variables
 * and their corresponding values, which can be accessed by Expressions and
 * Statements to facilitate evaluations and executions respectively.
 * Environments also store parent Environments that they hang off of.  
 * Child environments are created whenever a procedure is called within a 
 * statement or within another procedure.
 * 
 * @author hkunda
 * @date March 18, 2015
 */
public class Environment
{
    private HashMap<String, Integer> variableTable;
    private HashMap<String, ProcedureDeclaration> procedureTable;
    private Environment parent;
    
    /**
     * Creates a new instance of the Environment class.
     */
    public Environment(Environment parent)
    {
        variableTable = new HashMap<String, Integer>();
        procedureTable = new HashMap<String, ProcedureDeclaration>();
        this.parent = parent;
    }
    
    /**
     * Declares the specified variable with the specified value within the 
     * global Environment object by adding the variable and value as a 
     * key-value pair to the variableTable instance field.
     * 
     * @param variable the name of the variable to be instantiated
     * @param value the value of the instantiated variable
     */
    public void setVariable(String variable, int value)
    {
        if (variableTable.containsKey(variable) || parent == null)
        {
            variableTable.put(variable, value);
        }
        else
        {
            parent.setVariable(variable, value);
        }
    }
    
    /**
     * Declares the specified variable with the specified value within the
     * current Environment object by adding the variable and value as a 
     * key-value pair to the variableTable instance field.
     *  
     * @param variable the name of the variable to be instantiated
     * @param value the value of the instantiated variable
     */
    public void declareVariable(String variable, int value)
    {
        variableTable.put(variable, value);
    }
    
    /**
     * Retrieves the integer value of the specified variable. If the 
     * Environment has not encountered the specified variable (if it doesn't
     * appear in the variable table), then it will check all parent
     * Environments; if the method reaches the global Environment and fails
     * to find a declaration of the variable, an IllegalArgumentException is
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
        else if (parent == null)
        {
            throw new IllegalArgumentException("Variable " + variable + " has"
                    + " not been initialized.");
        }
        else
        {
            return parent.getVariable(variable);
        }
    }
    
    /**
     * Declares the specified procedure with the specified name 
     * within the global environment by adding the name and 
     * ProcedureDeclaration as a key-value pair to the procedureTable 
     * instance field.
     * 
     * @param name the name of the procedure to be defined
     * @param procedure the declaration of the procedure being defined
     */
    public void setProcedure(String name, ProcedureDeclaration procedure)
    {
        if (parent == null)
        {
            procedureTable.put(name, procedure);
        }
        else parent.setProcedure(name, procedure);
    }
    
    /**
     * Retrieves the procedure declaration with the specified name from the
     * global Environment object.  If the global Environment cannot find
     * the procedure name, an IllegalArgumentException is thrown with 
     * the invalid procedure name.
     * 
     * @param name the name of the procedure to be retrieved
     * @return the declaration of the procedure with the specified name
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent == null)
        {
            if (procedureTable.containsKey(name))
            {
                return procedureTable.get(name);
            }
            throw new IllegalArgumentException("Procedure " + name + "has not"
                    + " been declared.");
        }
        else return parent.getProcedure(name);
        
    }
}
