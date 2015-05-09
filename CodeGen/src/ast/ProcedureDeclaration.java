package ast;

import java.util.List;

import environment.Environment;

/**
 * The ProcedureDeclaration class encapsulates the definition of a procedure
 * within a Program. Each ProcedureDeclaration object stores the name of the 
 * procedure, any relevant parameters, and the body of the procedure, which
 * is to be executed whenever the procedure name is found within the executable
 * portion of the Program.
 * 
 * @author hkunda
 * @date April 6, 2015
 */
public class ProcedureDeclaration
{
    private Statement statement;
    private String name;
    private List<String> params;
    
    /**
     * Creates a new instance of the ProcedureDeclaration class with the 
     * specified body, name, and parameters.
     * 
     * @param statement the body of the procedure
     * @param name the name of the procedure
     * @param params relevant arguments for the procedure
     */
    public ProcedureDeclaration(Statement statement, String name, 
                List<String> params)
    {
        this.statement = statement;
        this.name = name;
        this.params = params;
    }
    
    /**
     * Adds this ProcedureDeclaration object to the specified Environment.
     * 
     * @param env the Environment in which the procedure is to be stored
     */
    public void exec(Environment env)
    {
        env.setProcedure(name, this);
    }
    
    /**
     * Gets the executable portion of the ProcedureDeclaration.
     * 
     * @return the body of the ProcedureDeclaration
     */
    public Statement getStatement()
    {
        return statement;
    }
    
    /**
     * Returns the parameters of the procedure in the form of a List.
     * 
     * @return a List containing the names of all the procedure's parameters
     */
    public List<String> getParams()
    {
        return params;
    }
}
