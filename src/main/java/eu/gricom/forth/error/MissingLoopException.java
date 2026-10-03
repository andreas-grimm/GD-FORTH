package eu.gricom.forth.error;

/**
 * MissingLoopException
 *
 * Thrown when a DO statement is not terminated by LOOP or +LOOP.
 * This indicates a syntax error where a DO block is not properly closed.
 *
 * Example error: "0 5 DO I" without "LOOP"
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class MissingLoopException extends Exception {
    /**
     * Create a MissingLoopException with a message.
     *
     * @param sMessage description of the error
     */
    public MissingLoopException(String sMessage) {
        super(sMessage);
    }

    /**
     * Create a MissingLoopException with a message and cause.
     *
     * @param sMessage description of the error
     * @param oCause   the underlying exception
     */
    public MissingLoopException(String sMessage, Throwable oCause) {
        super(sMessage, oCause);
    }
}
