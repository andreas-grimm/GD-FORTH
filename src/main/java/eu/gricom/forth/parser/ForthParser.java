package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.memoryManager.LineNumberXRef;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.statements.*;
import eu.gricom.forth.statements.arithmetics.*;
import eu.gricom.forth.statements.comparison.*;
import eu.gricom.forth.statements.controlFlow.IfStatement;
import eu.gricom.forth.statements.controlFlow.DoStatement;
import eu.gricom.forth.statements.controlFlow.BeginStatement;
import eu.gricom.forth.statements.controlFlow.BeginUntilStatement;
import eu.gricom.forth.statements.controlFlow.BeginAgainStatement;
import eu.gricom.forth.statements.controlFlow.CurrentLoopIndexStatement;
import eu.gricom.forth.statements.controlFlow.OuterLoopIndexStatement;
import eu.gricom.forth.statements.controlFlow.LoopStatement;
import eu.gricom.forth.statements.controlFlow.PlusLoopStatement;
import eu.gricom.forth.statements.controlFlow.UnloopStatement;
import eu.gricom.forth.statements.controlFlow.LeaveStatement;
import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.error.MissingLoopException;
import eu.gricom.forth.statements.inOut.*;
import eu.gricom.forth.statements.mathematics.*;
import eu.gricom.forth.statements.stack.*;
import eu.gricom.forth.statements.variables.FetchStatement;
import eu.gricom.forth.statements.variables.StoreStatement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ForthParser - Recursive descent parser for the FORTH language.
 * <p>
 * The ForthParser takes a sequence of tokens and generates a sequence of executable
 * Statement objects. FORTH is a lightweight programming language, so the parser is simple
 * and short. The design philosophy is to use the least number of predefined functions
 * and build a lot of them in FORTH itself, which can be loaded before or during execution.
 * <p>
 * Variable Handling Architecture:
 * The parser uses a dual-class design for variable operations:
 * - VARIABLE token (definition): Creates VariableStatement(token, position, variableName)
 *   This defines a new variable and initializes it to "empty" in the Variables storage.
 * - WORD token (access): Creates WordStatement(token, position)
 *   This accesses a previously defined variable by looking up its index and pushing to stack.
 * <p>
 * Both VariableStatement and WordStatement interact with the shared Variables storage
 * (static dual-storage architecture using _astrVariableName list and _aoVariable map).
 * <p>
 * Token Processing:
 * The parser processes tokens in a switch statement (recursive descent style):
 * - Arithmetic operators: +, -, *, /, MOD
 * - Comparison operators: =, <>, <, >, <=, >=, 0=, 0<>, 0<, 0>
 * - Stack operations: DUP, DROP, SWAP, OVER, ROT, NIP, TUCK, PICK, ROLL, DEPTH
 * - I/O operations: PRINT (.), PRINT KEEP STACK (.S), CARRIAGE_RETURN (CR), QUESTION (?)
 * - Variable operations: VARIABLE (definition), WORD (access/reference)
 * - Memory operations: FETCH (@), STORE (!), and character variants (2@, 2!, C@, C!)
 * - Number literals: Push to stack
 * <p>
 * In technical terms, this is a recursive descent parser, the simplest kind to hand-write.
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
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

        _oLogger.debug("Start parsing...");

        while (getToken(0).getType() != ForthTokenType.EOP) {
            aoStatements.add(parseOneStatement());
        }

        Token oEmptyToken = new Token("", ForthTokenType.EMPTY_LINE, -1);
        aoStatements.add(new EmptyStatement(oEmptyToken, _iPosition++));

        for (Statement oStatement: aoStatements) {
            _oLineNumber.putStatementNumber(oStatement.getTokenNumber(), aoStatements.indexOf(oStatement));
        }

        return aoStatements;
    }

    private Statement parseOneStatement() throws SyntaxErrorException {
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
            // Control Flow: IF...THEN and IF...ELSE...THEN
            case IF:
                return parseIfStatement();

            // Control Flow: DO...LOOP and DO...+LOOP
            case DO:
                return parseDoStatement();

            // Control Flow: BEGIN...WHILE...REPEAT
            case BEGIN:
                return parseBeginStatement();

            // Loop Index Access: I (current) and J (outer)
            case I:
                _iPosition++;
                return new CurrentLoopIndexStatement(getToken(-1), _iPosition - 1);

            case J:
                _iPosition++;
                return new OuterLoopIndexStatement(getToken(-1), _iPosition - 1);

            // Loop Control: LOOP, +LOOP, UNLOOP
            case LOOP:
                _iPosition++;
                return new LoopStatement(getToken(-1), _iPosition - 1);

            case PLUS_LOOP:
                _iPosition++;
                return new PlusLoopStatement(getToken(-1), _iPosition - 1);

            case UNLOOP:
                _iPosition++;
                return new UnloopStatement(getToken(-1), _iPosition - 1);

            case LEAVE:
                _iPosition++;
                return new LeaveStatement(getToken(-1), _iPosition - 1);

            // Variable Statements
            case STORE:
            case TWO_STORE:
            case CHAR_STORE:
                _iPosition++;
                return new StoreStatement(getToken(-1).getType(), _iPosition - 1);

            case FETCH:
            case TWO_FETCH:
            case CHAR_FETCH:
                _iPosition++;
                return new FetchStatement(getToken(-1).getType(), _iPosition - 1);

            // PRINT Token: print to the terminal
            case CARRIAGE_RETURN:
                _iPosition++;
                return new CarriageReturnStatement(getToken(-1), _iPosition - 1);

            case PRINT:
                _iPosition++;
                return new PrintStatement(getToken(-1), _iPosition - 1);

            // Question operator: Debugging operator that fetches and prints a variable value
            // Stack: ( address -- ) - Pops variable address, fetches and prints value
            case QUESTION:
                _iPosition++;
                return new QuestionStatement(getToken(-1), _iPosition - 1);

            case PRINT_KEEP_STACK:
                _iPosition++;
                return new PrintKeepStackStatement(getToken(-1), _iPosition - 1);

            // NUMBER Token: In Forth, a number is pushed into the stack. This is done in the NUMBER statement
            case NUMBER:
                Statement oNumberStmt = parseNumberStatement();
                _iPosition++;
                return oNumberStmt;

            // Multiple Tokens: Can be one of these: +, - , *, /, MOD
            case PLUS:
                _iPosition++;
                return new PlusStatement(getToken(-1).getType(), _iPosition - 1);

            case MINUS:
                _iPosition++;
                return new MinusStatement(getToken(-1).getType(), _iPosition - 1);

            case MULTIPLY:
                _iPosition++;
                return new MultiplyStatement(getToken(-1).getType(), _iPosition - 1);

            case DIVIDE:
                _iPosition++;
                return new DivideStatement(getToken(-1).getType(), _iPosition - 1);

            case MOD:
                _iPosition++;
                return new ModuloStatement(getToken(-1).getType(), _iPosition - 1);

            case ONE_MINUS:
                _iPosition++;
                return new OneMinusStatement(getToken(-1).getType(), _iPosition - 1);

            case ONE_PLUS:
                _iPosition++;
                return new OnePlusStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_DIVIDE:
                _iPosition++;
                return new TwoDivideStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_MULTIPLY:
                _iPosition++;
                return new TwoMultiplyStatement(getToken(-1).getType(), _iPosition - 1);

            // Multiple Tokens: Can be one of these: =, <, > , <>, <=, >=
            case EQUALS:
                _iPosition++;
                return new EqualsStatement(getToken(-1).getType(), _iPosition - 1);

            case LESS_THAN:
                _iPosition++;
                return new LessThanStatement(getToken(-1).getType(), _iPosition - 1);

            case GREATER_THAN:
                _iPosition++;
                return new GreaterThanStatement(getToken(-1).getType(), _iPosition - 1);

            case NOT_EQUALS:
                _iPosition++;
                return new NotEqualsStatement(getToken(-1).getType(), _iPosition - 1);

            case LESS_EQUAL:
                _iPosition++;
                return new LessEqualStatement(getToken(-1).getType(), _iPosition - 1);

            case GREATER_EQUAL:
                _iPosition++;
                return new GreaterEqualStatement(getToken(-1).getType(), _iPosition - 1);

            case DOUBLE_ZERO_EQUALS:
            case ZERO_EQUALS:
                _iPosition++;
                return new ZeroEqualsStatement(getToken(-1).getType(), _iPosition - 1);

            case DOUBLE_ZERO_LESS:
            case ZERO_LESS:
                _iPosition++;
                return new ZeroLessStatement(getToken(-1).getType(), _iPosition - 1);

            case ZERO_GREATER:
                _iPosition++;
                return new ZeroGreaterStatement(getToken(-1).getType(), _iPosition - 1);

            case ZERO_NOT_EQUALS:
                _iPosition++;
                return new ZeroNotEqualsStatement(getToken(-1).getType(), _iPosition - 1);

            case DUPE:
                _iPosition++;
                return new DupeStatement(getToken(-1).getType(), _iPosition - 1);

            case QUESTION_DUPE:
                _iPosition++;
                return new QuestionDupeStatement(getToken(-1).getType(), _iPosition - 1);

            case DROP:
                _iPosition++;
                return new DropStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_DROP:
                _iPosition++;
                return new TwoDropStatement(getToken(-1).getType(), _iPosition - 1);

            case SWAP:
                _iPosition++;
                return new SwapStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_SWAP:
                _iPosition++;
                return new TwoSwapStatement(getToken(-1).getType(), _iPosition - 1);

            case OVER:
                _iPosition++;
                return new OverStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_OVER:
                _iPosition++;
                return new TwoOverStatement(getToken(-1).getType(), _iPosition - 1);

            case ROT:
                _iPosition++;
                return new RotStatement(getToken(-1).getType(), _iPosition - 1);

            case TWO_ROT:
                _iPosition++;
                return new TwoRotStatement(getToken(-1).getType(), _iPosition - 1);

            case MINUS_ROT:
                _iPosition++;
                return new MinusRotStatement(getToken(-1).getType(), _iPosition - 1);

            case NIP:
                _iPosition++;
                return new NipStatement(getToken(-1).getType(), _iPosition - 1);

            case TUCK:
                _iPosition++;
                return new TuckStatement(getToken(-1).getType(), _iPosition - 1);

            case PICK:
                _iPosition++;
                return new PickStatement(getToken(-1).getType(), _iPosition - 1);

            case ROLL:
                _iPosition++;
                return new RollStatement(getToken(-1).getType(), _iPosition - 1);

            case DEPTH:
                _iPosition++;
                return new DepthStatement(getToken(-1).getType(), _iPosition - 1);

            case ABS:
                _iPosition++;
                return new AbsStatement(getToken(-1).getType(), _iPosition - 1);

            case MAX:
                _iPosition++;
                return new MaxStatement(getToken(-1).getType(), _iPosition - 1);

            case MIN:
                _iPosition++;
                return new MinStatement(getToken(-1).getType(), _iPosition - 1);

            case NEGATE:
                _iPosition++;
                return new NegateStatement(getToken(-1).getType(), _iPosition - 1);

            case SIGN:
                _iPosition++;
                return new SignStatement(getToken(-1).getType(), _iPosition - 1);

            // Variable Definition: VARIABLE token creates a new variable
            // Syntax: VARIABLE variableName
            // The next token contains the variable name; both are consumed.
            case VARIABLE:
                String strVariableName = getToken(1).getText();
                Statement oVarStmt = new VariableStatement(getToken(0), _iPosition, strVariableName);
                _iPosition = _iPosition + 2;
                return oVarStmt;

            // Variable Access: WORD token accesses a previously defined variable
            // Pushes the variable's index to the stack for use with FETCH (@) and STORE (!)
            case WORD:
                _iPosition++;
                return new WordStatement(getToken(-1), _iPosition - 1);

            // No Token identified, Syntax Error
            default:
                throw new SyntaxErrorException("Incorrect Command: " + getToken(0).getLine() + ": ["
                        + getToken(0).getType() + "] <"
                        + getToken(0).getLine() + ">");
        }
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
     * Parse an IF statement with optional ELSE clause.
     * <p>
     * Syntax: IF ... THEN or IF ... ELSE ... THEN
     * <p>
     * The parser recursively collects statements for the true-branch until
     * ELSE or THEN is encountered, then optionally collects the false-branch
     * until THEN. This allows nested IF blocks to work correctly.
     *
     * @return the parsed IfStatement
     * @throws SyntaxErrorException if IF/THEN structure is malformed
     */
    private Statement parseIfStatement() throws SyntaxErrorException {
        Token oIfToken = getToken(0);
        int iIfPosition = _iPosition;
        _iPosition++; // consume IF

        List<Statement> aoTrueBranch = parseBlockUntil(ForthTokenType.ELSE, ForthTokenType.THEN);
        List<Statement> aoFalseBranch = new ArrayList<>();

        if (getToken(0).getType() == ForthTokenType.ELSE) {
            _iPosition++; // consume ELSE
            aoFalseBranch = parseBlockUntil(ForthTokenType.THEN);
        }

        if (getToken(0).getType() != ForthTokenType.THEN) {
            throw new SyntaxErrorException("IF without matching THEN at token [" + iIfPosition + "]");
        }
        _iPosition++; // consume THEN

        return new IfStatement(oIfToken, iIfPosition, aoTrueBranch, aoFalseBranch);
    }

    /**
     * Parse a DO...LOOP or DO...+LOOP statement.
     * <p>
     * Syntax: DO ... LOOP or DO ... +LOOP
     * <p>
     * The parser collects all statements between DO and LOOP/+LOOP into the loop body.
     * This allows nested DO blocks and ensures the complete loop structure is parsed
     * as a single DoStatement object.
     *
     * @return the parsed DoStatement
     * @throws SyntaxErrorException if DO/LOOP structure is malformed
     */
    private Statement parseDoStatement() throws SyntaxErrorException {
        Token oDoToken = getToken(0);
        int iDoPosition = _iPosition;
        _iPosition++; // consume DO

        // Parse loop body until LOOP or +LOOP is found
        List<Statement> aoLoopBody = parseBlockUntil(ForthTokenType.LOOP, ForthTokenType.PLUS_LOOP);

        // Verify that LOOP or +LOOP is present
        if (getToken(0).getType() != ForthTokenType.LOOP &&
            getToken(0).getType() != ForthTokenType.PLUS_LOOP) {
            throw new SyntaxErrorException("DO without matching LOOP or +LOOP at token [" + iDoPosition + "]");
        }

        // Consume the LOOP or +LOOP token
        _iPosition++;

        return new DoStatement(oDoToken, iDoPosition, aoLoopBody);
    }

    /**
     * Parse a BEGIN statement with its terminator.
     * <p>
     * Syntax: BEGIN ... WHILE ... REPEAT (condition at top, may not run)
     *         BEGIN ... UNTIL           (condition at bottom, always runs once)
     *         BEGIN ... AGAIN           (infinite loop)
     * <p>
     * The parser recursively collects statements until WHILE, UNTIL, or AGAIN
     * is encountered. For WHILE, the block before WHILE is the condition, and
     * the block between WHILE and REPEAT is the body.
     * This allows nested BEGIN blocks to work correctly.
     *
     * @return the parsed BeginStatement, BeginUntilStatement, or BeginAgainStatement
     * @throws SyntaxErrorException if BEGIN structure is malformed
     */
    private Statement parseBeginStatement() throws SyntaxErrorException {
        Token oBeginToken = getToken(0);
        int iBeginPosition = _iPosition;
        _iPosition++; // consume BEGIN

        // Parse statements until WHILE, UNTIL, or AGAIN is found
        List<Statement> aoBlock = parseBlockUntil(ForthTokenType.WHILE, ForthTokenType.UNTIL, ForthTokenType.AGAIN);

        ForthTokenType oTerminator = getToken(0).getType();

        if (oTerminator == ForthTokenType.WHILE) {
            // BEGIN ... WHILE ... REPEAT
            // aoBlock is the condition part
            List<Statement> aoCondition = aoBlock;
            _iPosition++; // consume WHILE

            // Parse body until REPEAT
            List<Statement> aoBody = parseBlockUntil(ForthTokenType.REPEAT);

            if (getToken(0).getType() != ForthTokenType.REPEAT) {
                throw new SyntaxErrorException("WHILE without matching REPEAT at token [" + iBeginPosition + "]");
            }
            _iPosition++; // consume REPEAT

            return new BeginStatement(oBeginToken, iBeginPosition, aoCondition, aoBody);

        } else if (oTerminator == ForthTokenType.UNTIL) {
            // BEGIN ... UNTIL
            // aoBlock is the body part
            _iPosition++; // consume UNTIL
            return new BeginUntilStatement(oBeginToken, iBeginPosition, aoBlock);

        } else if (oTerminator == ForthTokenType.AGAIN) {
            // BEGIN ... AGAIN
            // aoBlock is the body part
            _iPosition++; // consume AGAIN
            return new BeginAgainStatement(oBeginToken, iBeginPosition, aoBlock);

        } else {
            throw new SyntaxErrorException(
                "BEGIN without matching WHILE, UNTIL, or AGAIN at token [" + iBeginPosition + "]"
            );
        }
    }

    /**
     * Parse a block of statements until one of the terminator tokens is found.
     * <p>
     * Used by parseIfStatement() to collect the true-branch and false-branch
     * statements without consuming the terminator. Recursive: if a nested IF
     * is encountered, it fully parses and consumes the nested IF...THEN block.
     *
     * @param aoTerminators The token types that mark the end of this block (e.g., ELSE, THEN)
     * @return List of statements parsed before encountering a terminator
     * @throws SyntaxErrorException if block ends unexpectedly (EOP)
     */
    private List<Statement> parseBlockUntil(final ForthTokenType... aoTerminators) throws SyntaxErrorException {
        List<Statement> aoStatements = new ArrayList<>();

        while (!matchesAny(getToken(0).getType(), aoTerminators)) {
            if (getToken(0).getType() == ForthTokenType.EOP) {
                throw new SyntaxErrorException("Unexpected end of program inside IF block");
            }
            aoStatements.add(parseOneStatement());
        }

        return aoStatements;
    }

    /**
     * Helper to check if a token type matches any of the given types.
     *
     * @param oTokenType The token type to check
     * @param aoTerminators The array of token types to match against
     * @return true if oTokenType matches any in aoTerminators
     */
    private boolean matchesAny(final ForthTokenType oTokenType, final ForthTokenType... aoTerminators) {
        return Arrays.asList(aoTerminators).contains(oTokenType);
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

