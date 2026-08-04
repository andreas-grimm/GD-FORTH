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
 * Comprehensive test suite for MultiplyStatement class.
 * Tests arithmetic multiplication operation (( a b -- a*b )) on the stack.
 *
 * Stack notation: (( a b -- a*b )) means pop b (top), pop a, push a*b
 */
@DisplayName("MultiplyStatement Tests")
class MultiplyStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create MultiplyStatement with MULTIPLY token type")
    void testConstructorWithMultiplyTokenType() {
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create MultiplyStatement with token number")
    void testConstructorWithTokenNumber() {
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        assertEquals("", stmt.structure());
    }

    // ================= BASIC MULTIPLICATION OPERATIONS =================

    @Test
    @DisplayName("Should multiply two positive integers (6 * 7 = 42)")
    void testMultiplyTwoPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(6));
        stack.push(new IntegerValue(7));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply positive by negative")
    void testMultiplyPositiveByNegative() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(-3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-15, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply two negative integers (result positive)")
    void testMultiplyTwoNegativeIntegers() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }

    // ================= ZERO OPERATIONS =================

    @Test
    @DisplayName("Should multiply by zero (0 * 999 = 0)")
    void testMultiplyByZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(999));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply zero by any number (999 * 0 = 0)")
    void testMultiplyZeroByNumber() throws Exception {
        stack.push(new IntegerValue(999));
        stack.push(new IntegerValue(0));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply zero by zero")
    void testMultiplyZeroByZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= IDENTITY OPERATIONS =================

    @Test
    @DisplayName("Should multiply by one (42 * 1 = 42)")
    void testMultiplyByOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(1));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply one by any number (1 * 42 = 42)")
    void testMultiplyOneByNumber() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(42));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply by minus one (42 * -1 = -42)")
    void testMultiplyByMinusOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-1));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-42, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply by minus one negative number (-42 * -1 = 42)")
    void testMultiplyNegativeByMinusOne() throws Exception {
        stack.push(new IntegerValue(-42));
        stack.push(new IntegerValue(-1));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    // ================= LARGE NUMBER OPERATIONS =================

    @Test
    @DisplayName("Should multiply medium-sized numbers (1000 * 1000 = 1000000)")
    void testMultiplyMediumNumbers() throws Exception {
        stack.push(new IntegerValue(1000));
        stack.push(new IntegerValue(1000));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1000000, result.toInteger());
    }

    // ================= COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify multiplication is commutative (6 * 7 = 7 * 6)")
    void testMultiplicationIsCommutative() throws Exception {
        stack.push(new IntegerValue(6));
        stack.push(new IntegerValue(7));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(7));
        stack.push(new IntegerValue(6));

        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertEquals(result1.toInteger(), result2.toInteger());
        assertEquals(42, result1.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive multiplication operations")
    void testConsecutiveMultiplications() throws Exception {
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute(); // 2 * 3 = 6

        stack.push(new IntegerValue(4));
        stmt.execute(); // 6 * 4 = 24

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(24, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        assertEquals(2, stack.size());

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
        assertEquals(0, stack.size());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit multiplication")
    void testMultiplySingleDigits() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(12, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply by 2 (doubling)")
    void testMultiplyByTwo() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(2));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(200, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply by 10 (decimal shift)")
    void testMultiplyByTen() throws Exception {
        stack.push(new IntegerValue(123));
        stack.push(new IntegerValue(10));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1230, result.toInteger());
    }

    // ================= TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= SIGN RULES =================

    @Test
    @DisplayName("Should verify sign rules: positive * positive = positive")
    void testPositiveTimesPositive() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() > 0);
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: negative * negative = positive")
    void testNegativeTimesNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() > 0);
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: positive * negative = negative")
    void testPositiveTimesNegative() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(-3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() < 0);
        assertEquals(-15, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: negative * positive = negative")
    void testNegativeTimesPositive() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(3));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() < 0);
        assertEquals(-15, result.toInteger());
    }

    // ================= SPECIAL VALUES =================

    @Test
    @DisplayName("Should multiply same number by itself")
    void testMultiplySameNumber() throws Exception {
        stack.push(new IntegerValue(7));
        stack.push(new IntegerValue(7));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(49, result.toInteger());
    }

    @Test
    @DisplayName("Should multiply -1 by -1 = 1")
    void testNegativeOneTimesNegativeOne() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-1));

        MultiplyStatement stmt = new MultiplyStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }
}
