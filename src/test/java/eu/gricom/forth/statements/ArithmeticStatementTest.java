package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ArithmeticStatement class.
 * Tests arithmetic operations on the stack.
 */
class ArithmeticStatementTest {

    @Test
    void testArithmeticStatementCreation() {
        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        assertNotNull(stmt);
        assertEquals(0, stmt.getTokenNumber());
    }

    @Test
    void testTokenNumberRetrieval() {
        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    void testAllOperatorTypesCreation() {
        assertNotNull(new ArithmeticStatement(ForthTokenType.PLUS, 0));
        assertNotNull(new ArithmeticStatement(ForthTokenType.MINUS, 1));
        assertNotNull(new ArithmeticStatement(ForthTokenType.MULTIPLY, 2));
        assertNotNull(new ArithmeticStatement(ForthTokenType.DIVIDE, 3));
        assertNotNull(new ArithmeticStatement(ForthTokenType.MOD, 4));
    }

    @Test
    void testStatementContent() throws Exception {
        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        assertEquals("", stmt.content());
    }

    @Test
    void testStatementStructure() throws Exception {
        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        assertEquals("", stmt.structure());
    }

    @Test
    void testStatementInterface() {
        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 5);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    void testExecutePlusOperation() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    void testExecuteMinusOperation() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(20));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    void testExecuteMultiplyOperation() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(6));
        oStack.push(new IntegerValue(7));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    void testExecuteDivideOperation() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(20));
        oStack.push(new IntegerValue(4));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    void testExecuteModOperation() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(17));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(2, result.toInteger());
    }

    @Test
    void testExecutePlusWithNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-10));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    void testExecuteMinusWithNegativeResult() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(5));
        oStack.push(new IntegerValue(10));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    void testExecuteWithZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(0));
        oStack.push(new IntegerValue(42));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(42, result.toInteger());
    }

    // ================= NEGATIVE TEST CASES =================

    @Test
    void testExecuteWithEmptyStack() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        // Should not throw, but handles exception internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testExecuteWithOnlyOneValue() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        // Should not throw, but handles exception internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testDivisionByZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(0));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        // Should not throw, but handles exception internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testModuloByZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(0));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        // Should not throw, but handles exception internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testMultiplyWithNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-5));
        oStack.push(new IntegerValue(3));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-15, result.toInteger());
    }

    @Test
    void testMultiplyTwoNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-5));
        oStack.push(new IntegerValue(-3));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(15, result.toInteger());
    }

    @Test
    void testDivideWithNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-20));
        oStack.push(new IntegerValue(4));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-5, result.toInteger());
    }

    @Test
    void testDivideTwoNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-20));
        oStack.push(new IntegerValue(-4));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(5, result.toInteger());
    }

    @Test
    void testModuloWithNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-17));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-2, result.toInteger());
    }

    @Test
    void testModuloTwoNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-17));
        oStack.push(new IntegerValue(-5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Note: Java modulo preserves sign of dividend
        assertEquals(-2, result.toInteger());
    }

    @Test
    void testPlusWithAllNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-10));
        oStack.push(new IntegerValue(-20));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-30, result.toInteger());
    }

    @Test
    void testMinusWithAllNegativeNumbers() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-10));
        oStack.push(new IntegerValue(-20));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(10, result.toInteger());
    }

    @Test
    void testZeroDivisionByPositive() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(0));
        oStack.push(new IntegerValue(10));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    void testZeroMultiplication() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(0));
        oStack.push(new IntegerValue(999));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    void testLargeNumberAddition() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(1000000));
        oStack.push(new IntegerValue(2000000));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(3000000, result.toInteger());
    }

    @Test
    void testLargeNumberMultiplication() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(1000));
        oStack.push(new IntegerValue(1000));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(1000000, result.toInteger());
    }

    @Test
    void testNegativeDividedByNegative() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-100));
        oStack.push(new IntegerValue(-10));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(10, result.toInteger());
    }

    @Test
    void testOperationWithMaxInteger() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(Integer.MAX_VALUE));
        oStack.push(new IntegerValue(1));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Integer overflow wraps around
        assertEquals(Integer.MIN_VALUE, result.toInteger());
    }

    @Test
    void testOperationWithMinInteger() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(Integer.MIN_VALUE));
        oStack.push(new IntegerValue(-1));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Integer overflow wraps around
        assertEquals(Integer.MAX_VALUE, result.toInteger());
    }

    @Test
    void testOneMinusZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(0));
        oStack.push(new IntegerValue(1));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 0, push 1. Pop 1 (first), pop 0 (second). Result: 0 - 1 = -1
        assertEquals(-1, result.toInteger());
    }

    @Test
    void testZeroMinusOne() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(1));
        oStack.push(new IntegerValue(0));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 1, push 0. Pop 0 (first), pop 1 (second). Result: 1 - 0 = 1
        assertEquals(1, result.toInteger());
    }

    @Test
    void testModuloWithResultZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(5));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    void testModuloLargerDividend() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(17));
        oStack.push(new IntegerValue(3));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 17, push 3. Pop 3 (first), pop 17 (second). Result: 17 % 3 = 2
        assertEquals(2, result.toInteger());
    }

    @Test
    void testDivisionRemainder() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(3));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 10, push 3. Pop 3 (first), pop 10 (second). Result: 10 / 3 = 3 (truncated)
        assertEquals(3, result.toInteger());
    }

    @Test
    void testMultiplyByOne() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(1));
        oStack.push(new IntegerValue(42));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    void testMultiplyByMinusOne() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-1));
        oStack.push(new IntegerValue(42));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MULTIPLY, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-42, result.toInteger());
    }

    @Test
    void testDivideByOne() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(42));
        oStack.push(new IntegerValue(1));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 42, push 1. Pop 1 (first), pop 42 (second). Result: 42 / 1 = 42
        assertEquals(42, result.toInteger());
    }

    @Test
    void testDivideByMinusOne() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(42));
        oStack.push(new IntegerValue(-1));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.DIVIDE, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push 42, push -1. Pop -1 (first), pop 42 (second). Result: 42 / -1 = -42
        assertEquals(-42, result.toInteger());
    }

    @Test
    void testPlusZeroPlusZero() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(0));
        oStack.push(new IntegerValue(0));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.PLUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    void testMinusNegativeFromNegative() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(-5));
        oStack.push(new IntegerValue(-10));

        ArithmeticStatement stmt = new ArithmeticStatement(ForthTokenType.MINUS, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        // Stack: push -5, push -10. Pop -10 (first), pop -5 (second). Result: -5 - (-10) = 5
        assertEquals(5, result.toInteger());
    }
}
