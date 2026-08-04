package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;

/**
 * VariableStatement.java
 * <p>
 * Description: The VariableStatement class implements FORTH variable operations.
 * It handles two primary operations:
 * 1. Variable definition (VARIABLE token): Creates a new variable and initializes it to "empty"
 * 2. Variable access (WORD token): Pushes the index of a previously defined variable to the stack
 * <p>
 * When a VARIABLE token is processed, a new variable is added to the shared Variables storage
 * and initialized with an empty StringValue. When a WORD token references a variable name,
 * the statement retrieves the variable's index and pushes it to the stack for use with
 * FetchStatement (@) and StoreStatement (!) operations.
 * <p>
 * Stack behavior for WORD token: ( -- index )
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class VariableStatement implements Statement {
    private final Token _oToken;
    private final int _iTokenNumber;
    private final Variables _oVariables = new Variables();
    private final String _strVariableName;

    /**
     * Constructor for variable access (WORD token).
     * <p>
     * Used when referencing an existing variable by name. The statement will
     * look up the variable and push its index to the stack.
     *
     * @param oToken The token containing the variable name to access
     * @param iTokenNumber The position/number of this token in the source code
     */
    public VariableStatement(final Token oToken, final int iTokenNumber) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _strVariableName = null;
    }

    /**
     * Constructor for variable definition (VARIABLE token).
     * <p>
     * Used when defining a new variable with the VARIABLE keyword. The statement
     * will create a new variable in the Variables storage and initialize it to "empty".
     *
     * @param oToken The token containing the variable name
     * @param iTokenNumber The position/number of this token in the source code
     * @param strVariableName The name of the variable to define
     */
    public VariableStatement(final Token oToken, final int iTokenNumber, final String strVariableName) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _strVariableName = strVariableName;
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
     * Execute the variable operation.
     * <p>
     * For VARIABLE token: Defines a new variable with the provided name in the
     * Variables storage, initializing it to "empty" StringValue.
     * <p>
     * For WORD token: Looks up the variable by name from the token and pushes
     * its index to the stack for use by fetch (@) and store (!) operations.
     *
     * @throws Exception if variable is not found (for WORD token) or already defined (for VARIABLE token)
     */
    @Override
    public void execute() throws Exception {
        try {
            if (_strVariableName != null) {
                // VARIABLE token: define a new variable
                _oVariables.define(_strVariableName);
            } else {
                // WORD token: access existing variable by name from token
                String variableName = _oToken.getText();
                int variableIndex = _oVariables.index(variableName);
                Stack oStack = new Stack();
                oStack.push(new IntegerValue(variableIndex));
            }
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
