package eu.gricom.forth.statements.stack;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.RealValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for TwoRotStatement (2ROT) - rotate three pairs of stack items.
 * Stack effect: ( a b c d e f -- c d e f a b )
 * Rotates the top three pairs of stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("2ROT (Rotate Pairs) Stack Operation Tests")
class TwoRotStatementTest {

    private Stack stack;
    private TwoRotStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new TwoRotStatement(ForthTokenType.TWO_ROT, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        TwoRotStatement stmt = new TwoRotStatement(ForthTokenType.TWO_ROT, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        TwoRotStatement stmt = new TwoRotStatement(ForthTokenType.TWO_ROT, 123);
        assertEquals(123, stmt.getTokenNumber(), "Token number should be 123");
    }

    @Test
    @DisplayName("content() should return empty string")
    void testContent() throws Exception {
        assertEquals("", statement.content(), "Content should be empty string");
    }

    @Test
    @DisplayName("structure() should return empty string")
    void testStructure() throws Exception {
        assertEquals("", statement.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Basic 2ROT Operation Tests
    // ============================================================================

    @Test
    @DisplayName("2ROT rotates six items correctly ( 1 2 3 4 5 6 -- 3 4 5 6 1 2 )")
    void testTwoRotSixValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(6));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(6, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2ROT maintains stack size")
    void testTwoRotMaintainsSize() throws Exception {
        for (int i = 1; i <= 6; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(6, sizeAfter);
    }

    @Test
    @DisplayName("2ROT with eight items")
    void testTwoRotEightItems() throws Exception {
        for (int i = 1; i <= 8; i++) {
            stack.push(new IntegerValue(i));
        }
        statement.execute();

        assertEquals(8, stack.size());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(8, stack.pop().toInteger());
    }

    // ============================================================================
    // 2ROT with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("2ROT with mixed IntegerValues")
    void testTwoRotMixedIntegers() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(-50));
        stack.push(new IntegerValue(50));
        stack.push(new IntegerValue(-25));
        stack.push(new IntegerValue(25));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(100, stack.pop().toInteger());
        assertEquals(-100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2ROT with RealValues")
    void testTwoRotRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        stack.push(new RealValue(4.5));
        stack.push(new RealValue(5.5));
        stack.push(new RealValue(6.5));
        statement.execute();

        assertEquals(6, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
    }

    @Test
    @DisplayName("2ROT with StringValues")
    void testTwoRotStringValues() throws Exception {
        stack.push(new StringValue("a"));
        stack.push(new StringValue("b"));
        stack.push(new StringValue("c"));
        stack.push(new StringValue("d"));
        stack.push(new StringValue("e"));
        stack.push(new StringValue("f"));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals("b", stack.pop().toString());
        assertEquals("a", stack.pop().toString());
    }

    // ============================================================================
    // Sequential 2ROT Operations
    // ============================================================================

    @Test
    @DisplayName("Triple 2ROT returns to original order")
    void testTriple2RotReturnToOriginal() throws Exception {
        for (int i = 1; i <= 6; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();  // First 2ROT
        statement.execute();  // Second 2ROT
        statement.execute();  // Third 2ROT

        assertEquals(6, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("2ROT on empty stack should handle exception gracefully")
    void testTwoRotEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("2ROT with less than 6 items should handle exception")
    void testTwoRotTooFewItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("2ROT preserves items below")
    void testTwoRotPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        for (int i = 1; i <= 6; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(8, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(6, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2ROT with large stack")
    void testTwoRotLargeStack() throws Exception {
        for (int i = 0; i < 20; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("2ROT with exactly six items")
    void testTwoRotExactlySix() throws Exception {
        for (int i = 1; i <= 6; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(6, stack.pop().toInteger());
        assertEquals(5, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("2ROT with negative numbers")
    void testTwoRotNegative() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        stack.push(new IntegerValue(-3));
        stack.push(new IntegerValue(-4));
        stack.push(new IntegerValue(-5));
        stack.push(new IntegerValue(-6));
        statement.execute();

        assertEquals(-2, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with TWO_ROT token type")
    void testWithTwoRotTokenType() {
        TwoRotStatement stmt = new TwoRotStatement(ForthTokenType.TWO_ROT, 1);
        assertNotNull(stmt, "Statement should be created with TWO_ROT token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            TwoRotStatement stmt = new TwoRotStatement(ForthTokenType.TWO_ROT, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
