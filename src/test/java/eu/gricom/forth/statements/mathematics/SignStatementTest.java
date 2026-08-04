package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SignStatementTest - Comprehensive test suite for the SIGN operator
 */
@DisplayName("SignStatement Test Suite")
public class SignStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should return 1 for positive number")
    public void testExecutePositive() throws Exception {
        _oStack.push(new IntegerValue(5));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return -1 for negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-5));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return 0 for zero")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return 1 for large positive number")
    public void testExecuteLargePositive() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return -1 for large negative number")
    public void testExecuteLargeNegative() throws Exception {
        _oStack.push(new IntegerValue(Integer.MIN_VALUE + 1));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return 1 for one")
    public void testExecuteOne() throws Exception {
        _oStack.push(new IntegerValue(1));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return -1 for negative one")
    public void testExecuteNegativeOne() throws Exception {
        _oStack.push(new IntegerValue(-1));
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        stmt.execute();
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times maintains correct sign extraction")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(42));
        SignStatement stmt1 = new SignStatement(ForthTokenType.SIGN, 1);
        SignStatement stmt2 = new SignStatement(ForthTokenType.SIGN, 2);

        stmt1.execute();  // 42 -> 1
        stmt2.execute();  // 1 -> 1

        assertEquals(1, _oStack.size());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute on negative then sign again")
    public void testExecuteNegativeTwice() throws Exception {
        _oStack.push(new IntegerValue(-42));
        SignStatement stmt1 = new SignStatement(ForthTokenType.SIGN, 1);
        SignStatement stmt2 = new SignStatement(ForthTokenType.SIGN, 2);

        stmt1.execute();  // -42 -> -1
        stmt2.execute();  // -1 -> -1

        assertEquals(1, _oStack.size());
        assertEquals(-1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute on zero then sign again")
    public void testExecuteZeroTwice() throws Exception {
        _oStack.push(new IntegerValue(0));
        SignStatement stmt1 = new SignStatement(ForthTokenType.SIGN, 1);
        SignStatement stmt2 = new SignStatement(ForthTokenType.SIGN, 2);

        stmt1.execute();  // 0 -> 0
        stmt2.execute();  // 0 -> 0

        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("SIGN"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        SignStatement stmt = new SignStatement(ForthTokenType.SIGN, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("SIGN"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }

    @Test
    @DisplayName("getTokenNumber should return correct position")
    public void testGetTokenNumber() {
        SignStatement stmt1 = new SignStatement(ForthTokenType.SIGN, 5);
        SignStatement stmt2 = new SignStatement(ForthTokenType.SIGN, 15);
        assertEquals(5, stmt1.getTokenNumber());
        assertEquals(15, stmt2.getTokenNumber());
    }
}
