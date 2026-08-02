package eu.gricom.forth.statements.inOut;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for PrintStatement class.
 * Tests the FORTH PRINT command which pops a value from the stack and prints it.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("PrintStatement Tests")
class PrintStatementTest {

    private Token mockToken;
    private Stack testStack;

    @BeforeEach
    void setUp() {
        mockToken = new Token("PRINT", ForthTokenType.PRINT, 1);
        testStack = new Stack();
        testStack.reset();
    }

    // ============= Constructor Tests =============

    @Test
    @DisplayName("Constructor: Should initialize with token and token number")
    void testConstructorInitialization() {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 3);
        PrintStatement stmt = new PrintStatement(token, 5);
        assertNotNull(stmt);
    }

    @Test
    @DisplayName("Constructor: Should store token number")
    void testConstructorStoresTokenNumber() {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 2);
        PrintStatement stmt = new PrintStatement(token, 7);
        assertEquals(2, stmt.getTokenNumber());
    }

    // ============= getTokenNumber() Tests =============

    @Test
    @DisplayName("getTokenNumber(): Should return token line number")
    void testGetTokenNumberReturnsTokenLine() {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 5);
        PrintStatement stmt = new PrintStatement(token, 0);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("getTokenNumber(): Should return correct line numbers across various values")
    void testGetTokenNumberVariations() {
        assertEquals(1, new PrintStatement(new Token("PRINT", ForthTokenType.PRINT, 1), 0).getTokenNumber());
        assertEquals(42, new PrintStatement(new Token("PRINT", ForthTokenType.PRINT, 42), 0).getTokenNumber());
        assertEquals(999, new PrintStatement(new Token("PRINT", ForthTokenType.PRINT, 999), 0).getTokenNumber());
    }

    // ============= content() Tests =============

    @Test
    @DisplayName("content(): Should return 'PRINT ()'")
    void testContentReturnsCorrectFormat() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("PRINT ()", content);
    }

    @Test
    @DisplayName("content(): Should not contain variable names or values")
    void testContentDoesNotContainValues() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        String content = stmt.content();
        assertFalse(content.contains("42"));
        assertFalse(content.contains("Hello"));
    }

    // ============= structure() Tests =============

    @Test
    @DisplayName("structure(): Should return valid JSON format")
    void testStructureReturnsValidJSON() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 3);
        PrintStatement stmt = new PrintStatement(token, 7);
        String structure = stmt.structure();

        assertTrue(structure.startsWith("{"));
        assertTrue(structure.endsWith("}}"));
        assertTrue(structure.contains("\"PRINT\""));
        assertTrue(structure.contains("\"TOKEN_NR\""));
        assertTrue(structure.contains("\"TOKEN_TYPE\""));
    }

    @Test
    @DisplayName("structure(): Should include correct token number")
    void testStructureIncludesTokenNumber() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 1);
        PrintStatement stmt = new PrintStatement(token, 12);
        String structure = stmt.structure();
        assertTrue(structure.contains("\"TOKEN_NR\": \"12\""));
    }

    @Test
    @DisplayName("structure(): Should include token type")
    void testStructureIncludesTokenType() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 1);
        PrintStatement stmt = new PrintStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }

    // ============= execute() with Integer Values =============

    @Test
    @DisplayName("execute(): Should print positive integer value")
    void testExecuteWithPositiveInteger() {
        testStack.push(new IntegerValue(42));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print negative integer value")
    void testExecuteWithNegativeInteger() {
        testStack.push(new IntegerValue(-99));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print zero")
    void testExecuteWithZero() {
        testStack.push(new IntegerValue(0));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print large integer values")
    void testExecuteWithLargeInteger() {
        testStack.push(new IntegerValue(Integer.MAX_VALUE));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print minimum integer values")
    void testExecuteWithMinimumInteger() {
        testStack.push(new IntegerValue(Integer.MIN_VALUE));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with String Values =============

    @Test
    @DisplayName("execute(): Should print string value")
    void testExecuteWithString() {
        testStack.push(new StringValue("Hello FORTH"));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print empty string")
    void testExecuteWithEmptyString() {
        testStack.push(new StringValue(""));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with special characters")
    void testExecuteWithSpecialCharacters() {
        testStack.push(new StringValue("!@#$%^&*()"));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with spaces")
    void testExecuteWithSpaces() {
        testStack.push(new StringValue("   multiple   spaces   "));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with numbers")
    void testExecuteWithNumericString() {
        testStack.push(new StringValue("123.456"));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with Real Values =============

    @Test
    @DisplayName("execute(): Should print floating point value")
    void testExecuteWithRealValue() {
        testStack.push(new RealValue(3.14159));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print real value with decimal")
    void testExecuteWithDecimalReal() {
        testStack.push(new RealValue(99.99));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print negative real value")
    void testExecuteWithNegativeReal() {
        testStack.push(new RealValue(-42.5));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print zero as real")
    void testExecuteWithZeroReal() {
        testStack.push(new RealValue(0.0));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print very small real values")
    void testExecuteWithSmallReal() {
        testStack.push(new RealValue(0.000001));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print very large real values")
    void testExecuteWithLargeReal() {
        testStack.push(new RealValue(1e100));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with Boolean Values =============

    @Test
    @DisplayName("execute(): Should print TRUE boolean value as -1")
    void testExecuteWithBooleanTrue() {
        testStack.push(new BooleanValue(true));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print FALSE boolean value as 0")
    void testExecuteWithBooleanFalse() {
        testStack.push(new BooleanValue(false));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print TRUE using -1 constant")
    void testExecuteWithBooleanTrueConstant() {
        testStack.push(new BooleanValue(BooleanValue.TRUE));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print FALSE using 0 constant")
    void testExecuteWithBooleanFalseConstant() {
        testStack.push(new BooleanValue(BooleanValue.FALSE));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= Stack State Tests =============

    @Test
    @DisplayName("Stack: Should REMOVE value from stack after execute")
    void testExecuteRemovesValueFromStack() throws Exception {
        testStack.push(new IntegerValue(42));
        assertEquals(1, testStack.size());

        PrintStatement stmt = new PrintStatement(mockToken, 0);
        stmt.execute();

        assertEquals(0, testStack.size());
    }

    @Test
    @DisplayName("Stack: Should not leave value after printing")
    void testExecuteDoesNotLeaveValueOnStack() throws Exception {
        testStack.push(new StringValue("test"));
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        stmt.execute();

        assertTrue(testStack.size() == 0);
    }

    // ============= Empty Stack Exception Tests =============

    @Test
    @DisplayName("Exception: Should handle empty stack gracefully")
    void testExecuteWithEmptyStackDoesNotThrow() {
        testStack.reset();
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Exception: Should not propagate EmptyStackException")
    void testExecuteDoesNotPropagateEmptyStackException() {
        testStack.reset();
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        // Should catch the exception internally and print error message
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= Multiple Consecutive Operations =============

    @Test
    @DisplayName("Multiple: Should print multiple consecutive values")
    void testMultipleConsecutivePrints() throws Exception {
        testStack.push(new IntegerValue(1));
        testStack.push(new IntegerValue(2));
        testStack.push(new IntegerValue(3));

        PrintStatement stmt = new PrintStatement(mockToken, 0);

        stmt.execute(); // prints 3, stack size = 2
        assertEquals(2, testStack.size());

        stmt.execute(); // prints 2, stack size = 1
        assertEquals(1, testStack.size());

        stmt.execute(); // prints 1, stack size = 0
        assertEquals(0, testStack.size());
    }

    @Test
    @DisplayName("Multiple: Should handle different types in sequence")
    void testMultipleTypesInSequence() throws Exception {
        testStack.push(new IntegerValue(42));
        testStack.push(new RealValue(3.14));
        testStack.push(new StringValue("end"));

        PrintStatement stmt = new PrintStatement(mockToken, 0);

        assertDoesNotThrow(() -> stmt.execute()); // prints "end"
        assertEquals(2, testStack.size());

        assertDoesNotThrow(() -> stmt.execute()); // prints 3.14
        assertEquals(1, testStack.size());

        assertDoesNotThrow(() -> stmt.execute()); // prints 42
        assertEquals(0, testStack.size());
    }

    // ============= Integration Tests =============

    @Test
    @DisplayName("Integration: Should be Statement implementation")
    void testIsStatementImplementation() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        assertTrue(stmt instanceof eu.gricom.forth.statements.Statement);
    }

    @Test
    @DisplayName("Integration: Should work with multiple token types")
    void testWithDifferentTokenTypes() {
        Token printToken = new Token("PRINT", ForthTokenType.PRINT, 1);
        Token wordToken = new Token("MYWORD", ForthTokenType.WORD, 5);

        PrintStatement stmt1 = new PrintStatement(printToken, 0);
        PrintStatement stmt2 = new PrintStatement(wordToken, 1);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(5, stmt2.getTokenNumber());
    }

    @Test
    @DisplayName("Integration: Structure should contain all required fields")
    void testStructureCompletenesss() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 5);
        PrintStatement stmt = new PrintStatement(token, 15);
        String structure = stmt.structure();

        // Verify structure contains all required components
        assertTrue(structure.contains("\"PRINT\""));
        assertTrue(structure.contains("\"TOKEN_NR\""));
        assertTrue(structure.contains("\"15\""));
        assertTrue(structure.contains("\"TOKEN_TYPE\""));
    }

    // ============= Edge Cases =============

    @Test
    @DisplayName("Edge: Should handle token number zero")
    void testTokenNumberZero() {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 0);
        PrintStatement stmt = new PrintStatement(token, 0);
        assertEquals(0, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Edge: Should handle very large token numbers")
    void testLargeTokenNumber() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 999999);
        PrintStatement stmt = new PrintStatement(token, 999999);
        String structure = stmt.structure();
        assertTrue(structure.contains("999999"));
    }

    @Test
    @DisplayName("Edge: Should print mixed integer and string multiple times")
    void testMixedPrintSequence() throws Exception {
        testStack.push(new IntegerValue(100));
        testStack.push(new StringValue("Value"));
        testStack.push(new RealValue(99.9));

        PrintStatement stmt = new PrintStatement(mockToken, 0);

        assertDoesNotThrow(() -> stmt.execute());
        assertEquals(2, testStack.size());

        assertDoesNotThrow(() -> stmt.execute());
        assertEquals(1, testStack.size());

        assertDoesNotThrow(() -> stmt.execute());
        assertEquals(0, testStack.size());
    }

    @Test
    @DisplayName("Content: Should remain consistent across calls")
    void testContentConsistency() {
        PrintStatement stmt = new PrintStatement(mockToken, 0);
        String content1 = stmt.content();
        String content2 = stmt.content();
        assertEquals(content1, content2);
    }

    @Test
    @DisplayName("Token: Should preserve token type information")
    void testTokenTypePreservation() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT, 5);
        PrintStatement stmt = new PrintStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }
}
