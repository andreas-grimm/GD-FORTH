package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;

public class VariableStatement implements Statement {
    private Token _oToken = null;
    private final int  _iTokenNumber;
    private Variables _oVariables = new Variables();
    private String _strVariableName = null;

    public VariableStatement(final Token oToken,
                             final int iTokenNumber) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
    }

    public VariableStatement(final Token oToken,
                             final int iTokenNumber,
                             String strVariableName) {
        _oToken = oToken;
        _iTokenNumber = iTokenNumber;
        _strVariableName = strVariableName;
    }

    /**
     * Get Token Number - get the number of the corresponding token to this statement.
     *
     * @return the command line number of the statement
     */
    @Override
    public int getTokenNumber() {
        return _iTokenNumber;
    }

    /**
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     *
     * @throws Exception as any execution error found during execution
     */
    @Override
    public void execute() throws Exception {
        try {
            switch (_oToken.getType()) {
                case STORE:
                    break;
                case FETCH:
                    break;
                case TWO_STORE:
                    break;
                case TWO_FETCH:
                    break;
                case CHAR_STORE:
                    break;
                case CHAR_FETCH:
                    break;
                case VARIABLE:
                    if (_strVariableName != null) {
                        _oVariables.define(_strVariableName);
                    } else {
                        throw (new RuntimeException("Variable name should have been provided"));
                    }
                    break;
                case WORD:
                    Stack oStack = new Stack();
                    int iIndex = _oVariables.index(_oToken.getText());
                    oStack.push(new IntegerValue(iIndex));
                    break;
                default:
                    throw new ArithmeticException("Unknown token type in variable statement");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Content.
     * <p>
     * Method for JUnit to return the content of the statement.
     *
     * @return gives the name of the statement ("INPUT") and the variable name
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String content() throws Exception {
        return "";
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
        return "";
    }
}
