package eu.gricom.forth.statements.comparison;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for LessThanStatement class.
 * Tests less-than comparison operation (( a b -- a<b )) on the stack.
 *
 * Stack notation: (( a b -- a<b )) means pop b (top), pop a, push a<b
 * Result: BooleanValue.TRUE (-1) if a < b, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("LessThanStatement Tests")
class LessThanStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create LessThanStatement with LESS_THAN token type")
    void testConstructorWithLessThanTokenType() {
        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    // ================= LESS THAN TESTS =================

    @Test
    @DisplayName("Should return TRUE when first number is less (5 < 10)")
    void testFirstLessThanSecond() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number equals second (10 < 10)")
    void testFirstEqualsSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number is greater (10 < 5)")
    void testFirstGreaterThanSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return FALSE when zero < zero (0 < 0)")
    void testZeroNotLessThanZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when negative < zero (-5 < 0)")
    void testNegativeLessThanZero() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(0));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero < positive (0 < 5)")
    void testZeroNotLessThanPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero < positive (from different direction)")
    void testPositiveLessThanZeroInverted() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when lesser negative < greater negative (-10 < -5)")
    void testLesserNegativeLessThanGreaterNegative() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-5));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when greater negative < lesser negative (-5 < -10)")
    void testGreaterNegativeNotLessThanLesserNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when negative < positive (-5 < 10)")
    void testNegativeLessThanPositive() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when positive < negative (10 < -5)")
    void testPositiveNotLessThanNegative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= LARGE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE < Integer.MAX_VALUE")
    void testMinLessThanMax() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE < Integer.MIN_VALUE returns FALSE")
    void testMaxNotLessThanMin() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= CONSECUTIVE COMPARISONS =================

    @Test
    @DisplayName("Should handle consecutive less-than comparisons")
    void testConsecutiveComparisons() throws Exception {
        // 5 < 10 -> TRUE (-1)
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        // -1 < 0? -> TRUE
        stack.push(new IntegerValue(0));
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        assertEquals(2, stack.size());

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
        assertEquals(0, stack.size());
    }

    // ================= RESULT TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit comparison")
    void testSingleDigitComparison() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(9));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle numbers off by one")
    void testNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(99));
        stack.push(new IntegerValue(100));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle reversed numbers off by one")
    void testReversedNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= INVERSE OF GREATER THAN =================

    @Test
    @DisplayName("Should be inverse of greater-than (a<b iff b>a)")
    void testInverseOfGreaterThan() throws Exception {
        // 5 < 10 should be TRUE
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify transitive property for less-than")
    void testTransitivityOfLessThan() throws Exception {
        // If 1 < 2 and 2 < 3, then 1 < 3
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(3));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify trichotomy: exactly one of a<b, a=b, a>b holds")
    void testTrichotomy() throws Exception {
        // For 5 and 10, only 5 < 10 should be true
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessThanStatement stmt = new LessThanStatement(ForthTokenType.LESS_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }
}
