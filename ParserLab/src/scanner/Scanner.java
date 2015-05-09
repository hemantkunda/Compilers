package scanner;


import java.io.*;

import scanner.Token.TokenType;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015).     
 *  
 * @author Hemant Kunda
 *  
 * Usage:
 * Provide an input to the Scanner, whether it be a file or a String. 
 * The Scanner can retrieve individual lexemes from the input in order of 
 * their appearance in the input stream, though it currently (1/25/2015) 
 * cannot classify them in terms of different types of tokens.  
 * 
 * 
 * Several regular expressions are defined in short hand:
 * C: [1...9]
 * D: [0...9]
 * B: [!, <, >]
 * L: [A...Z a...z]
 * O: [=, +, -, /, *, %, ;]
 * E: [(, ', "]
 * W: [\t, \r, \n, ' ']
 * 
 * These short hand definitions will appear in the method descriptions.
 */

public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  It sets the end-of-file 
     * (eof) flag and then reads the first character of the input file into 
     * the instance field currentChar.
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);   
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file (eof) flag and 
     * then reads the first character of the input string into the instance 
     * field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar
     * Takes in the next character from the input stream.  This character is 
     * read as an integer;  if the integer is negative, then the stream has 
     * reached the end of the file, so the eof flag is set to true to indicate
     * that.  Otherwise, the integer is converted to a char via ASCII and 
     * stored in the currentChar variable.
     * 
     * An IOException can be caught in the event that the BufferedReader 
     * was incorrectly initialized.
     */
    private void getNextChar()
    {
        try
        {
            int next = in.read();
            if (next == -1)
            {
                eof = true;
            }
            else
            {
                currentChar = (char)next;
            }

        } //end try
        catch (IOException e)
        {
            System.err.println("The input was incorrectly initialized.");
        }
        return;
    }
    
    /**
     * Method: eat
     * 
     * Advances the input stream (via the getNextChar method) provided that 
     * the current character matches the expected character provided to this
     * method.  If not, a ScanErrorException is thrown.
     * 
     * @param expected the char expected by the Scanner
     * @throws ScanErrorException if the current character does not match 
     * the expected character
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Illegal character: expected " + 
                    currentChar + " and received " + expected);
        }
        return;
    }

    /**
     * Method: hasNext
     * Checks to see if the BufferedReader has a valid next character to 
     * return.
     * 
     * @return true if the BufferedReader has not reached the end of the file
     *         false otherwise
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Method: isDigit
     * Checks to see if the specified character is a digit, returning true 
     * if it is and false otherwise.  The method checks the character's ASCII 
     * value to see if it lies between 48 and 57 inclusive.
     * 
     * @param numTest the character to be tested
     * @return true if the character is a digit (0-9)
     *         false otherwise
     */
    public static boolean isDigit(char numTest)
    {
        return 48 <= numTest && numTest <= 57;
    }

    /**
     * Method: isPosDigit
     * Checks to see if the specified character is a positive digit, returning 
     * true if it is and false otherwise.  The method checks the character's
     * ASCII value to see if it lies between 49 and 57 inclusive.  The key 
     * difference between this method and isDigit is that this method will
     * not recognize 0 as valid.
     * 
     * @param numTest the character to be tested
     * @return true if the character is a positive digit (1-9)
     *         false otherwise
     */
    public static boolean isPosDigit(char numTest)
    {
        return 49 <= numTest && numTest <= 57;
    }


    /**
     * Method: isLetter
     * Checks to see if the specified character is a letter, returning true if
     * it is and false otherwise. The method checks the character's ASCII 
     * value to see if it lies between 65 and 90 inclusive or 97 and 122 
     * inclusive.
     * 
     * @param letTest the character to be tested
     * @return true if the character is a letter (A-Z || a-z)
     *         false otherwise
     */
    public static boolean isLetter(char letTest)
    {
        return (65 <= letTest && letTest <= 90) || 
               (97 <= letTest && letTest <= 122);
    }

    /**
     * Method: isWhiteSpace
     * Checks to see if the specified character is some sort of whitespace 
     * character, returning true if it is and false otherwise. A whitespace 
     * character is defined as one of the following: ' ', '\t', '\r', '\n'.
     * 
     * @param spaceTest the character to be tested
     * @return true if the character is a whitespace character 
     *         false otherwise
     */
    public static boolean isWhiteSpace(char spaceTest)
    {
        return (spaceTest == ' ' || spaceTest == '\t' || spaceTest == '\r' ||
                spaceTest == '\n');
    }

    /**
     * Method: isNewLine
     * Checks to see if the specified character denotes a new line, 
     * returning true if it does and false otherwise. A new line 
     * character is defined as one of the following: '\r', '\n'.
     * 
     * @param spaceTest the character to be tested
     * @return true if the character specifies a new line 
     *         false otherwise
     */
    public static boolean isNewLine(char spaceTest)
    {
        return (spaceTest == '\r' || spaceTest == '\n');
    }

    /**
     * Method: isOperand
     * Checks to see if the specified character is an operand, returning true
     * if it is and false otherwise.  An operand is defined as one of the
     * following: '=', '+', '-', '*', '/', '%', ';'.
     * 
     * @param opTest the character to be tested
     * @return true if the character is an operand
     *         false otherwise
     */
    public static boolean isOperand(char opTest)
    {
        return (isEqualSign(opTest) || isMathOperator(opTest) || 
                isLogicOperator(opTest) || isParen(opTest) ||
                isQuote(opTest) || opTest == ';' || opTest == ':' ||
                opTest == '.');
    }
    
    /**
     * Method: isLogicOperator
     * Checks to see if the specified character is a logic operator, returning
     * true if it is and false otherwise.  A logic operator can be described
     * by the regular expression B.
     * 
     * @param logTest the character to be tested
     * @return true if the character is a logic operator
     *         false otherwise
     */
    public static boolean isLogicOperator(char logTest)
    {
        return (logTest == '!' || logTest == '<' || logTest == '>');
    }

    /**
     * Method: isEqualSign
     * Checks to see if the specified character is an equals sign, 
     * returning true if it is and false otherwise.
     * 
     * @param opTest the character to be tested
     * @return true if the character is an equals sign
     *         false otherwise
     */
    public static boolean isEqualSign(char opTest)
    {
        return opTest == '=';
    }

    /**
     * Method: isMathOperator
     * Checks to see if the specified character is a math operator, returning 
     * true if it is and false otherwise.  A math operator is defined as one
     * of the following: '+', '-', '*', '/', '%'.
     * 
     * @param opTest the character to be tested
     * @return true if the character is a math operator
     *         false otherwise
     */
    public static boolean isMathOperator(char opTest)
    {
        return (opTest == '+' || opTest == '-' || opTest == '*' || 
                opTest == '/' || opTest == '%');
    }

    /**
     * Method: isOpenParen
     * Checks to see if the specified character is an opening parenthesis, 
     * returning true if it is and false otherwise.
     * 
     * @param parTest the character to be tested
     * @return true if the character is an opening parenthesis
     *         false otherwise
     */
    public static boolean isOpenParen(char parTest)
    {
        return parTest == '(';
    }

    /**
     * Method: isCloseParen
     * Checks to see if the specified character is a closing parenthesis, 
     * returning true if it is and false otherwise.
     * 
     * @param parTest the character to be tested
     * @return true if the character is a closing parenthesis
     *         false otherwise
     */
    public static boolean isCloseParen(char parTest)
    {
        return parTest == ')';
    }
    
    public static boolean isParen(char parTest)
    {
        return isOpenParen(parTest) || isCloseParen(parTest);
    }

    /**
     * Method: isQuote
     * Checks to see if the specified character is a quotation mark, 
     * returning true if it is and false otherwise. A quotation mark is 
     * defined to be one of the following: ' or ". 
     * 
     * @param quoteTest the character to be tested
     * @return true if the character is a quotation mark
     *         false otherwise
     */
    public static boolean isQuote(char quoteTest)
    {
        return (quoteTest == '\'' || quoteTest == '\"');
    }

    /**
     * Method: scanNumber
     * Called from nextToken if the first character is a number.  
     * The method will continue scanning until it runs out of numbers; once it
     * does, the method will return the entire lexeme.  However, if it finds a 
     * character that is neither a digit nor a white space, the method will 
     * instead throw a ScanErrorException.  A number is defined by the regular 
     * expression PD*.
     * 
     * Precondition: currentChar has been recognized as a number.
     * 
     * @return a String containing a number lexeme at the head of the 
     *         input stream
     * 
     * @throws a ScanErrorException if the Scanner encounters a character that
     *         isn't a digit or a white space
     */
    private Token scanNumber() throws ScanErrorException
    {
        String num = "";
        while (isDigit(currentChar) && !eof)
        {
            num += currentChar;
            eat(currentChar);
        }
        if ((!isWhiteSpace(currentChar) && !isParen(currentChar)) && !eof && currentChar != ';')
        {
            throw new ScanErrorException
            ("Invalid character: expected a digit and found: " + currentChar);
        }
        return new Token(num, TokenType.number);
    }

    /**
     * Method: scanIdentifier
     * Called from nextToken if currentChar is a letter.  The method will
     * continue scanning until it encounters something that isn't a number or
     * a letter; once it does, the method will return the entire lexeme.  
     * However, if it finds a character that is an operand, the method will 
     * instead throw a ScanErrorException.  
     * An identifier is defined by the regular expression L(L+D)*.
     * 
     * Precondition: currentChar has been recognized as a letter.
     * 
     * @return a String containing the identifier lexeme at the head 
     *         of the input stream
     * 
     * @throws a ScanErrorException if the Scanner encounters an operand 
     *         character that's not a quote or parenthesis character
     */
    private Token scanIdentifier() throws ScanErrorException
    {
        String iden = "";
        while ((isDigit(currentChar) || isLetter(currentChar)) && !eof)
        {
            iden += currentChar;
            eat(currentChar);
        }
        if (isOperand(currentChar) && !eof && currentChar != ';' 
        && !isParen(currentChar) && !isQuote(currentChar))
        {
            throw new ScanErrorException("Invalid character: expected a digit"
                    + " or a letter and found: " + currentChar);
        }
        if (iden.equals("mod"))
        {
            return new Token("%", TokenType.operand);
        }
        if (iden.equals("WRITELN") || iden.equals("BEGIN") || 
                                                iden.equals("END"))
        {
            return new Token(iden, TokenType.keyword);
        }
        return new Token(iden, TokenType.identifier);
    }

    /**
     * Method: scanOperand
     * Called from nextToken if currentChar is an operand.  The method will
     * scan the next two characters in the input stream and make one of a few
     * decisions based on the characters. Acceptable regular expressions are
     * defined in the following paragraphs.  
     * 
     * If one character is an operand and the other is an equals
     * sign, the method will return a lexeme containing those two characters.
     * 
     * If both characters are math operators, then the characters will be 
     * checked to see if they match the beginning of a comment block.  If so, 
     * the method skipComments or skipLineComment, depending on the type of 
     * comment, will be called to advance the input stream past the comment 
     * block. Otherwise, a ScanErrorException will be called.  
     * Acceptable comment block initiators are '/' + '*' (block comment) and 
     *                                         '/' + '/' (line comment).
     * 
     * If the second character is a white space, the method will return a 
     * lexeme containing only the first character.
     * 
     * If the first character is an opening parenthesis, then the method
     * scanParen will be called to scan and return the entire lexeme enclosed
     * by the opening and closing parentheses. Any errors in scanning this 
     * lexeme are handled by the scanParen method.
     * 
     * If the first character is a quotation mark, then the method scanQuotes
     * will be called to scan and return the entire lexeme enclosed by but not 
     * including the opening and closing quotation marks.  Any errors in 
     * scanning this lexeme are handled by the scanQuotes method.
     * 
     * Precondition: currentChar has been recognized as an operand.
     * 
     * @return a String containing the operand lexeme at the head of the 
     *         input stream
     * 
     * @throws a ScanErrorException if (1) the input stream reaches the end of 
     * the file and doesn't find a semicolon; (2) skipComments fails to find 
     * an end to a comment block; (3) 2 unrecognized math operators are 
     * adjacent in the input stream; (4) the Scanner finds an equals sign 
     * followed by a logic operator; (5) the method outright fails to 
     * recognize the operand.
     */
    private Token scanOperand() throws ScanErrorException
    {
        Token operand;
        char first = currentChar;
        if (isParen(first))
        {
            String lexeme = "" + first;
            eat(first);
            return new Token(lexeme, TokenType.parenExp);
        }
        eat(first);
        if (first == '.')
        {
            eof = true;
            return new Token(".", TokenType.end);
        }
        if (eof)
        {
            if (first == ';')
            {
                return new Token(";", TokenType.eoL);
            }
            else
            {
                throw new ScanErrorException("Invalid character: " + first);
            }
        } //end if
        char second = currentChar;
        if (isMathOperator(first) && isMathOperator(second))
        {
            if (first == '/' && second == '*')
            {
                skipComments();
            }
            else if (first == '/' && second == '/')
            {
                skipLineComment();
            }
            else
            {
                throw new ScanErrorException
                ("Invalid lexeme: " + first + second);
            }
        } //end if
        if (isEqualSign(first) && isOperand(second))
        {
            if (isLogicOperator(second))
            {
                throw new ScanErrorException
                ("Invalid lexeme: " + first + second);
            }
            operand = new Token("" + first + second, TokenType.operand);
        } //end if
        else if (isEqualSign(second) && isOperand(first))
        {
            operand = new Token("" + first + second, TokenType.operand);
        }
        else if (isWhiteSpace(second))
        {
            if (first == ';')
            {
                operand = new Token(";", TokenType.eoL);
            }
            else
            {
                operand = new Token("" + first, TokenType.mathOperand);
            }
        }
        else if (isQuote(first))
        {
            operand = new Token(scanQuotes(first), TokenType.quoteExp);
        }
        else if (first == '-' && (isDigit(second) || isOpenParen(second)))
        {
            operand = new Token("" + first, TokenType.mathOperand);
        }
        else
        {
            throw new ScanErrorException("Invalid lexeme: " + first + second);
        }
        if (!(first == '-' && (isDigit(second) || isParen(second))))
        {
            eat(currentChar);
        }
        return operand;
    }
    
    private String removeParen(String input)
    {
        String spliced = "";
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (!(isParen(c)))
            {
                spliced += c;
            }
        }
        return spliced;
    }

    /**
     * Method: skipComments
     * This method is called by scanOperand if the beginnings of a block 
     * comment are detected. The method scans each character until it 
     * encounters a '*'.
     * If it encounters a '*' and the next character is a '/', the input stream 
     * is advanced to one position past the '/' before the method returns. 
     * If the method reaches the end of the input stream before finding the end
     * of the comment block, the input stream is closed and the method throws 
     * a ScanErrorException.
     * 
     * Precondition: The input stream is currently at a position such that
     * currentChar = '*' and currentChar's previous value was '/'. 
     * 
     * PostCondition: The input stream has skipped over the entire comment 
     * block and is one position past the end of the comment block.
     * 
     * @throws a ScanErrorException if the method reaches the end of the input
     * stream without locating the end of the comment block, or if the Scanner
     * is caused to eat the wrong character.
     */
    private void skipComments() throws ScanErrorException
    {
        while (true)
        {
            if (eof)
            {
                throw new ScanErrorException("Missing End to Comment Block.");
            }
            if (currentChar == '*')
            {
                eat(currentChar);
                if (currentChar == '/')
                {
                    eat(currentChar);
                    return;
                }
            } //end if
            else
            {
                eat(currentChar);
            }
        } //end while
    }

    /**
     * Method: skipLineComment
     * This method is called by scanOperand if a line comment is detected. The 
     * method continues scanning until it encounters a new line character, 
     * which must be used to end line comments. Once it finds a new line 
     * character, it advances the input stream by one more character before 
     * returning.  Alternatively, if the method reaches the end of the input 
     * stream before a new line character is located, the method simply 
     * returns.
     * 
     * Precondition: The input stream is currently at a position such that 
     * currentChar's current value and previous value are both '/'.  
     * 
     * Postcondition: The input stream is either positioned on the new line 
     * character that denotes the end of the line comment or is at the end of
     * the input.
     */
    private void skipLineComment()
    {
        try
        {
            while (true)
            {
                if (isNewLine(currentChar) || eof)
                {
                    return;
                }
                else
                {
                    eat(currentChar);
                }
            } // end while
        } // end try
        catch (ScanErrorException e)
        {
            return;
        }
    }

    /**
     * Method: scanParen
     * This method is called by scanOperand if an open parenthesis is found. 
     * Each character in the input stream is accumulated until the closing 
     * parenthesis is encountered; once the closing parenthesis is found, 
     * the entire String, from open parenthesis to closing parenthesis, is
     * returned.
     * 
     * Precondition: The input stream is at a position such that it previously
     * equaled '('.
     * 
     * Postcondition: The input stream is at a position such that it is one 
     * position past the end of the parenthesis block.
     * 
     * @return a String containing the characters enclosed by and including
     * the opening and closing parentheses
     * 
     * @throws a ScanExceptionError if the Scanner is caused to eat the wrong
     * character, or if the Scanner fails to find the end of the parentheses
     * block.
     */

    private String scanParen() throws ScanErrorException
    {
        String lex =  "" + currentChar;
        while (!isCloseParen(currentChar) && !eof)
        {
            eat(currentChar);
            if (isOpenParen(currentChar))
            {
                lex += scanParen() + " ";
            }
            else
            {
                lex += currentChar;
            }
        }
        if (eof)
        {
            throw new ScanErrorException("Missing end to parentheses block.");
        }
        eat(currentChar);
        return lex.substring(0);
    }

    /**
     * Method: scanQuotes
     * This method is called by scanQuotes if a quotation mark is found.  Each 
     * character in the input stream is accumulated until the closing quotation
     * mark is located;  once the closing quotation mark is found, the entire 
     * String is returned, but the quotation marks are omitted.
     * 
     * Precondition: (1) openingQuote is in E and != '('; (2) the input stream
     * is at a position such that it is one past the opening quotation mark
     * 
     * @return a String containing the characters enclosed by but not including
     * the opening and closing parentheses
     * 
     * @throws a ScanExceptionError if (1) the Scanner is caused to eat the 
     * wrong character; (2) the Scanner fails to find the end of the comment 
     * block; (3) the opening quotation mark is different from the closing 
     * quotation mark
     */
    private String scanQuotes(char openingQuote) throws ScanErrorException
    {
        String lex = "" + currentChar;
        while (!isQuote(currentChar) && !eof)
        {
            eat(currentChar);
            lex += currentChar;
        }
        if (eof)
        {
            throw new ScanErrorException("Missing end to quotation block.");
        }
        if (openingQuote != currentChar)
        {
            throw new ScanErrorException("Invalid character: expected " + 
                    openingQuote + " and received " + 
                    currentChar);
        }
        return lex.substring(0, lex.length() - 1);
    }

    /**
     * Method: nextToken
     * This method returns the next token in the input stream by calling 
     * and returning the output of either scanNumber, scanIdentifier, or 
     * scanOperand. The method called is dependent on the type of character 
     * that currentChar is. If currentChar is initially a whitespace character, 
     * the method continues to advance the input stream until currentChar is no
     * longer a whitespace character.  While advancing through the white space, 
     * the method continually checks to see if it's reached the end of the 
     * input stream, returning "END" if it has.
     * 
     * @return a String object containing the next token in the input stream
     * b
     * @throws a ScanErrorException if (1) the Scanner is caused to eat the
     * wrong character; (2) scanOperand encounters a problem (see the comments
     * for scanOperand for more information); (3) the Scanner encounters a 
     * character that it cannot classify as either a positive digit, a letter,
     * or an operand.  
     */
    public Token nextToken() throws ScanErrorException
    {
        Token token;
        while (isWhiteSpace(currentChar))
        {
            eat(currentChar);
            if (eof)
            {
                break;
            }
        } // end while
        if (eof)
        {
            return new Token("END", TokenType.end);
        }
        if (isDigit(currentChar))
        {
            token = scanNumber();
        }
        else if (isLetter(currentChar))
        {
            token = scanIdentifier();
        }
        else if (isOperand(currentChar))
        {
            token = scanOperand();
        }
        else
        {
            throw new ScanErrorException
            ("Invalid start to lexeme: " + currentChar);
        }
        return token;
    }      
    
    /**
     * Method: advanceToNextToken
     * Called if the scan method catches an error thrown by nextToken. This 
     * would occur if the Scanner notices a character that is not recognizable.
     * In this scenario, the input stream is advanced until it reaches a white
     * space character, signifying the end of the faulty token. 
     * 
     * Precondition: currentChar is not in the regular language recognized 
     *               by the Scanner
     */
    private void advanceToNextToken()
    {
        try
        {
            while (!isWhiteSpace(currentChar))
            {
                if (eof)
                {
                    return;
                }
                eat(currentChar);
            }
        }
        catch (ScanErrorException e)
        {
            return;
        }
    }
    
    /**
     * Method: scan
     * Continually prints out the next token in the input stream until the 
     * Scanner reaches the end of the input.  In the event that a 
     * ScanErrorException is thrown by nextToken, due to an unrecognized 
     * character, the error is printed out and the input stream is advanced 
     * to the next token via the advanceToNextToken method.
     */
    public void scan()
    {
        while (!eof)
        {
            try
            {
                System.out.println(nextToken());
            }
            catch (ScanErrorException e)
            {
                System.err.println(e);
                advanceToNextToken();
            }
        }
    }    
}