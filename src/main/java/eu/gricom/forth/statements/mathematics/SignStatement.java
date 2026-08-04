package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * SignStatement.java
 * <p>
 * Description: The SignStatement class implements FORTH's "SIGN" operator.
 * It pops the top value from the stack and pushes back its sign: -1 for negative, 0 for zero, 1 for positive.
 * This extracts the sign of the number without changing its magnitude.
 * <p>
 * Stack behavior: ( n -- sign(n) ) - Pops value, pushes -1, 0, or 1 based on sign
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class SignStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes the SignStatement with token type and position.
     *
     * @param oForthTokenType The token type (SIGN)
     * @param iTokenNumber The position/number of this token in the source code
     */
    public SignStatement(final ForthTokenType oForthTokenType,
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
     * Execute the SIGN operation.
     * <p>
     * Pops the top value from the stack, determines its sign, and pushes the result back.
     * Returns -1 for negative numbers, 0 for zero, and 1 for positive numbers.
     * If the stack is empty, prints an error message.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        try {
            int iInteger_1 = oStack.pop().toInteger();
            int iResult = 0;

            if (iInteger_1 < 0) {
                iResult = -1;
            }

            if (iInteger_1 > 0) {
                iResult = 1;
            }

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
        return "SIGN (SIGN)";
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
        StringBuilder strReturn = new StringBuilder("{\"SIGN\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
