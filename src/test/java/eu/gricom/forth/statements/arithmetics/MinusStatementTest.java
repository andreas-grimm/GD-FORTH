package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MinusStatement class.
 * Tests arithmetic subtraction operation (( a b -- a-b )) on the stack.
 *
 * Stack notation: (( a b -- a-b )) means pop b (top), pop a, push a-b
 */
@DisplayName("MinusStatement Tests")
class MinusStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create MinusStatement with MINUS token type")
    void testConstructorWithMinusTokenType() {
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create MinusStatement with token number")
    void testConstructorWithTokenNumber() {
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should handle different token numbers")
    void testConstructorWithVariousTokenNumbers() {
        assertEquals(0, new MinusStatement(ForthTokenType.MINUS, 0).getTokenNumber());
        assertEquals(1, new MinusStatement(ForthTokenType.MINUS, 1).getTokenNumber());
        assertEquals(100, new MinusStatement(ForthTokenType.MINUS, 100).getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number via getTokenNumber()")
    void testGetTokenNumber() {
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        assertEquals("", stmt.structure());
    }

    // ================= BASIC SUBTRACTION OPERATIONS =================

    @Test
    @DisplayName("Should subtract two positive integers (10 - 5 = 5)")
    void testSubtractTwoPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should subtract correctly with reversed operands (5 - 10 = -5)")
    void testSubtractReversedOperands() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should subtract positive from negative")
    void testSubtractPositiveFromNegative() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-15, result.toInteger());
    }

    @Test
    @DisplayName("Should subtract negative from positive")
    void testSubtractNegativeFromPositive() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should subtract negative from negative (result can be positive)")
    void testSubtractNegativeFromNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-10));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Stack: push -5, push -10. Pop -10, pop -5. Result: -5 - (-10) = 5
        assertEquals(5, result.toInteger());
    }

    // ================= ZERO OPERATIONS =================

    @Test
    @DisplayName("Should handle subtraction with zero (0 - 5 = -5)")
    void testSubtractFromZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle subtraction of zero (5 - 0 = 5)")
    void testSubtractZero() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle subtraction of zero from zero (0 - 0 = 0)")
    void testSubtractZeroFromZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle 1 - 0 = 1")
    void testOneMinusZero() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(0));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }

    @Test
    @DisplayName("Should handle 0 - 1 = -1")
    void testZeroMinusOne() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(1));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    // ================= LARGE NUMBER OPERATIONS =================

    @Test
    @DisplayName("Should handle large positive numbers")
    void testSubtractLargePositiveNumbers() throws Exception {
        stack.push(new IntegerValue(2000000));
        stack.push(new IntegerValue(1000000));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1000000, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE - 1")
    void testSubtractFromMaxInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(1));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(Integer.MAX_VALUE - 1, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE + 1")
    void testSubtractFromMinInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(-1));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // MIN_VALUE - (-1) should cause overflow
        assertEquals(Integer.MIN_VALUE + 1, result.toInteger());
    }

    // ================= NON-COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify subtraction is NOT commutative (10 - 5 != 5 - 10)")
    void testSubtractionIsNotCommutative() throws Exception {
        // First case: 10 - 5
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        // Reset and test: 5 - 10
        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertNotEquals(result1.toInteger(), result2.toInteger());
        assertEquals(5, result1.toInteger());
        assertEquals(-5, result2.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive subtraction operations")
    void testConsecutiveSubtractions() throws Exception {
        // 10 - 3 - 2 = 5
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(3));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute(); // 10 - 3 = 7

        stack.push(new IntegerValue(2));
        stmt.execute(); // 7 - 2 = 5

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(3));

        assertEquals(2, stack.size());

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(7, result.toInteger());
        assertEquals(0, stack.size());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should correctly subtract near Integer.MAX_VALUE")
    void testSubtractNearMaxInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE - 10));
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(Integer.MAX_VALUE - 15, result.toInteger());
    }

    @Test
    @DisplayName("Should handle single digit subtraction")
    void testSubtractSingleDigits() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(2));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(3, result.toInteger());
    }

    // ================= TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(3));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= SPECIAL CASES =================

    @Test
    @DisplayName("Should handle subtracting the same number (n - n = 0)")
    void testSubtractSameNumber() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle subtraction resulting in negative")
    void testSubtractionResultsInNegative() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle subtracting negative results in increase")
    void testSubtractNegativeIncreasesResult() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should demonstrate opposite of addition")
    void testMinusIsOppositeOfPlus() throws Exception {
        // If a + b = c, then c - b = a
        // Let's test with 10 + 5 = 15, and 15 - 5 = 10
        stack.push(new IntegerValue(15));
        stack.push(new IntegerValue(5));

        MinusStatement stmt = new MinusStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(10, result.toInteger());
    }
}
