package parser;

import java.text.ParseException;
import java.util.InputMismatchException;

import expressionTree.*;
import scanner.Token;
import scanner.Token.TokenType;
import scanner.Scanner;
import scanner.ScanErrorException;

/**
 * ExpressionParser can parse and interpret arithmetic expressions by 
 * converting a String input to a stream of Tokens to an abstract syntax tree,
 * then evaluating that tree.  It also can store variable names and their 
 * current values.
 * 
 * ExpressionParser follows this grammar:
 * 
 * Expression -> Term Op Term | Expression
 * Term -> Number | Identifier| ( Expression )
 * Op -> '=' | '+' | '-' | '*' | '/' | '%'
 * 
 * With this grammar defintion, the ExpressionParser evaluates arithmetic 
 * expressions from right to left. For example, given the expression
 * 
 * 4 * 5 + 3
 * 
 * PEMDAS rules would evaluate this expression to 23, giving precedence to 
 * multiplication, while this grammar would evaluate the expression to 32,
 * giving precedence to the addition, as it is to the right of multiplication.
 * 
 * The grammar will follow the hierarchy defined by parentheses; that is, 
 * (4 * 5) + 3 will evaluate to 23 rather than 32, as the ExpressionParser
 * will recognize that the multiplication occurs first due to the parentheses.
 * 
 * @author Hemant Kunda
 *
 */
public class ExpressionParser
{
    private Scanner scanner;
    private Token currentToken;
    private EvalState state;
        
    private int numOpenParen; // Keeps track of the number of opening 
                              // parentheses that have yet to be closed
    
    /**
     * Creates a new instance of the ExpressionParser.
     */
    public ExpressionParser()
    {
        state = new EvalState();
    }
    
    /**
     * Advances the input stream by one token if there is still output 
     * available from the scanner.  If there is no more input available, 
     * currentToken is instead set to the special end of input token, with 
     * TokenType eoL (end of Line).  If the expected Token does match the 
     * type of the current Token, an InputMismatchException is thrown, 
     * indicating an invalid Token in the input stream.
     * 
     * @param expected the Token that the parser expects to find
     * @throws InputMismatchException if the expected Token does not match the
     * actual token
     * @throws ScanErrorException if the Scanner detects an error in the input
     * stream while returning the results of nextToken()
     */
    private void eat(Token expected) throws InputMismatchException, 
                                                          ScanErrorException
    {
        if (expected.isSameType(currentToken) && scanner.hasNext())
        {
            currentToken = scanner.nextToken();
        }
        else if (!scanner.hasNext())
        {
            currentToken = new Token("END", TokenType.eoL);
        }
        else throw new InputMismatchException
                                    ("Invalid Token found: " + currentToken);
    }
    
    /**
     * A method that is mutually recursive with parseTerm.  parseExp parses 
     * and returns an abstract syntax tree that represents an Expression.  An
     * Expression is recursively defined as a Term Op Expression | Term, 
     * where Op is some operator that connects the first Term and the 
     * potential second Term or Expression.  A Term is defined as either an 
     * Identifier, Number, or Expression enclosed within parentheses.
     * 
     * The first Term is retrieved via the parseTerm method.  The method then
     * checks to see if the next Token in the input stream signifies the end 
     * of the input stream; if so, then the result of parseTerm is returned.  
     * 
     * If the currentToken is a closing parenthesis, then this indicates that
     * parseExp was called by parseTerm to handle an Expression enclosed 
     * within parentheses. In this case, the variable storing the number of 
     * open parentheses is reduced by 1 (because a closing parenthesis was 
     * found, canceling one of the open parentheses).  The number of still 
     * open parentheses is then checked; if it is negative, this indicates 
     * that there are extra illegal closing parentheses, causing a 
     * ParseException to be thrown.  Otherwise, the input that has been 
     * scanned so far is perfectly legal.  The presence of the closing 
     * parenthesis indicates that the Expression within the parentheses has 
     * been parsed, so the tree scanned by parseTerm is returned.
     * 
     * If the currentToken is not a math operand (e.g. '+', '-', '*', etc.), 
     * then the method throws an InputMismatchException, as a Term can only 
     * be followed by an operand per the definition of an Expression.
     * 
     * Otherwise, a CompoundNode node is created, storing the value of the 
     * current lexeme (an operand). The Term parsed by parseTerm is stored
     * in the left branch, while the output of a recursive call to parseExp
     * is stored in the right branch. This CompoundNode is then returned as 
     * the output of parseExp.
     * 
     * The base case of this method is if the Expression parsed by parseExp
     * is equivalent to simply a Term that is equivalent to either a Number 
     * or Identifier.
     * 
     * @return an ExpNode that can be post-order processed to receive the 
     * information parsed by parseExp
     * 
     * @throws InputMismatchException if a Term is followed by a Token that 
     * is not a valid Operator
     * @throws ScanErrorException if the Scanner encounters an error in the
     * input stream while returning the results of nextToken
     * @throws ParseException if the method detects that the number of 
     * closing parentheses exceeds the number of opening parentheses.
     */
    private ExpNode parseExp() throws InputMismatchException, 
                                        ScanErrorException, ParseException
    {
        ExpNode term = parseTerm();
        if (currentToken.getType() == TokenType.eoL)
        {
            return term;
        }
        if (currentToken.getLexeme().equals(")"))
        {
            numOpenParen--;
            if (numOpenParen >= 0)
            {
                return term;
            }
            throw new ParseException("The number of detected closing "
                    + "parentheses is more than the number of detected "
                    + "opening parentheses.", numOpenParen);
        }
        else if (currentToken.getType() != TokenType.mathOperand)
        {
            throw new InputMismatchException("Invalid Token: " + 
                                                  currentToken.getLexeme());
        }
        else
        {
            CompoundNode root = new CompoundNode(term, null, 
                                            currentToken.getLexeme());
            eat(currentToken);
            ExpNode right = parseExp();
            root.setRight(right);
            return root;
        }
    }
 
