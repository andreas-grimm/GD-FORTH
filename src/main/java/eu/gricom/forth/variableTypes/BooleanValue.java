package eu.gricom.forth.variableTypes;

import eu.gricom.forth.error.SyntaxErrorException;

/**
 * BooleanValue.java
 * <p>
 * Description: The BooleanValue class implements the Value interface for boolean data types. It stores true/false
 * values and provides logical operations and comparisons. Boolean variables in BASIC are identified by the @ suffix
 * and are used in conditional expressions and logical operations.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class BooleanValue implements Value {
    private final boolean _bValue;

    // Forth does normally not support native boolean variables, so we use int representatives.
    public static final int TRUE = -1;
    public static final int FALSE = 0;

    /**
     * Default constructor.
     *
     * @param iValue Value to be stored in the container
     */
    public BooleanValue(final int iValue) {
        if (iValue == TRUE) {
            _bValue = true;
        } else {
            _bValue = false;
        }
    }

    public BooleanValue(final boolean bValue) {
        _bValue = bValue;
    }


    private boolean toBoolean() {
        return _bValue;
    }


    public boolean isTrue() {
        return _bValue;
    }

    /**
     * Override the standart toString method.
     *
     * @return the content of the variable as a string
     */
    @Override
    public final String toString() {

        if (_bValue == true) {
            return "TRUE";
        }

        return "FALSE";
    }

    /**
     * Value types override this to convert themselves to a numeric representation.
     *
     * @return the value as a double
     */
    @Override
    public double toReal() {
        if (_bValue == true) {
            return TRUE;
        }

        return FALSE;
    }

    /**
     * Value types override this to convert themselves to a numeric representation.
     *
     * @return the value as a double
     */
    @Override
    public int toInteger() {
        if (_bValue == true) {
            return TRUE;
        }

        return FALSE;
    }

    /**
     * Return the value field as an object.
     *
     * @return the number value as an object
     */
    public final Value evaluate() {

        return this;
    }

    @Override
    public final Value equals(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof IntegerValue) {
            if (this.toReal() == oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a boolean");
    }

    @Override
    public final Value notEqual(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            if (this.toReal() != oValue.toReal()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not a boolean");
    }

    @Override
    public final Value plus(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            // A + A = A
            if (((BooleanValue) oValue).isTrue() == _bValue) {
                return this;
            }

            // A + (non A) = 1
            if (((BooleanValue) oValue).isTrue() != _bValue) {
                return new BooleanValue(true);
            }

            // A + 1 = 1
            if (((BooleanValue) oValue).isTrue()) {
                return new BooleanValue(true);
            } else {
                // A + 0 = A
                return this;
            }
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type boolean");
    }

    @Override
    public final Value minus(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '-' for boolean expression is not defined");
    }

    @Override
    public final Value multiply(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            // 0 * A = 0
            if (!_bValue) {
                return new BooleanValue(false);
            }

            // 1 * A = A
            if (_bValue) {
                return new BooleanValue(true);
            }

            // A * A = A: covered above
            // A * (non A) = 0 : covered above
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type boolean");
    }

    @Override
    public final Value divide(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '/' for boolean expression is not defined");
    }

    @Override
    public final Value modulo(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '%' for boolean expression is not defined");
    }

    @Override
    public final Value shiftLeft(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '<<' for boolean expression is not defined");
    }

    @Override
    public final Value shiftRight(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '>>' for boolean expression is not defined");
    }

    @Override
    public final Value and(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {

            if (_bValue && ((BooleanValue) oValue).isTrue()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type boolean");
    }

    @Override
    public final Value or(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {

            if (_bValue || ((BooleanValue) oValue).isTrue()) {
                return new BooleanValue(true);
            }

            return new BooleanValue(false);
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type boolean");
    }

    // This one implemented the XOR statement
    @Override
    public final Value power(final Value oValue) throws SyntaxErrorException {
        if (oValue instanceof BooleanValue) {
            BooleanValue oWorkValue = (BooleanValue) oValue;

            if (_bValue == oWorkValue.toBoolean()) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        }

        throw new SyntaxErrorException(oValue.content() + " is not of type boolean");
    }

    @Override
    public final Value smallerThan(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '<' for boolean expression is not defined");
    }

    @Override
    public final Value smallerEqualThan(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '<=' for boolean expression is not defined");
    }

    @Override
    public final Value largerThan(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '>' for boolean expression is not defined");
    }

    @Override
    public final Value largerEqualThan(final Value oValue) throws SyntaxErrorException {
        throw new SyntaxErrorException(oValue.content() + " '>=' for boolean expression is not defined");
    }

    /**
     * Content.
     *
     * Method for JUnit to return the content of the statement.
     *
     * @return - gives the name of the statement ("INPUT") and the variable name
     */
    @Override
    public final String content() {

        return String.valueOf(_bValue);
    }

    /**
     * Structure.
     *
     * Method for the compiler to get the structure of the program.
     *
     * @return gives the name of the statement ("INPUT") and a list of the parameters
     * @throws Exception based on errors in the implementation classes
     */
    @Override
    public String structure() throws Exception {
        String strReturn = this.toString();
        return strReturn;
    }
}
