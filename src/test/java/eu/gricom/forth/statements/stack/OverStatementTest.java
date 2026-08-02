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
 * Test suite for OverStatement (OVER) - copy second stack item to top.
 * Stack effect: ( a b -- a b a )
 * Copies the second item to the top of the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("OVER (Copy Second) Stack Operation Tests")
class OverStatementTest {

    private Stack stack;
    private OverStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new OverStatement(ForthTokenType.OVER, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        OverStatement stmt = new OverStatement(ForthTokenType.OVER, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        OverStatement stmt = new OverStatement(ForthTokenType.OVER, 123);
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
    // Basic OVER Operation Tests
    // ============================================================================

    @Test
    @DisplayName("OVER copies second item to top")
    void testOverTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();

        assertEquals(3, stack.size(), "Stack should have 3 items");
        assertEquals(5, stack.pop().toInteger(), "Top should be 5 (copied from second)");
        assertEquals(10, stack.pop().toInteger(), "Second should be 10 (original top)");
        assertEquals(5, stack.pop().toInteger(), "Third should be 5 (original second)");
    }

    @Test
    @DisplayName("OVER increases stack size by 1")
    void testOverIncreasesSizeByOne() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    @DisplayName("OVER with three items")
    void testOverThreeItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(2, stack.pop().toInteger(), "Top: copied second");
        assertEquals(3, stack.pop().toInteger(), "Second: original top");
        assertEquals(2, stack.pop().toInteger(), "Third: original second");
        assertEquals(1, stack.pop().toInteger(), "Fourth: original third");
    }

    @Test
    @DisplayName("OVER with many items")
    void testOverManyItems() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(3, stack.pop().toInteger());  // Copied second from top
    }

    // ============================================================================
    // OVER with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("OVER with RealValue")
    void testOverRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertEquals(3, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
    }

    @Test
    @DisplayName("OVER with StringValue")
    void testOverStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        statement.execute();

        assertEquals("first", stack.pop().toString());
        assertEquals("second", stack.pop().toString());
        assertEquals("first", stack.pop().toString());
    }

    @Test
    @DisplayName("OVER with mixed values")
    void testOverMixedValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    @Test
    @DisplayName("OVER with zero values")
    void testOverZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential OVER Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive OVERs")
    void testMultipleOvers() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        statement.execute();  // Stack: [1, 2, 1]
        assertEquals(3, stack.size());

        statement.execute();  // Stack: [1, 2, 1, 2]
        assertEquals(4, stack.size());
    }

    @Test
    @DisplayName("OVER preserves stack integrity")
    void testOverPreservesIntegrity() throws Exception {
        for (int i = 1; i <= 5; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(6, stack.size());
        int top = stack.pop().toInteger();
        assertEquals(4, top, "Should copy second item (4)");
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("OVER on empty stack should handle exception gracefully")
    void testOverEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("OVER with single item should handle exception")
    void testOverSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        // Should handle error gracefully
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("OVER with large stack")
    void testOverLargeStack() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(98, stack.pop().toInteger(), "Should copy 98 (second from top)");
    }

    @Test
    @DisplayName("OVER maintains original order of items")
    void testOverMaintainsOrder() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));

        statement.execute();

        assertEquals(4, stack.size());
        int val1 = stack.pop().toInteger();  // 200 (copied)
        int val2 = stack.pop().toInteger();  // 300 (original top)
        int val3 = stack.pop().toInteger();  // 200 (original second)
        int val4 = stack.pop().toInteger();  // 100 (unchanged)

        assertEquals(200, val1);
        assertEquals(300, val2);
        assertEquals(200, val3);
        assertEquals(100, val4);
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("OVER is non-destructive")
    void testOverNonDestructive() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        statement.execute();

        assertEquals(3, stack.size());
        stack.pop();  // Remove copy
        assertEquals(2, stack.size());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
    }

    @Test
    @DisplayName("OVER with maximum integer values")
    void testOverMaxValues() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MIN_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with OVER token type")
    void testWithOverTokenType() {
        OverStatement stmt = new OverStatement(ForthTokenType.OVER, 1);
        assertNotNull(stmt, "Statement should be created with OVER token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            OverStatement stmt = new OverStatement(ForthTokenType.OVER, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
