package ast;

import java.util.List;

import environment.Environment;

/**
 * The Block class encapsulates a List of Statements.  Using this object,
 * a Block object can emulate a Block statement by executing each Statement
 * in its list of statements.  A Block object can hold a variable number of
 * Statements.
 * @author hkunda
 * @date March 17, 2015
 */
public class Block extends Statement
{
    private List<Statement> statements;
    
    /**
     * Creates a new instance of the Block class with the specified list of
     * Statements to be evaluated.
     * @param statements the statements encapsulated by the Block command
     */
    public Block(List<Statement> statements)
    {
        this.statements = statements;
    }
    
    /**
     * Evaluates the Block object by evaluating each Statement in the list
     * of statements in order of appearance.
     * 
     * @param env the Environment object that stores known variables
     */
    public void exec(Environment env)
    {
        for (Statement s : statements)
        {
            s.exec(env);
        }
    }
}
