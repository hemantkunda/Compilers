package ast;

import java.util.List;

import environment.Environment;

/**
 * The Program class serves as an encapsulation of an entire program;
 * it lies at the root of an AST representation of the program.  Each Program
 * contains a list of procedures that was defined within the Program as well
 * as a single Statement that the Program is to execute after having declared
 * all of its procedures.
 * 
 * @author hkunda
 * @date April 6, 2015
 *
 */
public class Program
{
    private List<ProcedureDeclaration> procedures;
    private Statement statement;
    
    /**
     * Creates a new instance of the Program class with the given set of 
     * procedures and executable statement.
     * 
     * @param procedures the procedures defined within the program
     * @param statement the Statement that the Program executes
     */
    public Program(List<ProcedureDeclaration> procedures, Statement statement)
    {
        this.procedures = procedures;
        this.statement = statement;
    }
    
    /**
     * Executes each procedure declaration within the given Environment
     * and then executes the stored statement using any procedures stored
     * within the Environment.
     * @param env the Environment in which the Program is to be executed
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration dec : procedures)
        {
            dec.exec(env);
        }
        statement.exec(env);
    }
}
