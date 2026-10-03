package eu.gricom.forth.error;

/**
 * MissingDoException
 *
 * Thrown when a LOOP or +LOOP statement is encountered without a matching DO.
 * This indicates a syntax error in the FORTH program.
 *
 * Example error: "LOOP THEN" without "0 5 DO" before it
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class MissingDoException extends Exception {
    /**
     * Create a MissingDoException with a message.
     *
     * @param sMessage description of the error
     */
    public MissingDoException(String sMessage) {
        super(sMessage);
    }

    /**
     * Create a MissingDoException with a message and cause.
     *
     * @param sMessage description of the error
     * @param oCause   the underlying exception
     */
    public MissingDoException(String sMessage, Throwable oCause) {
        super(sMessage, oCause);
    }
}
