package eu.gricom.forth.statements;


import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.Value;

public class StackStatement implements Statement {
    private final ForthTokenType _oTokenType;
    private final int  _iTokenNumber;

    /**
     * Constructor.
     *
     * @param oForthTokenType - to determine the function to be executed
     * @param iTokenNumber - the number of this token related to this statement
     */
    public StackStatement(final ForthTokenType oForthTokenType,
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
            switch (_oTokenType) {
                case DUPE: {
                        Value oValue = oStack.pop();
                        oStack.push(oValue);
                        oStack.push(oValue);
                    }
                    break;
                case QUESTION_DUPE: {
                        Value oValue = oStack.pop();
                        if (oValue.toInteger() != 0) {
                            oStack.push(oValue);
                            oStack.push(oValue);
                        }
                    }
                    break;
                case DROP:
                    oStack.pop();
                    break;
                case TWO_DROP:
                    oStack.pop();
                    oStack.pop();
                    break;
                case SWAP: {
                        Value oValue_1 = oStack.pop();
                        Value oValue_2 = oStack.pop();
                        oStack.push(oValue_1);
                        oStack.push(oValue_2);
                    }
                    break;
                case TWO_SWAP: {
                    Value oValue_1_1 = oStack.pop();
                    Value oValue_1_2 = oStack.pop();
                    Value oValue_2_1 = oStack.pop();
                    Value oValue_2_2 = oStack.pop();
                    oStack.push(oValue_1_2);
                    oStack.push(oValue_1_1);
                    oStack.push(oValue_2_2);
                    oStack.push(oValue_2_1);
                }
                break;
                case OVER: {
                    Value oValue_1 = oStack.pop();
                    Value oValue_2 = oStack.pop();
                    oStack.push(oValue_2);
                    oStack.push(oValue_1);
                    oStack.push(oValue_2);
                }
                break;
                case TWO_OVER: {
                    Value oValue_1_1 = oStack.pop();
                    Value oValue_1_2 = oStack.pop();
                    Value oValue_2_1 = oStack.pop();
                    Value oValue_2_2 = oStack.pop();
                    oStack.push(oValue_2_2);
                    oStack.push(oValue_2_1);
                    oStack.push(oValue_1_2);
                    oStack.push(oValue_1_1);
                    oStack.push(oValue_2_2);
                    oStack.push(oValue_2_1);
                }
                break;
                case ROT: {
                    Value oValue_3 = oStack.pop();
                    Value oValue_2 = oStack.pop();
                    Value oValue_1 = oStack.pop();
                    oStack.push(oValue_2);
                    oStack.push(oValue_3);
                    oStack.push(oValue_1);
                }
                break;
                case TWO_ROT: {
                    Value oValue_3_1 = oStack.pop();
                    Value oValue_3_2 = oStack.pop();
                    Value oValue_2_1 = oStack.pop();
                    Value oValue_2_2 = oStack.pop();
                    Value oValue_1_1 = oStack.pop();
                    Value oValue_1_2 = oStack.pop();
                    oStack.push(oValue_2_2);
                    oStack.push(oValue_2_1);
                    oStack.push(oValue_3_2);
                    oStack.push(oValue_3_1);
                    oStack.push(oValue_1_2);
                    oStack.push(oValue_1_1);
                }
                break;
                case MINUS_ROT: {
                    Value oValue_3 = oStack.pop();
                    Value oValue_2 = oStack.pop();
                    Value oValue_1 = oStack.pop();
                    oStack.push(oValue_3);
                    oStack.push(oValue_1);
                    oStack.push(oValue_2);
                }
                break;
                case NIP: {
                    Value oValue_1 = oStack.pop();
                    Value oValue_2 = oStack.pop();
                    oStack.push(oValue_1);
                }
                break;
                case TUCK: {
                    Value oValue_1 = oStack.pop();
                    Value oValue_2 = oStack.pop();
                    oStack.push(oValue_1);
                    oStack.push(oValue_2);
                    oStack.push(oValue_1);
                }
                break;
                case PICK: {
                    int iIndex = oStack.pop().toInteger();
                    int iStackDepth = oStack.size();
                    Value oValue = oStack.get(iStackDepth - iIndex - 1);
                    oStack.push(oValue);
                }
                break;
/*
TODO: missing: ROLL	Move nth stack value to top
 */
                case DEPTH:
                    oStack.push(new IntegerValue(oStack.size()));
                break;
                default:
                    throw new ArithmeticException("Unknown token type in calculation");
            }
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
