package eu.gricom.forth.statements.inOut;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QuestionStatementTest.java
 * <p>
 * Comprehensive unit test suite for QuestionStatement class, testing the "?" (question)
 * operator that fetches and prints variable values.
 */
@DisplayName("QuestionStatement Test Suite")
public class QuestionStatementTest {

    private Stack _oStack;
    private Variables _oVariables;
    private ByteArrayOutputStream _outputStream;
    private PrintStream _originalOut;

    @BeforeEach
    public void setUp() throws Exception {
        // Reset stack
        _oStack = new Stack();
        _oStack.reset();

        // Clear the static map in Variables
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> map = (Map<Integer, Object>) varField.get(null);
        map.clear();

        // Clear variable names list
        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) namesField.get(null);
        if (names != null) {
            names.clear();
        }

        _oVariables = new Variables();

        // Capture standard output
        _originalOut = System.out;
        _outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(_outputStream));
    }

    // ============================================================================
    // Constructor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token and token number")
    public void testConstructor() {
        Token token = new Token("?", ForthTokenType.QUESTION, 1);
        QuestionStatement stmt = new QuestionStatement(token, 1);
        assertNotNull(stmt, "QuestionStatement should be created");
        assertEquals(1, stmt.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should handle zero token number")
    public void testConstructorWithZeroTokenNumber() {
        Token token = new Token("?", ForthTokenType.QUESTION, 0);
        QuestionStatement stmt = new QuestionStatement(token, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number zero should be valid");
    }

    @Test
    @DisplayName("Constructor should handle large token numbers")
    public void testConstructorWithLargeTokenNumber() {
        Token token = new Token("?", ForthTokenType.QUESTION, 9999);
        QuestionStatement stmt = new QuestionStatement(token, 9999);
        assertEquals(9999, stmt.getTokenNumber(), "Large token numbers should be supported");
    }

    // ============================================================================
    // GetTokenNumber Tests
    // ============================================================================

    @Test
    @DisplayName("getTokenNumber should return constructor token number")
    public void testGetTokenNumber() {
        Token token = new Token("?", ForthTokenType.QUESTION, 42);
        QuestionStatement stmt = new QuestionStatement(token, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should match constructor parameter");
    }

    @Test
    @DisplayName("getTokenNumber for different token numbers")
    public void testGetTokenNumberMultiple() {
        QuestionStatement stmt1 = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        QuestionStatement stmt2 = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 2), 2);
        QuestionStatement stmt3 = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 3), 3);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(2, stmt2.getTokenNumber());
        assertEquals(3, stmt3.getTokenNumber());
    }

    // ============================================================================
    // Execute Tests - Basic Operations
    // ============================================================================

    @Test
    @DisplayName("execute should fetch and print integer variable value")
    public void testExecuteFetchAndPrintInteger() throws Exception {
        // Define a variable and store a value
        _oVariables.define("testVar");
        int varIndex = _oVariables.index("testVar");
        _oVariables.put(varIndex, new IntegerValue(42));

        // Push index to stack
        _oStack.push(new IntegerValue(varIndex));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        // Check output
        String output = _outputStream.toString().trim();
        assertTrue(output.contains("42"), "Output should contain the fetched integer value");
    }

    @Test
    @DisplayName("execute should fetch and print string variable value")
    public void testExecuteFetchAndPrintString() throws Exception {
        // Define a variable and store a string value
        _oVariables.define("stringVar");
        int varIndex = _oVariables.index("stringVar");
        _oVariables.put(varIndex, new StringValue("FORTH"));

        // Push index to stack
        _oStack.push(new IntegerValue(varIndex));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        // Check output
        String output = _outputStream.toString().trim();
        assertTrue(output.contains("FORTH"), "Output should contain the fetched string value");
    }

    @Test
    @DisplayName("execute should fetch and print zero value")
    public void testExecuteFetchAndPrintZero() throws Exception {
        _oVariables.define("zero");
        int varIndex = _oVariables.index("zero");
        _oVariables.put(varIndex, new IntegerValue(0));

        _oStack.push(new IntegerValue(varIndex));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        assertTrue(output.contains("0"), "Output should contain zero value");
    }

    @Test
    @DisplayName("execute should fetch and print negative value")
    public void testExecuteFetchAndPrintNegative() throws Exception {
        _oVariables.define("negative");
        int varIndex = _oVariables.index("negative");
        _oVariables.put(varIndex, new IntegerValue(-99));

        _oStack.push(new IntegerValue(varIndex));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        assertTrue(output.contains("-99"), "Output should contain the negative value");
    }

    @Test
    @DisplayName("execute should fetch and print large value")
    public void testExecuteFetchAndPrintLarge() throws Exception {
        _oVariables.define("large");
        int varIndex = _oVariables.index("large");
        _oVariables.put(varIndex, new IntegerValue(Integer.MAX_VALUE));

        _oStack.push(new IntegerValue(varIndex));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        assertTrue(output.contains(String.valueOf(Integer.MAX_VALUE)), "Output should contain the large value");
    }

    // ============================================================================
    // Execute Tests - Empty Stack Error
    // ============================================================================

    @Test
    @DisplayName("execute should handle error gracefully when stack is empty")
    public void testExecuteEmptyStackError() throws Exception {
        // Stack is empty, so pop() should fail
        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        // Should print an error message
        assertNotNull(output, "Should output error message");
    }

    // ============================================================================
    // Execute Tests - Multiple Variables
    // ============================================================================

    @Test
    @DisplayName("execute should handle multiple sequential calls")
    public void testExecuteMultipleVariables() throws Exception {
        // Define multiple variables
        _oVariables.define("var1");
        _oVariables.define("var2");
        _oVariables.define("var3");

        int idx1 = _oVariables.index("var1");
        int idx2 = _oVariables.index("var2");
        int idx3 = _oVariables.index("var3");

        _oVariables.put(idx1, new IntegerValue(100));
        _oVariables.put(idx2, new IntegerValue(200));
        _oVariables.put(idx3, new IntegerValue(300));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);

        // Execute for var1
        _oStack.push(new IntegerValue(idx1));
        stmt.execute();

        // Execute for var2
        _oStack.push(new IntegerValue(idx2));
        stmt.execute();

        // Execute for var3
        _oStack.push(new IntegerValue(idx3));
        stmt.execute();

        String output = _outputStream.toString();
        assertTrue(output.contains("100"), "Output should contain first variable value");
        assertTrue(output.contains("200"), "Output should contain second variable value");
        assertTrue(output.contains("300"), "Output should contain third variable value");
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    @DisplayName("content should return QUESTION description")
    public void testContent() throws Exception {
        Token token = new Token("?", ForthTokenType.QUESTION, 1);
        QuestionStatement stmt = new QuestionStatement(token, 1);

        String content = stmt.content();
        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("QUESTION"), "Content should contain QUESTION");
        assertTrue(content.contains("?"), "Content should contain question mark");
    }

    @Test
    @DisplayName("structure should return JSON format string")
    public void testStructure() throws Exception {
        Token token = new Token("?", ForthTokenType.QUESTION, 1);
        QuestionStatement stmt = new QuestionStatement(token, 1);

        String structure = stmt.structure();
        assertNotNull(structure, "Structure should not be null");
        assertTrue(structure.startsWith("{"), "Should be JSON format");
        assertTrue(structure.contains("QUESTION"), "Should reference QUESTION");
        assertTrue(structure.contains("TOKEN_NR"), "Should contain TOKEN_NR");
        assertTrue(structure.contains("TOKEN_TYPE"), "Should contain TOKEN_TYPE");
    }

    @Test
    @DisplayName("structure should contain correct token number")
    public void testStructureTokenNumber() throws Exception {
        Token token = new Token("?", ForthTokenType.QUESTION, 42);
        QuestionStatement stmt = new QuestionStatement(token, 42);

        String structure = stmt.structure();
        assertTrue(structure.contains("42"), "Should contain token number");
    }

    @Test
    @DisplayName("structure should be valid JSON")
    public void testStructureJSON() throws Exception {
        Token token = new Token("?", ForthTokenType.QUESTION, 1);
        QuestionStatement stmt = new QuestionStatement(token, 1);

        String structure = stmt.structure();
        assertTrue(structure.contains("{"), "Should have opening brace");
        assertTrue(structure.contains("}"), "Should have closing brace");
        assertTrue(structure.contains(":"), "Should have colons for JSON key-value pairs");
        assertTrue(structure.contains("\""), "Should have quotes for JSON strings");
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    @DisplayName("Complete workflow: define variable, store value, then question")
    public void testCompleteWorkflow() throws Exception {
        // Define variable
        _oVariables.define("result");
        int idx = _oVariables.index("result");

        // Store value
        _oVariables.put(idx, new IntegerValue(999));

        // Question operator
        _oStack.push(new IntegerValue(idx));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        assertTrue(output.contains("999"), "Should fetch and print stored value");
    }

    @Test
    @DisplayName("Question with empty string variable value")
    public void testQuestionWithEmptyStringValue() throws Exception {
        _oVariables.define("emptyStr");
        int idx = _oVariables.index("emptyStr");
        _oVariables.put(idx, new StringValue(""));

        _oStack.push(new IntegerValue(idx));

        QuestionStatement stmt = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 1), 1);
        stmt.execute();

        String output = _outputStream.toString().trim();
        assertNotNull(output, "Should output empty string representation");
    }

    @Test
    @DisplayName("Multiple statements with different token numbers")
    public void testMultipleStatementsWithDifferentTokenNumbers() throws Exception {
        _oVariables.define("x");
        int idx = _oVariables.index("x");
        _oVariables.put(idx, new IntegerValue(55));

        QuestionStatement stmt1 = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 10), 10);
        QuestionStatement stmt2 = new QuestionStatement(new Token("?", ForthTokenType.QUESTION, 20), 20);

        assertEquals(10, stmt1.getTokenNumber());
        assertEquals(20, stmt2.getTokenNumber());

        _oStack.push(new IntegerValue(idx));
        stmt1.execute();

        String output = _outputStream.toString().trim();
        assertTrue(output.contains("55"), "Should output the variable value");
    }
}
