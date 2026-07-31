package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ValueComparsionStatement class.
 * Tests unary comparison operations on a single value from the stack (against zero).
 */
public class ValueComparsionStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ============================================================================
    // ZERO_EQUALS Tests
    // ============================================================================

    @Test
    void testZeroEqualsTrue() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "0 == 0 should be TRUE");
    }

    @Test
    void testZeroEqualsFalsePositive() throws Exception {
        stack.push(new IntegerValue(5));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 == 0 should be FALSE");
    }

    @Test
    void testZeroEqualsFalseNegative() throws Exception {
        stack.push(new IntegerValue(-5));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "-5 == 0 should be FALSE");
    }

    @Test
    void testZeroEqualsOne() throws Exception {
        stack.push(new IntegerValue(1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "1 == 0 should be FALSE");
    }

    @Test
    void testZeroEqualsLargeNumber() throws Exception {
        stack.push(new IntegerValue(1000000));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "1000000 == 0 should be FALSE");
    }

    // ============================================================================
    // ZERO_LESS Tests
    // ============================================================================

    @Test
    void testZeroLessTrue() throws Exception {
        stack.push(new IntegerValue(-1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-1 < 0 should be TRUE");
    }

    @Test
    void testZeroLessTrueLargeNegative() throws Exception {
        stack.push(new IntegerValue(-1000));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-1000 < 0 should be TRUE");
    }

    @Test
    void testZeroLessFalseZero() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "0 < 0 should be FALSE");
    }

    @Test
    void testZeroLessFalsePositive() throws Exception {
        stack.push(new IntegerValue(5));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 < 0 should be FALSE");
    }

    @Test
    void testZeroLessFalseOne() throws Exception {
        stack.push(new IntegerValue(1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "1 < 0 should be FALSE");
    }

    // ============================================================================
    // ZERO_GREATER Tests
    // ============================================================================

    @Test
    void testZeroGreaterTrue() throws Exception {
        stack.push(new IntegerValue(1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "1 > 0 should be TRUE");
    }

    @Test
    void testZeroGreaterTrueLargePositive() throws Exception {
        stack.push(new IntegerValue(1000));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "1000 > 0 should be TRUE");
    }

    @Test
    void testZeroGreaterFalseZero() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "0 > 0 should be FALSE");
    }

    @Test
    void testZeroGreaterFalseNegative() throws Exception {
        stack.push(new IntegerValue(-5));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "-5 > 0 should be FALSE");
    }

    @Test
    void testZeroGreaterFalseNegativeOne() throws Exception {
        stack.push(new IntegerValue(-1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "-1 > 0 should be FALSE");
    }

    // ============================================================================
    // ZERO_NOT_EQUALS Tests
    // ============================================================================

    @Test
    void testZeroNotEqualsTrue() throws Exception {
        stack.push(new IntegerValue(1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "1 != 0 should be TRUE");
    }

    @Test
    void testZeroNotEqualsTrueNegative() throws Exception {
        stack.push(new IntegerValue(-42));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-42 != 0 should be TRUE");
    }

    @Test
    void testZeroNotEqualsFalse() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "0 != 0 should be FALSE");
    }

    @Test
    void testZeroNotEqualsLargePositive() throws Exception {
        stack.push(new IntegerValue(999999));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "999999 != 0 should be TRUE");
    }

    @Test
    void testZeroNotEqualsLargeNegative() throws Exception {
        stack.push(new IntegerValue(-999999));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-999999 != 0 should be TRUE");
    }

    // ============================================================================
    // Token Number Tests
    // ============================================================================

    @Test
    void testGetTokenNumber() throws Exception {
        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    void testGetTokenNumberZero() throws Exception {
        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number should be 0");
    }

    @Test
    void testGetTokenNumberLarge() throws Exception {
        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 9999);
        assertEquals(9999, stmt.getTokenNumber(), "Token number should be 9999");
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    void testContent() throws Exception {
        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        assertEquals("", stmt.content(), "Content should be empty string");
    }

    @Test
    void testStructure() throws Exception {
        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        assertEquals("", stmt.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Boundary Tests
    // ============================================================================

    @Test
    void testBoundaryIntegerMax() throws Exception {
        stack.push(new IntegerValue(Integer.MAX_VALUE));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "Integer.MAX_VALUE > 0 should be TRUE");
    }

    @Test
    void testBoundaryIntegerMin() throws Exception {
        stack.push(new IntegerValue(Integer.MIN_VALUE));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "Integer.MIN_VALUE < 0 should be TRUE");
    }

    @Test
    void testBoundaryMinusOne() throws Exception {
        stack.push(new IntegerValue(-1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "-1 == 0 should be FALSE");
    }

    @Test
    void testBoundaryPlusOne() throws Exception {
        stack.push(new IntegerValue(1));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "1 == 0 should be FALSE");
    }

    // ============================================================================
    // Multiple Operation Sequence Tests
    // ============================================================================

    @Test
    void testMultipleZeroComparisons() throws Exception {
        // First comparison: -5 < 0 -> TRUE
        stack.push(new IntegerValue(-5));

        ValueComparsionStatement stmt1 = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt1.execute();

        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Second comparison: 10 > 0 -> TRUE
        stack.push(new IntegerValue(10));

        ValueComparsionStatement stmt2 = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 2);
        stmt2.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result2.toInteger());

        // Third comparison: 0 == 0 -> TRUE
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt3 = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 3);
        stmt3.execute();

        IntegerValue result3 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result3.toInteger());
    }

    @Test
    void testComparisonResultReusable() throws Exception {
        stack.push(new IntegerValue(42));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "42 > 0 should be TRUE");
    }

    // ============================================================================
    // Truth Value Tests (Verify -1 and 0 are used correctly)
    // ============================================================================

    @Test
    void testZeroEqualsReturnsTrueValue() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger(), "TRUE should be represented as -1");
    }

    @Test
    void testZeroEqualsFalseFalseValue() throws Exception {
        stack.push(new IntegerValue(42));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger(), "FALSE should be represented as 0");
    }

    @Test
    void testZeroLessReturnsTrueValue() throws Exception {
        stack.push(new IntegerValue(-99));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger(), "TRUE should be represented as -1");
    }

    @Test
    void testZeroGreaterReturnsTrueValue() throws Exception {
        stack.push(new IntegerValue(99));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger(), "TRUE should be represented as -1");
    }

    @Test
    void testZeroNotEqualsReturnsTrueValue() throws Exception {
        stack.push(new IntegerValue(7));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(-1, result.toInteger(), "TRUE should be represented as -1");
    }

    @Test
    void testZeroNotEqualsReturnsFalseValue() throws Exception {
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt = new ValueComparsionStatement(ForthTokenType.ZERO_NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger(), "FALSE should be represented as 0");
    }

    // ============================================================================
    // Symmetry Tests
    // ============================================================================

    @Test
    void testZeroLessAndZeroGreaterSymmetry() throws Exception {
        // Test: -5 < 0 should be TRUE, and 0 > -5 should be TRUE (symmetric)
        stack.push(new IntegerValue(-5));

        ValueComparsionStatement lessStmt = new ValueComparsionStatement(ForthTokenType.ZERO_LESS, 1);
        lessStmt.execute();

        IntegerValue lessResult = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, lessResult.toInteger(), "-5 < 0 should be TRUE");

        // Now test the symmetric case
        stack.push(new IntegerValue(5));

        ValueComparsionStatement greaterStmt = new ValueComparsionStatement(ForthTokenType.ZERO_GREATER, 2);
        greaterStmt.execute();

        IntegerValue greaterResult = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, greaterResult.toInteger(), "5 > 0 should be TRUE");
    }

    @Test
    void testZeroEqualsSymmetry() throws Exception {
        // Test: 0 == 0 should always be TRUE
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt1 = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 1);
        stmt1.execute();

        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger(), "0 == 0 should be TRUE (first check)");

        // Test again to ensure consistency
        stack.push(new IntegerValue(0));

        ValueComparsionStatement stmt2 = new ValueComparsionStatement(ForthTokenType.ZERO_EQUALS, 2);
        stmt2.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result2.toInteger(), "0 == 0 should be TRUE (second check)");
    }
}
