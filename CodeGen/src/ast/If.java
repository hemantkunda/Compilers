package ast;

import parser.Emitter;
import environment.Environment;

/**
 * The If class encapsulates a Condition object and a Statement object. Using
 * these objects, an If object emulates an if statement by executing its
 * stored Statement if its Condition evaluated to 1 (true).
 * 
 * @author hkunda
 * @date March 18, 2015
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;
    
    /**
     * Creates a new instance of the If class with the specified condition 
     * statement and the statement to execute if the condition is true.
     * 
     * @param condition the if statement's condition
     * @param statement the statement to execute if the condition is true
     */
    public If (Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;
    }
    
    /**
     * Executes the stored Statement if the If's Condition evaluates to 
     * 1 (true).
     * 
     * @param env the Environment in which the If is to be executed
     */
    public void exec(Environment env)
    {
        if (condition.eval(env) == 1)
        {
            statement.exec(env);
        }
            
    }
    
    /**
     * Compiles the If statement to MIPS code.  The condition is first compiled, and
     * the statement is compiled after that. This allows the statement to be executed
     * if the MIPS condition evaluates to false; if the MIPS condition evaluates to false,
     * the AST condition will evaluate to true.  The If statement's label is then printed
     * so that the entire block is skipped if the AST condition is found to be false.
     * 
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	String id = "IfStatement" + e.nextLabelID();
    	condition.compile(e, id);
    	statement.compile(e);
    	e.emit(id + ":");
    }
}
