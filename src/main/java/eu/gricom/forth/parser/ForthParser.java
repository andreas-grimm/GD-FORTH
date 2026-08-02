package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.memoryManager.LineNumberXRef;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.statements.*;
import eu.gricom.forth.statements.arithmetics.*;
import eu.gricom.forth.statements.comparison.*;
import eu.gricom.forth.statements.inOut.*;
import eu.gricom.forth.statements.stack.*;
import eu.gricom.forth.statements.variables.StoreStatement;
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
                // Variable Statements
                case STORE:
                    aoStatements.add(new StoreStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                // PRINT Token: print to the terminal
                case CARRIAGE_RETURN:
                    aoStatements.add(new CarriageReturnStatement(getToken(0), _iPosition));
                    _iPosition++;
                    break;

                case PRINT:
                    aoStatements.add(new PrintStatement(getToken(0), _iPosition));
                    _iPosition++;
                    break;

                case PRINT_KEEP_STACK:
                    aoStatements.add(new PrintKeepStackStatement(getToken(0), _iPosition));
                    _iPosition++;
                    break;

                // NUMBER Token: In Forth, a number is pushed into the stack. This is done in the NUMBER statement
                case NUMBER:
                    aoStatements.add(parseNumberStatement());
                    _iPosition++;
                    break;

                // Multiple Tokens: Can be one of these: +, - , *, /, MOD
                case PLUS:
                    aoStatements.add(new PlusStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case MINUS:
                    aoStatements.add(new MinusStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case MULTIPLY:
                    aoStatements.add(new MultiplyStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case DIVIDE:
                    aoStatements.add(new DivideStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case MOD:
                    aoStatements.add(new ModuloStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                // Multiple Tokens: Can be one of these: =, <, > , <>, <=, >=
                case EQUALS:
                    aoStatements.add(new EqualsStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case LESS_THAN:
                    aoStatements.add(new LessThanStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case GREATER_THAN:
                    aoStatements.add(new GreaterThanStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case NOT_EQUALS:
                    aoStatements.add(new NotEqualsStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case LESS_EQUAL:
                    aoStatements.add(new LessEqualStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case GREATER_EQUAL:
                    aoStatements.add(new GreaterEqualStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ZERO_EQUALS:
                    aoStatements.add(new ZeroEqualsStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ZERO_LESS:
                    aoStatements.add(new ZeroLessStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ZERO_GREATER:
                    aoStatements.add(new ZeroGreaterStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ZERO_NOT_EQUALS:
                    aoStatements.add(new ZeroNotEqualsStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case DUPE:
                    aoStatements.add(new DupeStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case QUESTION_DUPE:
                    aoStatements.add(new QuestionDupeStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case DROP:
                    aoStatements.add(new DropStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case TWO_DROP:
                    aoStatements.add(new TwoDropStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case SWAP:
                    aoStatements.add(new SwapStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case TWO_SWAP:
                    aoStatements.add(new TwoSwapStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case OVER:
                    aoStatements.add(new OverStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case TWO_OVER:
                    aoStatements.add(new TwoOverStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ROT:
                    aoStatements.add(new RotStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case TWO_ROT:
                    aoStatements.add(new TwoRotStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case MINUS_ROT:
                    aoStatements.add(new MinusRotStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case NIP:
                    aoStatements.add(new NipStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case TUCK:
                    aoStatements.add(new TuckStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case PICK:
                    aoStatements.add(new PickStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case ROLL:
                    aoStatements.add(new RollStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case DEPTH:
                    aoStatements.add(new DepthStatement(getToken(0).getType(), _iPosition));
                    _iPosition++;
                    break;

                case VARIABLE:
                    String strVariableName = getToken(1).getText();
                    aoStatements.add(new VariableStatement(getToken(0),_iPosition,strVariableName));
                    _iPosition = _iPosition + 2;
                    break;

                case WORD:
                    aoStatements.add(parseWordStatement());
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

    /**
     * Parse a PRINT statement.
     *
     * @return the parsed PrintStatement
     * @throws SyntaxErrorException if parsing fails
     */
    private Statement parsePrintStatement() throws SyntaxErrorException {
        return new PrintStatement(getToken(0), _iPosition);
    }

    private Statement parseWordStatement() throws SyntaxErrorException {
        Variables oVariables = new Variables();

        if (oVariables.isVariable(getToken(0).getText())) {
            return (new VariableStatement(getToken(0), _iPosition));
        }

        throw new SyntaxErrorException("Word not identified: " + getToken(0).getText());
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

