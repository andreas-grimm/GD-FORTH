package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

import java.util.List;

/**
 * BeginUntilStatement.java
 *
 * Implements the BEGIN...UNTIL control structure in FORTH.
 * This is a do-while loop: body executes at least once, then condition is tested.
 *
 * Stack behavior for body: ( -- ) arbitrary stack effects allowed
 * Condition must pop and leave a flag: ( -- flag )
 *
 * Standard FORTH semantics:
 * - BEGIN marks the start of a do-while loop
 * - UNTIL is the loop terminator with condition check
 * - Body always executes at least once
 * - Loop exits when condition flag is true (non-zero)
 * - Loop continues when condition flag is false (zero)
 *
 * Execution flow:
 * 1. Execute body statements
 * 2. Pop condition flag from stack
 * 3. If flag is true (non-zero), exit loop
 * 4. If flag is false (zero), jump back to step 1
 *
 * Example FORTH:
 * VARIABLE count
 * 0 count !
 * BEGIN count @ 1 + DUP count ! DUP 5 > UNTIL
 * This increments count from 1 until it exceeds 5.
 *
 * Comparison with WHILE:
 * - WHILE: condition at top, may not execute body at all
 * - UNTIL: condition at bottom, always executes body at least once
 *
 * Related classes:
 * - ForthParser: Handles parsing BEGIN...UNTIL blocks
 * - Statement: All FORTH words implement this interface
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BeginUntilStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final List<Statement> _aoBodyStatements;

    /**
     * Constructor for BEGIN...UNTIL statement.
     *
     * @param oToken the BEGIN token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     * @param aoBodyStatements list of statements in loop body (before UNTIL check)
     */
    public BeginUntilStatement(final Token oToken, final int iTokenNumber,
                              final List<Statement> aoBodyStatements) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _aoBodyStatements = aoBodyStatements;
    }

    /**
     * Get the token number for error reporting.
     *
     * @return the position of this statement in the source code
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the do-while loop.
     *
     * Execution flow:
     * 1. Execute body statements (always at least once)
     * 2. Pop condition flag from stack
     * 3. If flag is true (non-zero), exit loop
     * 4. If flag is false (zero), go back to step 1
     *
     * @throws Exception if stack underflow occurs or body execution fails
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        // Do-while loop: body executes at least once
        do {
            // Execute body statements
            for (Statement oStatement : _aoBodyStatements) {
                oStatement.execute();
            }

            // Pop the condition flag from the stack
            if (oStack.size() == 0) {
                throw new Exception(
                    "BEGIN...UNTIL: Stack underflow in condition at token [" +
                    _iTokenNumber + "]"
                );
            }

            // Check flag: non-zero = true (exit), zero = false (continue)
            int iFlag = oStack.pop().toInteger();
            if (iFlag != 0) {
                // Condition is true, exit the loop
                break;
            }
            // Condition is false, loop continues (do-while condition at bottom)

        } while (true);
    }

    /**
     * Return content representation (for debugging).
     *
     * @return string representation of this statement
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return structural representation
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
