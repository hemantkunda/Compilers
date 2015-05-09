package ast;

import parser.Emitter;
import environment.Environment;

/**
 * The While class encapsulates a Condition and Statement object. Using these
 * two objects, a While object can emulate a while statement, executing its
 * Statement as long as its Condition evaluates to true (1).
 * 
 * @author hkunda
 * @date March 18, 2015
 */
public class While extends Statement
{
    private Condition condition;
    private Statement statement;
    
    /**
     * Creates a new instance of the While class with the specified condition 
     * statement and the statement to execute while the condition is true.
     * 
     * @param condition the while statement's condition
     * @param statement the statement to execute while the condition is true
     */
    public While (Condition condition, Statement statement)
    {
        this.condition = condition;
        this.statement = statement;
    }
    
    /**
     * Continually executes the While object's stored statement as long as
     * the While's condition evaluates to 1. The condition is evaluated after
     * each execution of the statement in case the statement causes the 
     * condition to change its value.
     * 
     * @param env the Environment object that stores known variables
     */
    public void exec(Environment env)
    {
        int cond = condition.eval(env);
        while (cond == 1)
        {
            statement.exec(env);
            cond = condition.eval(env);
        }
    }
    
    /**
     * Compiles the While statement to MIPS by creating a label that signifies the while
     * statement and compiling the Condition (with its own exit label) followed by the 
     * executed Statement. A line to jump back to the beginning is then emitted, followed
     * by a label call to exit the loop structure.  
     *
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	int labelID = e.nextLabelID();
    	String id = "WhileStatement" + labelID;
    	String end = "endWhile" + labelID;
    	e.emit(id + ":");
    	condition.compile(e, end);
    	statement.compile(e);
    	e.emit("j " + id);
    	e.emit(end + ":");
    }
    
}
