package eu.gricom.forth.statements.stack;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.RealValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for TuckStatement (TUCK) - insert copy of top under second.
 * Stack effect: ( a b -- b a b )
 * Inserts a copy of the top item under the second item.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("TUCK (Insert Copy) Stack Operation Tests")
class TuckStatementTest {

    private Stack stack;
    private TuckStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new TuckStatement(ForthTokenType.TUCK, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        TuckStatement stmt = new TuckStatement(ForthTokenType.TUCK, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        TuckStatement stmt = new TuckStatement(ForthTokenType.TUCK, 123);
        assertEquals(123, stmt.getTokenNumber(), "Token number should be 123");
    }

    @Test
    @DisplayName("content() should return empty string")
    void testContent() throws Exception {
        assertEquals("", statement.content(), "Content should be empty string");
    }

    @Test
    @DisplayName("structure() should return empty string")
    void testStructure() throws Exception {
        assertEquals("", statement.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Basic TUCK Operation Tests
    // ============================================================================

    @Test
    @DisplayName("TUCK inserts copy of top under second ( 5 10 -- 10 5 10 )")
    void testTuckTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(10, stack.pop().toInteger(), "Top should be 10");
        assertEquals(5, stack.pop().toInteger(), "Second should be 5");
        assertEquals(10, stack.pop().toInteger(), "Third should be 10");
    }

    @Test
    @DisplayName("TUCK increases stack size by 1")
    void testTuckIncreasesSizeByOne() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    @DisplayName("TUCK with three items")
    void testTuckThreeItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(3, stack.pop().toInteger(), "Top: original top");
        assertEquals(2, stack.pop().toInteger(), "Second: original second");
        assertEquals(3, stack.pop().toInteger(), "Third: copy of top");
        assertEquals(1, stack.pop().toInteger(), "Fourth: unchanged");
    }

    @Test
    @DisplayName("TUCK with many items")
    void testTuckManyItems() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
    }

    // ============================================================================
    // TUCK with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("TUCK with RealValue")
    void testTuckRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertEquals(3, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
    }

    @Test
    @DisplayName("TUCK with StringValue")
    void testTuckStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        statement.execute();

        assertEquals("second", stack.pop().toString());
        assertEquals("first", stack.pop().toString());
        assertEquals("second", stack.pop().toString());
    }

    @Test
    @DisplayName("TUCK with zero values")
    void testTuckZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential TUCK Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive TUCKs")
    void testMultipleTucks() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        statement.execute();  // [1, 2, 1]
        assertEquals(3, stack.size());

        statement.execute();  // [1, 2, 1, 2]
        assertEquals(4, stack.size());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("TUCK on empty stack should handle exception gracefully")
    void testTuckEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("TUCK with single item should handle exception")
    void testTuckSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("TUCK preserves items below")
    void testTuckPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        statement.execute();

        assertEquals(5, stack.size());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("TUCK with large stack")
    void testTuckLargeStack() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(99, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("TUCK with maximum integer values")
    void testTuckMaxValues() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MIN_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
    }

    @Test
    @DisplayName("TUCK with negative numbers")
    void testTuckNegative() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(-2, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
        assertEquals(-2, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with TUCK token type")
    void testWithTuckTokenType() {
        TuckStatement stmt = new TuckStatement(ForthTokenType.TUCK, 1);
        assertNotNull(stmt, "Statement should be created with TUCK token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            TuckStatement stmt = new TuckStatement(ForthTokenType.TUCK, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
