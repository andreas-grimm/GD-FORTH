package eu.gricom.forth.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Token class.
 * Tests the Token data structure used in lexical analysis.
 */
class TokenTest {

    @Test
    void testTokenCreation() {
        Token token = new Token("42", ForthTokenType.NUMBER, 1);

        assertEquals("42", token.getText());
        assertEquals(ForthTokenType.NUMBER, token.getType());
        assertEquals(1, token.getLine());
        assertEquals(1, token.getCommandSequence());
    }

    @Test
    void testTokenWithCommandSequence() {
        Token token = new Token("PLUS", ForthTokenType.PLUS, 5, 3);

        assertEquals("PLUS", token.getText());
        assertEquals(ForthTokenType.PLUS, token.getType());
        assertEquals(5, token.getLine());
        assertEquals(3, token.getCommandSequence());
    }

    @Test
    void testSetText() {
        Token token = new Token("oldtext", ForthTokenType.STRING, 1);
        String result = token.setText("newtext");

        assertEquals("newtext", result);
        assertEquals("newtext", token.getText());
    }

    @Test
    void testTokenEquality() {
        Token token1 = new Token("42", ForthTokenType.NUMBER, 1);
        Token token2 = new Token("42", ForthTokenType.NUMBER, 1);

        assertTrue(token1.equals(token2));
    }

    @Test
    void testTokenInequalityDifferentText() {
        Token token1 = new Token("42", ForthTokenType.NUMBER, 1);
        Token token2 = new Token("43", ForthTokenType.NUMBER, 1);

        assertFalse(token1.equals(token2));
    }

    @Test
    void testTokenInequalityDifferentType() {
        Token token1 = new Token("42", ForthTokenType.NUMBER, 1);
        Token token2 = new Token("42", ForthTokenType.WORD, 1);

        assertFalse(token1.equals(token2));
    }

    @Test
    void testTokenInequalityDifferentLine() {
        Token token1 = new Token("42", ForthTokenType.NUMBER, 1);
        Token token2 = new Token("42", ForthTokenType.NUMBER, 2);

        assertFalse(token1.equals(token2));
    }

    @Test
    void testTokenInequalityDifferentSequence() {
        Token token1 = new Token("42", ForthTokenType.NUMBER, 1, 1);
        Token token2 = new Token("42", ForthTokenType.NUMBER, 1, 2);

        assertFalse(token1.equals(token2));
    }

    @Test
    void testTokenStructure() {
        Token token = new Token("42", ForthTokenType.NUMBER, 1);
        String structure = token.structure();

        assertNotNull(structure);
        assertTrue(structure.contains("TOKEN"));
        assertTrue(structure.contains("42"));
        assertTrue(structure.contains("NUMBER"));
    }

    @Test
    void testTokenStructureWithSequence() {
        Token token = new Token("test", ForthTokenType.WORD, 2, 5);
        String structure = token.structure();

        assertNotNull(structure);
        assertTrue(structure.contains("WORD"));
        assertTrue(structure.contains("test"));
    }

    @Test
    void testEmptyTokenText() {
        Token token = new Token("", ForthTokenType.WORD, 1);

        assertEquals("", token.getText());
        assertEquals(ForthTokenType.WORD, token.getType());
    }

    @Test
    void testStringToken() {
        Token token = new Token("Hello World", ForthTokenType.STRING, 3);

        assertEquals("Hello World", token.getText());
        assertEquals(ForthTokenType.STRING, token.getType());
        assertEquals(3, token.getLine());
    }

    @Test
    void testNullSafeEquals() {
        Token token = new Token(null, ForthTokenType.NUMBER, 1);
        Token token2 = new Token(null, ForthTokenType.NUMBER, 1);

        assertTrue(token.equals(token2));
    }

    @Test
    void testEndOfProgramToken() {
        Token token = new Token("", ForthTokenType.EOP, 0);

        assertEquals("", token.getText());
        assertEquals(ForthTokenType.EOP, token.getType());
        assertEquals(0, token.getLine());
    }

    @Test
    void testEmptyLineToken() {
        Token token = new Token("empty", ForthTokenType.EMPTY_LINE, 5);

        assertEquals("empty", token.getText());
        assertEquals(ForthTokenType.EMPTY_LINE, token.getType());
        assertEquals(5, token.getLine());
    }
}
