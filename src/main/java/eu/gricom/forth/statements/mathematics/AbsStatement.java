package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * AbsStatement.java
 * <p>
 * Description: The AbsStatement class implements FORTH's "ABS" (absolute value) operator.
 * It pops the top value from the stack and pushes back its absolute value.
 * Negative numbers become positive, while positive numbers and zero remain unchanged.
 * <p>
 * Stack behavior: ( n -- |n| ) - Pops value, pushes its absolute value
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class AbsStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes the AbsStatement with token type and position.
     *
     * @param oForthTokenType The token type (ABS)
     * @param iTokenNumber The position/number of this token in the source code
     */
    public AbsStatement(final ForthTokenType oForthTokenType,
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
     * Execute the ABS (absolute value) operation.
     * <p>
     * Pops the top value from the stack, calculates its absolute value,
     * and pushes the result back. If the stack is empty, prints an error message.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        try {
            int iInteger_1 = oStack.pop().toInteger();
            int iResult = iInteger_1;

            if (iInteger_1 < 0) {
                iResult = iInteger_1 * -1;
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
        return "ABS (ABSOLUTE_VALUE)";
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
        StringBuilder strReturn = new StringBuilder("{\"ABS\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
