package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.RuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LineNumberXRefTest.java
 * <p>
 * Test suite for LineNumberXRef class, testing line number cross-reference functionality.
 */
@DisplayName("LineNumberXRef Test Suite")
public class LineNumberXRefTest {

    private LineNumberXRef _oXRef;

    @BeforeEach
    public void setUp() throws Exception {
        _oXRef = new LineNumberXRef();
        // Reset the static maps using reflection since they are private and static
        resetStaticMaps();
    }

    private void resetStaticMaps() throws Exception {
        Field lineNumbersField = LineNumberXRef.class.getDeclaredField("_aoLineNumbers");
        lineNumbersField.setAccessible(true);
        Map<?, ?> lineNumbers = (Map<?, ?>) lineNumbersField.get(null);
        lineNumbers.clear();

        Field statementNumbersField = LineNumberXRef.class.getDeclaredField("_aoStatementNumbers");
        statementNumbersField.setAccessible(true);
        Map<?, ?> statementNumbers = (Map<?, ?>) statementNumbersField.get(null);
        statementNumbers.clear();
    }

    @Test
    @DisplayName("Should put and get line number correctly")
    public void testPutAndGetLineNumber() throws RuntimeException {
        _oXRef.putLineNumber(100, 10); // line 100 at token 10
        assertEquals(100, _oXRef.getLineNumberFromToken(10));
    }

    @Test
    @DisplayName("Should put and get statement number correctly")
    public void testPutAndGetStatementNumber() throws RuntimeException {
        _oXRef.putStatementNumber(10, 1); // token 10 is statement 1
        assertEquals(10, _oXRef.getTokenFromStatement(1));
    }

    @Test
    @DisplayName("Should get statement number from line number")
    public void testGetStatementFromLineNumber() throws RuntimeException {
        _oXRef.putLineNumber(100, 10);
        _oXRef.putStatementNumber(10, 1);
        
        assertEquals(1, _oXRef.getStatementFromLineNumber(100));
    }

    @Test
    @DisplayName("Should get statement number from token")
    public void testGetStatementFromToken() throws RuntimeException {
        _oXRef.putStatementNumber(20, 2);
        assertEquals(2, _oXRef.getStatementFromToken(20));
    }

    @Test
    @DisplayName("Should get next line number correctly")
    public void testGetNextLineNumber() {
        _oXRef.putLineNumber(10, 1);
        _oXRef.putLineNumber(20, 5);
        _oXRef.putLineNumber(30, 10);

        assertEquals(20, _oXRef.getNextLineNumber(10));
        assertEquals(30, _oXRef.getNextLineNumber(20));
        assertEquals(30, _oXRef.getNextLineNumber(25));
        assertEquals(0, _oXRef.getNextLineNumber(30));
        assertEquals(0, _oXRef.getNextLineNumber(40));
    }

    @Test
    @DisplayName("Should check if token exists in cross-reference")
    public void testContains() {
        _oXRef.putLineNumber(100, 10);
        assertTrue(_oXRef.contains(10));
        assertFalse(_oXRef.contains(20));
    }

    @Test
    @DisplayName("Should throw exception for non-existent token in line number lookup")
    public void testGetLineNumberFromTokenNegative() {
        assertThrows(RuntimeException.class, () -> {
            _oXRef.getLineNumberFromToken(999);
        });
    }

    @Test
    @DisplayName("Should throw exception for non-existent statement")
    public void testGetTokenFromStatementNegative() {
        assertThrows(RuntimeException.class, () -> {
            _oXRef.getTokenFromStatement(999);
        });
    }

    @Test
    @DisplayName("Should throw exception for non-existent line number")
    public void testGetStatementFromLineNumberNegative() {
        assertThrows(RuntimeException.class, () -> {
            _oXRef.getStatementFromLineNumber(999);
        });
    }

    @Test
    @DisplayName("Should throw exception for non-existent token in statement lookup")
    public void testGetStatementFromTokenNegative() {
        assertThrows(RuntimeException.class, () -> {
            _oXRef.getStatementFromToken(999);
        });
    }

    @Test
    @DisplayName("Should list all cross-references without crashing")
    public void testList() {
        // Just ensure it doesn't crash
        _oXRef.putLineNumber(10, 1);
        _oXRef.list();
    }
}
