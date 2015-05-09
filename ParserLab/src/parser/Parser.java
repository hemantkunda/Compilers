package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import scanner.*;
import scanner.Token.TokenType;

/**
 * The Parser class can parse and interpret basic Pascal statements that
 * are inputted from the user.  
 * The Parser follows the following grammar:
 * statement -> WRITELN(expr); | BEGIN whileBegin | id := expr
 * whileBegin -> END; | statement whileBegin
 * expr -> term whileExpr
 * whileExpr -> + term whileExpr | - term whileExpr | <Empty String>
 * term -> factor whileTerm
 * whileTerm -> * factor whileTerm | / factor whileTerm |
 *              mod factor whileTerm | <Empty String>
 * factor -> (expr) | - factor | num | id
 * 
 * where num represents numbers and id represents identifiers (variables).
 * 
 * Because (a) parentheses are handled in the lowest defined term, 
 * (b) multiplication, division, and modulus are handled in the next highest
 * defined term, and (c) addition and subtraction are handled in the 
 * second highest defined term, the Parser's evaluation follows the order
 * of operations defined by PEMDAS.  
 * 
 * The Parser class uses HashMaps to store variable names and their
 * corresponding values. As of 3/13/2015, variables can only store numbers.
 * 
 * @author hkunda
 * @date March 3, 2015
 */
public class Parser
{
    private Token currentToken;
    private Scanner scanner;
    private HashMap<String, Integer> variableTable;
    
    /**
     * Creates a new instance of the Parser class, using the provided Scanner
     * object to draw input.
     * 
     * @param scanner the Scanner that will provide the Parser with
     * expressions to parse and evaluate
     * @throws ScanErrorException if the Scanner encounters an error while 
     * scanning its input stream for the next Token
     */
    public Parser(Scanner scanner) throws ScanErrorException
    {
        this.scanner = scanner;
        currentToken = scanner.nextToken();
        variableTable = new HashMap<String, Integer>();
    }
    
    /**
     * Advances the Parser's input stream by one Token if the Token parameter
     * matches the currentToken stored by the Parser.  If not, an
     * IllegalArgumentException is thrown, along with the expected and found
     * lexemes, to signal input that violates the Parser's grammar.
     * 
     * @param expected the Token expected by the Parser
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the expected Token and the 
     * current Token do not match
     */
    private void eat(Token expected) throws ScanErrorException,
                                            IllegalArgumentException
    {
        if (expected.isSameType(currentToken) && scanner.hasNext())
        {
            currentToken = scanner.nextToken();
        }
        else if (!scanner.hasNext())
        {
            currentToken = new Token(".", TokenType.end);
        }
        else 
        {
            String expectedLex = expected.getLexeme();
            String foundLex = currentToken.getLexeme();
            throw new IllegalArgumentException("\nExpected " + expectedLex + 
                    "\nFound " + foundLex);
        }
    }
    
