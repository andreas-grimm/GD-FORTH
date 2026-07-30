package eu.gricom.forth.statements;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for PrintStatement class.
 * Tests the PRINT command functionality.
 */
class PrintStatementTest {

    private Token mockToken;

    @BeforeEach
    void setUp() {
        mockToken = new Token("PRINT", ForthTokenType.PRINT, 1);
    }

    @Test
    void testPrintStatementCreation() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    void testGetTokenNumber() {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 5);
        PrintStatement stmt = new PrintStatement(token, 0);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    void testStatementInterface() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    void testContentMethod() throws Exception {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("PRINT"));
    }

    @Test
    void testStructureMethod() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 3);
        PrintStatement stmt = new PrintStatement(token, 5);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("\"PRINT\""));
        assertTrue(structure.contains("\"TOKEN_NR\": \"5\""));
    }

    @Test
    void testExecuteWithIntegerValue() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();
        oStack.push(new IntegerValue(42));

        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testExecuteWithStringValue() throws Exception {
        Stack oStack = new Stack();
        oStack.reset();
        oStack.push(new StringValue("Hello FORTH"));

        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testExecuteWithCarriageReturn() throws Exception {
        Token crToken = new Token("", ForthTokenType.CARRIAGE_RETURN, 1);
        PrintStatement stmt = new PrintStatement(crToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testContentFormat() throws Exception {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("PRINT ()", content);
    }

    @Test
    void testStructureJsonFormat() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 2);
        PrintStatement stmt = new PrintStatement(token, 7);
        String structure = stmt.structure();
        assertTrue(structure.contains("{\"PRINT\""));
        assertTrue(structure.contains("}}"));
        assertTrue(structure.startsWith("{"));
    }

}