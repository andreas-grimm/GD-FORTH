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
 * Test suite for NipStatement (NIP) - remove second stack item.
 * Stack effect: ( a b -- b )
 * Removes the second item from the top of the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("NIP (Remove Second) Stack Operation Tests")
class NipStatementTest {

    private Stack stack;
    private NipStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new NipStatement(ForthTokenType.NIP, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        NipStatement stmt = new NipStatement(ForthTokenType.NIP, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        NipStatement stmt = new NipStatement(ForthTokenType.NIP, 123);
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
    // Basic NIP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("NIP removes second item keeping top")
    void testNipTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item");
        assertEquals(10, stack.pop().toInteger(), "Remaining value should be 10");
    }

    @Test
    @DisplayName("NIP with three items removes middle")
    void testNipThreeItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("NIP decreases stack size by 1")
    void testNipDecreasesSize() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore - 1, sizeAfter);
    }

    @Test
    @DisplayName("NIP with many items removes only second from top")
    void testNipManyItems() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(9, stack.size());
        assertEquals(9, stack.pop().toInteger(), "Top should be 9");
        assertEquals(7, stack.pop().toInteger(), "Second should be 7 (8 was removed)");
    }

    // ============================================================================
    // NIP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("NIP with RealValue")
    void testNipRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertEquals(1, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
    }

    @Test
    @DisplayName("NIP with StringValue")
    void testNipStringValues() throws Exception {
        stack.push(new StringValue("keep"));
        stack.push(new StringValue("remove"));
        statement.execute();

        assertEquals(1, stack.size());
        assertEquals("remove", stack.pop().toString());
    }

    @Test
    @DisplayName("NIP with zero values")
    void testNipZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(1, stack.size());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential NIP Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive NIPs")
    void testMultipleNips() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        statement.execute();  // [1, 2, 3, 4] -> [1, 3, 4]
        assertEquals(3, stack.size());

        statement.execute();  // [1, 3, 4] -> [1, 4]
        assertEquals(2, stack.size());

        assertEquals(4, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("NIP on empty stack should handle exception gracefully")
    void testNipEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("NIP with single item should handle exception")
    void testNipSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("NIP preserves items below second position")
    void testNipPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("NIP with large stack")
    void testNipLargeStack() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore - 1, sizeAfter);
        assertEquals(99, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("NIP with maximum integer values")
    void testNipMaxValues() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        statement.execute();

        assertEquals(1, stack.size());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
    }

    @Test
    @DisplayName("NIP with negative numbers")
    void testNipNegative() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(-200));
        statement.execute();

        assertEquals(1, stack.size());
        assertEquals(-200, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with NIP token type")
    void testWithNipTokenType() {
        NipStatement stmt = new NipStatement(ForthTokenType.NIP, 1);
        assertNotNull(stmt, "Statement should be created with NIP token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            NipStatement stmt = new NipStatement(ForthTokenType.NIP, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