    /**
     * A method that is mutually recursive with parseExp.  As a Term is 
     * defined to be a number, identifier, or an Expression enclosed within
     * parentheses, currentToken should be of the types identifier, number, 
     * or parenExp when parseTerm is called by parseExp.  If this is not the
     * case, an InputMismatchException is thrown to indicate that there is
     * invalid input within the input stream.
     * 
     * An ExpNode named node is then declared to store the Term.
     * 
     * If the current Token is an Identifier, then node is initialized to an 
     * IdentifierNode containing the lexeme stored in the current Token.
     * 
     * If the current Token is a Number, then the int stored within the Token
     * is extracted via the parseInt method of the Integer class and wrapped
     * within a ConstantNode. node is then initialized to this ConstantNode.
     * 
     * Finally, if the current Token is a parenthesis, then the method checks
     * to see if the Token is a closing parenthesis. As closing parentheses 
     * are illegal without having first detecting opening parentheses, if the
     * Token is a closing parenthesis, an InputMismatchException is thrown to 
     * indicate the invalid closing parenthesis.  Otherwise, numOpenParen is 
     * incremented by 1 to indicate that another opening parenthesis has been
     * detected.  The Token stream is advanced one, and node is initialized  
     * to the output of parseExp. 
     * 
     * The Token stream is then advanced by one, and node is returned as the
     * output of the method.
     * 
     * The base cases of this method are if the current Token is either
     * an identifier or a number.
     * 
     * @return an ExpNode that can be post-order processed to receive the 
     * information parsed by parseTerm
     *
     * @throws ScanErrorException if the Scanner encounters an error in the
     * input stream while returning the results of nextToken
     * @throws InputMismatchException if the currentToken cannot be identified
     * as a valid Term
     * @throws ParseException if parseExp detects more closing parentheses
     * than opening parentheses while parsing an Expression enclosed within
     * parentheses
     */
    private ExpNode parseTerm() throws ScanErrorException, 
                                    InputMismatchException, ParseException
    {
        ExpNode node;
        if (currentToken.getType() == TokenType.identifier)
        {
            node = new IdentifierNode(currentToken.getLexeme());
        }
        else if (currentToken.getType() == TokenType.number)
        {
            int number = Integer.parseInt(currentToken.getLexeme());
            node = new ConstantNode(number);
        }
        else if (currentToken.getType() == TokenType.parenExp)
        {
            if (Scanner.isCloseParen(currentToken.getLexeme().charAt(0)))
            {
                throw new InputMismatchException
                                           ("Invalid Token: " + currentToken);
            }
            numOpenParen++;
            eat(currentToken);
            node = parseExp();
        }
        else
        {
            throw new InputMismatchException("Invalid Token: "
                                                 + currentToken.getLexeme());
        }
        eat(currentToken);
        return node;
    }
    
