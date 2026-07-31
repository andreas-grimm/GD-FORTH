package eu.gricom.forth.statements;


import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;

public class ValueComparsionStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int  _iTokenNumber;

    /**
     * Constructor.
     *
     * @param oForthTokenType - to determine the function to be executed
     * @param iTokenNumber - the number of this token related to this statement
     */
    public ValueComparsionStatement(final ForthTokenType oForthTokenType,
                                    final int iTokenNumber) {
        _oTokenType = oForthTokenType;
        _iTokenNumber = iTokenNumber;
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
        Stack oStack = new Stack();

        try {
            int iInteger = oStack.pop().toInteger();
            int iResult = 0;

            switch (_oTokenType) {
                case ZERO_EQUALS:
                    if (iInteger == 0) {
                        iResult = BooleanValue.TRUE;
                    } else {
                        iResult = BooleanValue.FALSE;
                    }
                    break;
                case ZERO_LESS:
                    if (iInteger < 0) {
                        iResult = BooleanValue.TRUE;
                    } else {
                        iResult = BooleanValue.FALSE;
                    }
                    break;
                case ZERO_GREATER:
                    if (iInteger > 0) {
                        iResult = BooleanValue.TRUE;
                    } else {
                        iResult = BooleanValue.FALSE;
                    }
                    break;
                case ZERO_NOT_EQUALS:
                    if (iInteger != 0) {
                        iResult = BooleanValue.TRUE;
                    } else {
                        iResult = BooleanValue.FALSE;
                    }
                    break;
                default:
                    throw new ArithmeticException("Unknown token type in comparison");
            }

            oStack.push(new IntegerValue(iResult));

        } catch (Exception eException) {
            Printer.println(ConsoleColors.RED + eException.getMessage() + ConsoleColors.RESET);
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
