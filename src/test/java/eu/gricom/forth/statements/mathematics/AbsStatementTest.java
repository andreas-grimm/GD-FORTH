package eu.gricom.forth.statements.mathematics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AbsStatementTest - Comprehensive test suite for the ABS (absolute value) operator
 */
@DisplayName("AbsStatement Test Suite")
public class AbsStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("Constructor initializes correctly")
    public void testConstructor() {
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("execute should return absolute value of positive number")
    public void testExecutePositive() throws Exception {
        _oStack.push(new IntegerValue(42));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return absolute value of negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-42));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should return zero for zero")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large positive numbers")
    public void testExecuteLargePositive() throws Exception {
        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large negative numbers")
    public void testExecuteLargeNegative() throws Exception {
        _oStack.push(new IntegerValue(-Integer.MAX_VALUE));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle negative one")
    public void testExecuteNegativeOne() throws Exception {
        _oStack.push(new IntegerValue(-1));
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        stmt.execute();
        assertEquals(1, _oStack.size());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should maintain correct operation")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(-42));

        AbsStatement stmt1 = new AbsStatement(ForthTokenType.ABS, 1);
        AbsStatement stmt2 = new AbsStatement(ForthTokenType.ABS, 2);
        AbsStatement stmt3 = new AbsStatement(ForthTokenType.ABS, 3);

        stmt1.execute();
        stmt2.execute();
        stmt3.execute();

        assertEquals(1, _oStack.size());
        assertEquals(42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("ABS"));
        assertTrue(content.contains("ABSOLUTE_VALUE"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        AbsStatement stmt = new AbsStatement(ForthTokenType.ABS, 1);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("ABS"));
        assertTrue(structure.contains("TOKEN_NR"));
        assertTrue(structure.contains("TOKEN_TYPE"));
    }
}