    /**
     * The overarching parse method that calls parseExp to parse the given
     * input String and return an abstract syntax tree representing that 
     * input.
     * 
     * The scanner instance field is first initialized using the input, and
     * the currentToken field is initialized with the first Token in the
     * scanner's input stream.
     * 
     * The output of parseExp is stored and returned by the method.
     * 
     * Before returning the ExpNode, the method checks the numOpenParen field
     * to see if there are any opening parentheses that have not been closed
     * (extra closing parentheses are already handled in parseExp but are
     * implicitly handled here as well). If so, this indicates invalid Tokens
     * in the input, and a ParseException is thrown to notify the user of 
     * the invalid input.
     * 
     * @param input the input to be parsed by the ExpressionParser
     * 
     * @return an ExpNode that can be post-order processed to receive the 
     * information parsed by parseTerm
     * 
     * @throws ScanErrorException if the Scanner encounters an error in the
     * input stream while returning the results of nextToken
     * @throws InputMismatchException if there are invalid Tokens in the input
     * stream - see parseExp and parseTerm for more a precise definition
     * @throws ParseException if the number of opening parentheses does not
     * match the number of closing parentheses
     */
    public ExpNode parse(String input) throws ScanErrorException, 
                                      InputMismatchException, ParseException
    {
        scanner = new Scanner(input);
        currentToken = scanner.nextToken();
        ExpNode node = parseExp();
        if (numOpenParen != 0)
        {
            int offset = numOpenParen;
            numOpenParen = 0;
            throw new ParseException("The number of opening and closing "
                    + "parentheses does not match.", offset); 
        }
        return node;
    }
    
    /**
     * Returns the EvalState object stored by the ExpressionParser.
     * 
     * @return the symbol table used by the ExpressionParser as a reference
     * for variables
     */
    public EvalState evalState()
    {
        return state;
    }
    
    /**
     * In the current implementation of the grammar, the expression 
     * y = x = 5 
     * is valid. It assigns the value of 5 to the variable y, while nothing
     * happens to the variable x.  This should be illegal in the language,
     * so this method returns true if it finds an equals sign anywhere in
     * the tree.  This method should only be called in the interpret method,
     * with the right side of the tree as the input.  This will help weed
     * out expressions such as 
     * y = x = 5
     * and 
     * y + 5 = 4.
     * 
     * @param tree the tree to be tested
     * @return true if there are any equal signs used as operands in the
     * given expression tree
     *         false otherwise
     */
    private boolean hasEquals(ExpNode tree)
    {
        if (tree instanceof ConstantNode || tree instanceof IdentifierNode)
        {
            return false;
        }
        CompoundNode node = (CompoundNode)tree;
        if (node.getOperand().equals("="))
        {
            return true;
        }
        return hasEquals(node.getRight()) || hasEquals(node.getLeft());
    }
    
    /**
     * Given a String input, creates an abstract syntax tree via the parse
     * method and evaluates that tree to arrive at an integer value.  
     * There are two scenarios: either the tree generated by parse is a
     * CompoundNode, or it is not.
     * 
     * If it is a CompoundNode, then the method checks the over-arching 
     * operand to see if it is an equals sign.  As equals signs denote
     * assignment, the right side of the tree is evaluated and stored in the
     * EvalState field, with the value of the IdentifierNode on the left
     * as the corresponding Key. The value of the right tree is then returned.
     * If the node on the left is not an IdentifierNode, then an
     * InputMismatchException is thrown, as only variables are legally  
     * allowed on the left side of an equals sign.  
     * 
     * If the over-arching operand is not an equals sign, then the method
     * verifies that it is a legal operator ('+', '-', '*', '/', '%').
     * If the operand fails this test, then an ArithmeticException is thrown
     * to indicate an illegal operand character.
     * 
     * If the tree is not a CompoundNode, then the output of the tree's
     * evaluation is returned. 
     * 
     * @param input the input to be parsed and then interpreted
     * 
     * @return the integer value of the right side of the tree when evaluated
     * 
     * @throws ArithmeticException if an illegal operand is detected or 
     * CompoundNode's eval method attempts to divide by 0
     * @throws InputMismatchException (1) if a number is detected on the left
     * side of an equals sign or (2) if there are invalid Tokens in the Token 
     * stream - see parseExp and parseTerm for a more precise definition
     * @throws ScanErrorException if the Scanner encounters an error in the
     * input stream while returning the results of nextToken
     * @throws ParseException if the number of opening parentheses and the
     * number of closing parentheses do not match
     */
    public int interpret(String input) throws ArithmeticException,  
                  InputMismatchException, ScanErrorException, ParseException, 
                  IllegalArgumentException, NullPointerException
    {
        ExpNode tree = parse(input);
        int value = 0;
        if (tree instanceof CompoundNode)
        {
            CompoundNode node = (CompoundNode)tree;
            ExpNode right = node.getRight();
            if (hasEquals(right))
            {
                throw new IllegalArgumentException("Please check the "
         + "number and placement of\nthe equals signs in your expression.");
            }
            String op = node.getOperand();
            if (op.equals("="))
            {
                value = right.eval(state);
                if (!(node.getLeft() instanceof IdentifierNode))
                {
                    throw new InputMismatchException("The left hand side of"
                            + "the equation is not a variable.");
                }
                String var = ((IdentifierNode)(node.getLeft())).getValue();
                state.setValue(var, value);
                return value;
            }
            if (!(Scanner.isOperand(op.charAt(0))))
            {
                throw new ArithmeticException("Illegal operand: " + op);
            }
        }
        return tree.eval(state);
    }
    
    
}
