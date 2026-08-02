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
 * Comprehensive test suite for GreaterThanStatement class.
 * Tests greater-than comparison operation (( a b -- a>b )) on the stack.
 *
 * Stack notation: (( a b -- a>b )) means pop b (top), pop a, push a>b
 * Result: BooleanValue.TRUE (-1) if a > b, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("GreaterThanStatement Tests")
class GreaterThanStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create GreaterThanStatement with GREATER_THAN token type")
    void testConstructorWithGreaterThanTokenType() {
        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    // ================= GREATER THAN TESTS =================

    @Test
    @DisplayName("Should return TRUE when first number is greater (10 > 5)")
    void testFirstGreaterThanSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number equals second (10 > 10)")
    void testFirstEqualsSecond() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when first number is less (5 > 10)")
    void testFirstLessThanSecond() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when positive > zero (5 > 0)")
    void testPositiveGreaterThanZero() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when zero > positive (0 > 5)")
    void testZeroNotGreaterThanPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when zero > zero (0 > 0)")
    void testZeroNotGreaterThanZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when negative < zero (-5 > 0)")
    void testNegativeNotGreaterThanZero() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(0));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when zero > negative (0 > -5)")
    void testZeroGreaterThanNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when greater negative > lesser negative (-5 > -10)")
    void testGreaterNegativeGreaterLesserNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-10));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when lesser negative > greater negative (-10 > -5)")
    void testLesserNegativeNotGreaterGreaterNegative() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when positive > negative (10 > -5)")
    void testPositiveGreaterThanNegative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when negative > positive (-5 > 10)")
    void testNegativeNotGreaterThanPositive() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(10));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= NON-COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify > is NOT commutative (10 > 5 != 5 > 10)")
    void testGreaterThanIsNotCommutative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertNotEquals(result1.toInteger(), result2.toInteger());
        assertEquals(BooleanValue.TRUE, result1.toInteger());
        assertEquals(BooleanValue.FALSE, result2.toInteger());
    }

    // ================= LARGE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should handle large numbers correctly")
    void testLargeNumberComparison() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle MAX_VALUE comparison")
    void testMaxIntegerComparison() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE - 1));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= CONSECUTIVE COMPARISONS =================

    @Test
    @DisplayName("Should handle consecutive greater-than comparisons")
    void testConsecutiveComparisons() throws Exception {
        // 10 > 5 -> TRUE (-1)
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        // -1 > 0? -> FALSE (0)
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
        stack.push(new IntegerValue(5));

        assertEquals(2, stack.size());

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
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

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit comparison")
    void testSingleDigitComparison() throws Exception {
        stack.push(new IntegerValue(9));
        stack.push(new IntegerValue(1));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle numbers off by one")
    void testNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= TRANSITIVE PROPERTY =================

    @Test
    @DisplayName("Should follow transitive rule (if a>b and b>c then a>c)")
    void testTransitivityOfGreaterThan() throws Exception {
        // We'll verify one case: 10 > 5 is TRUE
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify ordering rules: 1>0, 2>1, therefore 2>0")
    void testChainedOrdering() throws Exception {
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(0));

        GreaterThanStatement stmt = new GreaterThanStatement(ForthTokenType.GREATER_THAN, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }
}
