package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NegateStatementTest - Comprehensive test suite for the NEGATE operator
 */
@DisplayName("NegateStatement Test Suite")
public class NegateStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should negate positive number")
    public void testExecutePositive() throws Exception {
        _oStack.push(new IntegerValue(5));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should negate negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-5));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should negate zero")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large positive numbers")
    public void testExecuteLargePositive() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MIN_VALUE + 1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large negative numbers")
    public void testExecuteLargeNegative() throws Exception {
        _oStack.push(new IntegerValue(Integer.MIN_VALUE + 1));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should return to original (for non-zero)")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(10));
        NegateStatement stmt1 = new NegateStatement(ForthTokenType.NEGATE, 1);
        NegateStatement stmt2 = new NegateStatement(ForthTokenType.NEGATE, 2);

        stmt1.execute();
        stmt2.execute();

        assertEquals(1, _oStack.size());
        assertEquals(10, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("NEGATE"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("NEGATE"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }

    @Test
    @DisplayName("getTokenNumber should return correct position")
    public void testGetTokenNumber() {
        NegateStatement stmt1 = new NegateStatement(ForthTokenType.NEGATE, 5);
        NegateStatement stmt2 = new NegateStatement(ForthTokenType.NEGATE, 15);
        assertEquals(5, stmt1.getTokenNumber());
        assertEquals(15, stmt2.getTokenNumber());
    }

    @Test
    @DisplayName("execute should handle one value then negate")
    public void testExecuteOneValue() throws Exception {
        _oStack.push(new IntegerValue(42));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(-42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute on small positive value")
    public void testExecuteSmallPositive() throws Exception {
        _oStack.push(new IntegerValue(1));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute on small negative value")
    public void testExecuteSmallNegative() throws Exception {
        _oStack.push(new IntegerValue(-1));
        NegateStatement stmt = new NegateStatement(ForthTokenType.NEGATE, 1);
        stmt.execute();
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }
}
