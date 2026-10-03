package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

/**
 * PlusLoopStatement.java
 *
 * Implements the +LOOP command in FORTH.
 * Increments the loop index by a custom value from the data stack.
 *
 * Stack behavior: ( increment -- )
 * Pops one value from the data stack and uses it as the increment for this iteration.
 *
 * Execution:
 * +LOOP pops a value from the stack, increments the current loop index by that value,
 * and returns control to the DoStatement, which checks if the loop should continue or exit.
 *
 * Standard FORTH semantics:
 * - +LOOP is used instead of LOOP for variable-increment loops
 * - Each iteration can have a different increment value
 * - The increment is popped from the data stack before incrementing
 * - If new index >= limit, loop exits; otherwise, loop continues
 * - +LOOP cannot be used outside a DO...LOOP block
 *
 * Example:
 * 0 10 DO I +LOOP  -- increments by I each iteration
 * 0 20 DO 2 +LOOP  -- increments by 2 each iteration (step by 2)
 *
 * Note: PlusLoopStatement is used internally by DoStatement.
 * The parser includes +LOOP in the loop body, and DoStatement checks
 * loop completion after executing each body.
 *
 * Related classes:
 * - DoStatement: Manages loop execution and completion checking
 * - LoopStatement: Fixed increment (1) version (LOOP)
 * - LoopContext: Stores the loop state
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class PlusLoopStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for +LOOP statement.
     *
     * @param oToken       the +LOOP token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public PlusLoopStatement(final Token oToken, final int iTokenNumber) {
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
     * Execute the +LOOP statement.
     * Pops a value from the data stack and uses it to increment the loop index.
     *
     * The DoStatement that owns this loop body will check isComplete()
     * after this statement executes to determine if the loop should continue.
     *
     * @throws InvalidLoopIndexException if +LOOP is used outside a loop context
     * @throws Exception                 if stack is empty or other errors occur
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException(
                "+LOOP without matching DO at token [" + _iTokenNumber + "]"
            );
        }

        // Pop the increment value from the data stack
        Stack oStack = new Stack();
        long lIncrement = (long) oStack.pop().toInteger();

        // Get the current loop context and set its step size
        var oLoopContext = ReturnStack.peekLoop();
        oLoopContext.setStepSize(lIncrement);

        // Increment the loop index
        oLoopContext.increment();

        // DoStatement will check oLoopContext.isComplete() after this statement
        // executes to determine if the loop should continue or exit
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (+LOOP has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (+LOOP has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
