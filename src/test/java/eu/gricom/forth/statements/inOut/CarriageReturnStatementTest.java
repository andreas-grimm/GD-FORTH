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
 * Comprehensive test suite for CarriageReturnStatement class.
 * Tests the FORTH CR (Carriage Return) command which prints an empty line (newline).
 * This statement does not interact with the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("CarriageReturnStatement Tests")
class CarriageReturnStatementTest {

    private Token mockToken;
    private Stack testStack;

    @BeforeEach
    void setUp() {
        mockToken = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1);
        testStack = new Stack();
        testStack.reset();
    }

    // ============= Constructor Tests =============

    @Test
    @DisplayName("Constructor: Should initialize with token and token number")
    void testConstructorInitialization() {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 3);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 5);
        assertNotNull(stmt);
    }

    @Test
    @DisplayName("Constructor: Should store token number")
    void testConstructorStoresTokenNumber() {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 2);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 7);
        assertEquals(2, stmt.getTokenNumber());
    }

    // ============= getTokenNumber() Tests =============

    @Test
    @DisplayName("getTokenNumber(): Should return token line number")
    void testGetTokenNumberReturnsTokenLine() {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 5);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 0);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("getTokenNumber(): Should return correct line numbers across various values")
    void testGetTokenNumberVariations() {
        assertEquals(1, new CarriageReturnStatement(new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1), 0).getTokenNumber());
        assertEquals(42, new CarriageReturnStatement(new Token("CR", ForthTokenType.CARRIAGE_RETURN, 42), 0).getTokenNumber());
        assertEquals(999, new CarriageReturnStatement(new Token("CR", ForthTokenType.CARRIAGE_RETURN, 999), 0).getTokenNumber());
    }

    // ============= content() Tests =============

    @Test
    @DisplayName("content(): Should return 'CR ()'")
    void testContentReturnsCorrectFormat() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("CR ()", content);
    }

    @Test
    @DisplayName("content(): Should not contain any values")
    void testContentDoesNotContainValues() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        String content = stmt.content();
        assertFalse(content.contains("42"));
        assertFalse(content.contains("PRINT"));
        assertFalse(content.contains("newline"));
    }

    @Test
    @DisplayName("content(): Should be exactly 'CR ()'")
    void testContentExact() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        assertEquals("CR ()", stmt.content());
    }

    // ============= structure() Tests =============

    @Test
    @DisplayName("structure(): Should return valid JSON format")
    void testStructureReturnsValidJSON() throws Exception {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 3);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 7);
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
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 12);
        String structure = stmt.structure();
        assertTrue(structure.contains("\"TOKEN_NR\": \"12\""));
    }

    @Test
    @DisplayName("structure(): Should include token type")
    void testStructureIncludesTokenType() throws Exception {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }

    // ============= execute() Tests =============

    @Test
    @DisplayName("execute(): Should not throw exception")
    void testExecuteDoesNotThrow() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should succeed with empty stack")
    void testExecuteWithEmptyStack() {
        testStack.reset();
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("execute(): Should succeed with populated stack")
    void testExecuteWithPopulatedStack() {
        testStack.push(new IntegerValue(42));
        testStack.push(new StringValue("test"));
        testStack.push(new RealValue(3.14));

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt.execute());
    }

    // ============= Stack State Tests - KEY: Stack Not Affected =============

    @Test
    @DisplayName("Stack: Should NOT modify stack size")
    void testExecuteDoesNotModifyStackSize() throws Exception {
        testStack.push(new IntegerValue(42));
        assertEquals(1, testStack.size());

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(1, testStack.size());
    }

    @Test
    @DisplayName("Stack: Should preserve all values on stack")
    void testExecutePreservesStackValues() throws Exception {
        testStack.push(new IntegerValue(1));
        testStack.push(new IntegerValue(2));
        testStack.push(new IntegerValue(3));
        int originalSize = testStack.size();

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(originalSize, testStack.size());
    }

    @Test
    @DisplayName("Stack: Should preserve stack order")
    void testExecutePreservesStackOrder() throws Exception {
        testStack.push(new IntegerValue(10));
        testStack.push(new IntegerValue(20));
        testStack.push(new IntegerValue(30));

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(30, testStack.pop().toInteger());
        assertEquals(20, testStack.pop().toInteger());
        assertEquals(10, testStack.pop().toInteger());
    }

    @Test
    @DisplayName("Stack: Should keep top value accessible")
    void testExecuteKeepsTopValue() throws Exception {
        IntegerValue topValue = new IntegerValue(99);
        testStack.push(topValue);

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        Value peekedValue = testStack.peek();
        assertEquals(99, peekedValue.toInteger());
    }

    @Test
    @DisplayName("Stack: Should not remove any values from stack")
    void testExecuteRemovesNoValues() throws Exception {
        testStack.push(new StringValue("first"));
        testStack.push(new RealValue(1.5));
        testStack.push(new BooleanValue(true));

        int originalSize = testStack.size();

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(originalSize, testStack.size());
    }

    // ============= Multiple Consecutive Operations =============

    @Test
    @DisplayName("Multiple: Should print multiple newlines without stack interaction")
    void testMultipleConsecutiveCarriageReturns() throws Exception {
        testStack.push(new IntegerValue(42));
        int originalSize = testStack.size();

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);

        stmt.execute(); // prints newline 1
        assertEquals(originalSize, testStack.size());

        stmt.execute(); // prints newline 2
        assertEquals(originalSize, testStack.size());

        stmt.execute(); // prints newline 3
        assertEquals(originalSize, testStack.size());
    }

    @Test
    @DisplayName("Multiple: Should work in sequence without affecting other operations")
    void testCarriageReturnInSequenceWithOtherValues() throws Exception {
        testStack.push(new IntegerValue(100));
        testStack.push(new StringValue("line1"));

        CarriageReturnStatement crStmt = new CarriageReturnStatement(mockToken, 0);

        crStmt.execute();
        assertEquals(2, testStack.size());
        assertEquals("line1", testStack.peek().toString());

        crStmt.execute();
        assertEquals(2, testStack.size());

        crStmt.execute();
        assertEquals(2, testStack.size());
    }

    @Test
    @DisplayName("Multiple: Should print empty lines on multiple calls")
    void testPrintEmptyLinesMultipleTimes() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);

        assertDoesNotThrow(() -> stmt.execute()); // empty line 1
        assertDoesNotThrow(() -> stmt.execute()); // empty line 2
        assertDoesNotThrow(() -> stmt.execute()); // empty line 3
        assertDoesNotThrow(() -> stmt.execute()); // empty line 4
        assertDoesNotThrow(() -> stmt.execute()); // empty line 5
    }

    // ============= Integration Tests =============

    @Test
    @DisplayName("Integration: Should be Statement implementation")
    void testIsStatementImplementation() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        assertTrue(stmt instanceof eu.gricom.forth.statements.Statement);
    }

    @Test
    @DisplayName("Integration: Should work with multiple token types")
    void testWithDifferentTokenTypes() {
        Token crToken = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1);
        Token wordToken = new Token("MYWORD", ForthTokenType.WORD, 5);

        CarriageReturnStatement stmt1 = new CarriageReturnStatement(crToken, 0);
        CarriageReturnStatement stmt2 = new CarriageReturnStatement(wordToken, 1);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(5, stmt2.getTokenNumber());
    }

    @Test
    @DisplayName("Integration: Structure should contain all required fields")
    void testStructureCompleteness() throws Exception {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 5);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 15);
        String structure = stmt.structure();

        assertTrue(structure.contains("\"PRINT\""));
        assertTrue(structure.contains("\"TOKEN_NR\""));
        assertTrue(structure.contains("\"15\""));
        assertTrue(structure.contains("\"TOKEN_TYPE\""));
    }

    @Test
    @DisplayName("Integration: Should be independent of stack state")
    void testIndependentOfStackState() throws Exception {
        // Test 1: Empty stack
        testStack.reset();
        CarriageReturnStatement stmt1 = new CarriageReturnStatement(mockToken, 0);
        assertDoesNotThrow(() -> stmt1.execute());

        // Test 2: Full stack
        testStack.reset();
        for (int i = 0; i < 100; i++) {
            testStack.push(new IntegerValue(i));
        }
        CarriageReturnStatement stmt2 = new CarriageReturnStatement(mockToken, 0);
        int sizeBeforeCR = testStack.size();
        assertDoesNotThrow(() -> stmt2.execute());
        assertEquals(sizeBeforeCR, testStack.size());
    }

    // ============= Edge Cases =============

    @Test
    @DisplayName("Edge: Should handle token number zero")
    void testTokenNumberZero() {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 0);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 0);
        assertEquals(0, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Edge: Should handle very large token numbers")
    void testLargeTokenNumber() throws Exception {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 1);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 999999);
        String structure = stmt.structure();
        assertTrue(structure.contains("999999"));
    }

    @Test
    @DisplayName("Edge: Should work with very large stack")
    void testWithVeryLargeStack() throws Exception {
        testStack.reset();
        for (int i = 0; i < 1000; i++) {
            testStack.push(new IntegerValue(i));
        }
        int originalSize = testStack.size();

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(originalSize, testStack.size());
    }

    @Test
    @DisplayName("Content: Should remain consistent across calls")
    void testContentConsistency() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        String content1 = stmt.content();
        String content2 = stmt.content();
        assertEquals(content1, content2);
    }

    @Test
    @DisplayName("Token: Should preserve token type information")
    void testTokenTypePreservation() throws Exception {
        Token token = new Token("CR", ForthTokenType.CARRIAGE_RETURN, 5);
        CarriageReturnStatement stmt = new CarriageReturnStatement(token, 0);
        String structure = stmt.structure();
        assertTrue(structure.contains("PRINT"));
    }

    @Test
    @DisplayName("Design: Should be specifically for carriage return only")
    void testPurposeIsCarriageReturnOnly() {
        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        String content = stmt.content();
        assertEquals("CR ()", content);
        assertNotEquals("PRINT ()", content);
    }

    @Test
    @DisplayName("Output: Should print empty string not consume stack")
    void testPrintsEmptyLineWithoutStackConsumption() throws Exception {
        testStack.push(new IntegerValue(42));
        testStack.push(new StringValue("value"));

        CarriageReturnStatement stmt = new CarriageReturnStatement(mockToken, 0);
        stmt.execute();

        assertEquals(2, testStack.size());
        assertEquals("value", testStack.peek().toString());
    }

    @Test
    @DisplayName("Sequence: Should work between print statements")
    void testWorksBetweenPrintStatements() throws Exception {
        testStack.push(new StringValue("line1"));
        testStack.push(new StringValue("line2"));
        testStack.push(new StringValue("line3"));

        // Simulate: PRINT CR PRINT CR PRINT CR
        PrintKeepStackStatement printStmt = new PrintKeepStackStatement(mockToken, 0);
        CarriageReturnStatement crStmt = new CarriageReturnStatement(mockToken, 0);

        printStmt.execute();     // prints "line3"
        crStmt.execute();        // prints newline, stack unchanged
        assertEquals(3, testStack.size());

        crStmt.execute();        // prints newline, stack unchanged
        assertEquals(3, testStack.size());

        crStmt.execute();        // prints newline, stack unchanged
        assertEquals(3, testStack.size());
    }
}
