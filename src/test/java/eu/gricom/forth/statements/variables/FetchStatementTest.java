package eu.gricom.forth.statements.variables;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.ForthTokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import eu.gricom.forth.variableTypes.Value;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FetchStatementTest.java
 * <p>
 * Comprehensive unit test suite for FetchStatement class, testing all fetch operations
 * and variable retrieval from memory.
 */
@DisplayName("FetchStatement Test Suite")
public class FetchStatementTest {

    private FetchStatement _oStatement;
    private Stack _oStack;
    private Variables _oVariables;

    @BeforeEach
    public void setUp() throws Exception {
        // Reset stack
        _oStack = new Stack();
        _oStack.reset();

        // Clear variables
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> map = (Map<Integer, Object>) varField.get(null);
        map.clear();

        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) namesField.get(null);
        if (names != null) {
            names.clear();
        }

        _oVariables = new Variables();
    }

    // ============================================================================
    // Constructor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with FETCH token type")
    public void testConstructorWithFetch() {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        assertNotNull(_oStatement, "FetchStatement should be created");
        assertEquals(1, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should initialize with TWO_FETCH token type")
    public void testConstructorWithTwoFetch() {
        _oStatement = new FetchStatement(ForthTokenType.TWO_FETCH, 5);
        assertNotNull(_oStatement, "FetchStatement should be created");
        assertEquals(5, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should initialize with CHAR_FETCH token type")
    public void testConstructorWithCharFetch() {
        _oStatement = new FetchStatement(ForthTokenType.CHAR_FETCH, 10);
        assertNotNull(_oStatement, "FetchStatement should be created");
        assertEquals(10, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should handle zero token number")
    public void testConstructorWithZeroTokenNumber() {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 0);
        assertEquals(0, _oStatement.getTokenNumber(), "Token number zero should be valid");
    }

    @Test
    @DisplayName("Constructor should handle large token numbers")
    public void testConstructorWithLargeTokenNumber() {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 9999);
        assertEquals(9999, _oStatement.getTokenNumber(), "Large token numbers should be supported");
    }

    // ============================================================================
    // GetTokenNumber Tests
    // ============================================================================

    @Test
    @DisplayName("getTokenNumber should return constructor token number")
    public void testGetTokenNumber() {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 42);
        assertEquals(42, _oStatement.getTokenNumber(), "Should return token number from constructor");
    }

    @Test
    @DisplayName("getTokenNumber should return correct value for different token numbers")
    public void testGetTokenNumberMultiple() {
        FetchStatement stmt1 = new FetchStatement(ForthTokenType.FETCH, 1);
        FetchStatement stmt2 = new FetchStatement(ForthTokenType.TWO_FETCH, 2);
        FetchStatement stmt3 = new FetchStatement(ForthTokenType.CHAR_FETCH, 3);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(2, stmt2.getTokenNumber());
        assertEquals(3, stmt3.getTokenNumber());
    }

    // ============================================================================
    // Content Tests
    // ============================================================================

    @Test
    @DisplayName("content should return FETCH operation description")
    public void testContentFetch() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("FETCH"), "Content should contain FETCH");
    }

    @Test
    @DisplayName("content should return TWO_FETCH operation description")
    public void testContentTwoFetch() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.TWO_FETCH, 5);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("FETCH"), "Content should contain FETCH");
        assertTrue(content.contains("TWO_FETCH"), "Content should reference TWO_FETCH token type");
    }

    @Test
    @DisplayName("content should return CHAR_FETCH operation description")
    public void testContentCharFetch() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.CHAR_FETCH, 10);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("FETCH"), "Content should contain FETCH");
        assertTrue(content.contains("CHAR_FETCH"), "Content should reference CHAR_FETCH token type");
    }

    @Test
    @DisplayName("content should have consistent format")
    public void testContentFormat() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        String content = _oStatement.content();

        assertTrue(content.startsWith("FETCH"), "Should start with FETCH");
        assertTrue(content.contains("("), "Should contain opening parenthesis");
        assertTrue(content.contains(")"), "Should contain closing parenthesis");
        assertTrue(content.endsWith(" "), "Should end with space");
    }

    // ============================================================================
    // Structure Tests
    // ============================================================================

    @Test
    @DisplayName("structure should return JSON format string")
    public void testStructureFormat() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        String structure = _oStatement.structure();

        assertNotNull(structure, "Structure should not be null");
        assertTrue(structure.startsWith("{"), "Should be JSON format");
        assertTrue(structure.contains("FETCH"), "Should reference FETCH");
        assertTrue(structure.contains("TOKEN_NR"), "Should contain TOKEN_NR");
        assertTrue(structure.contains("TOKEN_TYPE"), "Should contain TOKEN_TYPE");
    }

    @Test
    @DisplayName("structure should contain token number")
    public void testStructureTokenNumber() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 42);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("42"), "Should contain token number");
    }

    @Test
    @DisplayName("structure should contain token type for FETCH")
    public void testStructureTokenTypeFetch() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("FETCH"), "Should contain FETCH token type");
    }

    @Test
    @DisplayName("structure should contain token type for TWO_FETCH")
    public void testStructureTokenTypeTwoFetch() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.TWO_FETCH, 1);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("TWO_FETCH"), "Should contain TWO_FETCH token type");
    }

    @Test
    @DisplayName("structure should be valid JSON")
    public void testStructureJSON() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("{"), "Should have opening brace");
        assertTrue(structure.contains("}"), "Should have closing brace");
        assertTrue(structure.contains(":"), "Should have colons for JSON key-value pairs");
        assertTrue(structure.contains("\""), "Should have quotes for JSON strings");
    }

    // ============================================================================
    // Execute Tests - Basic Operations
    // ============================================================================

    @Test
    @DisplayName("execute should fetch integer value from variable")
    public void testExecuteFetchInteger() throws Exception {
        // Setup: Define a variable and store a value
        _oVariables.define("testVar");
        int varIndex = _oVariables.index("testVar");
        _oVariables.put(varIndex, new IntegerValue(42));

        // Push index to stack
        _oStack.push(new IntegerValue(varIndex));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        // Verify the value was fetched and pushed to stack
        assertEquals(1, _oStack.size(), "Stack should have one item");
        IntegerValue result = (IntegerValue) _oStack.pop();
        assertEquals(42, result.toInteger(), "Should fetch correct integer value");
    }

    @Test
    @DisplayName("execute should fetch string value from variable")
    public void testExecuteFetchString() throws Exception {
        _oVariables.define("stringVar");
        int varIndex = _oVariables.index("stringVar");
        _oVariables.put(varIndex, new StringValue("FORTH"));

        _oStack.push(new IntegerValue(varIndex));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        Value result = _oStack.pop();
        assertEquals("FORTH", result.toString(), "Should fetch correct string value");
    }

    // ============================================================================
    // Execute Tests - Multiple Variables
    // ============================================================================

    @Test
    @DisplayName("execute should handle multiple variable fetches")
    public void testExecuteMultipleFetches() throws Exception {
        _oVariables.define("var1");
        _oVariables.define("var2");
        _oVariables.define("var3");

        int idx1 = _oVariables.index("var1");
        int idx2 = _oVariables.index("var2");
        int idx3 = _oVariables.index("var3");

        _oVariables.put(idx1, new IntegerValue(100));
        _oVariables.put(idx2, new IntegerValue(200));
        _oVariables.put(idx3, new IntegerValue(300));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);

        // Fetch var1
        _oStack.push(new IntegerValue(idx1));
        _oStatement.execute();

        // Fetch var2
        _oStack.push(new IntegerValue(idx2));
        _oStatement.execute();

        // Fetch var3
        _oStack.push(new IntegerValue(idx3));
        _oStatement.execute();

        // Verify all values in correct order
        assertEquals(300, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(200, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(100, ((IntegerValue) _oStack.pop()).toInteger());
    }

    // ============================================================================
    // Execute Tests - Token Types
    // ============================================================================

    @Test
    @DisplayName("execute should work with FETCH token type")
    public void testExecuteWithFetchToken() throws Exception {
        _oVariables.define("x");
        int idx = _oVariables.index("x");
        _oVariables.put(idx, new IntegerValue(77));

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        assertEquals(77, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should work with TWO_FETCH token type")
    public void testExecuteWithTwoFetchToken() throws Exception {
        _oVariables.define("pair");
        int idx = _oVariables.index("pair");
        _oVariables.put(idx, new IntegerValue(99));

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.TWO_FETCH, 2);
        _oStatement.execute();

        assertEquals(99, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should work with CHAR_FETCH token type")
    public void testExecuteWithCharFetchToken() throws Exception {
        _oVariables.define("char");
        int idx = _oVariables.index("char");
        _oVariables.put(idx, new IntegerValue(65));  // ASCII 'A'

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.CHAR_FETCH, 3);
        _oStatement.execute();

        assertEquals(65, ((IntegerValue) _oStack.pop()).toInteger());
    }

    // ============================================================================
    // Execute Tests - Edge Cases
    // ============================================================================

    @Test
    @DisplayName("execute should handle zero values")
    public void testExecuteZeroValue() throws Exception {
        _oVariables.define("zero");
        int idx = _oVariables.index("zero");
        _oVariables.put(idx, new IntegerValue(0));

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle negative values")
    public void testExecuteNegativeValue() throws Exception {
        _oVariables.define("negative");
        int idx = _oVariables.index("negative");
        _oVariables.put(idx, new IntegerValue(-42));

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        assertEquals(-42, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should handle large positive values")
    public void testExecuteLargeValue() throws Exception {
        _oVariables.define("large");
        int idx = _oVariables.index("large");
        _oVariables.put(idx, new IntegerValue(Integer.MAX_VALUE));

        _oStack.push(new IntegerValue(idx));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);
        _oStatement.execute();

        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oStack.pop()).toInteger());
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    @DisplayName("execute with different statements should maintain independence")
    public void testMultipleStatements() throws Exception {
        _oVariables.define("a");
        _oVariables.define("b");

        int idxA = _oVariables.index("a");
        int idxB = _oVariables.index("b");

        _oVariables.put(idxA, new IntegerValue(111));
        _oVariables.put(idxB, new IntegerValue(222));

        FetchStatement stmt1 = new FetchStatement(ForthTokenType.FETCH, 1);
        FetchStatement stmt2 = new FetchStatement(ForthTokenType.FETCH, 2);

        // Fetch a
        _oStack.push(new IntegerValue(idxA));
        stmt1.execute();

        // Fetch b
        _oStack.push(new IntegerValue(idxB));
        stmt2.execute();

        assertEquals(222, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(111, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("execute should work after multiple content/structure calls")
    public void testExecuteAfterInspection() throws Exception {
        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);

        // Call inspection methods
        _oStatement.content();
        _oStatement.structure();
        _oStatement.content();
        _oStatement.structure();

        // Execute should still work
        _oVariables.define("test");
        int idx = _oVariables.index("test");
        _oVariables.put(idx, new IntegerValue(55));

        _oStack.push(new IntegerValue(idx));

        assertDoesNotThrow(() -> _oStatement.execute(), "Execute should work after inspection methods");
        assertEquals(55, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("statement sequence: define, store, fetch")
    public void testFullWorkflow() throws Exception {
        // Define variables
        _oVariables.define("x");
        _oVariables.define("y");

        int idxX = _oVariables.index("x");
        int idxY = _oVariables.index("y");

        // Store values using StoreStatement-like behavior
        _oVariables.put(idxX, new IntegerValue(10));
        _oVariables.put(idxY, new IntegerValue(20));

        _oStatement = new FetchStatement(ForthTokenType.FETCH, 1);

        // Fetch value from x
        _oStack.push(new IntegerValue(idxX));
        _oStatement.execute();

        // Fetch value from y
        _oStack.push(new IntegerValue(idxY));
        _oStatement.execute();

        // Verify fetched values
        assertEquals(20, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) _oStack.pop()).toInteger());
    }
}
