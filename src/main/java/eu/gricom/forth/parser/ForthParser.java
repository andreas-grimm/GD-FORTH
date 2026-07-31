package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.memoryManager.LineNumberXRef;
import eu.gricom.forth.statements.*;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * This defines the Forth parser. The parser takes in a sequence of tokens
 * and generates sequence of executable classes. Forth is a light weight
 * programming language, which makes the parser simple and short. The idea is
 * to use the least number of predefined functions and build a lot of them
 * in Forth itself. They can be loaded before or during the execution.
 * In technical terms, what we have is a recursive descent parser, the
 * simplest kind to hand-write.
 * <p>
 */
public class ForthParser implements Parser {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private List<Token> _aoTokens;
    private int _iPosition;
    private final LineNumberXRef _oLineNumber = new LineNumberXRef();

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     *
     * @param aoTokens - the tokenized program
     */
    public ForthParser(final List<Token> aoTokens) {
        _aoTokens = new ArrayList<>(aoTokens);
        _iPosition = 0;
    }


    @Override
    public final List<Statement> parse() throws SyntaxErrorException {
        List<Statement> aoStatements = new ArrayList<>();
        _iPosition = 0;

        int iOrgPosition;
        int iFileId;

        _oLogger.debug("Start parsing...");
        boolean bContinue = true;

        while (bContinue && getToken(0).getType() != ForthTokenType.EOP) {
            switch (getToken(0).getType()) {
/*
                // PRAGMA Token: Change execution behaviour of the program.
                case PRAGMA:
                    int iPragmaLineNumber = _iPosition;
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [@PRAGMA] ");
                    _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
                    _iPosition++;

                    // Get start assignment, target value, and step size
                    _iPosition++;
                    String strSetting = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [STRING] " + strSetting);

                    if (getToken(0).getType() != BasicTokenType.ASSIGN_EQUAL) {
                        throw new SyntaxErrorException("Incorrect Operator: " + getToken(0).getType().toString() + " in Line ["
                                                               + getToken(0).getLine() + "]");
                    }

                    _iPosition++;
                    String strValue = consumeToken(BasicTokenType.STRING).getText();
                    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [VALUE] " + strValue);

                    aoStatements.add(new PragmaStatement(iPragmaLineNumber, strSetting, strValue));

                    _iPosition++;
                    break;
*/
                // PRINT Token: print to the terminal
                case CARRIAGE_RETURN:
                case PRINT:
                case PRINT_KEEP_STACK:
                    aoStatements.add(parsePrintStatement());
                    _iPosition++;
                    break;

                // NUMBER Token: In Forth, a number is pushed into the stack. This is done in the NUMBER statement
                case NUMBER:
                    aoStatements.add(parseNumberStatement());
                    _iPosition++;
                    break;

                // Multiple Tokens: Can be one of these: +, - , *, /, MOD
                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                case MOD:
                    aoStatements.add(parseArithmeticStatement());
                    _iPosition++;
                    break;

                // Multiple Tokens: Can be one of these: =, <, > , <>, <=, >=
                case EQUALS:
                case LESS_THAN:
                case GREATER_THAN:
                case NOT_EQUALS:
                case LESS_EQUAL:
                case GREATER_EQUAL:
                    aoStatements.add(parseComparisonsStatement());
                    _iPosition++;
                    break;

                // Multiple Tokens: Can be one of these: 0=, 0<, 0> , 0<>
                case ZERO_EQUALS:
                case ZERO_LESS:
                case ZERO_GREATER:
                case ZERO_NOT_EQUALS:
//                case LESS_EQUAL:
//                case GREATER_EQUAL:
                    aoStatements.add(parseValueComparisonStatement());
                    _iPosition++;
                    break;

                case DUPE:
                case QUESTION_DUPE:
                case DROP:
                case TWO_DROP:
                case SWAP:
                case TWO_SWAP:
                case OVER:
                case TWO_OVER:
                case ROT:
                case TWO_ROT:
                case MINUS_ROT:
                case NIP:
                case TUCK:
                case PICK:
                case ROLL:
                case DEPTH:
                    aoStatements.add(parseStackStatement());
                    _iPosition++;
                    break;

                // No Token identified, Syntax Error
                default:
                    throw new SyntaxErrorException("Incorrect Command: " + getToken(0).getLine() + ": ["
                            + getToken(0).getType() + "] <"
                            + getToken(0).getLine() + ">");
            }
        }

        Token oEmptyToken = new Token("", ForthTokenType.EMPTY_LINE, -1);
        aoStatements.add(new EmptyStatement(oEmptyToken, _iPosition++));

        for (Statement oStatement: aoStatements) {
            _oLineNumber.putStatementNumber(oStatement.getTokenNumber(), aoStatements.indexOf(oStatement));
        }

        return aoStatements;
    }

    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().

    /**
     * Gets an unconsumed token, indexing forward. Whether the index is really needed
     * - I doubt it bit I will find out later in the project. Right now, get(0)
     * will be the next token to be consumed, get(1) the one after that, etc.
     *
     * @param  iOffset How far forward in the token stream to look.
     * @return        The yet-to-be-consumed token.
     */
    public final Token getToken(final int iOffset) {

        //check whether the current position in the tokenized program is larger or equal the token size
        if (_iPosition + iOffset >= _aoTokens.size()) {
            // send an end_of_file token back - this is an unexpected EOP
            // TODO actually this is a syntax error and should throw the syntax error exception
            return new Token("", ForthTokenType.EOP, 0);
        }

        // get the requested token
        return _aoTokens.get(_iPosition + iOffset);
    }

    private Statement parseNumberStatement() throws SyntaxErrorException {
        String numberText = getToken(0).getText();
        int iNumber;
        try {
            // Try to parse as integer first
            if (numberText.contains(".")) {
                // For floating point, truncate to integer
                iNumber = (int) Double.parseDouble(numberText);
            } else {
                iNumber = Integer.parseInt(numberText);
            }
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException("Invalid number format: " + numberText);
        }

        return new NumberStatement(iNumber, _iPosition);
    }

    private Statement parseArithmeticStatement() {
        return new ArithmeticStatement(getToken(0).getType(), _iPosition);
    }

    private Statement parseComparisonsStatement() {
        return new ComparsionsStatement(getToken(0).getType(), _iPosition);
    }

    private Statement parseValueComparisonStatement() {
        return new ValueComparsionStatement(getToken(0).getType(), _iPosition);
    }

    private Statement parseStackStatement() {
        return new StackStatement(getToken(0).getType(), _iPosition);
    }

    /**
     * Parse a PRINT statement.
     *
     * @return the parsed PrintStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parsePrintStatement() throws SyntaxErrorException {
        return new PrintStatement(getToken(0), _iPosition);
    }

    /**
     * Parse an INPUT statement.
     *
     * @return the parsed InputStatement
     * @throws SyntaxErrorException if parsing fails
     */
/*
    private Statement parseInputStatement() throws SyntaxErrorException {
        _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [INPUT] ");
        _oLineNumber.putLineNumber(getToken(0).getLine(), _iPosition);
        int iInputPos = _iPosition;
        _iPosition++;
        return new InputStatement(iInputPos, consumeToken(BasicTokenType.WORD).getText());
    }
 */
}

