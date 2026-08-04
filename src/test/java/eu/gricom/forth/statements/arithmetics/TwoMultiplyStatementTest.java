package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TwoMultiplyStatementTest - Comprehensive test suite for the 2* (two-multiply) operator
 */
@DisplayName("TwoMultiplyStatement Test Suite")
public class TwoMultiplyStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should multiply by 2 for positive number")
    public void testExecutePositive() throws Exception {
        _oStack.push(new IntegerValue(5));
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(10, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should multiply by 2 for negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-5));
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-10, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should multiply zero by 2")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should accumulate")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(5));
        TwoMultiplyStatement stmt1 = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        TwoMultiplyStatement stmt2 = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 2);

        stmt1.execute();
        stmt2.execute();

        assertEquals(1, _oStack.size());
        assertEquals(20, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("2*"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        TwoMultiplyStatement stmt = new TwoMultiplyStatement(ForthTokenType.TWO_MULTIPLY, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("2*"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }
}
