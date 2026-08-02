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
 * Comprehensive test suite for DivideStatement class.
 * Tests arithmetic division operation (( a b -- a/b )) on the stack.
 *
 * Stack notation: (( a b -- a/b )) means pop b (top), pop a, push a/b
 * Special attention to division by zero handling.
 */
@DisplayName("DivideStatement Tests")
class DivideStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create DivideStatement with DIVIDE token type")
    void testConstructorWithDivideTokenType() {
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create DivideStatement with token number")
    void testConstructorWithTokenNumber() {
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        assertEquals("", stmt.structure());
    }

    // ================= BASIC DIVISION OPERATIONS =================

    @Test
    @DisplayName("Should divide two positive integers (20 / 4 = 5)")
    void testDivideTwoPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should divide with remainder (10 / 3 = 3 in integer division)")
    void testDivideWithRemainder() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(3));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(3, result.toInteger());
    }

    @Test
    @DisplayName("Should divide positive by negative")
    void testDividePositiveByNegative() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(-4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should divide negative by positive")
    void testDivideNegativeByPositive() throws Exception {
        stack.push(new IntegerValue(-20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should divide two negative integers (result positive)")
    void testDivideTwoNegativeIntegers() throws Exception {
        stack.push(new IntegerValue(-20));
        stack.push(new IntegerValue(-4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    // ================= ZERO OPERATIONS =================

    @Test
    @DisplayName("Should handle division of zero by positive (0 / 5 = 0)")
    void testDivideZeroByPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle division of zero by negative (0 / -5 = 0)")
    void testDivideZeroByNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= DIVISION BY ZERO =================

    @Test
    @DisplayName("Should handle division by zero gracefully (10 / 0)")
    void testDivisionByZero() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(0));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);

        // Should not throw an exception but handles internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle zero divided by zero gracefully (0 / 0)")
    void testZeroDividedByZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle negative divided by zero gracefully (-10 / 0)")
    void testNegativeDivisionByZero() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(0));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= IDENTITY OPERATIONS =================

    @Test
    @DisplayName("Should divide by one (42 / 1 = 42)")
    void testDivideByOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(1));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    @DisplayName("Should divide by minus one (42 / -1 = -42)")
    void testDivideByMinusOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-1));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-42, result.toInteger());
    }

    @Test
    @DisplayName("Should divide number by itself (42 / 42 = 1)")
    void testDivideByItself() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }

    @Test
    @DisplayName("Should divide by -1 (negate)")
    void testDivideByNegativeOne() throws Exception {
        stack.push(new IntegerValue(-42));
        stack.push(new IntegerValue(-1));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    // ================= LARGE NUMBER OPERATIONS =================

    @Test
    @DisplayName("Should divide large positive numbers")
    void testDivideLargePositiveNumbers() throws Exception {
        stack.push(new IntegerValue(2000000));
        stack.push(new IntegerValue(1000));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2000, result.toInteger());
    }

    // ================= NON-COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify division is NOT commutative (20 / 4 != 4 / 20)")
    void testDivisionIsNotCommutative() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(4));
        stack.push(new IntegerValue(20));

        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertNotEquals(result1.toInteger(), result2.toInteger());
        assertEquals(5, result1.toInteger());
        assertEquals(0, result2.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive division operations")
    void testConsecutiveDivisions() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(2));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute(); // 100 / 2 = 50

        stack.push(new IntegerValue(5));
        stmt.execute(); // 50 / 5 = 10

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(10, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(4));

        assertEquals(2, stack.size());

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
        assertEquals(0, stack.size());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit division")
    void testDivideSingleDigits() throws Exception {
        stack.push(new IntegerValue(8));
        stack.push(new IntegerValue(2));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(4, result.toInteger());
    }

    @Test
    @DisplayName("Should divide even number by two")
    void testDivideByTwo() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(2));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(50, result.toInteger());
    }

    @Test
    @DisplayName("Should divide odd number by two (truncates)")
    void testDivideOddByTwo() throws Exception {
        stack.push(new IntegerValue(9));
        stack.push(new IntegerValue(2));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(4, result.toInteger());
    }

    @Test
    @DisplayName("Should divide by 10 (decimal shift)")
    void testDivideByTen() throws Exception {
        stack.push(new IntegerValue(1230));
        stack.push(new IntegerValue(10));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(123, result.toInteger());
    }

    // ================= TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= SIGN RULES =================

    @Test
    @DisplayName("Should verify sign rules: positive / positive = positive")
    void testPositiveDividePositive() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() > 0);
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: negative / negative = positive")
    void testNegativeDivideNegative() throws Exception {
        stack.push(new IntegerValue(-20));
        stack.push(new IntegerValue(-4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() > 0);
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: positive / negative = negative")
    void testPositiveDivideNegative() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(-4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() < 0);
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should verify sign rules: negative / positive = negative")
    void testNegativeDividePositive() throws Exception {
        stack.push(new IntegerValue(-20));
        stack.push(new IntegerValue(4));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(result.toInteger() < 0);
        assertEquals(-5, result.toInteger());
    }

    // ================= SPECIAL CASES =================

    @Test
    @DisplayName("Should verify smaller / larger results in zero")
    void testSmallerDivideLarger() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(10));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should verify 1 / 2 = 0 (integer division)")
    void testOneHalf() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should demonstrate division truncates towards zero")
    void testDivisionTruncatesTowardZero() throws Exception {
        stack.push(new IntegerValue(7));
        stack.push(new IntegerValue(3));

        DivideStatement stmt = new DivideStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2, result.toInteger());
    }
}
