package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

/**
 * UnloopStatement.java
 *
 * Implements the UNLOOP command in FORTH.
 * Discards the current loop context and exits the loop.
 *
 * Stack behavior: ( -- )
 * Does not consume or produce stack values.
 *
 * Execution:
 * UNLOOP removes the current loop context from the loop stack,
 * effectively exiting the loop immediately.
 *
 * Standard FORTH semantics:
 * - UNLOOP is used in combination with LEAVE to exit a loop early
 * - UNLOOP removes the loop context from the return stack
 * - After UNLOOP, the loop is completely exited
 * - UNLOOP cannot be used outside a DO...LOOP block
 *
 * Common pattern:
 * 0 100 DO I 50 = IF UNLOOP LEAVE THEN LOOP
 * -- Exits loop when I equals 50
 *
 * Note: In standard FORTH, LEAVE is often implemented using UNLOOP.
 * UNLOOP cleans up the loop context so the program doesn't leave stale
 * loop information on the return stack.
 *
 * Related classes:
 * - DoStatement: Manages loop execution
 * - LoopContext: Stores the loop state
 * - ReturnStack: Manages the loop stack
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class UnloopStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for UNLOOP statement.
     *
     * @param oToken       the UNLOOP token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public UnloopStatement(final Token oToken, final int iTokenNumber) {
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
     * Execute the UNLOOP statement.
     * Removes and discards the current loop context from the loop stack.
     *
     * @throws InvalidLoopIndexException if UNLOOP is used outside a loop context
     * @throws Exception                 if other errors occur
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException(
                "UNLOOP without matching DO at token [" + _iTokenNumber + "]"
            );
        }

        // Pop the loop context from the loop stack
        ReturnStack.popLoop();

        // After this, the loop will exit because DoStatement will no longer
        // have a loop context to check, or the parent loop context (if nested)
        // will become the current context
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (UNLOOP has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (UNLOOP has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
