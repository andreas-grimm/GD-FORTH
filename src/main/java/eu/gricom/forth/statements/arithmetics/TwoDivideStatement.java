package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * TwoDivideStatement.java
 * <p>
 * Description: The TwoDivideStatement class implements FORTH's "2/" (two-divide) operator.
 * It pops the top value from the stack, divides it by 2 (equivalent to a right bit shift),
 * and pushes the result back. This is a convenience operator for halving a value.
 * <p>
 * Stack behavior: ( n -- n/2 ) - Pops value, divides by 2, pushes result
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class TwoDivideStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int _iTokenNumber;

    /**
     * Constructor.
     * <p>
     * Initializes the TwoDivideStatement with token type and position.
     *
     * @param oForthTokenType The token type (TWO_DIVIDE)
     * @param iTokenNumber The position/number of this token in the source code
     */
    public TwoDivideStatement(final ForthTokenType oForthTokenType,
                              final int iTokenNumber) {
        _oTokenType = oForthTokenType;
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number - retrieve the position of this statement in the source code.
     *
     * @return the command line number/position of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the 2/ (two-divide) operation.
     * <p>
     * Pops the top value from the stack, divides it by 2, and pushes the result back.
     * This is equivalent to a right bit shift by 1. If the stack is empty, prints an error.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        Stack oStack = new Stack();

        try {
            int iInteger_1 = oStack.pop().toInteger();
            int iResult = iInteger_1 / 2;
            oStack.push(new IntegerValue(iResult));
        } catch (Exception eException) {
            Printer.println(ConsoleColors.RED + eException.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Content - return a human-readable representation of this statement.
     *
     * @return gives the name of the statement and operation details
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "2/ (TWO_DIVIDE)";
    }

    /**
     * Structure - return the JSON structure of this statement for compilation/analysis.
     *
     * @return gives the name of the statement and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        StringBuilder strReturn = new StringBuilder("{\"2/\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
