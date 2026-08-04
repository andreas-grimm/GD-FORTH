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
 * Test suite for DupeStatement (DUP) - duplicate top stack item.
 * Stack effect: ( a -- a a )
 * Duplicates the top stack item.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("DUP (Duplicate) Stack Operation Tests")
class DupeStatementTest {

    private Stack stack;
    private DupeStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new DupeStatement(ForthTokenType.DUPE, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        DupeStatement stmt = new DupeStatement(ForthTokenType.DUPE, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        DupeStatement stmt = new DupeStatement(ForthTokenType.DUPE, 123);
        assertEquals(123, stmt.getTokenNumber(), "Token number should be 123");
    }

    @Test
    @DisplayName("getTokenNumber should handle zero token number")
    void testGetTokenNumberZero() {
        DupeStatement stmt = new DupeStatement(ForthTokenType.DUPE, 0);
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
    // Basic DUP Operation Tests
    // ============================================================================

    @Test
    @DisplayName("DUP with single integer value")
    void testDupSingleInteger() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items after DUP");
        assertEquals(5, stack.pop().toInteger(), "Top item should be 5");
        assertEquals(5, stack.pop().toInteger(), "Second item should be 5");
        assertTrue(stack.size() == 0, "Stack should be empty after popping");
    }

    @Test
    @DisplayName("DUP with multiple values on stack")
    void testDupMultipleValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        statement.execute();

        assertEquals(3, stack.size(), "Stack should have 3 items");
        assertEquals(20, stack.pop().toInteger(), "Top should be 20");
        assertEquals(20, stack.pop().toInteger(), "Second should be 20");
        assertEquals(10, stack.pop().toInteger(), "Third should be 10");
    }

    @Test
    @DisplayName("DUP with large positive integer")
    void testDupLargePositiveInteger() throws Exception {
        int largeValue = 1_000_000;
        stack.push(new IntegerValue(largeValue));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(largeValue, stack.pop().toInteger());
        assertEquals(largeValue, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP with negative integer")
    void testDupNegativeInteger() throws Exception {
        stack.push(new IntegerValue(-42));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(-42, stack.pop().toInteger());
        assertEquals(-42, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP with zero")
    void testDupZero() throws Exception {
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP with maximum integer value")
    void testDupMaxInteger() throws Exception {
        int maxValue = Integer.MAX_VALUE;
        stack.push(new IntegerValue(maxValue));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(maxValue, stack.pop().toInteger());
        assertEquals(maxValue, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP with minimum integer value")
    void testDupMinInteger() throws Exception {
        int minValue = Integer.MIN_VALUE;
        stack.push(new IntegerValue(minValue));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(minValue, stack.pop().toInteger());
        assertEquals(minValue, stack.pop().toInteger());
    }

    // ============================================================================
    // DUP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("DUP with RealValue")
    void testDupRealValue() throws Exception {
        stack.push(new RealValue(3.14159));
        statement.execute();

        assertEquals(2, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 3.14159) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 3.14159) < 0.00001);
    }

    @Test
    @DisplayName("DUP with StringValue")
    void testDupStringValue() throws Exception {
        stack.push(new StringValue("hello"));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals("hello", stack.pop().toString());
        assertEquals("hello", stack.pop().toString());
    }

    @Test
    @DisplayName("DUP with empty string")
    void testDupEmptyString() throws Exception {
        stack.push(new StringValue(""));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals("", stack.pop().toString());
        assertEquals("", stack.pop().toString());
    }

    // ============================================================================
    // Sequential DUP Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple consecutive DUP operations")
    void testMultipleConsecutiveDups() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();  // Stack: [5, 5]

        statement.execute();  // Stack: [5, 5, 5]

        assertEquals(3, stack.size(), "Stack should have 3 items");
        assertEquals(5, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP then DUP result forms pyramid")
    void testDupPyramid() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        statement.execute();  // Dup 2: [1, 2, 2]

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("DUP on empty stack should handle exception gracefully")
    void testDupEmptyStack() throws Exception {
        // Empty stack - should not throw, but print error
        statement.execute();

        // Stack should still be empty after error
        assertEquals(0, stack.size(), "Stack should remain empty after error on empty stack");
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("DUP preserves order of other stack items")
    void testDupPreservesOrder() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(300, stack.pop().toInteger());  // Duplicated item
        assertEquals(300, stack.pop().toInteger());  // Original duplicate
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DUP with alternating operations")
    void testDupWithAlternatingOperations() throws Exception {
        stack.push(new IntegerValue(7));

        DupeStatement dup1 = new DupeStatement(ForthTokenType.DUPE, 1);
        dup1.execute();  // Stack: [7, 7]
        assertEquals(2, stack.size());

        stack.pop();  // Remove one 7
        assertEquals(1, stack.size());

        DupeStatement dup2 = new DupeStatement(ForthTokenType.DUPE, 2);
        dup2.execute();  // Stack: [7, 7]
        assertEquals(2, stack.size());
    }

    // ============================================================================
    // Stack Size Tests
    // ============================================================================

    @Test
    @DisplayName("DUP increases stack size by exactly 1")
    void testDupIncreasesStackByOne() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBeforeDup = stack.size();
        statement.execute();
        int sizeAfterDup = stack.size();

        assertEquals(sizeBeforeDup + 1, sizeAfterDup, "Stack size should increase by exactly 1");
    }

    @Test
    @DisplayName("DUP maintains stack integrity with many items")
    void testDupWithManyStackItems() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(101, stack.size(), "Stack should have 101 items");
        assertEquals(99, stack.pop().toInteger(), "Top should be 99 (duplicated)");
        assertEquals(99, stack.pop().toInteger(), "Second should be 99 (original)");
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with DUPE token type")
    void testWithDupeTokenType() {
        DupeStatement stmt = new DupeStatement(ForthTokenType.DUPE, 1);
        assertNotNull(stmt, "Statement should be created with DUPE token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            DupeStatement stmt = new DupeStatement(ForthTokenType.DUPE, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
