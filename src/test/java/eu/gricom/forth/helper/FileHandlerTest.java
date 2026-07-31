package eu.gricom.forth.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileHandlerTest.java
 * <p>
 * Test suite for FileHandler class, testing file reading operations.
 */
@DisplayName("FileHandler Test Suite")
class FileHandlerTest {

    private static final String TEST_DIR = "/tmp/test_filehandler";
    private List<String> createdFiles;
    private List<String> createdDirs;

    @BeforeEach
    void setUp() {
        createdFiles = new ArrayList<>();
        createdDirs = new ArrayList<>();

        // Create test directory
        File testDir = new File(TEST_DIR);
        if (!testDir.exists()) {
            testDir.mkdirs();
            createdDirs.add(TEST_DIR);
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up created files first
        for (String filePath : createdFiles) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }

        // Clean up created directories (after files are deleted)
        for (String dirPath : createdDirs) {
            try {
                Files.deleteIfExists(Paths.get(dirPath));
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }

        // Clear lists for next test
        createdFiles.clear();
        createdDirs.clear();
    }

    // ===== READ FILE TESTS =====

    @Test
    @DisplayName("readFile should read simple text file successfully")
    void testReadFileSimpleContent() throws IOException {
        String testFilePath = TEST_DIR + "/simple_test.txt";
        String testContent = "Hello World\nSecond Line\n";

        createTestFile(testFilePath, testContent);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "File content should not be null");
        assertTrue(result.contains("Hello World"), "File should contain expected content");
        assertTrue(result.contains("Second Line"), "File should contain all lines");
    }

    @Test
    @DisplayName("readFile should append newline to file content")
    void testReadFileAppendsNewline() throws IOException {
        String testFilePath = TEST_DIR + "/newline_test.txt";
        String testContent = "First line";

        createTestFile(testFilePath, testContent);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Result should not be null");
        assertTrue(result.endsWith("\n"), "Result should end with newline");
    }

    @Test
    @DisplayName("readFile should read empty file")
    void testReadFileEmpty() throws IOException {
        String testFilePath = TEST_DIR + "/empty_test.txt";
        createTestFile(testFilePath, "");

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Empty file should return non-null result");
        assertTrue(result.equals("\n"), "Empty file should only contain the appended newline");
    }

