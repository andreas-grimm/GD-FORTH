package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OneMinusStatementTest - Comprehensive test suite for the 1- (one-minus) operator
 */
@DisplayName("OneMinusStatement Test Suite")
public class OneMinusStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should subtract 1 from positive number")
    public void testExecutePositive() throws Exception {
        _oStack.push(new IntegerValue(5));
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(4, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should subtract 1 from negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-5));
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-6, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should subtract 1 from zero")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large numbers")
    public void testExecuteLarge() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE - 1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should accumulate")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(10));
        OneMinusStatement stmt1 = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        OneMinusStatement stmt2 = new OneMinusStatement(ForthTokenType.ONE_MINUS, 2);
        OneMinusStatement stmt3 = new OneMinusStatement(ForthTokenType.ONE_MINUS, 3);

        stmt1.execute();
        stmt2.execute();
        stmt3.execute();

        assertEquals(1, _oStack.size());
        assertEquals(7, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("1-"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        OneMinusStatement stmt = new OneMinusStatement(ForthTokenType.ONE_MINUS, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("1-"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }
}
