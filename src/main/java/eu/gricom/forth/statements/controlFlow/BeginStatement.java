package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;

import java.util.List;

/**
 * BeginStatement.java
 *
 * Implements the BEGIN...WHILE...REPEAT control structure in FORTH.
 * This is an indefinite loop that executes until a condition becomes false.
 *
 * Stack behavior for condition part: ( ... -- ... flag )
 * The condition statements should leave a boolean flag on the stack.
 * Stack behavior for body part: ( -- ) - arbitrary stack effects allowed
 *
 * Standard FORTH semantics:
 * - BEGIN marks the start of an indefinite loop
 * - Statements before WHILE form the condition (executed before each iteration check)
 * - WHILE checks the flag: non-zero = continue, zero = exit
 * - Statements between WHILE and REPEAT form the loop body
 * - REPEAT unconditionally jumps back to BEGIN
 *
 * Execution flow:
 * 1. Execute condition statements (may manipulate stack, must leave flag)
 * 2. Pop flag from stack and check: if 0 (false), exit loop
 * 3. If flag is non-zero (true), execute body statements
 * 4. Jump back to step 1
 *
 * Example FORTH:
 * 5 BEGIN DUP . 1 - DUP 0> WHILE REPEAT
 * This counts down from 5 to 1.
 *
 * Nested example:
 * 3 BEGIN 2 BEGIN DUP I . 1 - DUP 0> WHILE REPEAT 1 - DUP 0> WHILE REPEAT
 * This prints 2 1 0 twice.
 *
 * Related classes:
 * - ForthParser: Handles parsing BEGIN...WHILE...REPEAT blocks
 * - Statement: All FORTH words implement this interface
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BeginStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final List<Statement> _aoConditionStatements;
    private final List<Statement> _aoBodyStatements;

    /**
     * Constructor for BEGIN...WHILE...REPEAT statement.
     *
     * @param oToken the BEGIN token from the tokenizer
     * @param iTokenNumber the position of this token in the source code
     * @param aoConditionStatements list of statements executed before WHILE check
     * @param aoBodyStatements list of statements in loop body (between WHILE and REPEAT)
     */
    public BeginStatement(final Token oToken, final int iTokenNumber,
                         final List<Statement> aoConditionStatements,
                         final List<Statement> aoBodyStatements) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _aoConditionStatements = aoConditionStatements;
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
     * Execute the indefinite loop.
     *
     * Execution flow:
     * 1. Execute condition statements (leave flag on stack)
     * 2. Pop flag from stack
     * 3. If flag is zero (false), exit loop
     * 4. If flag is non-zero (true), execute body statements
     * 5. Jump back to step 1
     *
     * @throws Exception if stack underflow occurs or body execution fails
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        // Infinite loop until condition becomes false
        while (true) {
            // Execute condition statements (they should leave a flag on the stack)
            for (Statement oStatement : _aoConditionStatements) {
                oStatement.execute();
            }

            // Pop the condition flag from the stack
            // Stack underflow here means condition didn't push a flag
            if (oStack.size() == 0) {
                throw new Exception(
                    "BEGIN...WHILE: Stack underflow in condition at token [" +
                    _iTokenNumber + "]"
                );
            }

            // Check flag: 0 = false (exit), non-zero = true (continue)
            int iFlag = oStack.pop().toInteger();
            if (iFlag == 0) {
                // Condition is false, exit the loop
                break;
            }

            // Condition is true, execute body statements
            for (Statement oStatement : _aoBodyStatements) {
                oStatement.execute();
            }
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
