package eu.gricom.forth.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for FileNotFoundException class.
 * Tests exception thrown when a file cannot be found.
 */
class FileNotFoundExceptionTest {

    @Test
    void testExceptionCreation() {
        String message = "File not found";
        FileNotFoundException exception = new FileNotFoundException(message);

        assertNotNull(exception);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithMessage() {
        String message = "File not found: config.txt";
        FileNotFoundException exception = new FileNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionThrow() {
        assertThrows(FileNotFoundException.class, () -> {
            throw new FileNotFoundException("input.txt not found");
        });
    }

    @Test
    void testExceptionInheritance() {
        FileNotFoundException exception = new FileNotFoundException("test");

        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithFilePath() {
        String filePath = "/path/to/missing/file.txt";
        FileNotFoundException exception = new FileNotFoundException(filePath);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("file.txt"));
    }

    @Test
    void testMultipleExceptionInstances() {
        FileNotFoundException exception1 = new FileNotFoundException("file1.txt");
        FileNotFoundException exception2 = new FileNotFoundException("file2.txt");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        FileNotFoundException exception = new FileNotFoundException("Missing file");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
    }

    @Test
    void testExceptionToString() {
        String message = "File not found: data.ini";
        FileNotFoundException exception = new FileNotFoundException(message);

        String exceptionString = exception.toString();
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("FileNotFoundException"));
    }

    @Test
    void testExceptionTryCatch() {
        try {
            throw new FileNotFoundException("test.txt not found");
        } catch (FileNotFoundException e) {
            assertNotNull(e);
            assertTrue(e.getMessage().contains("test.txt"));
        }
    }

    @Test
    void testExceptionWithEmptyMessage() {
        FileNotFoundException exception = new FileNotFoundException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionWithAbsolutePath() {
        String absolutePath = "/absolute/path/to/missing/file.txt";
        FileNotFoundException exception = new FileNotFoundException(absolutePath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithRelativePath() {
        String relativePath = "relative/path/file.txt";
        FileNotFoundException exception = new FileNotFoundException(relativePath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithNonexistentDirectory() {
        String dirPath = "/nonexistent/directory/file.txt";
        FileNotFoundException exception = new FileNotFoundException(dirPath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithSystemPath() {
        String sysPath = "/etc/missing/config.conf";
        FileNotFoundException exception = new FileNotFoundException(sysPath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithWildcardPath() {
        String wildcardPath = "*.txt";
        FileNotFoundException exception = new FileNotFoundException(wildcardPath);

        assertNotNull(exception);
    }
}
