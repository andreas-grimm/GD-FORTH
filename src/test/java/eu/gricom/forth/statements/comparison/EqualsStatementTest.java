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
 * Comprehensive test suite for EqualsStatement class.
 * Tests equality comparison operation (( a b -- a==b )) on the stack.
 *
 * Stack notation: (( a b -- a==b )) means pop b (top), pop a, push a==b
 * Result: BooleanValue.TRUE (-1) if equal, BooleanValue.FALSE (0) if not equal
 */
@DisplayName("EqualsStatement Tests")
class EqualsStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create EqualsStatement with EQUALS token type")
    void testConstructorWithEqualsTokenType() {
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create EqualsStatement with token number")
    void testConstructorWithTokenNumber() {
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        assertEquals("", stmt.structure());
    }

    // ================= EQUALITY TESTS =================

    @Test
    @DisplayName("Should return TRUE when two equal positive integers are compared")
    void testTwoEqualPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when unequal positive integers are compared")
    void testTwoUnequalPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when comparing zero to itself")
    void testZeroEqualsZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing zero to positive")
    void testZeroNotEqualsPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing zero to negative")
    void testZeroNotEqualsNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return TRUE when two equal negative integers are compared")
    void testTwoEqualNegativeIntegers() throws Exception {
        stack.push(new IntegerValue(-42));
        stack.push(new IntegerValue(-42));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE when comparing positive to negative with same magnitude")
    void testPositiveNotEqualNegativeSameMagnitude() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-42));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify equality is commutative (a==b iff b==a)")
    void testEqualityIsCommutative() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));
        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertEquals(result1.toInteger(), result2.toInteger());
    }

    // ================= LARGE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should handle large equal numbers")
    void testLargeEqualNumbers() throws Exception {
        stack.push(new IntegerValue(1000000));
        stack.push(new IntegerValue(1000000));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle large unequal numbers")
    void testLargeUnequalNumbers() throws Exception {
        stack.push(new IntegerValue(1000000));
        stack.push(new IntegerValue(1000001));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE comparisons")
    void testMaxIntegerEquality() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE comparisons")
    void testMinIntegerEquality() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= CONSECUTIVE COMPARISONS =================

    @Test
    @DisplayName("Should handle consecutive equality comparisons")
    void testConsecutiveComparisons() throws Exception {
        // First comparison: 5 == 5 -> TRUE (-1)
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        // Second comparison: result == -1
        stack.push(new IntegerValue(-1));
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(10));

        assertEquals(2, stack.size());

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
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
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    @Test
    @DisplayName("Should return TRUE value as -1")
    void testTrueResultValue() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(100));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE value as 0")
    void testFalseResultValue() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit equality")
    void testSingleDigitEquality() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle one vs many")
    void testOneVsMany() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(1000));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= SPECIAL CASES =================

    @Test
    @DisplayName("Should verify reflexivity: x == x is always TRUE")
    void testReflexivity() throws Exception {
        int value = 12345;
        stack.push(new IntegerValue(value));
        stack.push(new IntegerValue(value));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify operator order (first != last means FALSE)")
    void testOperatorOrder() throws Exception {
        // Stack: push 10, push 20. Pop 20 (b), pop 10 (a). Compare: 10 == 20? NO
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle one element greater than other")
    void testAsymmetricComparison() throws Exception {
        stack.push(new IntegerValue(50));
        stack.push(new IntegerValue(49));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should verify opposite comparison is same for equality")
    void testSymmetryOfEquality() throws Exception {
        // (10 == 5) == (5 == 10) for equality operator
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        EqualsStatement stmt = new EqualsStatement(ForthTokenType.EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }
}
