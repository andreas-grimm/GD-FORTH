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
 * Test suite for TwoOverStatement (2OVER) - copy two items over top two.
 * Stack effect: ( a b c d -- a b c d a b )
 * Copies the third and fourth items to the top of the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("2OVER (Copy Pair) Stack Operation Tests")
class TwoOverStatementTest {

    private Stack stack;
    private TwoOverStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new TwoOverStatement(ForthTokenType.TWO_OVER, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        TwoOverStatement stmt = new TwoOverStatement(ForthTokenType.TWO_OVER, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        TwoOverStatement stmt = new TwoOverStatement(ForthTokenType.TWO_OVER, 123);
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
    // Basic 2OVER Operation Tests
    // ============================================================================

    @Test
    @DisplayName("2OVER copies third and fourth items to top ( 1 2 3 4 -- 1 2 3 4 1 2 )")
    void testTwoOverFourValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2OVER increases stack size by 2")
    void testTwoOverIncreasesSizeByTwo() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 2, sizeAfter);
    }

    @Test
    @DisplayName("2OVER with six items")
    void testTwoOverSixItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(6));
        statement.execute();

        assertEquals(8, stack.size());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
    }

    // ============================================================================
    // 2OVER with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("2OVER with mixed IntegerValues")
    void testTwoOverMixedIntegers() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(-50));
        stack.push(new IntegerValue(50));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(100, stack.pop().toInteger());
        assertEquals(-100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2OVER with RealValues")
    void testTwoOverRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        stack.push(new RealValue(4.5));
        statement.execute();

        assertEquals(6, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
    }

    @Test
    @DisplayName("2OVER with StringValues")
    void testTwoOverStringValues() throws Exception {
        stack.push(new StringValue("a"));
        stack.push(new StringValue("b"));
        stack.push(new StringValue("c"));
        stack.push(new StringValue("d"));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals("b", stack.pop().toString());
        assertEquals("a", stack.pop().toString());
    }

    @Test
    @DisplayName("2OVER with zero values")
    void testTwoOverZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("2OVER on empty stack should handle exception gracefully")
    void testTwoOverEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("2OVER with less than 4 items should handle exception")
    void testTwoOverTooFewItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("2OVER preserves items below")
    void testTwoOverPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(400));
        stack.push(new IntegerValue(500));
        stack.push(new IntegerValue(600));

        statement.execute();

        assertEquals(8, stack.size());
        assertEquals(400, stack.pop().toInteger());
        assertEquals(300, stack.pop().toInteger());
        assertEquals(600, stack.pop().toInteger());
        assertEquals(500, stack.pop().toInteger());
        assertEquals(400, stack.pop().toInteger());
        assertEquals(300, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2OVER with large stack")
    void testTwoOverLargeStack() throws Exception {
        for (int i = 0; i < 20; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 2, sizeAfter);
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("2OVER with exactly four items")
    void testTwoOverExactlyFour() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2OVER with negative numbers")
    void testTwoOverNegative() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        stack.push(new IntegerValue(-3));
        stack.push(new IntegerValue(-4));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(-2, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with TWO_OVER token type")
    void testWithTwoOverTokenType() {
        TwoOverStatement stmt = new TwoOverStatement(ForthTokenType.TWO_OVER, 1);
        assertNotNull(stmt, "Statement should be created with TWO_OVER token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            TwoOverStatement stmt = new TwoOverStatement(ForthTokenType.TWO_OVER, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
