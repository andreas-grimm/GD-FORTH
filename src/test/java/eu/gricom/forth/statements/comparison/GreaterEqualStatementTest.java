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
 * Comprehensive test suite for GreaterEqualStatement class.
 * Tests greater-than-or-equal comparison operation (( a b -- a>=b )) on the stack.
 *
 * Stack notation: (( a b -- a>=b )) means pop b (top), pop a, push a>=b
 * Result: BooleanValue.TRUE (-1) if a >= b, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("GreaterEqualStatement Tests")
class GreaterEqualStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create GreaterEqualStatement with GREATER_EQUAL token type")
    void testConstructorWithGreaterEqualTokenType() {
        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    // ================= GREATER THAN OR EQUAL TESTS =================

    @Test
    @DisplayName("Should return TRUE when first number is greater (10 >= 5)")
    void testFirstGreaterThanSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when first number equals second (10 >= 10)")
    void testFirstEqualsSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number is less (5 >= 10)")
    void testFirstLessThanSecond() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when positive >= zero (5 >= 0)")
    void testPositiveGreaterEqualZero() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero >= zero (0 >= 0)")
    void testZeroGreaterEqualZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when zero >= positive (0 >= 5)")
    void testZeroNotGreaterEqualPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero >= negative (0 >= -5)")
    void testZeroGreaterEqualNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when negative >= zero (-5 >= 0)")
    void testNegativeNotGreaterEqualZero() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(0));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when greater negative >= lesser negative (-5 >= -10)")
    void testGreaterNegativeGreaterEqualLesserNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-10));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when equal negatives (-5 >= -5)")
    void testEqualNegatives() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when lesser negative >= greater negative (-10 >= -5)")
    void testLesserNegativeNotGreaterEqualGreaterNegative() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when positive >= negative (10 >= -5)")
    void testPositiveGreaterEqualNegative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= RELATIONSHIP WITH EQUALITY =================

    @Test
    @DisplayName("Should include equality case that > excludes")
    void testIncludesEqualityThatGreaterExcludes() throws Exception {
        // 5 >= 5 should be TRUE, unlike 5 > 5 which is FALSE
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= LARGE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should handle large numbers correctly")
    void testLargeNumberComparison() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle equal large numbers")
    void testEqualLargeNumbers() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= CONSECUTIVE COMPARISONS =================

    @Test
    @DisplayName("Should handle consecutive comparisons")
    void testConsecutiveComparisons() throws Exception {
        // 10 >= 5 -> TRUE (-1)
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        // -1 >= 0? -> FALSE
        stack.push(new IntegerValue(0));
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        assertEquals(2, stack.size());

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
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
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit comparison")
    void testSingleDigitComparison() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle numbers off by one")
    void testNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle reversedcomparison")
    void testReversedNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(99));
        stack.push(new IntegerValue(100));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= RELATIONSHIP VALIDATION =================

    @Test
    @DisplayName("Should verify >= includes > and =")
    void testIncludesGreaterAndEqual() throws Exception {
        // >= should be TRUE for both > and = cases
        // Case 1: 10 > 5 -> >= should be TRUE
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterEqualStatement stmt = new GreaterEqualStatement(ForthTokenType.GREATER_EQUAL, 0);
        stmt.execute();

        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Case 2: 5 == 5 -> >= should be TRUE
        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));
        stmt.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result2.toInteger());
    }
}
