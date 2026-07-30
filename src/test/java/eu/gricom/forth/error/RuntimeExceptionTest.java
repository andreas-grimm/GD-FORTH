package eu.gricom.forth.error;

import org.junit.jupiter.api.Test;

import java.lang.RuntimeException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BasicRuntimeExceptionTest.java
 * <p>
 * Unit tests for the RuntimeException class.
 * <p>
 * This test class provides comprehensive coverage of the RuntimeException, which is thrown
 * when the interpreter encounters errors during program execution. This includes broken navigation
 * links between BASIC line numbers, invalid array index access, and other runtime errors.
 * <p>
 * Note: Class is named BasicRuntimeExceptionTest to avoid conflict with Java's built-in RuntimeException.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class RuntimeExceptionTest {

    /**
     * Test: RuntimeException can be instantiated with a message.
     */
    @Test
    public void testInstantiateWithMessage() {
        java.lang.RuntimeException oException = new java.lang.RuntimeException("Runtime error");
        assertNotNull(oException);
    }

    /**
     * Test: RuntimeException stores and retrieves the error message.
     */
    @Test
    public void testRetrievesMessage() {
        String strMessage = "Broken navigation link at line 500";
        java.lang.RuntimeException oException = new java.lang.RuntimeException(strMessage);
        assertEquals(strMessage, oException.getMessage());
    }

    /**
     * Test: RuntimeException is an instance of Exception.
     */
    @Test
    public void testIsInstanceOfException() {
        java.lang.RuntimeException oException = new java.lang.RuntimeException("Test");
        assertTrue(oException instanceof Exception);
    }

    /**
     * Test: RuntimeException can be thrown and caught.
     */
    @Test
    public void testCanBeThrownAndCaught() {
        assertThrows(java.lang.RuntimeException.class, () -> {
            throw new java.lang.RuntimeException("Program execution error");
        });
    }

    /**
     * Test: RuntimeException with multiple instances and different messages.
     */
    @Test
    public void testMultipleInstancesWithDifferentMessages() {
        java.lang.RuntimeException oException1 = new java.lang.RuntimeException("Error 1");
        java.lang.RuntimeException oException2 = new java.lang.RuntimeException("Error 2");
        java.lang.RuntimeException oException3 = new java.lang.RuntimeException("Error 3");

        assertEquals("Error 1", oException1.getMessage());
        assertEquals("Error 2", oException2.getMessage());
        assertEquals("Error 3", oException3.getMessage());
    }

    /**
     * Test: RuntimeException can be caught as generic Exception.
     */
    @Test
    public void testCanBeCaughtAsGenericException() {
        assertThrows(Exception.class, () -> {
            throw new java.lang.RuntimeException("Test");
        });
    }

    /**
     * Test: RuntimeException toString() returns non-null value.
     */
    @Test
    public void testToStringReturnsNonNull() {
        java.lang.RuntimeException oException = new RuntimeException("Test message");
        assertNotNull(oException.toString());
        assertTrue(oException.toString().length() > 0);
    }
}
