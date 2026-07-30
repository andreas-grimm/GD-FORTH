package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * NumberStatement.java
 * <p>
 * Description: The NumberStatement class implements Forth's handling of number. It pushes the number onto the central
 * stack object. No further transaction is done.
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class NumberStatement implements Statement {
    private IntegerValue _oNumber = null;
    private int _iTokenNumber = 0;

    /**
     * Constructor.
     *
     * @param iNumber - Number to be managed.
     * @param iTokenNumber - the number of this token related to this statement
     */
    public NumberStatement(final int iNumber,
                           final int iTokenNumber) {
        _oNumber = new IntegerValue(iNumber);
        _iTokenNumber = iTokenNumber;
    }

    /**
     * Get Token Number.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Execute the If statement.
     *
     * @throws Exception - exposes any exception coming from the memory management
     */
    public void execute() throws Exception {
        Stack oStack = new Stack();
        oStack.push(_oNumber);

    }

    /**
     * This method is used in testing and debugging. It returns the set values when the constructor has been called.
     *
     * @return - readable string with the name and the value of the assignment
     */
    @Override
    public String content() {

        return "NUMBER (" + _oNumber.toString() + ") ";
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
        String strReturn = "{\"NUMBER\": {";
        strReturn += "\"TOKEN_NR\": \""+ _iTokenNumber +"\",";
        strReturn += "\"NUMBER\": \""+ _oNumber.structure() +"\"";
        strReturn += "}}";
        return strReturn;
    }
}
