package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.LoopContext;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

import java.util.List;

/**
 * DoStatement.java
 *
 * Implements the DO...LOOP control structure in FORTH.
 * A structured statement similar to IfStatement, but for loops.
 *
 * Stack behavior: ( limit index -- )
 * Pops two values: the loop limit and starting index.
 * The loop executes while index < limit (for positive step) or index > limit (for negative step).
 *
 * Standard FORTH semantics:
 * - DO expects limit on top of stack, index below it
 * - The loop body executes repeatedly until the loop condition is met
 * - I and J can access loop indices inside the loop body
 * - LOOP increments by 1, +LOOP increments by TOS value
 * - Nested loops work correctly with independent contexts
 *
 * Parser Notes:
 * The parser (ForthParser) creates one DoStatement that owns the complete
 * loop body (all statements between DO and LOOP/+LOOP).
 * This is a "structured" approach like IfStatement, enabling early syntax checking.
 *
 * Example FORTH:
 * 0 5 DO I LOOP
 * Pushes 0 (index) and 5 (limit), then executes I LOOP 5 times.
 * Prints: 0 1 2 3 4
 *
 * Nested example:
 * 0 3 DO 0 2 DO I J LOOP LOOP
 * Outer loop: 3 iterations (J = 0, 1, 2)
 * Inner loop: 2 iterations each (I = 0, 1)
 * Prints pairs: (0,0) (1,0) (0,1) (1,1) (0,2) (1,2)
 *
 * Related classes:
 * - LoopContext: Manages loop state (index, limit, step)
 * - CurrentLoopIndexStatement: Accesses I (current index)
 * - OuterLoopIndexStatement: Accesses J (outer index)
 * - ReturnStack: Manages the loop stack for nesting
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class DoStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final List<Statement> _aoLoopBody;

    /**
     * Constructor for DO statement.
     *
     * @param oToken       the DO token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     * @param aoLoopBody   the list of statements that form the loop body
     */
    public DoStatement(final Token oToken, final int iTokenNumber,
                       final List<Statement> aoLoopBody) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _aoLoopBody = aoLoopBody;
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
     * Execute the DO statement.
     * Pops limit and index from stack, creates loop context, iterates loop body.
     *
     * Execution flow:
     * 1. Pop limit and index from data stack
     * 2. Create LoopContext with these values
     * 3. Push LoopContext onto loop stack
     * 4. Execute loop body repeatedly
     * 5. After each iteration, LOOP or +LOOP increments and checks completion
     * 6. When loop completes, pop LoopContext from loop stack
     *
     * @throws Exception if stack is empty, loop errors occur, or body execution fails
     */
    @Override
    public void execute() throws Exception {
        // Get the data stack
        Stack oStack = new Stack();

        // Pop limit and index from the stack
        // Order: TOS is index, second is limit
        long lIndex = (long) oStack.pop().toInteger();
        long lLimit = (long) oStack.pop().toInteger();

        // Create a new loop context for this DO...LOOP
        LoopContext oLoopContext = new LoopContext(lLimit, lIndex, 1);

        // Push the loop context onto the loop stack
        // This makes it available to I, J, LOOP, and +LOOP statements
        ReturnStack.pushLoop(oLoopContext);

        try {
            // Execute the loop body repeatedly until completion
            while (!oLoopContext.isComplete()) {
                // Execute each statement in the loop body
                for (Statement oStatement : _aoLoopBody) {
                    oStatement.execute();
                }
            }
        } finally {
            // Always clean up the loop context when exiting
            // This ensures proper nesting even if errors occur
            ReturnStack.popLoop();
        }
    }

    /**
     * Return content representation (for debugging).
     *
     * @return empty string (DO statement manages structure, not content)
     * @throws Exception if errors occur
     */
    @Override
    public String content() throws Exception {
        return "";
    }

    /**
     * Return structure representation (for compilation/analysis).
     *
     * @return empty string (DO statement structure is implicit)
     * @throws Exception if errors occur
     */
    @Override
    public String structure() throws Exception {
        return "";
    }
}
