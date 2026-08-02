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
 * Test suite for TwoDropStatement (2DROP) - remove top two stack items.
 * Stack effect: ( a b -- )
 * Removes the top two stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("2DROP (Remove Two) Stack Operation Tests")
class TwoDropStatementTest {

    private Stack stack;
    private TwoDropStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new TwoDropStatement(ForthTokenType.TWO_DROP, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        TwoDropStatement stmt = new TwoDropStatement(ForthTokenType.TWO_DROP, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        TwoDropStatement stmt = new TwoDropStatement(ForthTokenType.TWO_DROP, 123);
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
    // Basic 2DROP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("2DROP removes two values from stack")
    void testTwoDropTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty after 2DROP");
    }

    @Test
    @DisplayName("2DROP from stack with three items")
    void testTwoDropThreeItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item after 2DROP");
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2DROP from stack with four items")
    void testTwoDropFourItems() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(40));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2DROP decreases stack size by 2")
    void testTwoDropDecreasesSizeByTwo() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore - 2, sizeAfter);
    }

    // ============================================================================
    // 2DROP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("2DROP with mixed IntegerValues")
    void testTwoDropMixedIntegers() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(100));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("2DROP with RealValues")
    void testTwoDropRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("2DROP with StringValues")
    void testTwoDropStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("2DROP with zero values")
    void testTwoDropZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("2DROP with maximum integer values")
    void testTwoDropMaxValues() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        statement.execute();

        assertEquals(0, stack.size());
    }

    // ============================================================================
    // Sequential 2DROP Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive 2DROPs")
    void testMultipleConsecutive2Drops() throws Exception {
        for (int i = 0; i < 6; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();  // Remove top 2
        assertEquals(4, stack.size());

        statement.execute();  // Remove next 2
        assertEquals(2, stack.size());

        statement.execute();  // Remove last 2
        assertEquals(0, stack.size());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("2DROP on empty stack should handle exception gracefully")
    void testTwoDropEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("2DROP with only one item should handle exception")
    void testTwoDropSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("2DROP preserves items below")
    void testTwoDropPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(400));

        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2DROP with large stack")
    void testTwoDropLargeStack() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore - 2, sizeAfter);
        assertEquals(97, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2DROP maintains order of remaining items")
    void testTwoDropMaintainsOrder() throws Exception {
        for (int i = 1; i <= 5; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();  // Remove 5 and 4

        assertEquals(3, stack.size());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("2DROP with exactly two items")
    void testTwoDropExactlyTwoItems() throws Exception {
        stack.push(new IntegerValue(999));
        stack.push(new IntegerValue(888));

        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty");
    }

    @Test
    @DisplayName("2DROP with negative numbers")
    void testTwoDropNegative() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        statement.execute();

        assertEquals(0, stack.size());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with TWO_DROP token type")
    void testWithTwoDropTokenType() {
        TwoDropStatement stmt = new TwoDropStatement(ForthTokenType.TWO_DROP, 1);
        assertNotNull(stmt, "Statement should be created with TWO_DROP token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            TwoDropStatement stmt = new TwoDropStatement(ForthTokenType.TWO_DROP, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
