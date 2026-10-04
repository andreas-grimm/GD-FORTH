package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

import java.util.List;

/**
 * BeginAgainStatement.java
 *
 * Implements the BEGIN...AGAIN control structure in FORTH.
 * This is an infinite loop that can only exit via LEAVE from an enclosing DO.
 *
 * Stack behavior: ( -- ) arbitrary stack effects allowed
 *
 * Standard FORTH semantics:
 * - BEGIN marks the start of an infinite loop
 * - AGAIN unconditionally jumps back to BEGIN
 * - Loop only exits via LEAVE from an enclosing DO...LOOP
 * - Without an enclosing DO, the loop runs forever (or until an exception)
 *
 * Execution flow:
 * Loop forever: execute body statements, jump back to BEGIN.
 * Only exit via:
 *   - LeaveException from an enclosing DO...LOOP
 *   - An exception thrown from body execution
 *
 * Example FORTH:
 * 0 100 DO
 *   I EMIT BEGIN I 1 + DUP I > IF LEAVE THEN EMIT AGAIN
 * LOOP
 * This uses BEGIN...AGAIN inside a DO to print characters repeatedly
 * until a condition in the outer DO triggers LEAVE.
 *
 * Note: BEGIN...AGAIN is typically used in combination with LEAVE inside
 * an enclosing DO...LOOP. Without such a construct, the loop runs forever.
 *
 * Related classes:
 * - ForthParser: Handles parsing BEGIN...AGAIN blocks
 * - LeaveStatement: Exits the nearest enclosing DO...LOOP
 * - Statement: All FORTH words implement this interface
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BeginAgainStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final List<Statement> _aoBodyStatements;

    /**
     * Constructor for BEGIN...AGAIN statement.
     *
     * @param oToken the BEGIN token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     * @param aoBodyStatements list of statements in the infinite loop body
     */
    public BeginAgainStatement(final Token oToken, final int iTokenNumber,
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
     * Execute the infinite loop.
     *
     * Loops forever executing body statements. The loop only exits via:
     * - LeaveException thrown from an enclosing DO...LOOP
     * - An exception thrown during body execution
     *
     * Note: LeaveException is not caught here; it propagates to the
     * nearest enclosing DO...LOOP which catches it.
     *
     * @throws Exception if body execution fails or LeaveException propagates
     */
    @Override
    public void execute() throws Exception {
        // Infinite loop - only exits via exception or LEAVE
        while (true) {
            // Execute body statements
            for (Statement oStatement : _aoBodyStatements) {
                oStatement.execute();
            }
            // AGAIN: unconditionally jump back to BEGIN
            // (continue loop)
        }
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
