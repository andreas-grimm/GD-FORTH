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
 * Comprehensive test suite for NotEqualsStatement class.
 * Tests not-equals comparison operation (( a b -- a!=b )) on the stack.
 *
 * Stack notation: (( a b -- a!=b )) means pop b (top), pop a, push a!=b
 * Result: BooleanValue.TRUE (-1) if not equal, BooleanValue.FALSE (0) if equal
 */
@DisplayName("NotEqualsStatement Tests")
class NotEqualsStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create NotEqualsStatement with NOT_EQUALS token type")
    void testConstructorWithNotEqualsTokenType() {
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create NotEqualsStatement with token number")
    void testConstructorWithTokenNumber() {
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        assertEquals("", stmt.structure());
    }

    // ================= INEQUALITY TESTS =================

    @Test
    @DisplayName("Should return FALSE when two equal positive integers are compared")
    void testTwoEqualPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when unequal positive integers are compared")
    void testTwoUnequalPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= ZERO COMPARISONS =================

    @Test
    @DisplayName("Should return FALSE when comparing zero to itself")
    void testZeroEqualsZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when comparing zero to positive")
    void testZeroNotEqualsPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when comparing zero to negative")
    void testZeroNotEqualsNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= NEGATIVE NUMBER COMPARISONS =================

    @Test
    @DisplayName("Should return FALSE when two equal negative integers are compared")
    void testTwoEqualNegativeIntegers() throws Exception {
        stack.push(new IntegerValue(-42));
        stack.push(new IntegerValue(-42));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should return TRUE when comparing positive to negative with same magnitude")
    void testPositiveNotEqualNegativeSameMagnitude() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-42));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify inequality is commutative (a!=b iff b!=a)")
    void testInequalityIsCommutative() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
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

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle large unequal numbers")
    void testLargeUnequalNumbers() throws Exception {
        stack.push(new IntegerValue(1000000));
        stack.push(new IntegerValue(1000001));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MAX_VALUE comparisons")
    void testMaxIntegerEquality() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle Integer.MIN_VALUE comparisons")
    void testMinIntegerEquality() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= OPPOSITE OF EQUALS =================

    @Test
    @DisplayName("Should be opposite of EqualsStatement")
    void testOppositeOfEquals() throws Exception {
        // When equals returns TRUE, not-equals should return FALSE
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // 42 != 42 is FALSE
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should be opposite of EqualsStatement for unequal values")
    void testOppositeOfEqualsUnequal() throws Exception {
        // When equals returns FALSE, not-equals should return TRUE
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(43));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // 42 != 43 is TRUE
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        assertEquals(2, stack.size());

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
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
        stack.push(new IntegerValue(3));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    @Test
    @DisplayName("Should return TRUE value as -1")
    void testTrueResultValue() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(99));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger());
    }

    @Test
    @DisplayName("Should return FALSE value as 0")
    void testFalseResultValue() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(100));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit inequality")
    void testSingleDigitInequality() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle consecutive inequality comparisons")
    void testConsecutiveComparisons() throws Exception {
        // First: 5 != 3 -> TRUE (-1)
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        // Second: -1 != -1 -> FALSE (0)
        stack.push(new IntegerValue(-1));
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    // ================= SPECIAL CASES =================

    @Test
    @DisplayName("Should verify reflexivity negation: x != x is always FALSE")
    void testReflexivityNegation() throws Exception {
        int value = 12345;
        stack.push(new IntegerValue(value));
        stack.push(new IntegerValue(value));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle one element less than other")
    void testAsymmetricComparison() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle numbers off by one")
    void testNumbersOffByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(101));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }

    @Test
    @DisplayName("Should handle negative one inequality")
    void testNegativeOneInequality() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(1));

        NotEqualsStatement stmt = new NotEqualsStatement(ForthTokenType.NOT_EQUALS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger());
    }
}
