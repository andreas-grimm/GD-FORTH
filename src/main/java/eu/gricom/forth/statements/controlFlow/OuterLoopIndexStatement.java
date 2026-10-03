package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * OuterLoopIndexStatement.java
 *
 * Implements the J command in FORTH.
 * Pushes the outer loop index onto the data stack (for nested loops).
 *
 * Stack behavior: ( -- outer-index )
 * Pushes the loop iteration counter from the loop one level out from the current.
 *
 * Standard FORTH semantics:
 * J can only be used inside nested DO...LOOP blocks.
 * It accesses the index of the loop that contains the current loop.
 * Single-level loops: J cannot be used (no outer loop exists).
 *
 * Example:
 * 0 3 DO 0 2 DO I J LOOP LOOP
 *   -- Prints pairs: (0,0) (1,0) (0,1) (1,1) (0,2) (1,2)
 *   -- I is innermost loop index, J is outer loop index
 *
 * Related classes:
 * - CurrentLoopIndexStatement: Accesses current loop index (I)
 * - LoopContext: Stores the loop state
 * - ReturnStack: Manages the loop stack
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class OuterLoopIndexStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;

    /**
     * Constructor for J statement.
     *
     * @param oToken       the J token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     */
    public OuterLoopIndexStatement(final Token oToken, final int iTokenNumber) {
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
     * Execute the J statement.
     * Pushes the outer loop index onto the data stack.
     *
     * @throws InvalidLoopIndexException if J is used outside nested loops
     * @throws Exception                 if other errors occur
     */
    @Override
    public void execute() throws Exception {
        // Verify we are inside at least two nested loops
        if (ReturnStack.getLoopStackSize() < 2) {
            throw new InvalidLoopIndexException(
                "J (outer loop index) used outside nested DO...LOOP at token [" + _iTokenNumber + "]"
            );
        }

        // Get the outer loop context (one level up from current)
        // Depth 1 gets us the loop that contains the current loop
        var oOuterLoopContext = ReturnStack.peekLoop(1);

        // Push the outer loop index onto the data stack
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(oOuterLoopContext.getCurrentIndex()));
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (J statement has no content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (J statement has no structure)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
