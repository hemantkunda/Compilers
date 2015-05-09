package parser;

import java.io.*;

public class Emitter
{
	private PrintWriter out;
	private int currentID;

	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		currentID = 0;
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}
	
	public void emitPush(String reg)
	{
		emit("subu $sp, $sp, 4");
		emit("sw " + reg + ", ($sp) # pushing " + reg + " to stack");
	}
	
	public void emitPop(String reg)
	{
		emit("lw " + reg + ", ($sp)");
		emit("addu $sp, $sp, 4 # popping to " + reg + " from stack");
	}
	
	public void nextLine()
	{
		emit("");
	}
	
	public int nextLabelID()
	{
		currentID++;
		return currentID;
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}
}