    /**
     * Parses and returns a number from the currentToken using the Integer's 
     * parseInt method.
     * 
     * @return the number stored in the current Token
     * @throws ScanErrorException if the Scanner encounters an error while 
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private int parseNumber() throws ScanErrorException, 
                                        IllegalArgumentException
    {
        int num = Integer.parseInt(currentToken.getLexeme());
        eat(currentToken);
        return num;
    }
    
    /**
     * Parses a statement and prints its output to the console.
     * There are three different types of statements: assignment statements,
     * print statements, and block statements. 
     * 
     * Assignment statements begin with an identifier followed by a ":=", 
     * which denotes assignment.  The right side of the ":=" is parsed via
     * parseExpr, and its value is stored along with the variable name 
     * (initial identifier found) in the variableTable instance field.
     * 
     * Print statements begin with the keyword "WRITELN".  parseExpr is used
     * to evaluate the expression within the WRITELN's parentheses, and this
     * value is printed to the console.
     * 
     * Block statements begin with the keyword "BEGIN" and end with the 
     * keyword "END".  Between these keywords, a variable number of 
     * statements may appear (including additional block comments).  To handle
     * this recursive definition, a while loop is used to continue parsing
     * statements until an "END" keyword is located.
     * 
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    public void parseStatement() throws ScanErrorException, 
                                        IllegalArgumentException
    {
        if (currentToken.getType() == TokenType.identifier)
        {
            String var = currentToken.getLexeme();
            eat(currentToken);
            eat(new Token(":=", TokenType.operand));
            int num = parseExpr();
            variableTable.put(var, num);
            eat(new Token(";", TokenType.eoL));
        }
        else if (currentToken.getLexeme().equals("WRITELN"))
        {    
            eat(new Token("WRITELN", TokenType.keyword));
            eat(new Token ("(", TokenType.parenExp));
            System.out.println(parseExpr());
            eat(new Token(")", TokenType.parenExp));
            eat(new Token(";", TokenType.eoL));
        }
        else if (currentToken.getType() == TokenType.end)
        {
            return;
        }
        else
        {
            eat(new Token("BEGIN", TokenType.keyword));
            String endToken = currentToken.getLexeme();
            while (!currentToken.getLexeme()
                    .equals("END") && scanner.hasNext())
            {
                parseStatement();
                endToken = currentToken.getLexeme();
            }
            eat(new Token("END", TokenType.keyword));
            eat(new Token(";", TokenType.eoL));        
        }
    }
    
    /**
     * Parses a factor and returns its value. A factor is defined to be
     * a number, identifier, a factor preceded by a negative sign, or an
     * expression contained within parentheses.  
     * 
     * If the current Token is a number, parseNumber is called to extract the
     * number stored within the current Token.
     * 
     * If the current Token is an identifier, the variableTable field is
     * used to locate the value stored within the provided variable.  If the
     * variableTable doesn't contain information on that variable, an
     * IllegalArgumentException is thrown to inform the user.
     * 
     * If the current Token is a negative sign, parseFactor advances the input
     * stream to the next Token and returns the value of parseFactor, called
     * once more, multiplied by -1 to account for the negative sign.
     * 
     * If the currentToken is an opening parenthesis, the parenthesis is 
     * eaten. parseExpr is then called to evaluate the expression within the
     * parentheses; this value is then returned after the closing parenthesis
     * is eaten.
     * 
     * @return the numerical value of the factor
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private int parseFactor() throws ScanErrorException, 
                          IllegalArgumentException
    {
        if (currentToken.getLexeme().equals("-"))
        {
            eat(new Token("-", TokenType.mathOperand));
            return -1 * parseFactor();
        }
        else if (currentToken.getLexeme().equals("("))
        {
            eat(new Token("(", TokenType.parenExp));
            int num = parseExpr();
            eat(new Token(")", TokenType.parenExp));
            return num;
        }
        else if (currentToken.getType() == TokenType.identifier)
        {
            String var = currentToken.getLexeme();
            eat(currentToken);
            if (variableTable.containsKey(var))
            {
                return variableTable.get(var);
            }
            throw new IllegalArgumentException("\nVariable " + var + " has not "
                    + " been initialized.");
        }
        return parseNumber();
    }
    
    /**
     * Parses a term and returns its value.  A term is defined to be either
     * a single factor or a factor either multiplied, divided by, or modded 
     * with another factor.  Due to this definition, a term consists of 
     * a variable number of factors linked via multiplication, division, or
     * modulus.  
     * 
     * To begin, a single factor is parsed and its value is stored. As long
     * as multiplication, division, or modulus symbols are stored in the
     * current Token, additional factors are parsed and combined with the
     * term's current value according to the symbol that has been most 
     * recently scanned. The term's value is returned once the Parser
     * finds no more multiplication, division, or modulus signs.
     *  
     * @return the numerical value of the term
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private int parseTerm() throws ScanErrorException, 
                                                     IllegalArgumentException
    {
        int value = parseFactor();
        String op = currentToken.getLexeme();
        while (op.equals("*") || op.equals("/") || op.equals("%"))
        {
            if (op.equals("*"))
            {
                eat(currentToken);
                value *= parseFactor();
            }
            else if (op.equals("%"))
            {
                eat(currentToken);
                value %= parseFactor();
            }
            else
            {
                eat(currentToken);
                value /= parseFactor();
            }
            op = currentToken.getLexeme();
        }
        return value;
    }
    
    /**
     * Parses an expression and returns its value.  An expression is defined
     * to be either a single term or a term either added to or subtracted
     * from another term.  Due to this definition, an expression consists
     * of a variable number of terms linked via addition or subtraction.
     * 
     * To begin, a single term is parsed and its value is stored. As long
     * as addition or subtraction symbols are stored in the current Token,
     * additional terms are parsed and combined with the expression's current
     * value according to the symbol that has been most recently scanned. 
     * The term's value is returned once the Parser finds no more addition
     * or subtraction signs.
     * 
     * @return the numerical value of the expression
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private int parseExpr() throws ScanErrorException, 
                                                     IllegalArgumentException
    {  
        int value = parseTerm();
        String op = currentToken.getLexeme();
        while (op.equals("+") || op.equals("-"))
        {
            if (op.equals("+"))
            {
                eat(currentToken);
                value += parseTerm();
            }
            else
            {
                eat(currentToken);
                value -= parseTerm();
            }
            op = currentToken.getLexeme();
        }
        return value;
    }
    
    /**
     * Continually prints out statements as long as the Scanner has not 
     * reached the end of the input, as denoted by a ".".
     * 
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    public void parse() throws ScanErrorException, IllegalArgumentException
    {
        while (!currentToken.getLexeme().equals("."))
        {
            parseStatement();
        }
    }
}
