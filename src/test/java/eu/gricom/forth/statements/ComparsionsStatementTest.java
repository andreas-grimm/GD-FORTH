package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ComparsionsStatement class.
 * Tests binary comparison operations on two values from the stack.
 */
public class ComparsionsStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ============================================================================
    // EQUALS Tests
    // ============================================================================

    @Test
    void testEqualsTrue() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 == 5 should be TRUE");
    }

    @Test
    void testEqualsFalse() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 == 3 should be FALSE");
    }

    @Test
    void testEqualsNegativeNumbers() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(-10));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-10 == -10 should be TRUE");
    }

    @Test
    void testEqualsZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "0 == 0 should be TRUE");
    }

    // ============================================================================
    // LESS_THAN Tests
    // ============================================================================

    @Test
    void testLessThanTrue() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "3 < 5 should be TRUE");
    }

    @Test
    void testLessThanFalse() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 < 3 should be FALSE");
    }

    @Test
    void testLessThanEqual() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 < 5 should be FALSE");
    }

    @Test
    void testLessThanNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(0));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-5 < 0 should be TRUE");
    }

    // ============================================================================
    // GREATER_THAN Tests
    // ============================================================================

    @Test
    void testGreaterThanTrue() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 > 3 should be TRUE");
    }

    @Test
    void testGreaterThanFalse() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "3 > 5 should be FALSE");
    }

    @Test
    void testGreaterThanEqual() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 > 5 should be FALSE");
    }

    @Test
    void testGreaterThanNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "0 > -5 should be TRUE");
    }

    // ============================================================================
    // NOT_EQUALS Tests
    // ============================================================================

    @Test
    void testNotEqualsTrue() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 != 3 should be TRUE");
    }

    @Test
    void testNotEqualsFalse() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "5 != 5 should be FALSE");
    }

    @Test
    void testNotEqualsNegativeNumbers() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(10));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.NOT_EQUALS, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-10 != 10 should be TRUE");
    }

    // ============================================================================
    // LESS_EQUAL Tests
    // ============================================================================

    @Test
    void testLessEqualTrueLess() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "3 <= 5 should be TRUE");
    }

    @Test
    void testLessEqualTrueEqual() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 <= 5 should be TRUE");
    }

    @Test
    void testLessEqualFalse() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "10 <= 5 should be FALSE");
    }

    @Test
    void testLessEqualNegative() throws Exception {
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-5 <= -5 should be TRUE");
    }

    // ============================================================================
    // GREATER_EQUAL Tests
    // ============================================================================

    @Test
    void testGreaterEqualTrueGreater() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "10 >= 5 should be TRUE");
    }

    @Test
    void testGreaterEqualTrueEqual() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 >= 5 should be TRUE");
    }

    @Test
    void testGreaterEqualFalse() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result.toInteger(), "3 >= 5 should be FALSE");
    }

    @Test
    void testGreaterEqualNegative() throws Exception {
        stack.push(new IntegerValue(-3));
        stack.push(new IntegerValue(-5));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_EQUAL, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-3 >= -5 should be TRUE");
    }

    // ============================================================================
    // Boundary and Edge Case Tests
    // ============================================================================

    @Test
    void testCompareZeroWithPositive() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(1));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "0 < 1 should be TRUE");
    }

    @Test
    void testCompareZeroWithNegative() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(-1));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "0 > -1 should be TRUE");
    }

    @Test
    void testCompareLargeNumbers() throws Exception {
        stack.push(new IntegerValue(1000000));
        stack.push(new IntegerValue(999999));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "1000000 > 999999 should be TRUE");
    }

    @Test
    void testCompareNegativeLargeNumbers() throws Exception {
        stack.push(new IntegerValue(-1000000));
        stack.push(new IntegerValue(-999999));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "-1000000 < -999999 should be TRUE");
    }

    // ============================================================================
    // Token Number Tests
    // ============================================================================

    @Test
    void testGetTokenNumber() throws Exception {
        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    void testGetTokenNumberZero() throws Exception {
        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number should be 0");
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    void testContent() throws Exception {
        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        assertEquals("", stmt.content(), "Content should be empty string");
    }

    @Test
    void testStructure() throws Exception {
        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.EQUALS, 1);
        assertEquals("", stmt.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Multiple Operation Sequence Tests
    // ============================================================================

    @Test
    void testMultipleComparisons() throws Exception {
        // First comparison: 5 > 3 -> TRUE
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(3));

        ComparsionsStatement stmt1 = new ComparsionsStatement(ForthTokenType.GREATER_THAN, 1);
        stmt1.execute();

        IntegerValue result1 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result1.toInteger());

        // Second comparison: 2 < 1 -> FALSE
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(1));

        ComparsionsStatement stmt2 = new ComparsionsStatement(ForthTokenType.LESS_THAN, 2);
        stmt2.execute();

        IntegerValue result2 = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.FALSE, result2.toInteger());
    }

    @Test
    void testComparisonResultReusable() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        ComparsionsStatement stmt = new ComparsionsStatement(ForthTokenType.LESS_THAN, 1);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(BooleanValue.TRUE, result.toInteger(), "5 < 10 should be TRUE");
    }
}
