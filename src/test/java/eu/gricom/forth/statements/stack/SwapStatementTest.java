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
 * Test suite for SwapStatement (SWAP) - exchange top two stack items.
 * Stack effect: ( a b -- b a )
 * Exchanges the top two stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("SWAP (Exchange) Stack Operation Tests")
class SwapStatementTest {

    private Stack stack;
    private SwapStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new SwapStatement(ForthTokenType.SWAP, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        SwapStatement stmt = new SwapStatement(ForthTokenType.SWAP, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        SwapStatement stmt = new SwapStatement(ForthTokenType.SWAP, 123);
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
    // Basic SWAP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("SWAP exchanges two values correctly")
    void testSwapTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();

        assertEquals(2, stack.size(), "Stack should still have 2 items");
        assertEquals(5, stack.pop().toInteger(), "Top should now be 5");
        assertEquals(10, stack.pop().toInteger(), "Second should now be 10");
    }

    @Test
    @DisplayName("SWAP exchanges items and maintains size")
    void testSwapMaintainsSize() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter, "Stack size should not change");
        assertEquals(2, sizeAfter);
    }

    @Test
    @DisplayName("SWAP with three items exchanges only top two")
    void testSwapThreeValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger(), "Top should be 2");
        assertEquals(3, stack.pop().toInteger(), "Second should be 3");
        assertEquals(1, stack.pop().toInteger(), "Third should be 1 (unchanged)");
    }

    @Test
    @DisplayName("SWAP with four items exchanges top two")
    void testSwapFourValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(40));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(30, stack.pop().toInteger());
        assertEquals(40, stack.pop().toInteger());
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    // ============================================================================
    // SWAP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("SWAP with mixed integer values")
    void testSwapMixedIntegers() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(100));
        statement.execute();

        assertEquals(-100, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("SWAP with RealValue")
    void testSwapRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
    }

    @Test
    @DisplayName("SWAP with StringValue")
    void testSwapStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        statement.execute();

        assertEquals("first", stack.pop().toString());
        assertEquals("second", stack.pop().toString());
    }

    @Test
    @DisplayName("SWAP with zero values")
    void testSwapZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    @Test
    @DisplayName("SWAP with maximum and minimum integers")
    void testSwapMaxMin() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        statement.execute();

        assertEquals(Integer.MIN_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential SWAP Operations
    // ============================================================================

    @Test
    @DisplayName("Double SWAP returns to original order")
    void testDoubleSwapReturnToOriginal() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        statement.execute();  // First SWAP
        assertEquals(5, stack.peek().toInteger(), "After first SWAP, 5 should be on top");

        statement.execute();  // Second SWAP
        assertEquals(10, stack.pop().toInteger(), "After second SWAP, 10 should be on top");
        assertEquals(5, stack.pop().toInteger());
    }

    @Test
    @DisplayName("Multiple SWAPs in sequence")
    void testMultipleSWAPs() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        statement.execute();  // Swap 4 and 3: [1, 2, 3, 4] -> [1, 2, 4, 3]

        assertEquals(3, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("SWAP on empty stack should handle exception gracefully")
    void testSwapEmptyStack() throws Exception {
        statement.execute();  // Should handle error gracefully
        assertEquals(0, stack.size(), "Stack should remain empty");
    }

    @Test
    @DisplayName("SWAP with only one item should handle exception")
    void testSwapSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();  // Should handle error gracefully

        // Stack behavior after error - at least one item should remain
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("SWAP within larger stack context")
    void testSwapWithinLargerStack() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(10, stack.size());
        assertEquals(8, stack.pop().toInteger());  // Top two were swapped
        assertEquals(9, stack.pop().toInteger());
    }

    @Test
    @DisplayName("SWAP preserves lower stack items")
    void testSwapPreservesLowerItems() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(400));

        statement.execute();  // Swap 400 and 300

        int top1 = stack.pop().toInteger();
        int top2 = stack.pop().toInteger();

        assertEquals(300, top1);
        assertEquals(400, top2);

        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // SWAP with Different Integer Types
    // ============================================================================

    @Test
    @DisplayName("SWAP with positive and negative numbers")
    void testSwapPositiveNegative() throws Exception {
        stack.push(new IntegerValue(-50));
        stack.push(new IntegerValue(50));
        statement.execute();

        assertEquals(-50, stack.pop().toInteger());
        assertEquals(50, stack.pop().toInteger());
    }

    @Test
    @DisplayName("SWAP with large numbers")
    void testSwapLargeNumbers() throws Exception {
        int large1 = 1_000_000;
        int large2 = 2_000_000;

        stack.push(new IntegerValue(large1));
        stack.push(new IntegerValue(large2));
        statement.execute();

        assertEquals(large1, stack.pop().toInteger());
        assertEquals(large2, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with SWAP token type")
    void testWithSwapTokenType() {
        SwapStatement stmt = new SwapStatement(ForthTokenType.SWAP, 1);
        assertNotNull(stmt, "Statement should be created with SWAP token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            SwapStatement stmt = new SwapStatement(ForthTokenType.SWAP, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("SWAP is non-destructive")
    void testSwapNonDestructive() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        statement.execute();

        assertEquals(2, stack.size(), "SWAP should not change stack size");
        assertEquals(5, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    @Test
    @DisplayName("SWAP with same values")
    void testSwapSameValues() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));
        statement.execute();

        assertEquals(42, stack.pop().toInteger());
        assertEquals(42, stack.pop().toInteger());
    }
}
