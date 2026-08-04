package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * OneMinusStatement.java
 * <p>
 * Description: The OneMinusStatement class implements FORTH's "1-" (one-minus) operator.
 * It pops the top value from the stack, subtracts 1 from it, and pushes the result back.
 * This is a convenience operator for decrementing the top stack value by one.
 * <p>
 * Stack behavior: ( n -- n-1 ) - Pops value, subtracts 1, pushes result
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class OneMinusStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes the OneMinusStatement with token type and position.
     *
     * @param oForthTokenType The token type (ONE_MINUS)
     * @param iTokenNumber The position/number of this token in the source code
     */
    public OneMinusStatement(final ForthTokenType oForthTokenType,
                             final int iTokenNumber) {
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
     * Execute the 1- (one-minus) operation.
     * <p>
     * Pops the top value from the stack, subtracts 1 from it, and pushes the result back.
     * If the stack is empty, prints an error message.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        try {
            int iInteger_1 = oStack.pop().toInteger();
            int iResult = iInteger_1 - 1;
            oStack.push(new IntegerValue(iResult));
        } catch (Exception eException) {
            Printer.println(ConsoleColors.RED + eException.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Content - return a human-readable representation of this statement.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement and operation details
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "1- (ONE_MINUS)";
    }

    /**
     * Structure - return the JSON structure of this statement for compilation/analysis.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        StringBuilder strReturn = new StringBuilder("{\"1-\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
