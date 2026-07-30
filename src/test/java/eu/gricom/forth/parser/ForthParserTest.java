package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ForthParser class.
 * Tests parsing of FORTH tokens into statements.
 */
class ForthParserTest {
    private ForthParser parser;
    private List<Token> tokens;

    @BeforeEach
    void setUp() {
        tokens = new ArrayList<>();
    }

    @Test
    void testParserCreation() {
        parser = new ForthParser(tokens);

        assertNotNull(parser);
    }

    @Test
    void testEmptyTokenList() throws Exception {
        tokens.add(new Token("", ForthTokenType.EOP, 0));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testSingleNumberToken() throws Exception {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testPlusOperatorToken() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testMinusOperatorToken() throws Exception {
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testMultiplyOperatorToken() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testDivideOperatorToken() throws Exception {
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("/", ForthTokenType.DIVIDE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testModOperatorToken() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("MOD", ForthTokenType.MOD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testGetTokenMethod() {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        Token firstToken = parser.getToken(0);
        assertNotNull(firstToken);
        assertEquals(ForthTokenType.NUMBER, firstToken.getType());
        assertEquals("42", firstToken.getText());
    }

    @Test
    void testGetTokenOffset() {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        Token secondToken = parser.getToken(1);
        assertNotNull(secondToken);
        assertEquals(ForthTokenType.PLUS, secondToken.getType());
    }

    @Test
    void testGetTokenOutOfBounds() {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        Token token = parser.getToken(10);
        assertNotNull(token);
        assertEquals(ForthTokenType.EOP, token.getType());
    }

    @Test
    void testMultipleNumbersSequence() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testComplexArithmeticExpression() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testNegativeNumbers() throws Exception {
        tokens.add(new Token("-42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testFloatingPointNumbers() throws Exception {
        tokens.add(new Token("3.14", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2.71", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
    }

    @Test
    void testParserWithMultipleLines() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 3));
        tokens.add(new Token("", ForthTokenType.EOP, 3));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserWithPrintStatement() throws Exception {
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserWithCarriageReturn() throws Exception {
        tokens.add(new Token("", ForthTokenType.CARRIAGE_RETURN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserWithPrintKeepStack() throws Exception {
        tokens.add(new Token("", ForthTokenType.PRINT_KEEP_STACK, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserMixedNumbersAndOperators() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("15", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserZeroNumber() throws Exception {
        tokens.add(new Token("0", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserLargeNumbers() throws Exception {
        tokens.add(new Token("999999", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("888888", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserNegativeZero() throws Exception {
        tokens.add(new Token("-0", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserDecimalRounding() throws Exception {
        tokens.add(new Token("3.14159", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserSequentialOperations() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserSyntaxErrorInvalidToken() {
        tokens.add(new Token("INVALID", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertThrows(SyntaxErrorException.class, () -> parser.parse());
    }

    @Test
    void testParserAllArithmeticOperators() throws Exception {
        // Test with at least 2 numbers before each operator
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("/", ForthTokenType.DIVIDE, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("MOD", ForthTokenType.MOD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserLongSequenceOfNumbers() throws Exception {
        for (int i = 0; i < 20; i++) {
            tokens.add(new Token(String.valueOf(i), ForthTokenType.NUMBER, 1));
        }
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 20);
    }

    @Test
    void testParserPrintBeforeAndAfter() throws Exception {
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserMultiplePrintStatements() throws Exception {
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserStatementOrder() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("", ForthTokenType.PRINT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVeryLargeNumber() throws Exception {
        tokens.add(new Token("2147483647", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVerySmallNegativeNumber() throws Exception {
        tokens.add(new Token("-2147483648", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserCarriageReturnHandling() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.CARRIAGE_RETURN, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("", ForthTokenType.EOP, 2));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserCreatesFinalEmptyStatement() throws Exception {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        // Should have at least: NumberStatement + EmptyStatement
        assertTrue(statements.size() >= 2);
    }

    @Test
    void testParserInvalidFloatingPointFormat() {
        tokens.add(new Token("3.14.15", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertThrows(SyntaxErrorException.class, () -> parser.parse());
    }

    @Test
    void testParserMultipleOperatorsInRow() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("*", ForthTokenType.MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserGetTokenWithNegativeOffset() {
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        // Getting with negative offset causes IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> parser.getToken(-1));
    }

    @Test
    void testParserGetTokenMultipleOffsets() {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertEquals("1", parser.getToken(0).getText());
        assertEquals("2", parser.getToken(1).getText());
        assertEquals("3", parser.getToken(2).getText());
        assertEquals("4", parser.getToken(3).getText());
    }

    @Test
    void testParserConsecutiveNumbers() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() >= 5);
    }

    @Test
    void testParserDivisionOperator() throws Exception {
        tokens.add(new Token("100", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("/", ForthTokenType.DIVIDE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserModuloOperator() throws Exception {
        tokens.add(new Token("17", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("MOD", ForthTokenType.MOD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }
}
