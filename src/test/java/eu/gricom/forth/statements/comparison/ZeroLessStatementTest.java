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
 * Comprehensive test suite for ZeroLessStatement class.
 * Tests zero-less comparison operation (( a -- a<0 )) on the stack.
 *
 * Stack notation: (( a -- a<0 )) means pop a, push a<0
 * This is a unary operation - only one operand is required.
 * Result: BooleanValue.TRUE (-1) if a < 0, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("ZeroLessStatement Tests")
class ZeroLessStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create ZeroLessStatement with ZERO_LESS token type")
    void testConstructorWithZeroLessTokenType() {
        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    // ================= ZERO LESS TESTS =================

    @Test
    @DisplayName("Should return TRUE when comparing negative to zero (-5 < 0)")
    void testNegativeLessZero() throws Exception {
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing zero to zero (0 < 0)")
    void testZeroNotLessZero() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing positive to zero (5 < 0)")
    void testPositiveNotLessZero() throws Exception {
        stack.push(new IntegerValue(5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= POSITIVE AND NEGATIVE TESTS =================

    @Test
    @DisplayName("Should return FALSE for various positive integers")
    void testVariousPositiveIntegers() throws Exception {
        int[] positiveValues = {1, 5, 10, 100, 1000};

        for (int value : positiveValues) {
            stack.reset();
            stack.push(new IntegerValue(value));

            ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
            stmt.execute();

            IntegerValue result = (IntegerValue) stack.pop();
            assertEquals(BooleanValue.FALSE, result.toInteger(),
                    "Value " + value + " should not be less than zero");
        }
    }

    @Test
    @DisplayName("Should return TRUE for various negative integers")
    void testVariousNegativeIntegers() throws Exception {
        int[] negativeValues = {-1, -5, -10, -100, -1000};

        for (int value : negativeValues) {
            stack.reset();
            stack.push(new IntegerValue(value));

            ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
            stmt.execute();

            IntegerValue result = (IntegerValue) stack.pop();
            assertEquals(BooleanValue.TRUE, result.toInteger(),
                    "Value " + value + " should be less than zero");
        }
    }

    // ================= LARGE NUMBER TESTS =================

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE")
    void testMaxIntegerNotLessZero() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE")
    void testMinIntegerLessZero() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle one (1 < 0)")
    void testOneNotLessZero() throws Exception {
        stack.push(new IntegerValue(1));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle minus one (-1 < 0)")
    void testMinusOneLessZero() throws Exception {
        stack.push(new IntegerValue(-1));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive zero-less operations")
    void testConsecutiveOperations() throws Exception {
        // First: -5 < 0 -> TRUE (-1)
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        // Check result is TRUE (-1)
        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Second: -1 < 0? -> TRUE
        stack.push(new IntegerValue(-1));
        stmt.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result2.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(-10));

        assertEquals(2, stack.size());

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        // Should pop one and push one result
        assertEquals(2, stack.size());

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());

        IntegerValue remaining = (IntegerValue) stack.pop();
        assertEquals(5, remaining.toInteger());
    }

    @Test
    @DisplayName("Should preserve stack below operated element")
    void testStackPreservation() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        // Top should be result (TRUE)
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());

        // Next should still be 100
        IntegerValue preserved = (IntegerValue) stack.pop();
        assertEquals(100, preserved.toInteger());
    }

    // ================= RESULT TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    @Test
    @DisplayName("Should return TRUE value as -1 for negative")
    void testTrueResultValue() throws Exception {
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE value as 0 for non-negative")
    void testFalseResultValue() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= UNARY OPERATION TESTS =================

    @Test
    @DisplayName("Should be unary operation requiring only one operand")
    void testUnaryOperation() throws Exception {
        stack.push(new IntegerValue(-5));

        assertEquals(1, stack.size());

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        assertEquals(1, stack.size());
    }

    @Test
    @DisplayName("Should work with only one stack element")
    void testWorksWithSingleElement() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(-5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= SIGN-BASED TESTS =================

    @Test
    @DisplayName("Should verify negative numbers always produce TRUE")
    void testNegativeAlwaysTrue() throws Exception {
        stack.push(new IntegerValue(-42));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify zero produces FALSE")
    void testZeroProducesFalse() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify positive numbers always produce FALSE")
    void testPositiveAlwaysFalse() throws Exception {
        stack.push(new IntegerValue(42));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should be useful for sign testing (negative numbers)")
    void testSignTesting() throws Exception {
        // Test for negative sign
        stack.push(new IntegerValue(-100));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Negative number should produce TRUE
        assertTrue(result.toInteger() == BooleanValue.TRUE);
    }

    @Test
    @DisplayName("Should be opposite of ZeroGreaterStatement")
    void testOppositeOfZeroGreater() throws Exception {
        // For positive: ZeroGreater -> TRUE, ZeroLess -> FALSE
        stack.push(new IntegerValue(5));

        ZeroLessStatement stmt = new ZeroLessStatement(ForthTokenType.ZERO_LESS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }
}
