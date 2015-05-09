package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import scanner.ScanErrorException;
import scanner.Scanner;

/**
 * Tests the functionality of the Parser class using the 7 tester files
 * found on the Compilers Athena2 page.
 * 
 * @author hkunda
 * @date March 12, 2015
 */
public class ParserMain
{
    /**
     * Executes the first test method.
     * @param args arguments from the command line
     */
    public static void main(String[] args) 
    {
        ParserMain.testCompile();
    }
    
    /**
     * Loops through each parser test file and executes it.  If an exception
     * is thrown, the error and the number of the test that failed are both
     * printed to the console.
     */
    public static void test()
    {
        for (int i = 0; i < 7; i++)
        {
            try
            {
                String fileName = "parserTest" + i + ".txt";
                File input = new File(fileName);
                FileInputStream stream = new FileInputStream(input);
                Parser parser = new Parser(new Scanner(stream));
                System.out.println("Test " + i + ":");
                parser.parse();
            }
            catch (Exception e)
            {
                System.out.println("Test " + i + " failed");
                System.err.print(e + "\n");
            }
        }
    }
    
    /**
     * Uses the procedure test text file as input to a Parser and parses
     * the file.
     */
    public static void testProcedures()
    {
        try
        {
            File input = new File("procedureTest1.txt");
            FileInputStream stream = new FileInputStream(input);
            Parser parser = new Parser(new Scanner(stream));
            parser.parse();
        }
        catch (Exception e)
        {
            
        }
    }
    
    public static void testCompile()
    {
    	try
        {
            File input = new File("testfile.txt");
            FileInputStream stream = new FileInputStream(input);
            Parser parser = new Parser(new Scanner(stream));
            parser.compile();
        }
        catch (Exception e)
        {
            
        }
    }
}
