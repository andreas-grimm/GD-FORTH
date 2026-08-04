package eu.gricom.forth.statements.inOut;

import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.Value;

/**
 * QuestionStatement.java
 * <p>
 * Description: The QuestionStatement class implements FORTH's "?" (question) operator for debugging.
 * It retrieves a variable's address from the stack, fetches the value stored at that address,
 * and prints it to the console. This is a convenience operator combining variable fetch and print operations.
 * <p>
 * The "?" operator is commonly used in FORTH for inspecting variable values during development:
 * - Push variable address to stack (via WordStatement)
 * - Execute "?" to fetch and print the variable's value
 * <p>
 * Stack behavior: ( address -- ) - Pops address, fetches and prints the value at that address
 * <p>
 * Related classes:
 * - WordStatement: Provides variable addresses to the stack
 * - FetchStatement: Implements the "@" operator (fetch without print)
 * - PrintStatement: Implements the "." operator (print from stack)
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class QuestionStatement implements Statement {
    private final Token _oToken;
    private ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Default constructor.
     * <p>
     * Receive the statement targeted to be printed.
     *
     * @param oToken - provide the token used to build this
     */
    public QuestionStatement(Token oToken, int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
        _oToken = oToken;
        _oTokenType = oToken.getType();
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
     * Execute the "?" (question) operator.
     * <p>
     * Pops a variable address from the stack, fetches the value stored at that address,
     * and prints it to the console. This combines the fetch (@) and print (.) operations.
     *
     * @throws Exception any execution error found throws an exception
     */
    @Override
    public void execute() throws Exception {
        try {
            Variables oVariables = new Variables();
            Stack oStack = new Stack();

            // Pop the address (variable index) from the stack
            int iIndex = oStack.pop().toInteger();

            // Fetch the value at the specified variable index and push to stack
            oVariables.get(iIndex);

            // now get the actual value of the stack
            Value oValue = oStack.pop();

            // and print it...
            String strValue = oValue.toString();
            Printer.println(strValue);
        } catch (Exception e) {
            Printer.println(e.getMessage());
        }
    }

    /**
     * Content - return a human-readable representation of this statement.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return A string describing the question operator ("?")
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "QUESTION (?)";
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
        StringBuilder strReturn = new StringBuilder("{\"QUESTION\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
