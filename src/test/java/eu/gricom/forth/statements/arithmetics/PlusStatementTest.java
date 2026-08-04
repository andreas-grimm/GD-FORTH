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
 * Comprehensive test suite for PlusStatement class.
 * Tests arithmetic addition operation (( a b -- a+b )) on the stack.
 *
 * Stack notation: (( a b -- a+b )) means pop b (top), pop a, push a+b
 */
@DisplayName("PlusStatement Tests")
class PlusStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create PlusStatement with PLUS token type")
    void testConstructorWithPlusTokenType() {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create PlusStatement with token number")
    void testConstructorWithTokenNumber() {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should handle different token numbers")
    void testConstructorWithVariousTokenNumbers() {
        assertEquals(0, new PlusStatement(ForthTokenType.PLUS, 0).getTokenNumber());
        assertEquals(1, new PlusStatement(ForthTokenType.PLUS, 1).getTokenNumber());
        assertEquals(100, new PlusStatement(ForthTokenType.PLUS, 100).getTokenNumber());
        assertEquals(Integer.MAX_VALUE, new PlusStatement(ForthTokenType.PLUS, Integer.MAX_VALUE).getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number via getTokenNumber()")
    void testGetTokenNumber() {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        assertEquals("", stmt.structure());
    }

    @Test
    @DisplayName("Should implement Statement interface")
    void testStatementInterface() {
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        assertTrue(stmt instanceof Statement);
    }

    // ================= BASIC ARITHMETIC OPERATIONS =================

    @Test
    @DisplayName("Should add two positive integers")
    void testAddTwoPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should add in correct order (10 + 5 = 15)")
    void testAddInCorrectOrder() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    @DisplayName("Should add one positive and one negative integer")
    void testAddPositiveAndNegative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should add two negative integers")
    void testAddTwoNegativeIntegers() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-15, result.toInteger());
    }

    // ================= ZERO OPERATIONS =================

    @Test
    @DisplayName("Should handle addition with zero (0 + 5)")
    void testAddZeroAndPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle addition with zero (5 + 0)")
    void testAddPositiveAndZero() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(0));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle addition of zero and zero")
    void testAddZeroAndZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle zero plus negative")
    void testAddZeroAndNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    // ================= LARGE NUMBER OPERATIONS =================

    @Test
    @DisplayName("Should handle large positive numbers")
    void testAddLargePositiveNumbers() throws Exception {
        stack.push(new IntegerValue(1000000));
        stack.push(new IntegerValue(2000000));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(3000000, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE + positive (causes overflow)")
    void testAddMaxIntegerPlusPositive() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(1));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Integer overflow wraps around
        assertEquals(Integer.MIN_VALUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE + negative (causes overflow)")
    void testAddMinIntegerPlusNegative() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(-1));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Integer overflow wraps around
        assertEquals(Integer.MAX_VALUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle MAX_VALUE + MIN_VALUE")
    void testAddMaxAndMinInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    // ================= COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify addition is commutative (5 + 3 = 3 + 5)")
    void testAdditionIsCommutative() throws Exception {
        // First case: 5 + 3
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        // Reset and test: 3 + 5
        stack.reset();
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(5));
        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertEquals(result1.toInteger(), result2.toInteger());
        assertEquals(8, result1.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive addition operations")
    void testConsecutiveAdditions() throws Exception {
        // Push 1, 2, 3
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute(); // 1 + 2 = 3, stack now has [3]

        stack.push(new IntegerValue(3));
        stmt.execute(); // 3 + 3 = 6

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(6, result.toInteger());
    }

    @Test
    @DisplayName("Should handle multiple additions in sequence")
    void testMultipleSequentialAdditions() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        stack.push(new IntegerValue(30));
        stmt.execute();

        stack.push(new IntegerValue(40));
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(100, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        assertEquals(2, stack.size());

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(8, result.toInteger());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("Should maintain correct stack order with additional values")
    void testStackOrderWithAdditionalValues() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        // Top of stack should be 8
        IntegerValue top = (IntegerValue) stack.pop();
        assertEquals(8, top.toInteger());

        // Next should be 100
        IntegerValue next = (IntegerValue) stack.pop();
        assertEquals(100, next.toInteger());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);

        // Should not throw an exception, but handles internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);

        // Should not throw an exception
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should correctly add numbers near Integer.MAX_VALUE")
    void testAddNearMaxInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE - 10));
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(Integer.MAX_VALUE - 5, result.toInteger());
    }

    @Test
    @DisplayName("Should correctly add numbers near Integer.MIN_VALUE")
    void testAddNearMinInteger() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE + 10));
        stack.push(new IntegerValue(-5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(Integer.MIN_VALUE + 5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle single digit additions")
    void testAddSingleDigits() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(3, result.toInteger());
    }

    // ================= TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= MIXED SIGN OPERATIONS =================

    @Test
    @DisplayName("Should handle negative + positive correctly")
    void testNegativePlusPositive() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    @DisplayName("Should handle positive + negative correctly")
    void testPositivePlusNegative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(-5));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    @DisplayName("Should cancel out when adding positive and negative of same magnitude")
    void testCancelOutOppositeNumbers() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-42));

        PlusStatement stmt = new PlusStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }
}
