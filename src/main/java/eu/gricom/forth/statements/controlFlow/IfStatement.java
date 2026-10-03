package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.BooleanValue;

import java.util.List;

/**
 * IfStatement.java
 * <p>
 * Description: The IfStatement class implements FORTH conditional execution (IF...THEN and IF...ELSE...THEN).
 * It pops a flag from the stack and executes either the true-branch or false-branch (else-branch) based on
 * the flag value. This is a structured statement — the parser fully resolves the IF...THEN block at parse time
 * into one IfStatement object that owns two List<Statement> branches.
 * <p>
 * Stack behavior: ( flag -- )
 * Pops one flag value from the stack. If the flag is non-zero (true), executes the true-branch.
 * If the flag is zero (false), executes the false-branch (else-branch if present, or nothing).
 * <p>
 * Standard FORTH semantics: TRUE = -1, FALSE = 0, but any non-zero value is treated as true.
 * <p>
 * Nesting: IF blocks can be nested arbitrarily. An inner IF...THEN fully parses and consumes its
 * own THEN before the outer block resumes, so nested branches work correctly without special handling.
 * <p>
 * Related classes:
 * - ForthParser: Handles parsing IF...THEN and IF...ELSE...THEN blocks, recursively building IfStatement
 * - BooleanValue: Defines TRUE (-1) and FALSE (0) constants
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class IfStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final List<Statement> _aoTrueBranch;
    private final List<Statement> _aoFalseBranch;

    /**
     * Constructor for IF...THEN and IF...ELSE...THEN statements.
     * <p>
     * Creates a conditional statement with a true-branch and optional false-branch (else-branch).
     * If the false-branch is an empty list, no else-branch is executed when the condition is false.
     *
     * @param oToken The IF token
     * @param iTokenNumber The position/number of this token in the source code
     * @param aoTrueBranch List of statements to execute if flag is non-zero (true)
     * @param aoFalseBranch List of statements to execute if flag is zero (false); empty list if no ELSE
     */
    public IfStatement(final Token oToken, final int iTokenNumber,
                       final List<Statement> aoTrueBranch, final List<Statement> aoFalseBranch) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _aoTrueBranch = aoTrueBranch;
        _aoFalseBranch = aoFalseBranch;
    }

    /**
     * Get Token Number - retrieve the position of this statement in the source code.
     * <p>
     * Returns the line/token number that corresponds to the IF token, used for
     * debugging and error reporting.
     *
     * @return the command line number/position of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the conditional statement.
     * <p>
     * Pops one value from the stack and treats it as a boolean flag:
     * - Non-zero (including TRUE = -1): executes true-branch
     * - Zero (FALSE = 0): executes false-branch (or does nothing if no else-branch)
     *
     * @throws Exception if stack is empty or errors occur during branch execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();
        int iFlag = oStack.pop().toInteger();

        List<Statement> aoBranch = (iFlag != BooleanValue.FALSE) ? _aoTrueBranch : _aoFalseBranch;

        for (Statement oStatement : aoBranch) {
            oStatement.execute();
        }
    }

    /**
     * Content - return a human-readable representation of this statement.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Structure - return the JSON structure of this statement for compilation/analysis.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement and structure
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
