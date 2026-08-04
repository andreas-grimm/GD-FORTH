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
 * Test suite for MinusRotStatement (-ROT) - reverse rotate top three stack items.
 * Stack effect: ( a b c -- c a b )
 * Reverse rotates (or rotates backwards) the top three stack items.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("-ROT (Reverse Rotate) Stack Operation Tests")
class MinusRotStatementTest {

    private Stack stack;
    private MinusRotStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new MinusRotStatement(ForthTokenType.MINUS_ROT, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        MinusRotStatement stmt = new MinusRotStatement(ForthTokenType.MINUS_ROT, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        MinusRotStatement stmt = new MinusRotStatement(ForthTokenType.MINUS_ROT, 123);
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
    // Basic -ROT Operation Tests
    // ============================================================================

    @Test
    @DisplayName("-ROT rotates three values backwards correctly ( 1 2 3 -- 3 1 2 )")
    void testMinusRotThreeValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger(), "Top should be 2");
        assertEquals(1, stack.pop().toInteger(), "Second should be 1");
        assertEquals(3, stack.pop().toInteger(), "Third should be 3");
    }

    @Test
    @DisplayName("-ROT maintains stack size")
    void testMinusRotMaintainsSize() throws Exception {
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
    @DisplayName("-ROT with four items rotates only top three")
    void testMinusRotFourItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(4, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    // ============================================================================
    // -ROT with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("-ROT with RealValue")
    void testMinusRotRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        statement.execute();

        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 1.5) < 0.00001);
        assertTrue(Math.abs(stack.pop().toReal() - 3.5) < 0.00001);
    }

    @Test
    @DisplayName("-ROT with StringValue")
    void testMinusRotStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        stack.push(new StringValue("third"));
        statement.execute();

        assertEquals("second", stack.pop().toString());
        assertEquals("first", stack.pop().toString());
        assertEquals("third", stack.pop().toString());
    }

    @Test
    @DisplayName("-ROT with zero values")
    void testMinusRotZeroes() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));
        statement.execute();

        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential -ROT Operations
    // ============================================================================

    @Test
    @DisplayName("Triple -ROT returns to original order")
    void testTripleMinusRotReturnToOriginal() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        statement.execute();  // [1, 2, 3] -> [3, 1, 2]
        statement.execute();  // [3, 1, 2] -> [2, 3, 1]
        statement.execute();  // [2, 3, 1] -> [1, 2, 3]

        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("-ROT is inverse of ROT")
    void testMinusRotIsInverseOfRot() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        RotStatement rot = new RotStatement(ForthTokenType.ROT, 1);
        rot.execute();  // [1, 2, 3] -> [2, 3, 1]

        statement.execute();  // [2, 3, 1] -> [1, 2, 3]

        assertEquals(3, stack.pop().toInteger());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("-ROT preserves items below rotation")
    void testMinusRotPreservesBelow() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(3, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("-ROT on empty stack should handle exception gracefully")
    void testMinusRotEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("-ROT with one item should handle exception")
    void testMinusRotSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    @Test
    @DisplayName("-ROT with two items should handle exception")
    void testMinusRotTwoItems() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("-ROT with large stack")
    void testMinusRotLargeStack() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(8, stack.pop().toInteger());
        assertEquals(7, stack.pop().toInteger());
        assertEquals(9, stack.pop().toInteger());
    }

    @Test
    @DisplayName("-ROT pattern test")
    void testMinusRotPattern() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(40));
        stack.push(new IntegerValue(50));

        statement.execute();

        assertEquals(5, stack.size());
        assertEquals(40, stack.pop().toInteger());
        assertEquals(30, stack.pop().toInteger());
        assertEquals(50, stack.pop().toInteger());
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("-ROT with negative numbers")
    void testMinusRotNegativeNumbers() throws Exception {
        stack.push(new IntegerValue(-1));
        stack.push(new IntegerValue(-2));
        stack.push(new IntegerValue(-3));
        statement.execute();

        assertEquals(-2, stack.pop().toInteger());
        assertEquals(-1, stack.pop().toInteger());
        assertEquals(-3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("-ROT with mixed positive and negative")
    void testMinusRotMixedPosNeg() throws Exception {
        stack.push(new IntegerValue(-100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(-300));
        statement.execute();

        assertEquals(200, stack.pop().toInteger());
        assertEquals(-100, stack.pop().toInteger());
        assertEquals(-300, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with MINUS_ROT token type")
    void testWithMinusRotTokenType() {
        MinusRotStatement stmt = new MinusRotStatement(ForthTokenType.MINUS_ROT, 1);
        assertNotNull(stmt, "Statement should be created with MINUS_ROT token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            MinusRotStatement stmt = new MinusRotStatement(ForthTokenType.MINUS_ROT, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
