package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TwoDivideStatement Test Suite")
public class TwoDivideStatementTest {
    private Stack _oStack;

    @BeforeEach
    public void setUp() {
        _oStack = new Stack();
        _oStack.reset();
    }

    @Test
    @DisplayName("execute should divide by 2 for positive even number")
    public void testExecutePositiveEven() throws Exception {
        _oStack.push(new IntegerValue(10));
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        stmt.execute();
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should divide by 2 for positive odd number")
    public void testExecutePositiveOdd() throws Exception {
        _oStack.push(new IntegerValue(11));
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        stmt.execute();
        assertEquals(5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should divide by 2 for negative number")
    public void testExecuteNegative() throws Exception {
        _oStack.push(new IntegerValue(-10));
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        stmt.execute();
        assertEquals(-5, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle zero")
    public void testExecuteZero() throws Exception {
        _oStack.push(new IntegerValue(0));
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        stmt.execute();
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute multiple times should accumulate")
    public void testExecuteMultiple() throws Exception {
        _oStack.push(new IntegerValue(80));
        TwoDivideStatement stmt1 = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        TwoDivideStatement stmt2 = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 2);

        stmt1.execute();
        stmt2.execute();

        assertEquals(20, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("content should return operation description")
    public void testContent() throws Exception {
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        assertTrue(stmt.content().contains("2/"));
    }

    @Test
    @DisplayName("structure should return JSON format")
    public void testStructure() throws Exception {
        TwoDivideStatement stmt = new TwoDivideStatement(ForthTokenType.TWO_DIVIDE, 1);
        String structure = stmt.structure();
        assertTrue(structure.contains("2/"));
        assertTrue(structure.contains("TOKEN_NR"));
    }
}
