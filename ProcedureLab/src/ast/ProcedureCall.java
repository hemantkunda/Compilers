package ast;

import java.util.ArrayList;
import java.util.List;

import environment.Environment;

/**
 * The ProcedureCall class serves as one half of the procedure structure
 * within the AST representation of a Program.  A ProcedureCall object is 
 * created whenever a valid method invocation is detected by the Parser. 
 * Each ProcedureCall stores its name as well as a List of parameters that
 * were provided within the procedure invocation.  
 * 
 * @author hkunda
 * @date April 6, 2015
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> params;
    
    /**
     * Creates a new instance of the ProcedureCall class with the specified
     * procedure name and list of parameters.
     * 
     * @param name the name of the procedure being invoked
     * @param params the parameters relevant to the procedure
     */
    public ProcedureCall(String name, List<Expression> params)
    {
        this.name = name;
        this.params = params;
    }
    
    /**
     * Evaluates the ProcedureCall within the specified Environment.  
     * The ProcedureDeclaration is retrieved from the Environment using the
     * ProcedureCall's stored name; the two lists of parameters are then
     * compared to determine whether or not the ProcedureCall is valid. 
     * If the ProcedureCall is valid, a new Environment is created that hangs
     * off of the provided Environment.  The parameters are defined within this
     * local Environment, and the procedure's Statement is then executed within
     * the local Environment.  A variable that shares its name with the 
     * procedure is also created;  it serves as the return value of the
     * procedure.
     * 
     * @param env the Environment in which the ProcedureCall has been invoked
     * 
     * @return the value stored within the return variable - this value
     * is dependent on the exact function of the procedure
     */
    public int eval(Environment env)
    {
        ProcedureDeclaration dec = env.getProcedure(name);
        Environment local = new Environment(env);
        if (dec.getParams().size() != params.size())
        {
            throw new IllegalArgumentException("Invalid Number of parameters"
                    + "when calling Method " + name);
        }
        List<String> variableNames = dec.getParams();
        for (int i = 0; i < params.size(); i++)
        {
            String var = variableNames.get(i);
            int value = params.get(i).eval(env);
            local.declareVariable(var, value);
        }
        local.declareVariable(name, 0);
        dec.getStatement().exec(local);
        return local.getVariable(name);
    }
    
    /**
     * Returns a String representation of the ProcedureCall object. 
     * Currently only the name of the Procedure call is returned.
     * 
     * @return a String containing the ProcedureCall's name
     */
    public String toString()
    {
        return "Procedure: " + name;
    }
}
