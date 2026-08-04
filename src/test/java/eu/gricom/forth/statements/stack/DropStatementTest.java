package eu.gricom.forth.statements.stack;

import eu.gricom.forth.error.EmptyStackException;
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
 * Test suite for DropStatement (DROP) - remove top stack item.
 * Stack effect: ( a -- )
 * Removes the top stack item.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("DROP (Remove) Stack Operation Tests")
class DropStatementTest {

    private Stack stack;
    private DropStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new DropStatement(ForthTokenType.DROP, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        DropStatement stmt = new DropStatement(ForthTokenType.DROP, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        DropStatement stmt = new DropStatement(ForthTokenType.DROP, 123);
        assertEquals(123, stmt.getTokenNumber(), "Token number should be 123");
    }

    @Test
    @DisplayName("getTokenNumber should handle zero token number")
    void testGetTokenNumberZero() {
        DropStatement stmt = new DropStatement(ForthTokenType.DROP, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number should be 0");
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
    // Basic DROP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("DROP with single value removes it from stack")
    void testDropSingleValue() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty after DROP");
    }

    @Test
    @DisplayName("DROP with two values removes only top")
    void testDropTwoValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        statement.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item after DROP");
        assertEquals(10, stack.pop().toInteger(), "Remaining value should be 10");
    }

    @Test
    @DisplayName("DROP removes only the top item")
    void testDropRemovesOnlyTop() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(2, stack.pop().toInteger(), "Top should be 2");
        assertEquals(1, stack.pop().toInteger(), "Second should be 1");
    }

    @Test
    @DisplayName("DROP with large positive integer")
    void testDropLargePositiveInteger() throws Exception {
        int largeValue = 1_000_000;
        stack.push(new IntegerValue(largeValue));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with negative integer")
    void testDropNegativeInteger() throws Exception {
        stack.push(new IntegerValue(-42));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with zero")
    void testDropZero() throws Exception {
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with maximum integer value")
    void testDropMaxInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with minimum integer value")
    void testDropMinInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        statement.execute();

        assertEquals(0, stack.size());
    }

    // ============================================================================
    // DROP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("DROP with RealValue")
    void testDropRealValue() throws Exception {
        stack.push(new RealValue(3.14159));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with StringValue")
    void testDropStringValue() throws Exception {
        stack.push(new StringValue("hello"));
        statement.execute();

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP with empty string")
    void testDropEmptyString() throws Exception {
        stack.push(new StringValue(""));
        statement.execute();

        assertEquals(0, stack.size());
    }

    // ============================================================================
    // Sequential DROP Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive DROPs remove items in order")
    void testMultipleConsecutiveDrop() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        statement.execute();  // Remove 3
        assertEquals(2, stack.size());

        statement.execute();  // Remove 2
        assertEquals(1, stack.size());

        statement.execute();  // Remove 1
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DROP maintains proper order of remaining items")
    void testDropMaintainsOrder() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(400));

        statement.execute();  // Remove 400

        assertEquals(3, stack.size());
        assertEquals(300, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("DROP on empty stack should handle exception gracefully")
    void testDropEmptyStack() throws Exception {
        // Empty stack - should not throw, but print error
        statement.execute();

        // Stack should still be empty
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("DROP with large stack preserves lower items")
    void testDropPreservesLowerItems() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBeforeDrop = stack.size();
        statement.execute();
        int sizeAfterDrop = stack.size();

        assertEquals(sizeBeforeDrop - 1, sizeAfterDrop);
        assertEquals(8, stack.pop().toInteger(), "Top should now be 8");
    }

    @Test
    @DisplayName("DROP with alternating push and drop")
    void testDropWithAlternatingOperations() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        statement.execute();  // Stack: [1]

        stack.push(new IntegerValue(3));  // Stack: [1, 3]
        assertEquals(2, stack.size());

        statement.execute();  // Stack: [1]
        assertEquals(1, stack.size());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Stack Size Tests
    // ============================================================================

    @Test
    @DisplayName("DROP decreases stack size by exactly 1")
    void testDropDecreasesStackByOne() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBeforeDrop = stack.size();
        statement.execute();
        int sizeAfterDrop = stack.size();

        assertEquals(sizeBeforeDrop - 1, sizeAfterDrop, "Stack size should decrease by exactly 1");
    }

    @Test
    @DisplayName("DROP maintains stack integrity with many items")
    void testDropWithManyStackItems() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(99, stack.size(), "Stack should have 99 items");
        assertEquals(98, stack.pop().toInteger(), "Top should be 98");
    }

    @Test
    @DisplayName("DROP until stack is empty")
    void testDropUntilEmpty() throws Exception {
        int itemsToPush = 5;
        for (int i = 0; i < itemsToPush; i++) {
            stack.push(new IntegerValue(i));
        }

        for (int i = 0; i < itemsToPush; i++) {
            statement.execute();
        }

        assertEquals(0, stack.size(), "Stack should be empty after dropping all items");
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with DROP token type")
    void testWithDropTokenType() {
        DropStatement stmt = new DropStatement(ForthTokenType.DROP, 1);
        assertNotNull(stmt, "Statement should be created with DROP token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            DropStatement stmt = new DropStatement(ForthTokenType.DROP, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("DROP with only remaining item")
    void testDropOnlyRemainingItem() throws Exception {
        stack.push(new IntegerValue(999));
        assertEquals(1, stack.size());

        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty");
    }

    @Test
    @DisplayName("DROP after complex sequence")
    void testDropAfterComplexSequence() throws Exception {
        for (int i = 1; i <= 5; i++) {
            stack.push(new IntegerValue(i));
        }
        // Stack: [1, 2, 3, 4, 5]

        statement.execute();  // Remove 5
        assertEquals(4, stack.size());

        statement.execute();  // Remove 4
        assertEquals(3, stack.size());

        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }
}
