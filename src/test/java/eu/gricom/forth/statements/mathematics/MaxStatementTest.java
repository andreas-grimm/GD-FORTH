package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MaxStatementTest - Comprehensive test suite for the MAX (maximum) operator
 */
@DisplayName("MaxStatement Test Suite")
public class MaxStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should return maximum when first value is larger")
    public void testExecuteFirstLarger() throws Exception {
        _oStack.push(new IntegerValue(10));
        _oStack.push(new IntegerValue(5));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(10, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return maximum when second value is larger")
    public void testExecuteSecondLarger() throws Exception {
        _oStack.push(new IntegerValue(3));
        _oStack.push(new IntegerValue(8));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(8, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return value when both are equal")
    public void testExecuteEqual() throws Exception {
        _oStack.push(new IntegerValue(7));
        _oStack.push(new IntegerValue(7));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(7, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle both positive numbers")
    public void testExecutePositiveNumbers() throws Exception {
        _oStack.push(new IntegerValue(42));
        _oStack.push(new IntegerValue(37));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle both negative numbers")
    public void testExecuteNegativeNumbers() throws Exception {
        _oStack.push(new IntegerValue(-5));
        _oStack.push(new IntegerValue(-15));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle mixed sign numbers")
    public void testExecuteMixedSigns() throws Exception {
        _oStack.push(new IntegerValue(-10));
        _oStack.push(new IntegerValue(5));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large numbers")
    public void testExecuteLargeNumbers() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        _oStack.push(new IntegerValue(Integer.MAX_VALUE - 1));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle zero with positive")
    public void testExecuteZeroWithPositive() throws Exception {
        _oStack.push(new IntegerValue(0));
        _oStack.push(new IntegerValue(42));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle zero with negative")
    public void testExecuteZeroWithNegative() throws Exception {
        _oStack.push(new IntegerValue(0));
        _oStack.push(new IntegerValue(-42));
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should maintain correct operation")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(5));
        _oStack.push(new IntegerValue(10));
        _oStack.push(new IntegerValue(3));
        _oStack.push(new IntegerValue(8));

        MaxStatement stmt1 = new MaxStatement(ForthTokenType.MAX, 1);
        MaxStatement stmt2 = new MaxStatement(ForthTokenType.MAX, 2);

        stmt1.execute();
        stmt2.execute();

        assertEquals(2, _oStack.size());
        assertEquals(10, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("MAX"));
        assertTrue(content.contains("MAXIMUM"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        MaxStatement stmt = new MaxStatement(ForthTokenType.MAX, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("MAX"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }
}
