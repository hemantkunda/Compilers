package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import environment.Environment;
import scanner.*;
import scanner.Token.TokenType;
import ast.*;
import ast.Number;

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
    private int currentIndex;
    
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
            currentIndex++;
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
     * Parses and returns a Number from the currentToken using the Integer's 
     * parseInt method.
     * 
     * @return the number stored in the current Token
     * @throws ScanErrorException if the Scanner encounters an error while 
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private Number parseNumber() throws ScanErrorException, 
                                        IllegalArgumentException
    {
        int num = Integer.parseInt(currentToken.getLexeme());
        eat(currentToken);
        return new Number(num);
    }
    
    /**
     * Parses and returns a statement without executing it.
     * There are five different types of statements: if statements, while
     * statements, assignment statements, print statements, and 
     * block statements. 
     * 
     * If statements begin with the "IF" keyword. After parsing the "IF", 
     * two expressions and one logic operator are parsed and stored within
     * a Condition object.  A "THEN" keyword is then parsed, followed by
     * another statement that is parsed by parseStatement.  This statement
     * and the previously parsed Condition are then returned within an If
     * object.
     * 
     * While statements begin with the "WHILE" keyword. After parsing the 
     * "WHILE", two expressions and one logic operator are parsed and stored
     * within a Condition object.  A "DO" keyword is then parsed, followed by
     * another statement that is parsed by parseStatement.  This statement
     * and the previously parsed Condition are then returned within a While
     * object.  
     * 
     * Assignment statements begin with an identifier followed by a ":=", 
     * which denotes assignment.  The right side of the ":=" is parsed via
     * parseExpr.  The identifier and expression are then returned within
     * an Assignment object.
     * 
     * Print statements begin with the keyword "WRITELN".  parseExpr is used
     * to extract the expression within the parentheses, and this expression
     * is returned within a Writeln statement.
     * 
     * Block statements begin with the keyword "BEGIN" and end with the 
     * keyword "END".  Between these keywords, a variable number of 
     * statements may appear (including additional block comments).  To handle
     * this recursive definition, a while loop is used to continue parsing
     * statements until an "END" keyword is located.  Once each statement
     * is parsed, it is added to an ArrayList of statements, which is returned
     * within a Block object once an "END" keyword is located.
     * 
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    public Statement parseStatement() throws ScanErrorException, 
                                        IllegalArgumentException
    {
        if (currentToken.getLexeme().equals("IF"))
        {
            eat(new Token("IF", TokenType.keyword));
            Expression left = parseExpr();
            String logicOp = currentToken.getLexeme();
            eat(currentToken);
            Expression right = parseExpr();
            Condition condition = new Condition(logicOp, left, right);
            eat(new Token("THEN", TokenType.keyword));
            Statement statement = parseStatement();
            return new If(condition, statement);
        }
        if (currentToken.getLexeme().equals("WHILE"))
        {
            eat(new Token("WHILE", TokenType.keyword));
            Expression left = parseExpr();
            String logicOp = currentToken.getLexeme();
            eat(currentToken);
            Expression right = parseExpr();
            Condition condition = new Condition(logicOp, left, right);
            eat(new Token("DO", TokenType.keyword));
            Statement statement = parseStatement();
            return new While(condition, statement);
        }
        if (currentToken.getType() == TokenType.identifier)
        {
            String var = currentToken.getLexeme();
            eat(currentToken);
            eat(new Token(":=", TokenType.operand));
            Expression exp = parseExpr();
            Assignment assignment = new Assignment(var, exp);
            eat(new Token(";", TokenType.eoL));
            return assignment;
        }
        else if (currentToken.getLexeme().equals("WRITELN"))
        {    
            eat(new Token("WRITELN", TokenType.keyword));
            eat(new Token ("(", TokenType.parenExp));
            Expression exp = parseExpr();
            eat(new Token(")", TokenType.parenExp));
            eat(new Token(";", TokenType.eoL));
            return new Writeln(exp);
        }
        else
        {
            ArrayList<Statement> statements = new ArrayList<Statement>();
            eat(new Token("BEGIN", TokenType.keyword));
            String endToken = currentToken.getLexeme();
            while (!endToken.equals("END") && scanner.hasNext())
            {
                statements.add(parseStatement());
                endToken = currentToken.getLexeme();
            }
            eat(new Token("END", TokenType.keyword));
            eat(new Token(";", TokenType.eoL));        
            Block block = new Block(statements);
            return block;
        }
    }
    
    /**
     * Parses a factor and returns an Expression containing its value. A 
     * factor is defined to be a number, identifier, a factor preceded by a 
     * negative sign, or an expression contained within parentheses.  
     * 
     * If the current Token is a number, parseNumber is called to extract the
     * number stored within the current Token.
     * 
     * If the current Token is an identifier, a Variable object storing
     * the current Token's lexeme is returned.
     * 
     * If the current Token is a negative sign, parseFactor advances the input
     * stream to the next Token and returns a BinOp object. The operator of
     * the BinOp sign is a minus sign and the left Expression is a 0, to 
     * represent the negation that should occur.  The right Expression is 
     * retrieved with another call to parseFactor.
     * 
     * If the currentToken is an opening parenthesis, the parenthesis is 
     * eaten. parseExpr is then called to extract the expression within the
     * parentheses; this Expression is then returned after the closing 
     * parenthesis is eaten.
     * 
     * @return an Expression representing the value of the factor
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private Expression parseFactor() throws ScanErrorException, 
                          IllegalArgumentException
    {
        if (currentToken.getLexeme().equals("-"))
        {
            eat(new Token("-", TokenType.mathOperand));
            Number zero = new Number(0);
            BinOp op = new BinOp("-", zero, parseFactor());
            return op;
        }
        else if (currentToken.getLexeme().equals("("))
        {
            eat(new Token("(", TokenType.parenExp));
            Expression exp = parseExpr();
            eat(new Token(")", TokenType.parenExp));
            return exp;
        }
        else if (currentToken.getType() == TokenType.identifier)
        {
            String var = currentToken.getLexeme();
            eat(currentToken);
            return new Variable(var);
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
     * To begin, a single factor is parsed and its value is stored within an
     * Expression object. As long as multiplication, division, or modulus 
     * symbols are stored in the current Token, additional factors are
     * layered within BinOp objects.  Once the Scanner runs out of 
     * multiplication, division, or modulus signs, the final Expression is
     * returned.
     *  
     * @return an Expression that represents the term
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private Expression parseTerm() throws ScanErrorException, 
                                                     IllegalArgumentException
    {
        Expression exp = parseFactor();
        String op = currentToken.getLexeme();
        while (op.equals("*") || op.equals("/") || op.equals("%"))
        {
            eat(currentToken);
            exp = new BinOp(op, exp, parseFactor());
            op = currentToken.getLexeme();
        }
        return exp;
    }
    
    /**
     * Parses and returns an Expression.  An expression is defined
     * to be either a single term or a term either added to or subtracted
     * from another term.  Due to this definition, an expression consists
     * of a variable number of terms linked via addition or subtraction.
     * 
     * To begin, a single term is parsed and its value is stored within an
     * Expression object. As long as addition or subtraction symbols are 
     * stored in the current Token, additional terms are layered within 
     * BinOp objects.  Once the Scanner runs out of addition or subtraction
     * signs, the final Expression is returned.
     * 
     * @return an Expression that represents the scanned expression
     * @throws ScanErrorException if the Scanner encounters an error while
     * scanning its input stream for the next Token
     * @throws IllegalArgumentException if the eat method encounters an error
     * while advancing the Parser's input stream
     */
    private Expression parseExpr() throws ScanErrorException, 
                                                     IllegalArgumentException
    {  
        Expression exp = parseTerm();
        String op = currentToken.getLexeme();
        while (op.equals("+") || op.equals("-"))
        {
            eat(currentToken);
            exp = new BinOp(op, exp, parseTerm());
            op = currentToken.getLexeme();
        }
        return exp;
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
    public void parse()
    {
        Environment env = new Environment();
        while (!currentToken.getLexeme().equals("."))
        {
            try
            {
                Statement statement = parseStatement();
                statement.exec(env);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("currentToken: " + currentToken);
                return;
            }
        }
    }
}
