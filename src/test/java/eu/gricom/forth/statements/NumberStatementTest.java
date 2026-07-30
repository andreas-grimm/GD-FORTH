package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for NumberStatement class.
 * Tests number pushing onto the stack in FORTH.
 */
class NumberStatementTest {

    @Test
    void testNumberStatementCreation() throws Exception {
        NumberStatement stmt = new NumberStatement(42, 0);
        assertNotNull(stmt);
        assertEquals(0, stmt.getTokenNumber());
    }

    @Test
    void testTokenNumberVariations() throws Exception {
        assertEquals(0, new NumberStatement(42, 0).getTokenNumber());
        assertEquals(5, new NumberStatement(42, 5).getTokenNumber());
        assertEquals(999, new NumberStatement(42, 999).getTokenNumber());
    }

    @Test
    void testStatementInterface() throws Exception {
        NumberStatement stmt = new NumberStatement(10, 0);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    void testContentFormat() throws Exception {
        NumberStatement stmt = new NumberStatement(42, 0);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("NUMBER"));
        assertTrue(content.contains("42"));
    }

    @Test
    void testStructureFormat() throws Exception {
        NumberStatement stmt = new NumberStatement(42, 5);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("\"NUMBER\""));
        assertTrue(structure.contains("\"TOKEN_NR\": \"5\""));
    }

    @Test
    void testExecutePushesPositiveNumberToStack() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        NumberStatement stmt = new NumberStatement(42, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    void testExecutePushesNegativeNumberToStack() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        NumberStatement stmt = new NumberStatement(-50, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(-50, result.toInteger());
    }

    @Test
    void testExecutePushesZeroToStack() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        NumberStatement stmt = new NumberStatement(0, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) oStack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    void testExecuteMultipleNumbersStackOrder() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();

        new NumberStatement(10, 0).execute();
        new NumberStatement(20, 1).execute();
        new NumberStatement(30, 2).execute();

        assertEquals(30, ((IntegerValue) oStack.pop()).toInteger());
        assertEquals(20, ((IntegerValue) oStack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) oStack.pop()).toInteger());
    }

    @Test
    void testContentWithZero() throws Exception {
        NumberStatement stmt = new NumberStatement(0, 0);
        String content = stmt.content();
        assertTrue(content.contains("0"));
    }

    @Test
    void testContentWithNegativeNumber() throws Exception {
        NumberStatement stmt = new NumberStatement(-100, 0);
        String content = stmt.content();
        assertTrue(content.contains("-100"));
    }
}
