package eu.gricom.forth.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for EmptyProgramException class.
 * Tests exception thrown when program is empty.
 */
class EmptyProgramExceptionTest {

    @Test
    void testExceptionCreation() {
        EmptyProgramException exception = new EmptyProgramException("Program is empty");

        assertNotNull(exception);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithMessage() {
        String message = "Program is empty";
        EmptyProgramException exception = new EmptyProgramException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionThrow() {
        assertThrows(EmptyProgramException.class, () -> {
            throw new EmptyProgramException("Cannot execute empty program");
        });
    }

    @Test
    void testExceptionInheritance() {
        EmptyProgramException exception = new EmptyProgramException("test");

        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithCustomMessage() {
        String customMessage = "The program cannot be empty when execution starts";
        EmptyProgramException exception = new EmptyProgramException(customMessage);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("program"));
    }

    @Test
    void testMultipleExceptionInstances() {
        EmptyProgramException exception1 = new EmptyProgramException("Error 1");
        EmptyProgramException exception2 = new EmptyProgramException("Error 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        EmptyProgramException exception = new EmptyProgramException("Test exception");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
    }

    @Test
    void testExceptionToString() {
        String message = "Empty program error";
        EmptyProgramException exception = new EmptyProgramException(message);

        String exceptionString = exception.toString();
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("EmptyProgramException"));
    }

    @Test
    void testExceptionTryCatch() {
        try {
            throw new EmptyProgramException("Test message");
        } catch (EmptyProgramException e) {
            assertNotNull(e);
            assertTrue(e.getMessage().contains("Test message"));
        }
    }

    @Test
    void testExceptionWithEmptyMessage() {
        EmptyProgramException exception = new EmptyProgramException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionInLoop() {
        for (int i = 0; i < 3; i++) {
            EmptyProgramException exception = new EmptyProgramException("Error " + i);
            assertNotNull(exception);
        }
    }

    @Test
    void testExceptionWithComplexMessage() {
        String complexMessage = "Empty program at line 42 with code 'EOF'";
        EmptyProgramException exception = new EmptyProgramException(complexMessage);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Empty program"));
    }
}
