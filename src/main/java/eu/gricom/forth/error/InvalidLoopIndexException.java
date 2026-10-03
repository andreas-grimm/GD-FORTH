package eu.gricom.forth.error;

/**
 * InvalidLoopIndexException
 *
 * Thrown when I, J, or UNLOOP statements are used outside of a DO...LOOP block.
 * These statements are only valid when executing inside a loop context.
 *
 * Example errors:
 * - "I" used outside any loop
 * - "J" used in a single-level loop (no outer loop to reference)
 * - "UNLOOP" used outside any loop
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class InvalidLoopIndexException extends Exception {
    /**
     * Create an InvalidLoopIndexException with a message.
     *
     * @param sMessage description of the error
     */
    public InvalidLoopIndexException(String sMessage) {
        super(sMessage);
    }

    /**
     * Create an InvalidLoopIndexException with a message and cause.
     *
     * @param sMessage description of the error
     * @param oCause   the underlying exception
     */
    public InvalidLoopIndexException(String sMessage, Throwable oCause) {
        super(sMessage, oCause);
    }
}
