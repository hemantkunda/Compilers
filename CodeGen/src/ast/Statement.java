package ast;
import parser.Emitter;
import environment.Environment;

/**
 * The Statement class generalizes a single full command. Any subclass must
 * contain an exec method, which executes the specific form of Statement
 * that the subclass represents.
 * 
 * @author hkunda
 * @date March 17, 2015
 */
public abstract class Statement
{
    /**
     * Executes the Statement based on the provided Environment object.
     *
     * @param env the Environment object that stores known variables
     */
    public abstract void exec(Environment env);
    
    /**
     * Compiles the Statement to MIPS and prints it to a file via the provided
     * Emitter object.
     * 
     * @param e the Emitter that is used to write to file
     */
    public abstract void compile(Emitter e);
}
