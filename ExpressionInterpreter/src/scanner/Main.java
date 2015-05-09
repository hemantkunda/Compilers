package scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Main class used to test the Scanner class.
 * @author Hemant Kunda
 *
 */

public class Main
{
    /**
     * Advances through the Scanner's input stream and prints out each token.  
     * The file that the Scanner draws input from must be specified in the
     * method.
     * 
     * @param args arguments passed from the command line 
     * @throws FileNotFoundException if the file that the Scanner uses for
     *         input cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        //File input = new File("ScannerTest.txt");
        //FileInputStream stream = new FileInputStream(input);
        Scanner scanner = new Scanner("((((5 * 4) + 3)))");
        scanner.scan();
    }
}
