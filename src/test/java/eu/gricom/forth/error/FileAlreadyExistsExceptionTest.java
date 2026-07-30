package eu.gricom.forth.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for FileAlreadyExistsException class.
 * Tests exception thrown when a file already exists.
 */
class FileAlreadyExistsExceptionTest {

    @Test
    void testExceptionCreation() {
        FileAlreadyExistsException exception = new FileAlreadyExistsException("File already exists");

        assertNotNull(exception);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithMessage() {
        String message = "File already exists: test.txt";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionThrow() {
        assertThrows(FileAlreadyExistsException.class, () -> {
            throw new FileAlreadyExistsException("Cannot create file: already exists");
        });
    }

    @Test
    void testExceptionInheritance() {
        FileAlreadyExistsException exception = new FileAlreadyExistsException("test");

        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithFilePath() {
        String filePath = "/path/to/existing/file.txt";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(filePath);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("file.txt"));
    }

    @Test
    void testMultipleExceptionInstances() {
        FileAlreadyExistsException exception1 = new FileAlreadyExistsException("file1.txt");
        FileAlreadyExistsException exception2 = new FileAlreadyExistsException("file2.txt");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        FileAlreadyExistsException exception = new FileAlreadyExistsException("File exists");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
    }

    @Test
    void testExceptionToString() {
        String message = "File already exists: config.ini";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(message);

        String exceptionString = exception.toString();
        assertNotNull(exceptionString);
        assertTrue(exceptionString.contains("FileAlreadyExistsException"));
    }

    @Test
    void testExceptionTryCatch() {
        try {
            throw new FileAlreadyExistsException("test.txt exists");
        } catch (FileAlreadyExistsException e) {
            assertNotNull(e);
            assertTrue(e.getMessage().contains("test.txt"));
        }
    }

    @Test
    void testExceptionWithEmptyMessage() {
        FileAlreadyExistsException exception = new FileAlreadyExistsException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionWithAbsolutePath() {
        String absolutePath = "/absolute/path/to/file.txt";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(absolutePath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithRelativePath() {
        String relativePath = "relative/path/file.txt";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(relativePath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithDirectoryPath() {
        String dirPath = "/home/user/documents/";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(dirPath);

        assertNotNull(exception);
    }

    @Test
    void testExceptionWithTimestamp() {
        String messageWithTimestamp = "File exists created at 2026-07-28";
        FileAlreadyExistsException exception = new FileAlreadyExistsException(messageWithTimestamp);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("2026"));
    }
}
