package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * MinStatement.java
 * <p>
 * Description: The MinStatement class implements FORTH's "MIN" (minimum) operator.
 * It pops the top two values from the stack and pushes back the smaller of the two.
 * This is useful for finding the minimum value between two numbers on the stack.
 * <p>
 * Stack behavior: ( n1 n2 -- min(n1,n2) ) - Pops two values, pushes minimum
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class MinStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes the MinStatement with token type and position.
     *
     * @param oForthTokenType The token type (MIN)
     * @param iTokenNumber The position/number of this token in the source code
     */
    public MinStatement(final ForthTokenType oForthTokenType,
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
     * Execute the MIN (minimum) operation.
     * <p>
     * Pops the top two values from the stack, determines which is smaller,
     * and pushes the minimum value back. If fewer than two values are available,
     * prints an error message.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        try {
            int iInteger_1 = oStack.pop().toInteger();
            int iInteger_2 = oStack.pop().toInteger();
            int iResult = iInteger_1;

            if (iInteger_2 < iInteger_1) {
                iResult = iInteger_2;
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
        return "MIN (MINIMUM)";
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
        StringBuilder strReturn = new StringBuilder("{\"MIN\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
