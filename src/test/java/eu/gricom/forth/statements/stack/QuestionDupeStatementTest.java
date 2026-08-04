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
 * Test suite for QuestionDupeStatement (?DUP) - conditionally duplicate top item.
 * Stack effect: ( a -- a a ) if a != 0, or ( 0 -- ) if a == 0
 * Duplicates the top item only if it's non-zero.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("?DUP (Conditional Duplicate) Stack Operation Tests")
class QuestionDupeStatementTest {

    private Stack stack;
    private QuestionDupeStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new QuestionDupeStatement(ForthTokenType.QUESTION_DUPE, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        QuestionDupeStatement stmt = new QuestionDupeStatement(ForthTokenType.QUESTION_DUPE, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        QuestionDupeStatement stmt = new QuestionDupeStatement(ForthTokenType.QUESTION_DUPE, 123);
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
    // Basic ?DUP Operation Tests - Non-Zero Values
    // ============================================================================

    @Test
    @DisplayName("?DUP with positive integer duplicates")
    void testQuestionDupePositive() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items after ?DUP of non-zero");
        assertEquals(5, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with negative integer duplicates")
    void testQuestionDupeNegative() throws Exception {
        stack.push(new IntegerValue(-42));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(-42, stack.pop().toInteger());
        assertEquals(-42, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with large positive integer duplicates")
    void testQuestionDupeLargePositive() throws Exception {
        int largeValue = 1_000_000;
        stack.push(new IntegerValue(largeValue));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(largeValue, stack.pop().toInteger());
        assertEquals(largeValue, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with maximum integer duplicates")
    void testQuestionDupeMaxValue() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MAX_VALUE, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with minimum integer duplicates")
    void testQuestionDupeMinValue() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(Integer.MIN_VALUE, stack.pop().toInteger());
        assertEquals(Integer.MIN_VALUE, stack.pop().toInteger());
    }

    // ============================================================================
    // Basic ?DUP Operation Tests - Zero Values
    // ============================================================================

    @Test
    @DisplayName("?DUP with zero does not duplicate")
    void testQuestionDupeZero() throws Exception {
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty after ?DUP of zero");
    }

    @Test
    @DisplayName("?DUP with zero on stack with other items")
    void testQuestionDupeZeroWithOtherItems() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item (zero was removed)");
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // ?DUP with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("?DUP with positive RealValue duplicates")
    void testQuestionDupeRealPositive() throws Exception {
        stack.push(new RealValue(3.14159));
        statement.execute();

        assertEquals(2, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 3.14159) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 3.14159) < 0.00001);
    }

    @Test
    @DisplayName("?DUP with negative RealValue duplicates")
    void testQuestionDupeRealNegative() throws Exception {
        stack.push(new RealValue(-2.71828));
        statement.execute();

        assertEquals(2, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - (-2.71828)) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - (-2.71828)) < 0.00001);
    }

    @Test
    @DisplayName("?DUP with zero RealValue")
    void testQuestionDupeRealZero() throws Exception {
        stack.push(new RealValue(0.0));
        statement.execute();

        assertEquals(0, stack.size(), "Stack should be empty after ?DUP of zero");
    }

    @Test
    @DisplayName("?DUP with StringValue")
    void testQuestionDupeString() throws Exception {
        stack.push(new StringValue("hello"));
        statement.execute();

        // String cannot be converted to integer, so it's popped but not duplicated
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("?DUP with empty string")
    void testQuestionDupeEmptyString() throws Exception {
        stack.push(new StringValue(""));
        statement.execute();

        // Empty string cannot be converted to integer, so it's popped but not duplicated
        assertEquals(0, stack.size());
    }

    // ============================================================================
    // Sequential ?DUP Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple ?DUPs with non-zero values")
    void testMultipleQuestionDupesNonZero() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();  // Stack: [5, 5]
        assertEquals(2, stack.size());

        statement.execute();  // Stack: [5, 5, 5]
        assertEquals(3, stack.size());
    }

    @Test
    @DisplayName("?DUP then remove then ?DUP again")
    void testQuestionDupSequence() throws Exception {
        stack.push(new IntegerValue(10));
        statement.execute();  // Stack: [10, 10]

        stack.pop();  // Remove one: [10]
        statement.execute();  // Stack: [10, 10]

        assertEquals(2, stack.size());
        assertEquals(10, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("?DUP on empty stack should handle exception gracefully")
    void testQuestionDupeEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("?DUP with many items, non-zero top")
    void testQuestionDupeWithManyItems() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();

        assertEquals(sizeBefore + 1, stack.size());
        assertEquals(9, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with many items, zero top")
    void testQuestionDupeWithManyItemsZeroTop() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        stack.push(new IntegerValue(0));
        int sizeBefore = stack.size();
        statement.execute();

        assertEquals(sizeBefore - 1, stack.size(), "Zero should be removed, not duplicated");
    }

    @Test
    @DisplayName("?DUP in conditional pattern")
    void testQuestionDupeConditional() throws Exception {
        stack.push(new IntegerValue(42));
        statement.execute();

        assertTrue(stack.size() > 0, "Non-zero should result in duplicated item on stack");
        assertEquals(42, stack.peek().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("?DUP is idempotent for zero")
    void testQuestionDupeZeroIdempotent() throws Exception {
        stack.push(new IntegerValue(0));
        statement.execute();
        statement.execute();  // Second call on empty stack

        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("?DUP with one")
    void testQuestionDupeOne() throws Exception {
        stack.push(new IntegerValue(1));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("?DUP with negative one")
    void testQuestionDupeNegativeOne() throws Exception {
        stack.push(new IntegerValue(-1));
        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(-1, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with QUESTION_DUPE token type")
    void testWithQuestionDupeTokenType() {
        QuestionDupeStatement stmt = new QuestionDupeStatement(ForthTokenType.QUESTION_DUPE, 1);
        assertNotNull(stmt, "Statement should be created with QUESTION_DUPE token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            QuestionDupeStatement stmt = new QuestionDupeStatement(ForthTokenType.QUESTION_DUPE, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
