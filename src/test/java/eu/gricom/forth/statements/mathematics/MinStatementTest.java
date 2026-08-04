package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MinStatementTest - Comprehensive test suite for the MIN (minimum) operator
 */
@DisplayName("MinStatement Test Suite")
public class MinStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should return minimum when first value is smaller")
    public void testExecuteFirstSmaller() throws Exception {
        _oStack.push(new IntegerValue(5));
        _oStack.push(new IntegerValue(10));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return minimum when second value is smaller")
    public void testExecuteSecondSmaller() throws Exception {
        _oStack.push(new IntegerValue(8));
        _oStack.push(new IntegerValue(3));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(3, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return value when both are equal")
    public void testExecuteEqual() throws Exception {
        _oStack.push(new IntegerValue(7));
        _oStack.push(new IntegerValue(7));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(7, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle both positive numbers")
    public void testExecutePositiveNumbers() throws Exception {
        _oStack.push(new IntegerValue(42));
        _oStack.push(new IntegerValue(37));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(37, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle both negative numbers")
    public void testExecuteNegativeNumbers() throws Exception {
        _oStack.push(new IntegerValue(-5));
        _oStack.push(new IntegerValue(-15));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-15, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle mixed sign numbers")
    public void testExecuteMixedSigns() throws Exception {
        _oStack.push(new IntegerValue(-10));
        _oStack.push(new IntegerValue(5));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-10, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large numbers")
    public void testExecuteLargeNumbers() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        _oStack.push(new IntegerValue(Integer.MAX_VALUE - 1));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE - 1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle zero with positive")
    public void testExecuteZeroWithPositive() throws Exception {
        _oStack.push(new IntegerValue(0));
        _oStack.push(new IntegerValue(42));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle zero with negative")
    public void testExecuteZeroWithNegative() throws Exception {
        _oStack.push(new IntegerValue(0));
        _oStack.push(new IntegerValue(-42));
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should maintain correct operation")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(50));
        _oStack.push(new IntegerValue(30));
        _oStack.push(new IntegerValue(40));
        _oStack.push(new IntegerValue(20));

        MinStatement stmt1 = new MinStatement(ForthTokenType.MIN, 1);
        MinStatement stmt2 = new MinStatement(ForthTokenType.MIN, 2);

        stmt1.execute();
        stmt2.execute();

        assertEquals(2, _oStack.size());
        assertEquals(20, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(50, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("MIN"));
        assertTrue(content.contains("MINIMUM"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        MinStatement stmt = new MinStatement(ForthTokenType.MIN, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("MIN"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }
}
