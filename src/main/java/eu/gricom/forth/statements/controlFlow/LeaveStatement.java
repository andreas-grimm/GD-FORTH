package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.error.LeaveException;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

/**
 * LeaveStatement.java
 *
 * Implements the LEAVE command in FORTH.
 * Exits the nearest enclosing DO...LOOP by throwing a LeaveException.
 *
 * Stack behavior: ( -- )
 * Does not consume or produce stack values.
 *
 * Execution:
 * LEAVE throws LeaveException, which propagates to the nearest enclosing
 * DoStatement. The DoStatement catches it and exits the loop immediately,
 * skipping the rest of the body (including LOOP/+LOOP).
 *
 * LEAVE only works with DO...LOOP, not with BEGIN...WHILE...REPEAT or
 * BEGIN...UNTIL or BEGIN...AGAIN (those control structures do not catch
 * the exception, so it propagates further up).
 *
 * Standard FORTH semantics:
 * - LEAVE exits the nearest enclosing DO...LOOP
 * - LEAVE cannot be used outside a DO...LOOP block
 * - Remaining body statements and LOOP are skipped
 * - Nested loops: LEAVE only exits the innermost loop
 *
 * Common pattern:
 * 0 100 DO
 *   I 50 = IF LEAVE THEN
 *   I LOOP
 * ( This exits when I equals 50, printing 0..49 )
 *
 * Related classes:
 * - DoStatement: Catches LeaveException to exit the loop
 * - LeaveException: Control-flow exception signal
 * - ReturnStack: Checks if we are inside a loop
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class LeaveStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for LEAVE statement.
     *
     * @param oToken       the LEAVE token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public LeaveStatement(final Token oToken, final int iTokenNumber) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
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
     * Execute the LEAVE statement.
     *
     * Verifies we are inside a DO...LOOP, then throws LeaveException
     * to signal exit to the nearest enclosing DoStatement.
     *
     * @throws InvalidLoopIndexException if LEAVE is used outside a loop
     * @throws LeaveException always (control-flow signal to exit loop)
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException(
                "LEAVE without matching DO at token [" + _iTokenNumber + "]"
            );
        }

        // Throw LeaveException to signal exit to the nearest enclosing DO
        throw new LeaveException();
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (LEAVE has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (LEAVE has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
