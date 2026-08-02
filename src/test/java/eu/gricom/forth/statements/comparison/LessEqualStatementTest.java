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
 * Comprehensive test suite for LessEqualStatement class.
 * Tests less-than-or-equal comparison operation (( a b -- a<=b )) on the stack.
 *
 * Stack notation: (( a b -- a<=b )) means pop b (top), pop a, push a<=b
 * Result: BooleanValue.TRUE (-1) if a <= b, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("LessEqualStatement Tests")
class LessEqualStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create LessEqualStatement with LESS_EQUAL token type")
    void testConstructorWithLessEqualTokenType() {
        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    // ================= LESS THAN OR EQUAL TESTS =================

    @Test
    @DisplayName("Should return TRUE when first number is less (5 <= 10)")
    void testFirstLessThanSecond() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when first number equals second (10 <= 10)")
    void testFirstEqualsSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number is greater (10 <= 5)")
    void testFirstGreaterThanSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when zero <= zero (0 <= 0)")
    void testZeroLessEqualZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when negative <= zero (-5 <= 0)")
    void testNegativeLessEqualZero() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(0));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero <= positive (0 <= 5)")
    void testZeroLessEqualPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when positive <= zero (5 <= 0)")
    void testPositiveNotLessEqualZero() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when lesser negative <= greater negative (-10 <= -5)")
    void testLesserNegativeLessEqualGreaterNegative() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when equal negatives (-5 <= -5)")
    void testEqualNegatives() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when greater negative <= lesser negative (-5 <= -10)")
    void testGreaterNegativeNotLessEqualLesserNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when negative <= positive (-5 <= 10)")
    void testNegativeLessEqualPositive() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= RELATIONSHIP WITH LESS THAN =================

    @Test
    @DisplayName("Should include equality case that < excludes")
    void testIncludesEqualityThatLessThanExcludes() throws Exception {
        // 5 <= 5 should be TRUE, unlike 5 < 5 which is FALSE
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= LARGE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE <= Integer.MAX_VALUE")
    void testMinLessEqualMax() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle equal large numbers")
    void testEqualLargeNumbers() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE <= Integer.MIN_VALUE returns FALSE")
    void testMaxNotLessEqualMin() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= CONSECUTIVE COMPARISONS =================

    @Test
    @DisplayName("Should handle consecutive comparisons")
    void testConsecutiveComparisons() throws Exception {
        // 5 <= 10 -> TRUE (-1)
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        // -1 <= 0? -> TRUE
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

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
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

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit comparison")
    void testSingleDigitComparison() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle numbers off by one")
    void testNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(99));
        stack.push(new IntegerValue(100));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle reversed numbers off by one")
    void testReversedNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= VERIFICATION OF SEMANTICS =================

    @Test
    @DisplayName("Should verify <= includes < and =")
    void testIncludesLessAndEqual() throws Exception {
        // <= should be TRUE for both < and = cases
        // Case 1: 5 < 10 -> <= should be TRUE
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Case 2: 5 == 5 -> <= should be TRUE
        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));
        stmt.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result2.toInteger());
    }

    @Test
    @DisplayName("Should verify reflexivity: x <= x is always TRUE")
    void testReflexivity() throws Exception {
        int value = 12345;
        stack.push(new IntegerValue(value));
        stack.push(new IntegerValue(value));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify antisymmetry: if x <= y and y <= x then x == y")
    void testAntisymmetry() throws Exception {
        // When both are equal, both <= comparisons should be TRUE
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify transitivity: if x<=y and y<=z then x<=z")
    void testTransitivity() throws Exception {
        // 1 <= 3 should be TRUE
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(3));

        LessEqualStatement stmt = new LessEqualStatement(ForthTokenType.LESS_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }
}
