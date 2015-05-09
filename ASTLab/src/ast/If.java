package ast;

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
    
    public void exec(Environment env)
    {
        if (condition.eval(env) == 1)
        {
            statement.exec(env);
        }
            
    }
}
