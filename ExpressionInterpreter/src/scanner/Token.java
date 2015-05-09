package scanner;
/**
 * This class serves as a wrapper for the lexemes that are returned by the 
 * Scanner class.  It contains references to the lexeme itself as well as 
 * the type of Token denoted by the TokenType enum.
 * @author Hemant Kunda
 *
 */
public class Token
{
    public enum TokenType{identifier, 
                   operand, 
                   mathOperand,
                   number, 
                   whiteSpace, 
                   throwAway, 
                   end, 
                   eoL,
                   parenExp,
                   quoteExp,
                   keyword};
    
    private String lexeme;
    private TokenType type;
    
    /**
     * Creates a new instance of the Token class with the specified lexeme and
     * token type.
     * @param lex the lexeme stored by the Token
     * @param t the type of the Token
     */
    public Token(String lex, TokenType t)
    {
        lexeme = lex;
        type = t;
    }
    
    /**
     * Returns the lexeme encapsulated by the Token.
     * 
     * @return the Token's lexeme
     */
    public String getLexeme()
    {
        return lexeme;
    }
    
    /**
     * Returns the Token's type.
     * 
     * @return the type of the Token (defined by the TokenType enum)
     */
    public TokenType getType()
    {
        return type;
    }
    
    /**
     * Compares the instance Token to another Token and returns true if both
     * tokens share the same TokenType, and false otherwise.
     * 
     * @param other the Token to be compared to the instance Token
     * @return true if the other Token is the same type as the instance Token
     *         false otherwise
     */
    public boolean isSameType(Token other)
    {
        return type == other.getType();
    }
    
    /**
     * Returns a String representation of the Token, which is essentially
     * the lexeme followed by the type.
     * 
     * @return a String containing the Token's information
     */
    public String toString()
    {
        return lexeme + "\t\t" +  type;
    }
}
