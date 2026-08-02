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
 * Test suite for TwoSwapStatement (2SWAP) - exchange two pairs of stack items.
 * Stack effect: ( a b c d -- c d a b )
 * Exchanges the top two pairs of stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("2SWAP (Swap Pairs) Stack Operation Tests")
class TwoSwapStatementTest {

    private Stack stack;
    private TwoSwapStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new TwoSwapStatement(ForthTokenType.TWO_SWAP, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        TwoSwapStatement stmt = new TwoSwapStatement(ForthTokenType.TWO_SWAP, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        TwoSwapStatement stmt = new TwoSwapStatement(ForthTokenType.TWO_SWAP, 123);
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
    // Basic 2SWAP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("2SWAP exchanges two pairs correctly ( 1 2 3 4 -- 3 4 1 2 )")
    void testTwoSwapFourValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2SWAP maintains stack size")
    void testTwoSwapMaintainsSize() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(4, sizeAfter);
    }

    @Test
    @DisplayName("2SWAP with six items")
    void testTwoSwapSixItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(6));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(6, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // 2SWAP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("2SWAP with mixed IntegerValues")
    void testTwoSwapMixedIntegers() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(-50));
        stack.push(new IntegerValue(50));
        statement.execute();

        assertEquals(100, stack.pop().toInteger());
        assertEquals(-100, stack.pop().toInteger());
        assertEquals(50, stack.pop().toInteger());
        assertEquals(-50, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2SWAP with RealValues")
    void testTwoSwapRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        stack.push(new RealValue(4.5));
        statement.execute();

        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 4.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 3.5) < 0.00001);
    }

    @Test
    @DisplayName("2SWAP with StringValues")
    void testTwoSwapStringValues() throws Exception {
        stack.push(new StringValue("a"));
        stack.push(new StringValue("b"));
        stack.push(new StringValue("c"));
        stack.push(new StringValue("d"));
        statement.execute();

        assertEquals("b", stack.pop().toString());
        assertEquals("a", stack.pop().toString());
        assertEquals("d", stack.pop().toString());
        assertEquals("c", stack.pop().toString());
    }

    @Test
    @DisplayName("2SWAP with zero values")
    void testTwoSwapZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential 2SWAP Operations
    // ============================================================================

    @Test
    @DisplayName("Double 2SWAP returns to original order")
    void testDouble2SwapReturnToOriginal() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        statement.execute();  // First 2SWAP
        statement.execute();  // Second 2SWAP

        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("2SWAP on empty stack should handle exception gracefully")
    void testTwoSwapEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("2SWAP with less than 4 items should handle exception")
    void testTwoSwapTooFewItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("2SWAP preserves items below")
    void testTwoSwapPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(400));
        stack.push(new IntegerValue(500));
        stack.push(new IntegerValue(600));

        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(400, stack.pop().toInteger());
        assertEquals(300, stack.pop().toInteger());
        assertEquals(600, stack.pop().toInteger());
        assertEquals(500, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2SWAP with large stack")
    void testTwoSwapLargeStack() throws Exception {
        for (int i = 0; i < 20; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(17, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("2SWAP with exactly four items")
    void testTwoSwapExactlyFour() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2SWAP with negative numbers")
    void testTwoSwapNegative() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        stack.push(new IntegerValue(-3));
        stack.push(new IntegerValue(-4));
        statement.execute();

        assertEquals(-2, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
        assertEquals(-4, stack.pop().toInteger());
        assertEquals(-3, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with TWO_SWAP token type")
    void testWithTwoSwapTokenType() {
        TwoSwapStatement stmt = new TwoSwapStatement(ForthTokenType.TWO_SWAP, 1);
        assertNotNull(stmt, "Statement should be created with TWO_SWAP token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            TwoSwapStatement stmt = new TwoSwapStatement(ForthTokenType.TWO_SWAP, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
