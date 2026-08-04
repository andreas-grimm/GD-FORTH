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
 * Comprehensive test suite for ZeroEqualsStatement class.
 * Tests zero-equals comparison operation (( a -- a==0 )) on the stack.
 *
 * Stack notation: (( a -- a==0 )) means pop a, push a==0
 * This is a unary operation - only one operand is required.
 * Result: BooleanValue.TRUE (-1) if a == 0, BooleanValue.FALSE (0) otherwise
 */
@DisplayName("ZeroEqualsStatement Tests")
class ZeroEqualsStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create ZeroEqualsStatement with ZERO_EQUALS token type")
    void testConstructorWithZeroEqualsTokenType() {
        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        assertEquals("", stmt.structure());
    }

    // ================= ZERO EQUALS TESTS =================

    @Test
    @DisplayName("Should return TRUE when comparing zero to zero (0 == 0)")
    void testZeroEqualsZero() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing positive to zero (5 == 0)")
    void testPositiveNotEqualsZero() throws Exception {
        stack.push(new IntegerValue(5));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing negative to zero (-5 == 0)")
    void testNegativeNotEqualsZero() throws Exception {
        stack.push(new IntegerValue(-5));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
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

            ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
            stmt.execute();

            IntegerValue result = (IntegerValue) stack.pop();
            assertEquals(BooleanValue.FALSE, result.toInteger(),
                    "Value " + value + " should not equal zero");
        }
    }

    @Test
    @DisplayName("Should return FALSE for various negative integers")
    void testVariousNegativeIntegers() throws Exception {
        int[] negativeValues = {-1, -5, -10, -100, -1000};

        for (int value : negativeValues) {
            stack.reset();
            stack.push(new IntegerValue(value));

            ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
            stmt.execute();

            IntegerValue result = (IntegerValue) stack.pop();
            assertEquals(BooleanValue.FALSE, result.toInteger(),
                    "Value " + value + " should not equal zero");
        }
    }

    // ================= LARGE NUMBER TESTS =================

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE")
    void testMaxIntegerNotEqualsZero() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE")
    void testMinIntegerNotEqualsZero() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle one (1 == 0)")
    void testOneEqualsZero() throws Exception {
        stack.push(new IntegerValue(1));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle minus one (-1 == 0)")
    void testMinusOneEqualsZero() throws Exception {
        stack.push(new IntegerValue(-1));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive zero-equals operations")
    void testConsecutiveOperations() throws Exception {
        // First: 0 == 0 -> TRUE (-1)
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        // Check result is TRUE (-1)
        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Second: -1 == 0? -> FALSE
        stack.push(new IntegerValue(-1));
        stmt.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result2.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        assertEquals(2, stack.size());

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
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
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
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
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    @Test
    @DisplayName("Should return TRUE value as -1")
    void testTrueResultValue() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE value as 0")
    void testFalseResultValue() throws Exception {
        stack.push(new IntegerValue(42));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= UNARY OPERATION TESTS =================

    @Test
    @DisplayName("Should be unary operation requiring only one operand")
    void testUnaryOperation() throws Exception {
        stack.push(new IntegerValue(0));

        assertEquals(1, stack.size());

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        assertEquals(1, stack.size());
    }

    @Test
    @DisplayName("Should work with only one stack element")
    void testWorksWithSingleElement() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= SPECIAL CASES =================

    @Test
    @DisplayName("Should verify involution property: 0==0 produces TRUE, not zero")
    void testZeroProducesTrue() throws Exception {
        stack.push(new IntegerValue(0));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertNotEquals(0, result.toInteger());
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify opposite test with non-zero")
    void testNonZeroProducesFalse() throws Exception {
        stack.push(new IntegerValue(1));

        ZeroEqualsStatement stmt = new ZeroEqualsStatement(ForthTokenType.ZERO_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }
}
