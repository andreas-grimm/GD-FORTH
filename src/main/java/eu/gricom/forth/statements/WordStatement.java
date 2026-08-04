package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * WordStatement.java
 * <p>
 * Description: The WordStatement class implements FORTH variable access operations (WORD token).
 * It retrieves the index of a previously defined variable and pushes it to the stack for use with
 * FetchStatement (@) and StoreStatement (!) operations.
 * <p>
 * When a WORD token references a variable name, this statement looks up the variable by name
 * in the shared Variables storage and pushes its index to the stack. The variable must have been
 * previously defined using the VARIABLE token (handled by VariableStatement).
 * <p>
 * Stack behavior: ( -- index )
 * <p>
 * Related classes:
 * - VariableStatement: Handles VARIABLE token (variable definition)
 * - FetchStatement: Uses variable index to read values (@)
 * - StoreStatement: Uses variable index to write values (!)
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class WordStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final Variables _oVariables = new Variables();

    /**
     * Constructor for variable access (WORD token).
     * <p>
     * Used when referencing an existing variable by name. The statement will
     * look up the variable and push its index to the stack.
     *
     * @param oToken The token containing the variable name to access
     * @param iTokenNumber The position/number of this token in the source code
     */
    public WordStatement(final Token oToken, final int iTokenNumber) {
        _oToken = oToken;
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
     * Execute the variable access operation.
     * <p>
     * Looks up the variable by name from the token and pushes its index to the stack
     * for use by fetch (@) and store (!) operations. The variable must already be defined.
     *
     * @throws Exception if variable is not found or not defined
     */
    @Override
    public void execute() throws Exception {
        try {
            String variableName = _oToken.getText();
            int variableIndex = _oVariables.index(variableName);
            Stack oStack = new Stack();
            oStack.push(new IntegerValue(variableIndex));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Content - return a human-readable representation of this statement.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement and the variable name
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "";
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
        return "";
    }
}
