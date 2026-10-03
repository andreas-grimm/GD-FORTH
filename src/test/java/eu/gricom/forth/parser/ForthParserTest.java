package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.statements.controlFlow.IfStatement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ForthParserTest - Comprehensive test suite for the ForthParser class.
 * <p>
 * Tests parsing of FORTH tokens into executable Statement objects, covering:
 * - Basic number and operator tokens
 * - Arithmetic operations (+, -, *, /, MOD)
 * - Stack operations (DUP, DROP, SWAP, etc.)
 * - Comparison operations (=, <>, <, >, <=, >=, 0=, etc.)
 * - I/O operations (PRINT, PRINT KEEP STACK, CARRIAGE RETURN)
 * - Variable operations (VARIABLE definition, WORD access)
 * - Memory operations (FETCH @, STORE !)
 * - Complex workflows combining multiple operations
 * <p>
 * Variable Token Tests (new):
 * - VARIABLE token parsing (creates VariableStatement)
 * - WORD token parsing for variable access (creates WordStatement)
 * - Error handling for undefined variables
 * - Complete variable workflows with fetch/store operations
 * - Case-sensitive variable name handling
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

    // ============================================================================
    // Stack Operation Tests
    // ============================================================================

    @Test
    void testDupeStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("DUP", ForthTokenType.DUPE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testQuestionDupeStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("?DUP", ForthTokenType.QUESTION_DUPE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testDropStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("DROP", ForthTokenType.DROP, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoDropStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2DROP", ForthTokenType.TWO_DROP, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testSwapStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("SWAP", ForthTokenType.SWAP, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoSwapStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2SWAP", ForthTokenType.TWO_SWAP, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testOverStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("OVER", ForthTokenType.OVER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoOverStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2OVER", ForthTokenType.TWO_OVER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testRotStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("ROT", ForthTokenType.ROT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoRotStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("4", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2ROT", ForthTokenType.TWO_ROT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testMinusRotStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-ROT", ForthTokenType.MINUS_ROT, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testNipStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("NIP", ForthTokenType.NIP, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTuckStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("TUCK", ForthTokenType.TUCK, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testPickStackOperation() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("PICK", ForthTokenType.PICK, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testDepthStackOperation() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("6", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("DEPTH", ForthTokenType.DEPTH, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParseStackStatementMethod() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("DUP", ForthTokenType.DUPE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() >= 2, "Should have at least NumberStatement and StackStatement");
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
    void testParserSyntaxErrorInvalidToken() throws Exception {
        // Note: WORD tokens don't throw during parsing, only during execution.
        // This test creates a WordStatement for an undefined variable.
        // The parse succeeds, but execution would fail.
        tokens.add(new Token("undefined", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        // The parser succeeds; execution would throw if variable is not defined
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

    // ============================================================================
    // Variable Token Tests
    // ============================================================================

    @Test
    void testParserVariableDefinition() throws Exception {
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("myVar", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserMultipleVariableDefinitions() throws Exception {
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("var1", ForthTokenType.WORD, 1));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 2));
        tokens.add(new Token("var2", ForthTokenType.WORD, 2));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 3));
        tokens.add(new Token("var3", ForthTokenType.WORD, 3));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVariableDefinitionWithSpecialCharacterName() throws Exception {
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("my_var_name", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVariableDefinitionWithNumericName() throws Exception {
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("var123", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ============================================================================
    // Word Token Tests (Variable Access)
    // ============================================================================

    @Test
    void testParserWordTokenUndefinedVariable() throws Exception {
        // Note: WORD tokens don't throw during parsing, only during execution.
        // The parser accepts the token and creates a WordStatement.
        // Execution would fail if the variable is not defined.
        tokens.add(new Token("undefined", ForthTokenType.WORD, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0, "Parser should create statements for WORD tokens");
    }

    @Test
    void testParserWordTokenAfterVariableDefinition() throws Exception {
        // First define a variable
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("myVar", ForthTokenType.WORD, 1));
        // Then reference it
        tokens.add(new Token("myVar", ForthTokenType.WORD, 2));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserMultipleWordTokensAfterVariables() throws Exception {
        // Define three variables
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("x", ForthTokenType.WORD, 1));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 2));
        tokens.add(new Token("y", ForthTokenType.WORD, 2));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 3));
        tokens.add(new Token("z", ForthTokenType.WORD, 3));
        // Access them
        tokens.add(new Token("x", ForthTokenType.WORD, 4));
        tokens.add(new Token("y", ForthTokenType.WORD, 5));
        tokens.add(new Token("z", ForthTokenType.WORD, 6));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVariableWithFetchOperation() throws Exception {
        // Define a variable
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("data", ForthTokenType.WORD, 1));
        // Access and fetch
        tokens.add(new Token("data", ForthTokenType.WORD, 2));
        tokens.add(new Token("@", ForthTokenType.FETCH, 3));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVariableWithStoreOperation() throws Exception {
        // Define a variable
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("counter", ForthTokenType.WORD, 1));
        // Push value and store
        tokens.add(new Token("42", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("counter", ForthTokenType.WORD, 3));
        tokens.add(new Token("!", ForthTokenType.STORE, 4));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserCompleteVariableWorkflow() throws Exception {
        // Define variable
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("sum", ForthTokenType.WORD, 1));
        // Store initial value
        tokens.add(new Token("0", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("sum", ForthTokenType.WORD, 3));
        tokens.add(new Token("!", ForthTokenType.STORE, 4));
        // Push number for addition
        tokens.add(new Token("10", ForthTokenType.NUMBER, 5));
        tokens.add(new Token("sum", ForthTokenType.WORD, 6));
        tokens.add(new Token("@", ForthTokenType.FETCH, 7));
        tokens.add(new Token("+", ForthTokenType.PLUS, 8));
        tokens.add(new Token("sum", ForthTokenType.WORD, 9));
        tokens.add(new Token("!", ForthTokenType.STORE, 10));
        tokens.add(new Token("sum", ForthTokenType.WORD, 11));
        tokens.add(new Token("@", ForthTokenType.FETCH, 12));
        tokens.add(new Token(".", ForthTokenType.PRINT, 13));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserVariableInArithmeticExpression() throws Exception {
        // Define variables
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("a", ForthTokenType.WORD, 1));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 2));
        tokens.add(new Token("b", ForthTokenType.WORD, 2));
        // Store values
        tokens.add(new Token("5", ForthTokenType.NUMBER, 3));
        tokens.add(new Token("a", ForthTokenType.WORD, 4));
        tokens.add(new Token("!", ForthTokenType.STORE, 5));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 6));
        tokens.add(new Token("b", ForthTokenType.WORD, 7));
        tokens.add(new Token("!", ForthTokenType.STORE, 8));
        // Retrieve and add
        tokens.add(new Token("a", ForthTokenType.WORD, 9));
        tokens.add(new Token("@", ForthTokenType.FETCH, 10));
        tokens.add(new Token("b", ForthTokenType.WORD, 11));
        tokens.add(new Token("@", ForthTokenType.FETCH, 12));
        tokens.add(new Token("+", ForthTokenType.PLUS, 13));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserCaseSensitiveVariableNames() throws Exception {
        // Define variables with different cases
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("MyVar", ForthTokenType.WORD, 1));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 2));
        tokens.add(new Token("myvar", ForthTokenType.WORD, 2));
        // Access them
        tokens.add(new Token("MyVar", ForthTokenType.WORD, 3));
        tokens.add(new Token("myvar", ForthTokenType.WORD, 4));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ============================================================================
    // Question Token Tests (Debugging Operator)
    // ============================================================================

    @Test
    void testParserQuestionTokenBasic() throws Exception {
        // Define a variable
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("x", ForthTokenType.WORD, 1));
        // Store a value
        tokens.add(new Token("42", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("x", ForthTokenType.WORD, 3));
        tokens.add(new Token("!", ForthTokenType.STORE, 4));
        // Question operator to debug/print
        tokens.add(new Token("x", ForthTokenType.WORD, 5));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 6));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserQuestionOperator() throws Exception {
        // Simple variable inspection: x ?
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("counter", ForthTokenType.WORD, 1));
        tokens.add(new Token("counter", ForthTokenType.WORD, 2));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 3));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserQuestionWithMultipleVariables() throws Exception {
        // Define and inspect multiple variables
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("a", ForthTokenType.WORD, 1));
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 2));
        tokens.add(new Token("b", ForthTokenType.WORD, 2));
        // Inspect both
        tokens.add(new Token("a", ForthTokenType.WORD, 3));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 4));
        tokens.add(new Token("b", ForthTokenType.WORD, 5));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 6));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserQuestionInArithmeticContext() throws Exception {
        // Store value in variable, then debug it
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("result", ForthTokenType.WORD, 1));
        // Calculate and store
        tokens.add(new Token("5", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 3));
        tokens.add(new Token("+", ForthTokenType.PLUS, 4));
        tokens.add(new Token("result", ForthTokenType.WORD, 5));
        tokens.add(new Token("!", ForthTokenType.STORE, 6));
        // Debug the result
        tokens.add(new Token("result", ForthTokenType.WORD, 7));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 8));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testParserQuestionVsOtherIOOperators() throws Exception {
        // Mix of different I/O operators
        tokens.add(new Token("VARIABLE", ForthTokenType.VARIABLE, 1));
        tokens.add(new Token("x", ForthTokenType.WORD, 1));
        // Store value
        tokens.add(new Token("99", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("x", ForthTokenType.WORD, 3));
        tokens.add(new Token("!", ForthTokenType.STORE, 4));
        // Use different operators
        tokens.add(new Token("x", ForthTokenType.WORD, 5));
        tokens.add(new Token("?", ForthTokenType.QUESTION, 6));  // Question: fetch and print
        tokens.add(new Token("x", ForthTokenType.WORD, 7));
        tokens.add(new Token("@", ForthTokenType.FETCH, 8));     // Fetch: just retrieve
        tokens.add(new Token(".", ForthTokenType.PRINT, 9));     // Print: print from stack
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ============================================================================
    // Arithmetic Operations Tests (1+, 1-, 2*, 2/)
    // ============================================================================

    @Test
    void testOnePlusOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("1+", ForthTokenType.ONE_PLUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testOneMinusOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("1-", ForthTokenType.ONE_MINUS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoMultiplyOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2*", ForthTokenType.TWO_MULTIPLY, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testTwoDivideOperator() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("2/", ForthTokenType.TWO_DIVIDE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ============================================================================
    // Mathematics Operations Tests (ABS, MAX, MIN, NEGATE, SIGN)
    // ============================================================================

    @Test
    void testAbsOperator() throws Exception {
        tokens.add(new Token("-5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("ABS", ForthTokenType.ABS, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testMaxOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("MAX", ForthTokenType.MAX, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testMinOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("MIN", ForthTokenType.MIN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testNegateOperator() throws Exception {
        tokens.add(new Token("5", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("NEGATE", ForthTokenType.NEGATE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testSignOperator() throws Exception {
        tokens.add(new Token("-42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("SIGN", ForthTokenType.SIGN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ============================================================================
    // Combined Operations Tests
    // ============================================================================

    @Test
    void testComplexArithmeticSequence() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("1+", ForthTokenType.ONE_PLUS, 2));
        tokens.add(new Token("2*", ForthTokenType.TWO_MULTIPLY, 3));
        tokens.add(new Token("3", ForthTokenType.NUMBER, 4));
        tokens.add(new Token("-", ForthTokenType.MINUS, 5));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testMathematicsOperatorsSequence() throws Exception {
        tokens.add(new Token("-15", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("ABS", ForthTokenType.ABS, 2));
        tokens.add(new Token("SIGN", ForthTokenType.SIGN, 3));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    @Test
    void testMinMaxSequence() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 2));
        tokens.add(new Token("MIN", ForthTokenType.MIN, 3));
        tokens.add(new Token("5", ForthTokenType.NUMBER, 4));
        tokens.add(new Token("MAX", ForthTokenType.MAX, 5));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);
    }

    // ================= IF/THEN CONTROL FLOW TESTS =================

    @Test
    @DisplayName("Should parse simple IF...THEN structure")
    void testParseSimpleIfThen() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token(".", ForthTokenType.PRINT, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        boolean hasIfStatement = false;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                hasIfStatement = true;
                break;
            }
        }
        assertTrue(hasIfStatement, "Parser should create IfStatement for IF...THEN block");
    }

    @Test
    @DisplayName("Should parse IF...ELSE...THEN structure")
    void testParseIfElseThen() throws Exception {
        tokens.add(new Token("0", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token(".", ForthTokenType.PRINT, 1));
        tokens.add(new Token("ELSE", ForthTokenType.ELSE, 1));
        tokens.add(new Token("99", ForthTokenType.NUMBER, 1));
        tokens.add(new Token(".", ForthTokenType.PRINT, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        boolean hasIfStatement = false;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                hasIfStatement = true;
                break;
            }
        }
        assertTrue(hasIfStatement, "Parser should create IfStatement for IF...ELSE...THEN block");
    }

    @Test
    @DisplayName("Should parse nested IF statements")
    void testParseNestedIfStatements() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("2", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        // Count IfStatement instances
        int ifCount = 0;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                ifCount++;
            }
        }
        assertEquals(1, ifCount, "Outer IF should be one IfStatement; inner IF is in its branch");
    }

    @Test
    @DisplayName("Should throw SyntaxErrorException for IF without THEN")
    void testIfWithoutThenThrowsError() {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertThrows(SyntaxErrorException.class, () -> parser.parse(),
                "Parser should throw SyntaxErrorException for IF without matching THEN");
    }

    @Test
    @DisplayName("Should throw SyntaxErrorException for unexpected THEN without IF")
    void testThenWithoutIfThrowsError() {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertThrows(SyntaxErrorException.class, () -> parser.parse(),
                "Parser should throw SyntaxErrorException for THEN without matching IF");
    }

    @Test
    @DisplayName("Should throw SyntaxErrorException for unexpected ELSE without IF")
    void testElseWithoutIfThrowsError() {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("ELSE", ForthTokenType.ELSE, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        assertThrows(SyntaxErrorException.class, () -> parser.parse(),
                "Parser should throw SyntaxErrorException for ELSE without matching IF");
    }

    @Test
    @DisplayName("Should parse IF...THEN with complex true-branch")
    void testIfThenComplexTrueBranch() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token(".", ForthTokenType.PRINT, 1));
        tokens.add(new Token("DUP", ForthTokenType.DUPE, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        boolean hasIfStatement = false;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                hasIfStatement = true;
                break;
            }
        }
        assertTrue(hasIfStatement, "Parser should handle complex true-branch");
    }

    @Test
    @DisplayName("Should parse IF...ELSE...THEN with complex branches")
    void testIfElseThenComplexBranches() throws Exception {
        tokens.add(new Token("1", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("ELSE", ForthTokenType.ELSE, 1));
        tokens.add(new Token("100", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("200", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("-", ForthTokenType.MINUS, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        boolean hasIfStatement = false;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                hasIfStatement = true;
                break;
            }
        }
        assertTrue(hasIfStatement, "Parser should handle complex true and false branches");
    }

    @Test
    @DisplayName("Should parse IF after other statements")
    void testIfFollowingOtherStatements() throws Exception {
        tokens.add(new Token("10", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("20", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("+", ForthTokenType.PLUS, 1));
        tokens.add(new Token("DUP", ForthTokenType.DUPE, 1));
        tokens.add(new Token("0", ForthTokenType.NUMBER, 1));
        tokens.add(new Token(">", ForthTokenType.GREATER_THAN, 1));
        tokens.add(new Token("IF", ForthTokenType.IF, 1));
        tokens.add(new Token("42", ForthTokenType.NUMBER, 1));
        tokens.add(new Token("THEN", ForthTokenType.THEN, 1));
        tokens.add(new Token("", ForthTokenType.EOP, 1));
        parser = new ForthParser(tokens);

        List<Statement> statements = parser.parse();
        assertNotNull(statements);
        assertTrue(statements.size() > 0);

        // Should have parsed the preceding statements and the IF statement
        boolean hasIfStatement = false;
        int nonIfStatements = 0;
        for (Statement stmt : statements) {
            if (stmt instanceof IfStatement) {
                hasIfStatement = true;
            } else if (!(stmt instanceof eu.gricom.forth.statements.EmptyStatement)) {
                nonIfStatements++;
            }
        }
        assertTrue(hasIfStatement, "Parser should create IfStatement");
        assertTrue(nonIfStatements > 0, "Parser should have preceding statements");
    }
}
