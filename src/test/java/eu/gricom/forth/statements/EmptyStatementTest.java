package eu.gricom.forth.statements;

import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for EmptyStatement class.
 * Tests the empty statement (end of user input marker).
 */
class EmptyStatementTest {

    private Token mockToken;

    @BeforeEach
    void setUp() {
        mockToken = new Token("", ForthTokenType.CARRIAGE_RETURN, 1);
    }

    @Test
    void testEmptyStatementCreation() {
        EmptyStatement stmt = new EmptyStatement(mockToken, 0);
        assertNotNull(stmt);
        assertEquals(1, stmt.getTokenNumber());
    }

    @Test
    void testGetTokenNumber() {
        Token token = new Token("", ForthTokenType.CARRIAGE_RETURN, 5);
        EmptyStatement stmt = new EmptyStatement(token, 0);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    void testStatementInterface() {
        EmptyStatement stmt = new EmptyStatement(mockToken, 0);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    void testContentMethod() throws Exception {
        EmptyStatement stmt = new EmptyStatement(mockToken, 0);
        String content = stmt.content();
        assertNotNull(content);
        assertTrue(content.contains("EMPTY"));
    }

    @Test
    void testStructureMethod() throws Exception {
        Token token = new Token("", ForthTokenType.CARRIAGE_RETURN, 3);
        EmptyStatement stmt = new EmptyStatement(token, 5);
        String structure = stmt.structure();
        assertNotNull(structure);
        assertTrue(structure.contains("\"EMPTY\""));
        assertTrue(structure.contains("\"TOKEN_NR\": \"5\""));
    }

    @Test
    void testExecuteDoesNotThrowException() throws Exception {
        EmptyStatement stmt = new EmptyStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    void testContentFormat() throws Exception {
        EmptyStatement stmt = new EmptyStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("EMPTY ()", content);
    }

    @Test
    void testStructureJsonFormat() throws Exception {
        Token token = new Token("", ForthTokenType.CARRIAGE_RETURN, 2);
        EmptyStatement stmt = new EmptyStatement(token, 7);
        String structure = stmt.structure();
        assertTrue(structure.contains("{\"EMPTY\""));
        assertTrue(structure.contains("}}"));
        assertTrue(structure.startsWith("{"));
        assertTrue(structure.contains("\"TOKEN_NR\": \"7\""));
    }


    @Test
    void testMultipleInstances() throws Exception {
        Token token1 = new Token("", ForthTokenType.CARRIAGE_RETURN, 1);
        Token token2 = new Token("", ForthTokenType.CARRIAGE_RETURN, 2);

        EmptyStatement stmt1 = new EmptyStatement(token1, 1);
        EmptyStatement stmt2 = new EmptyStatement(token2, 2);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(2, stmt2.getTokenNumber());
    }

    @Test
    void testStructureWithDifferentTokenNumbers() throws Exception {
        Token token = new Token("", ForthTokenType.CARRIAGE_RETURN, 1);
        EmptyStatement stmt1 = new EmptyStatement(token, 0);
        EmptyStatement stmt2 = new EmptyStatement(token, 10);

        String structure1 = stmt1.structure();
        String structure2 = stmt2.structure();

        assertTrue(structure1.contains("\"0\""));
        assertTrue(structure2.contains("\"10\""));
    }
}