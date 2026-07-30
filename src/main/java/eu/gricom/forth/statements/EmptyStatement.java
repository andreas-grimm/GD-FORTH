package eu.gricom.forth.statements;

import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;

import java.util.EmptyStackException;

/**
 * EmptyStatement.java
 * <p>
 * Description: The EmptyStatement class implements - nothing. It is only used to demark the end of a command entered by
 * user.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class EmptyStatement implements Statement {
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
    public EmptyStatement(Token oToken, int iTokenNumber) {
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
    public void execute() throws Exception {
        System.out.print(ConsoleColors.GREEN + "ok\n" + ConsoleColors.RESET);
    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {
        return "EMPTY ()";
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
        StringBuilder strReturn = new StringBuilder("{\"EMPTY\": {");
        strReturn.append("\"TOKEN_NR\": \"").append(_iTokenNumber).append("\",");
        strReturn.append("\"TOKEN_TYPE\": \"").append(_oTokenType);
        strReturn.append("}}");
        return strReturn.toString();
    }
}
