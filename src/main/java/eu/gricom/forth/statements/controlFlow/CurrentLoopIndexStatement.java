package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * CurrentLoopIndexStatement.java
 *
 * Implements the I command in FORTH.
 * Pushes the current loop index onto the data stack.
 *
 * Stack behavior: ( -- index )
 * Pushes the current loop iteration counter (index) from the innermost DO...LOOP.
 *
 * Standard FORTH semantics:
 * I can only be used inside a DO...LOOP block.
 * It accesses the current (innermost) loop index.
 * Multiple nested loops: I always refers to the innermost loop.
 *
 * Example:
 * 0 3 DO I LOOP  -- prints: 0 1 2
 *
 * Related classes:
 * - OuterLoopIndexStatement: Accesses outer loop index (J)
 * - LoopContext: Stores the loop state
 * - ReturnStack: Manages the loop stack
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class CurrentLoopIndexStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for I statement.
     *
     * @param oToken       the I token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public CurrentLoopIndexStatement(final Token oToken, final int iTokenNumber) {
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
     * Execute the I statement.
     * Pushes the current loop index onto the data stack.
     *
     * @throws InvalidLoopIndexException if I is used outside a loop context
     * @throws Exception                 if other errors occur
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside a loop
        if (!ReturnStack.isLoopActive()) {
            throw new InvalidLoopIndexException(
                "I (current loop index) used outside of DO...LOOP at token [" + _iTokenNumber + "]"
            );
        }

        // Get the current loop context (innermost loop)
        var oLoopContext = ReturnStack.peekLoop();

        // Push the current index onto the data stack
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(oLoopContext.getCurrentIndex()));
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (I statement has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (I statement has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
