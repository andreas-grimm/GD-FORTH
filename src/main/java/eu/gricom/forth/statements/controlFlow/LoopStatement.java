package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

/**
 * LoopStatement.java
 *
 * Implements the LOOP command in FORTH.
 * Increments the loop index by 1 and signals loop control.
 *
 * Stack behavior: ( -- )
 * Does not consume or produce stack values.
 *
 * Execution:
 * LOOP increments the current loop index by 1 and returns control to the
 * DoStatement, which checks if the loop should continue or exit.
 *
 * Standard FORTH semantics:
 * - LOOP is always paired with DO
 * - LOOP marks the end of a loop body
 * - After LOOP, the index is incremented
 * - If new index >= limit, loop exits; otherwise, loop continues
 * - LOOP cannot be used outside a DO...LOOP block
 *
 * Example:
 * 0 5 DO I LOOP  -- prints: 0 1 2 3 4
 *
 * Note: LoopStatement is used internally by DoStatement.
 * The parser includes LOOP in the loop body, and DoStatement checks
 * loop completion after executing each body.
 *
 * Related classes:
 * - DoStatement: Manages loop execution and completion checking
 * - PlusLoopStatement: Variable increment version (+LOOP)
 * - LoopContext: Stores the loop state
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class LoopStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for LOOP statement.
     *
     * @param oToken       the LOOP token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public LoopStatement(final Token oToken, final int iTokenNumber) {
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
     * Execute the LOOP statement.
     * Increments the current loop index by the default step size (1).
     *
     * The DoStatement that owns this loop body will check isComplete()
     * after this statement executes to determine if the loop should continue.
     *
     * @throws InvalidLoopIndexException if LOOP is used outside a loop context
     * @throws Exception                 if other errors occur
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException(
                "LOOP without matching DO at token [" + _iTokenNumber + "]"
            );
        }

        // Get the current loop context and increment it
        var oLoopContext = ReturnStack.peekLoop();
        oLoopContext.increment();

        // DoStatement will check oLoopContext.isComplete() after this statement
        // executes to determine if the loop should continue or exit
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (LOOP has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (LOOP has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
