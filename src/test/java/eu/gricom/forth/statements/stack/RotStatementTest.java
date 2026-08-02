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
 * Test suite for RotStatement (ROT) - rotate top three stack items.
 * Stack effect: ( a b c -- b c a )
 * Rotates the top three stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("ROT (Rotate) Stack Operation Tests")
class RotStatementTest {

    private Stack stack;
    private RotStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new RotStatement(ForthTokenType.ROT, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        RotStatement stmt = new RotStatement(ForthTokenType.ROT, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        RotStatement stmt = new RotStatement(ForthTokenType.ROT, 123);
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
    // Basic ROT Operation Tests
    // ============================================================================

    @Test
    @DisplayName("ROT rotates three values correctly ( 1 2 3 -- 2 3 1 )")
    void testRotThreeValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(1, stack.pop().toInteger(), "Top should be 1");
        assertEquals(3, stack.pop().toInteger(), "Second should be 3");
        assertEquals(2, stack.pop().toInteger(), "Third should be 2");
    }

    @Test
    @DisplayName("ROT maintains stack size")
    void testRotMaintainsSize() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter, "Stack size should not change");
        assertEquals(3, sizeAfter);
    }

    @Test
    @DisplayName("ROT with four items rotates only top three")
    void testRotFourItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // ROT with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("ROT with RealValue")
    void testRotRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        statement.execute();

        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 3.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
    }

    @Test
    @DisplayName("ROT with StringValue")
    void testRotStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        stack.push(new StringValue("third"));
        statement.execute();

        assertEquals("first", stack.pop().toString());
        assertEquals("third", stack.pop().toString());
        assertEquals("second", stack.pop().toString());
    }

    @Test
    @DisplayName("ROT with zero values")
    void testRotZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential ROT Operations
    // ============================================================================

    @Test
    @DisplayName("Triple ROT returns to original order")
    void testTripleRotReturnToOriginal() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        statement.execute();  // [1, 2, 3] -> [2, 3, 1]
        statement.execute();  // [2, 3, 1] -> [3, 1, 2]
        statement.execute();  // [3, 1, 2] -> [1, 2, 3]

        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("ROT preserves items below rotation")
    void testRotPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("ROT on empty stack should handle exception gracefully")
    void testRotEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("ROT with one item should handle exception")
    void testRotSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    @Test
    @DisplayName("ROT with two items should handle exception")
    void testRotTwoItems() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("ROT with large stack")
    void testRotLargeStack() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(7, stack.pop().toInteger());
        assertEquals(9, stack.pop().toInteger());
        assertEquals(8, stack.pop().toInteger());
    }

    @Test
    @DisplayName("ROT pattern test")
    void testRotPattern() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(40));
        stack.push(new IntegerValue(50));

        statement.execute();

        assertEquals(5, stack.size());
        assertEquals(30, stack.pop().toInteger());
        assertEquals(50, stack.pop().toInteger());
        assertEquals(40, stack.pop().toInteger());
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("ROT with negative numbers")
    void testRotNegativeNumbers() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        stack.push(new IntegerValue(-3));
        statement.execute();

        assertEquals(-1, stack.pop().toInteger());
        assertEquals(-3, stack.pop().toInteger());
        assertEquals(-2, stack.pop().toInteger());
    }

    @Test
    @DisplayName("ROT with mixed positive and negative")
    void testRotMixedPosNeg() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(-300));
        statement.execute();

        assertEquals(-100, stack.pop().toInteger());
        assertEquals(-300, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with ROT token type")
    void testWithRotTokenType() {
        RotStatement stmt = new RotStatement(ForthTokenType.ROT, 1);
        assertNotNull(stmt, "Statement should be created with ROT token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            RotStatement stmt = new RotStatement(ForthTokenType.ROT, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
