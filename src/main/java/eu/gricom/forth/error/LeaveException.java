package eu.gricom.forth.error;

/**
 * LeaveException.java
 *
 * Control-flow exception used to implement the LEAVE statement.
 * LEAVE exits the nearest enclosing DO...LOOP (not BEGIN...WHILE...REPEAT).
 *
 * This exception is thrown by LeaveStatement and caught by DoStatement to break
 * out of the loop immediately, skipping the rest of the body (including LOOP/+LOOP).
 *
 * Stack trace is suppressed since this is a control-flow signal, not an error.
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class LeaveException extends Exception {

    /**
     * Suppress stack trace for control-flow exceptions.
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
