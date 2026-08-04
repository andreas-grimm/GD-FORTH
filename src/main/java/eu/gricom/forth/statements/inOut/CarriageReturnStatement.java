package eu.gricom.forth.statements.inOut;

import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.Value;

/**
 * PrintStatement.java
 * <p>
 * Description: The PrintStatement class implements the FORTH output operation. It evaluates one or more expressions,
 * converts the results to string format, and outputs them to the console. It supports multiple expressions separated
 * by spaces, with optional line termination.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class CarriageReturnStatement implements Statement {
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
    public CarriageReturnStatement(Token oToken, int iTokenNumber) {
        _iTokenNumber = iTokenNumber;
        _oToken = oToken;
        _oTokenType = oToken.getType();
    }

    /**
     * Get Line Number.
     *
     * @return iLineNumber - the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        if (_oToken != null) {
            return _oToken.getLine();
        }

        return -1;
    }

    /**
     * Execute the transaction.
     *
     * @throws Exception any execution error found throws an exception
     */
    public void execute() {
        Printer.println("");
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        return "CR ()";
    }

    /**
     * Structure.
     * <p>
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        StringBuilder strReturn = new StringBuilder("{\"PRINT\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
