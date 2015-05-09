package ast;

import java.util.List;

import parser.Emitter;
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
	private List<String> variables;
    private List<ProcedureDeclaration> procedures;
    private Statement statement;
    
    /**
     * Creates a new instance of the Program class with the given set of 
     * procedures and executable statement.
     * 
     * @param procedures the procedures defined within the program
     * @param statement the Statement that the Program executes
     */
    public Program(List<String> variables, List<ProcedureDeclaration> procedures, 
    															Statement statement)
    {
    	this.variables = variables;
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
    
    /**
     * Compiles the Program to MIPS code.
     * 
     * After the header is emitted to the file, the Statement is compiled.  The exit
     * command is then added, followed by the data section.  In the data section, a 
     * new line character is defined along with all variables declared at the beginning of
     * the Pascal code.
     * 
     * @param e the Emitter that is used to write to file
     */
    public void compile(Emitter e)
    {
    	e.emit("# Hemant Kunda");
    	e.emit("# Generated via Pascal to MIPS compiler");
    	e.emit("# Compilers 2014-2015 S2");
    	e.emit(".text");
    	e.emit(".globl main");
    	e.emit("main: ");
    	statement.compile(e);
    	e.emit("li $v0, 10");
    	e.emit("syscall");
    	e.emit(".data");
    	e.emit("nl:");
    	e.emit(".asciiz \"\\n\"");
    	for (String v : variables)
    	{
    		e.emit("var" + v + ":");
    		e.emit(".word 0");
    	}
    }
}
