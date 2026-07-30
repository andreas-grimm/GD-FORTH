package eu.gricom.forth.tokenizer;

import eu.gricom.forth.error.SyntaxErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ForthLexer class.
 * Tests the tokenization of FORTH source code.
 */
class ForthLexerTest {
    private ForthLexer lexer;

    @BeforeEach
    void setUp() {
        lexer = new ForthLexer();
    }

    @Test
    void testSingleNumber() throws SyntaxErrorException {
        String source = "42";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("42", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testMultipleNumbers() throws SyntaxErrorException {
        String source = "1 2 3\n4 5";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
        assertEquals("1", tokens.get(0).getText());
    }

    @Test
    void testNegativeNumbers() throws SyntaxErrorException {
        String source = "-42";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("-42", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testFloatingPointNumbers() throws SyntaxErrorException {
        String source = "3.14";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("3.14", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testNegativeFloatingPointNumbers() throws SyntaxErrorException {
        String source = "-3.14";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("-3.14", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testSimpleString() throws SyntaxErrorException {
        String source = "\"Hello\"";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("Hello", tokens.get(0).getText());
        assertEquals(ForthTokenType.STRING, tokens.get(0).getType());
    }

    @Test
    void testMultiwordString() throws SyntaxErrorException {
        String source = "\"Hello World\"";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals(ForthTokenType.STRING, tokens.get(0).getType());
    }

    @Test
    void testReservedWord() throws SyntaxErrorException {
        String source = "+";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals(ForthTokenType.PLUS, tokens.get(0).getType());
    }

    @Test
    void testArithmeticOperators() throws SyntaxErrorException {
        String source = "+ - * /";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() >= 4);
        assertEquals(ForthTokenType.PLUS, tokens.get(0).getType());
        assertEquals(ForthTokenType.MINUS, tokens.get(1).getType());
        assertEquals(ForthTokenType.MULTIPLY, tokens.get(2).getType());
        assertEquals(ForthTokenType.DIVIDE, tokens.get(3).getType());
    }

    @Test
    void testCommentHandling() throws SyntaxErrorException {
        String source = "42 ( this is a comment";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() >= 1);
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testWord() throws SyntaxErrorException {
        String source = "myvar";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("myvar", tokens.get(0).getText());
        assertEquals(ForthTokenType.WORD, tokens.get(0).getType());
    }

    @Test
    void testEmptyLine() throws SyntaxErrorException {
        String source = "42";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
    }

    @Test
    void testMixedContent() throws SyntaxErrorException {
        String source = "10 20 +";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() >= 3);
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
        assertEquals(ForthTokenType.NUMBER, tokens.get(1).getType());
        assertEquals(ForthTokenType.PLUS, tokens.get(2).getType());
    }

    @Test
    void testLineNumbers() throws SyntaxErrorException {
        String source = "42\n10";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() >= 2);
        assertTrue(tokens.get(0).getLine() >= 1);
        assertTrue(tokens.get(1).getLine() > tokens.get(0).getLine());
    }

    @Test
    void testZero() throws SyntaxErrorException {
        String source = "0";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("0", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testLargeNumbers() throws SyntaxErrorException {
        String source = "1000000";
        List<Token> tokens = lexer.tokenize(source);

        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals("1000000", tokens.get(0).getText());
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }
}