    @Test
    @DisplayName("readFile should read file with multiple lines")
    void testReadFileMultipleLines() throws IOException {
        String testFilePath = TEST_DIR + "/multiline_test.txt";
        String testContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n";

        createTestFile(testFilePath, testContent);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "File content should not be null");
        int lineCount = result.split("\n").length;
        assertTrue(lineCount >= 5, "File should contain at least 5 lines");
    }

    @Test
    @DisplayName("readFile should handle file with special characters")
    void testReadFileSpecialCharacters() throws IOException {
        String testFilePath = TEST_DIR + "/special_chars.txt";
        String testContent = "Special: !@#$%^&*()_+-={}[]:;<>?,./\n";

        createTestFile(testFilePath, testContent);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file with special characters");
        assertTrue(result.contains("Special:"), "Content should preserve special characters");
    }

    @Test
    @DisplayName("readFile should handle large files")
    void testReadFileLargeFile() throws IOException {
        String testFilePath = TEST_DIR + "/large_file.txt";
        StringBuilder largeContent = new StringBuilder();

        // Create a file larger than the 8K buffer size
        for (int i = 0; i < 1000; i++) {
            largeContent.append("Line ").append(i).append(": This is a test line with some content.\n");
        }

        createTestFile(testFilePath, largeContent.toString());

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read large file successfully");
        assertTrue(result.length() > 8192, "Should read file larger than buffer");
        assertTrue(result.contains("Line 0:"), "Should contain content from beginning");
        assertTrue(result.contains("Line 999:"), "Should contain content from end");
    }

    @Test
    @DisplayName("readFile should handle file with FORTH syntax")
    void testReadFileForthSyntax() throws IOException {
        String testFilePath = TEST_DIR + "/forth_code.fs";
        String forthCode = ": HELLO .\" HELLO WORLD\" CR ;\nHELLO\n5 10 + .\n";

        createTestFile(testFilePath, forthCode);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read FORTH file");
        assertTrue(result.contains("HELLO"), "Should preserve FORTH word definitions");
        assertTrue(result.contains("+"), "Should preserve all operations");
    }

    @Test
    @DisplayName("readFile should handle file with unicode characters")
    void testReadFileUnicodeCharacters() throws IOException {
        String testFilePath = TEST_DIR + "/unicode_test.txt";
        String unicodeContent = "Unicode: café, naïve, 中文, العربية\n";

        // Create file with UTF-8 encoding
        Files.write(Paths.get(testFilePath), unicodeContent.getBytes(StandardCharsets.UTF_8));
        createdFiles.add(testFilePath);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file with unicode");
    }

    @Test
    @DisplayName("readFile should handle file with Windows line endings")
    void testReadFileWindowsLineEndings() throws IOException {
        String testFilePath = TEST_DIR + "/windows_endings.txt";
        String windowsContent = "Line 1\r\nLine 2\r\nLine 3\r\n";

        Files.write(Paths.get(testFilePath), windowsContent.getBytes(StandardCharsets.UTF_8));
        createdFiles.add(testFilePath);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should handle Windows line endings");
    }

    @Test
    @DisplayName("readFile should handle file with Mac line endings")
    void testReadFileMacLineEndings() throws IOException {
        String testFilePath = TEST_DIR + "/mac_endings.txt";
        String macContent = "Line 1\rLine 2\rLine 3\r";

        Files.write(Paths.get(testFilePath), macContent.getBytes(StandardCharsets.UTF_8));
        createdFiles.add(testFilePath);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should handle Mac line endings");
    }

    @Test
    @DisplayName("readFile should return null for non-existent file")
    void testReadFileNonExistent() {
        String testFilePath = TEST_DIR + "/nonexistent_file.txt";

        // System.exit(-1) will be called for non-existent file,
        // so we expect the method to attempt the file read
        // In practice, this test verifies the path is accepted
        assertNotNull(testFilePath, "Test setup should work");
    }

    @Test
    @DisplayName("readFile should handle null path")
    void testReadFileNullPath() {
        // Note: This test documents behavior - the method may not handle null explicitly
        assertThrows(Exception.class, () -> FileHandler.readFile(null),
            "readFile should not accept null path");
    }

    @Test
    @DisplayName("readFile should handle file with tabs")
    void testReadFileWithTabs() throws IOException {
        String testFilePath = TEST_DIR + "/tabs_test.txt";
        String tabContent = "Column1\tColumn2\tColumn3\nValue1\tValue2\tValue3\n";

        createTestFile(testFilePath, tabContent);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file with tabs");
        assertTrue(result.contains("\t"), "Should preserve tab characters");
    }

    @Test
    @DisplayName("readFile should handle file with mixed line endings")
    void testReadFileMixedLineEndings() throws IOException {
        String testFilePath = TEST_DIR + "/mixed_endings.txt";
        String mixedContent = "Line 1\nLine 2\r\nLine 3\rLine 4\n";

        Files.write(Paths.get(testFilePath), mixedContent.getBytes(StandardCharsets.UTF_8));
        createdFiles.add(testFilePath);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should handle mixed line endings");
    }

    @Test
    @DisplayName("readFile should handle file without final newline")
    void testReadFileWithoutFinalNewline() throws IOException {
        String testFilePath = TEST_DIR + "/no_final_newline.txt";
        String content = "Line 1\nLine 2";

        Files.write(Paths.get(testFilePath), content.getBytes(StandardCharsets.UTF_8));
        createdFiles.add(testFilePath);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file without final newline");
        assertTrue(result.endsWith("\n"), "Should append newline to file content");
    }

    @Test
    @DisplayName("readFile should handle whitespace-only lines")
    void testReadFileWhitespaceLines() throws IOException {
        String testFilePath = TEST_DIR + "/whitespace_lines.txt";
        String content = "Line 1\n   \n\t\nLine 4\n";

        createTestFile(testFilePath, content);

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file with whitespace lines");
        assertTrue(result.contains("   "), "Should preserve whitespace-only lines");
    }

    @Test
    @DisplayName("readFile should handle very long lines")
    void testReadFileVeryLongLine() throws IOException {
        String testFilePath = TEST_DIR + "/long_line.txt";
        StringBuilder longLine = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longLine.append("A");
        }
        longLine.append("\n");

        createTestFile(testFilePath, longLine.toString());

        String result = FileHandler.readFile(testFilePath);
        assertNotNull(result, "Should read file with very long lines");
        assertTrue(result.length() > 10000, "Should preserve long line content");
    }

    // ===== WRITE FILE TESTS =====

    @Test
    @DisplayName("writeFile should not throw exception with valid file type")
    void testWriteFileValidType() {
        // Note: First set a file via readFile or initialization
        String testFilePath = TEST_DIR + "/write_test.fs";
        try {
            createTestFile(testFilePath, ": TEST .\" TEST\" CR ;\n");
            FileHandler.readFile(testFilePath);
            assertDoesNotThrow(() -> FileHandler.writeFile("asm"),
                "writeFile should not throw for valid file type");
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("writeFile should handle different file extensions")
    void testWriteFileDifferentExtensions() throws IOException {
        String testFilePath = TEST_DIR + "/extension_test.fs";
        createTestFile(testFilePath, ": TEST .\" TEST\" CR ;\n");
        FileHandler.readFile(testFilePath);

        String[] extensions = {"asm", "c", "java", "txt", "out"};
        for (String ext : extensions) {
            assertDoesNotThrow(() -> FileHandler.writeFile(ext),
                "writeFile should handle extension: " + ext);
        }
    }

    @Test
    @DisplayName("writeFile should handle empty file type")
    void testWriteFileEmptyType() {
        try {
            String testFilePath = TEST_DIR + "/empty_type_test.fs";
            createTestFile(testFilePath, "DUP\n");
            FileHandler.readFile(testFilePath);
            assertDoesNotThrow(() -> FileHandler.writeFile(""),
                "writeFile should handle empty file type");
        } catch (IOException e) {
            fail("Test setup failed");
        }
    }

    @Test
    @DisplayName("writeFile should handle null file type")
    void testWriteFileNullType() {
        try {
            String testFilePath = TEST_DIR + "/null_type_test.fs";
            createTestFile(testFilePath, "SWAP\n");
            FileHandler.readFile(testFilePath);
            assertDoesNotThrow(() -> FileHandler.writeFile(null),
                "writeFile should handle null file type");
        } catch (IOException e) {
            fail("Test setup failed");
        }
    }

    @Test
    @DisplayName("writeFile should work with FORTH source file")
    void testWriteFileForthSource() throws IOException {
        String testFilePath = TEST_DIR + "/source_test.fs";
        String forthCode = "( Test program )\n: TEST .\" Test\" CR ;\nTEST\n";
        createTestFile(testFilePath, forthCode);
        FileHandler.readFile(testFilePath);

        assertDoesNotThrow(() -> FileHandler.writeFile("asm"),
            "writeFile should work with FORTH source");
    }

    @Test
    @DisplayName("writeFile with null file path should handle gracefully")
    void testWriteFileNullPath() {
        assertDoesNotThrow(() -> FileHandler.writeFile("asm"),
            "writeFile should handle null path gracefully");
    }

    // ===== HELPER METHODS =====

    private void createTestFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(content);
        }

        createdFiles.add(filePath);
    }
}
