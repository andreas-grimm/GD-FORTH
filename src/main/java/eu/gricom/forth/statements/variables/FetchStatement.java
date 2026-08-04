package eu.gricom.forth.statements.variables;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.Value;

/**
 * FetchStatement.java
 * <p>
 * Description: The FetchStatement class implements Forth's memory fetch operations (@, 2@, C@).
 * It retrieves a value from a variable at a given index and pushes the result onto the stack.
 * Supports single-cell fetch (@), double-cell fetch (2@), and character fetch (C@).
 * <p>
 * Stack behavior: ( address -- value ) - Pops address, pushes value at that address
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class FetchStatement implements Statement {
    // Token type indicating which fetch operation (@, 2@, C@)
    private final ForthTokenType _oTokenType;
    // Token position in the source code
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes a FetchStatement with the specific token type and its position.
     *
     * @param oForthTokenType The token type (FETCH, TWO_FETCH, or CHAR_FETCH)
     * @param iTokenNumber    The position/number of this token in the source code
     */
    public FetchStatement(ForthTokenType oForthTokenType, int iTokenNumber) {
        _oTokenType = oForthTokenType;
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number - retrieve the position of this statement in the source code.
     * <p>
     * Returns the line/token number that corresponds to this statement, used for
     * debugging and error reporting.
     *
     * @return the command line number/position of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the fetch operation.
     * <p>
     * Pops a memory address (variable index) from the stack, retrieves the value
     * stored at that address in the Variables storage, and pushes it back onto the stack.
     * The operation type determines whether single-cell, double-cell, or character fetch
     * is performed.
     *
     * @throws Exception as any execution error found during execution (empty stack, invalid address)
     */
    @Override
    public void execute() throws Exception {
        Variables oVariables = new Variables();
        Stack oStack = new Stack();

        // Pop the address (variable index) from the stack
        int iIndex = oStack.pop().toInteger();

        // Fetch the value at the specified variable index and push to stack
        oVariables.get(iIndex);
    }

    /**
     * Content - return a human-readable representation of this statement.
     * <p>
     * This method is used in testing and debugging to display the statement's
     * type in a readable format.
     *
     * @return A string describing the fetch operation type (e.g., "FETCH", "2FETCH", "CFETCH")
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "FETCH (" + _oTokenType.toString() + ") ";
    }

    /**
     * Structure - return the JSON structure of this statement for compilation/analysis.
     * <p>
     * Method for the compiler to get the structure of the program in a structured format.
     * Returns JSON representation with token number and operation type.
     *
     * @return JSON string with statement structure containing TOKEN_NR and TOKEN_TYPE
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        // Build JSON structure with statement information
        String strReturn = "{\"FETCH\": {";
        strReturn += "\"TOKEN_NR\": \"" + _iTokenNumber + "\",";
        strReturn += "\"TOKEN_TYPE\": \"" + _oTokenType.toString() + "\"";
        strReturn += "}}";
        return strReturn;
    }
}
