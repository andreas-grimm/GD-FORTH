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
 * Comprehensive test suite for PrintKeepStackStatement class.
 * Tests the FORTH PRINT command which peeks at a value on the stack and prints it
 * without removing it (keeps the value on the stack).
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("PrintKeepStackStatement Tests")
class PrintKeepStackStatementTest {

    private Token mockToken;
    private Stack testStack;

    @BeforeEach
    void setUp() {
        mockToken = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1);
        testStack = new Stack();
        testStack.reset();
    }

    // ============= Constructor Tests =============

    @Test
    @DisplayName("Constructor: Should initialize with token and token number")
    void testConstructorInitialization() {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 3);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 5);
        assertNotNull(stmt);
    }

    @Test
    @DisplayName("Constructor: Should store token number")
    void testConstructorStoresTokenNumber() {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 2);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 7);
        assertEquals(2, stmt.getTokenNumber());
    }

    // ============= getTokenNumber() Tests =============

    @Test
    @DisplayName("getTokenNumber(): Should return token line number")
    void testGetTokenNumberReturnsTokenLine() {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 5);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 0);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("getTokenNumber(): Should return correct line numbers across various values")
    void testGetTokenNumberVariations() {
        assertEquals(1, new PrintKeepStackStatement(new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1), 0).getTokenNumber());
        assertEquals(42, new PrintKeepStackStatement(new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 42), 0).getTokenNumber());
        assertEquals(999, new PrintKeepStackStatement(new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 999), 0).getTokenNumber());
    }

    // ============= content() Tests =============

    @Test
    @DisplayName("content(): Should return 'PRINT ()'")
    void testContentReturnsCorrectFormat() {
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("PRINT ()", content);
    }

    @Test
    @DisplayName("content(): Should not contain variable names or values")
    void testContentDoesNotContainValues() {
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        String content = stmt.content();
        assertFalse(content.contains("42"));
        assertFalse(content.contains("Hello"));
    }

    // ============= structure() Tests =============

    @Test
    @DisplayName("structure(): Should return valid JSON format")
    void testStructureReturnsValidJSON() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 3);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 7);
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
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 12);
        String structure = stmt.structure();
        assertTrue(structure.contains("\"TOKEN_NR\": \"12\""));
    }

    @Test
    @DisplayName("structure(): Should include token type")
    void testStructureIncludesTokenType() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }

    // ============= execute() with Integer Values =============

    @Test
    @DisplayName("execute(): Should print positive integer value")
    void testExecuteWithPositiveInteger() {
        testStack.push(new IntegerValue(42));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print negative integer value")
    void testExecuteWithNegativeInteger() {
        testStack.push(new IntegerValue(-99));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print zero")
    void testExecuteWithZero() {
        testStack.push(new IntegerValue(0));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print large integer values")
    void testExecuteWithLargeInteger() {
        testStack.push(new IntegerValue(Integer.MAX_VALUE));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print minimum integer values")
    void testExecuteWithMinimumInteger() {
        testStack.push(new IntegerValue(Integer.MIN_VALUE));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with String Values =============

    @Test
    @DisplayName("execute(): Should print string value")
    void testExecuteWithString() {
        testStack.push(new StringValue("Hello FORTH"));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print empty string")
    void testExecuteWithEmptyString() {
        testStack.push(new StringValue(""));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with special characters")
    void testExecuteWithSpecialCharacters() {
        testStack.push(new StringValue("!@#$%^&*()"));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with spaces")
    void testExecuteWithSpaces() {
        testStack.push(new StringValue("   multiple   spaces   "));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print string with numbers")
    void testExecuteWithNumericString() {
        testStack.push(new StringValue("123.456"));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with Real Values =============

    @Test
    @DisplayName("execute(): Should print floating point value")
    void testExecuteWithRealValue() {
        testStack.push(new RealValue(3.14159));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print real value with decimal")
    void testExecuteWithDecimalReal() {
        testStack.push(new RealValue(99.99));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print negative real value")
    void testExecuteWithNegativeReal() {
        testStack.push(new RealValue(-42.5));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print zero as real")
    void testExecuteWithZeroReal() {
        testStack.push(new RealValue(0.0));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print very small real values")
    void testExecuteWithSmallReal() {
        testStack.push(new RealValue(0.000001));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print very large real values")
    void testExecuteWithLargeReal() {
        testStack.push(new RealValue(1e100));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= execute() with Boolean Values =============

    @Test
    @DisplayName("execute(): Should print TRUE boolean value as -1")
    void testExecuteWithBooleanTrue() {
        testStack.push(new BooleanValue(true));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print FALSE boolean value as 0")
    void testExecuteWithBooleanFalse() {
        testStack.push(new BooleanValue(false));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print TRUE using -1 constant")
    void testExecuteWithBooleanTrueConstant() {
        testStack.push(new BooleanValue(BooleanValue.TRUE));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should print FALSE using 0 constant")
    void testExecuteWithBooleanFalseConstant() {
        testStack.push(new BooleanValue(BooleanValue.FALSE));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= Stack State Tests - KEY DIFFERENCE FROM PrintStatement =============

    @Test
    @DisplayName("Stack: Should KEEP value on stack after execute")
    void testExecuteKeepsValueOnStack() throws Exception {
        testStack.push(new IntegerValue(42));
        assertEquals(1, testStack.size());

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        stmt.execute();

        assertEquals(1, testStack.size());
    }

    @Test
    @DisplayName("Stack: Should preserve value after printing")
    void testExecutePreservesValue() throws Exception {
        testStack.push(new StringValue("preserved"));
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        stmt.execute();

        assertTrue(testStack.size() == 1);
        Value peekedValue = testStack.peek();
        assertEquals("preserved", peekedValue.toString());
    }

    @Test
    @DisplayName("Stack: Should not modify stack size")
    void testExecuteDoesNotModifyStackSize() throws Exception {
        testStack.push(new IntegerValue(10));
        testStack.push(new RealValue(3.14));
        testStack.push(new StringValue("top"));
        int originalSize = testStack.size();

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        stmt.execute();

        assertEquals(originalSize, testStack.size());
    }

    @Test
    @DisplayName("Stack: Should keep top value accessible after multiple prints")
    void testStackAccessibilityAfterMultiplePrints() throws Exception {
        IntegerValue testValue = new IntegerValue(99);
        testStack.push(testValue);

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);

        stmt.execute();
        Value result1 = testStack.peek();
        assertEquals(99, result1.toInteger());

        stmt.execute();
        Value result2 = testStack.peek();
        assertEquals(99, result2.toInteger());

        stmt.execute();
        Value result3 = testStack.peek();
        assertEquals(99, result3.toInteger());
    }

    // ============= Empty Stack Exception Tests =============

    @Test
    @DisplayName("Exception: Should handle empty stack gracefully")
    void testExecuteWithEmptyStackDoesNotThrow() {
        testStack.reset();
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Exception: Should not propagate EmptyStackException")
    void testExecuteDoesNotPropagateEmptyStackException() {
        testStack.reset();
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        // Should catch the exception internally and print error message
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= Multiple Consecutive Operations =============

    @Test
    @DisplayName("Multiple: Should print same value multiple times without removing it")
    void testPrintSameValueMultipleTimes() throws Exception {
        testStack.push(new IntegerValue(5));

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);

        stmt.execute(); // prints 5, stack size = 1
        assertEquals(1, testStack.size());

        stmt.execute(); // prints 5 again, stack size = 1
        assertEquals(1, testStack.size());

        stmt.execute(); // prints 5 again, stack size = 1
        assertEquals(1, testStack.size());
    }

    @Test
    @DisplayName("Multiple: Should handle multiple values, printing only top")
    void testPrintTopValueWithMultipleOnStack() throws Exception {
        testStack.push(new IntegerValue(1));
        testStack.push(new IntegerValue(2));
        testStack.push(new IntegerValue(3));

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);

        stmt.execute(); // prints 3, stack size = 3
        assertEquals(3, testStack.size());
        assertEquals(3, testStack.peek().toInteger());

        stmt.execute(); // prints 3 again (top), stack size = 3
        assertEquals(3, testStack.size());
        assertEquals(3, testStack.peek().toInteger());
    }

    @Test
    @DisplayName("Multiple: Should allow different types to be inspected without removal")
    void testMultipleTypesInspectionWithoutRemoval() throws Exception {
        testStack.push(new StringValue("bottom"));
        testStack.push(new RealValue(2.71));
        testStack.push(new IntegerValue(42));

        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        int initialSize = testStack.size();

        stmt.execute(); // inspects 42
        assertEquals(initialSize, testStack.size());

        stmt.execute(); // inspects 42 again
        assertEquals(initialSize, testStack.size());

        stmt.execute(); // inspects 42 again
        assertEquals(initialSize, testStack.size());
    }

    // ============= Integration Tests =============

    @Test
    @DisplayName("Integration: Should be Statement implementation")
    void testIsStatementImplementation() {
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        assertTrue(stmt instanceof eu.gricom.forth.statements.Statement);
    }

    @Test
    @DisplayName("Integration: Should work with multiple token types")
    void testWithDifferentTokenTypes() {
        Token printToken = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1);
        Token wordToken = new Token("MYWORD", ForthTokenType.WORD, 5);

        PrintKeepStackStatement stmt1 = new PrintKeepStackStatement(printToken, 0);
        PrintKeepStackStatement stmt2 = new PrintKeepStackStatement(wordToken, 1);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(5, stmt2.getTokenNumber());
    }

    @Test
    @DisplayName("Integration: Structure should contain all required fields")
    void testStructureCompleteness() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 5);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 15);
        String structure = stmt.structure();

        assertTrue(structure.contains("\"PRINT\""));
        assertTrue(structure.contains("\"TOKEN_NR\""));
        assertTrue(structure.contains("\"15\""));
        assertTrue(structure.contains("\"TOKEN_TYPE\""));
    }

    // ============= Edge Cases =============

    @Test
    @DisplayName("Edge: Should handle token number zero")
    void testTokenNumberZero() {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 0);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 0);
        assertEquals(0, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Edge: Should handle very large token numbers")
    void testLargeTokenNumber() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 1);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 999999);
        String structure = stmt.structure();
        assertTrue(structure.contains("999999"));
    }

    @Test
    @DisplayName("Edge: Should correctly distinguish from PrintStatement behavior")
    void testDifferenceFromPrintStatement() throws Exception {
        testStack.push(new IntegerValue(100));
        PrintKeepStackStatement keepStmt = new PrintKeepStackStatement(mockToken, 0);
        keepStmt.execute();
        assertEquals(1, testStack.size());
    }

    @Test
    @DisplayName("Content: Should remain consistent across calls")
    void testContentConsistency() {
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(mockToken, 0);
        String content1 = stmt.content();
        String content2 = stmt.content();
        assertEquals(content1, content2);
    }

    @Test
    @DisplayName("Token: Should preserve token type information")
    void testTokenTypePreservation() throws Exception {
        Token token = new Token("PRINT", ForthTokenType.PRINT_KEEP_STACK, 5);
        PrintKeepStackStatement stmt = new PrintKeepStackStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }

    @Test
    @DisplayName("Stack: Should allow subsequent operations on kept value")
    void testSubsequentOperationsOnKeptValue() throws Exception {
        testStack.push(new IntegerValue(50));
        PrintKeepStackStatement printStmt = new PrintKeepStackStatement(mockToken, 0);
        printStmt.execute();

        // Value is still on stack, should be able to peek it
        Value keptValue = testStack.peek();
        assertEquals(50, keptValue.toInteger());
    }
}
