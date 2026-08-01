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
 * StoreStatementTest.java
 * <p>
 * Comprehensive unit test suite for StoreStatement class, testing all store operations
 * and edge cases related to variable storage in memory.
 */
@DisplayName("StoreStatement Test Suite")
public class StoreStatementTest {

    private StoreStatement _oStatement;
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
    @DisplayName("Constructor should initialize with STORE token type")
    public void testConstructorWithStore() {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        assertNotNull(_oStatement, "StoreStatement should be created");
        assertEquals(1, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should initialize with TWO_STORE token type")
    public void testConstructorWithTwoStore() {
        _oStatement = new StoreStatement(ForthTokenType.TWO_STORE, 5);
        assertNotNull(_oStatement, "StoreStatement should be created");
        assertEquals(5, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should initialize with CHAR_STORE token type")
    public void testConstructorWithCharStore() {
        _oStatement = new StoreStatement(ForthTokenType.CHAR_STORE, 10);
        assertNotNull(_oStatement, "StoreStatement should be created");
        assertEquals(10, _oStatement.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should handle zero token number")
    public void testConstructorWithZeroTokenNumber() {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 0);
        assertEquals(0, _oStatement.getTokenNumber(), "Token number zero should be valid");
    }

    @Test
    @DisplayName("Constructor should handle large token numbers")
    public void testConstructorWithLargeTokenNumber() {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 9999);
        assertEquals(9999, _oStatement.getTokenNumber(), "Large token numbers should be supported");
    }

    // ============================================================================
    // GetTokenNumber Tests
    // ============================================================================

    @Test
    @DisplayName("getTokenNumber should return constructor token number")
    public void testGetTokenNumber() {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 42);
        assertEquals(42, _oStatement.getTokenNumber(), "Should return token number from constructor");
    }

    @Test
    @DisplayName("getTokenNumber should return correct value for different token numbers")
    public void testGetTokenNumberMultiple() {
        StoreStatement stmt1 = new StoreStatement(ForthTokenType.STORE, 1);
        StoreStatement stmt2 = new StoreStatement(ForthTokenType.TWO_STORE, 2);
        StoreStatement stmt3 = new StoreStatement(ForthTokenType.CHAR_STORE, 3);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(2, stmt2.getTokenNumber());
        assertEquals(3, stmt3.getTokenNumber());
    }

    // ============================================================================
    // Content Tests
    // ============================================================================

    @Test
    @DisplayName("content should return STORE operation description")
    public void testContentStore() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("STORE"), "Content should contain STORE");
        assertTrue(content.contains("STORE"), "Content should reference token type");
    }

    @Test
    @DisplayName("content should return TWO_STORE operation description")
    public void testContentTwoStore() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.TWO_STORE, 5);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("STORE"), "Content should contain STORE");
        assertTrue(content.contains("TWO_STORE"), "Content should reference TWO_STORE token type");
    }

    @Test
    @DisplayName("content should return CHAR_STORE operation description")
    public void testContentCharStore() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.CHAR_STORE, 10);
        String content = _oStatement.content();

        assertNotNull(content, "Content should not be null");
        assertTrue(content.contains("STORE"), "Content should contain STORE");
        assertTrue(content.contains("CHAR_STORE"), "Content should reference CHAR_STORE token type");
    }

    @Test
    @DisplayName("content should have consistent format")
    public void testContentFormat() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        String content = _oStatement.content();

        // Should follow pattern: "STORE (TOKEN_TYPE) "
        assertTrue(content.startsWith("STORE"), "Should start with STORE");
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
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        String structure = _oStatement.structure();

        assertNotNull(structure, "Structure should not be null");
        assertTrue(structure.startsWith("{"), "Should be JSON format");
        assertTrue(structure.contains("STORE"), "Should reference STORE");
        assertTrue(structure.contains("TOKEN_NR"), "Should contain TOKEN_NR");
        assertTrue(structure.contains("TOKEN_TYPE"), "Should contain TOKEN_TYPE");
    }

    @Test
    @DisplayName("structure should contain token number")
    public void testStructureTokenNumber() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 42);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("42"), "Should contain token number");
    }

    @Test
    @DisplayName("structure should contain token type for STORE")
    public void testStructureTokenTypeStore() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("STORE"), "Should contain STORE token type");
    }

    @Test
    @DisplayName("structure should contain token type for TWO_STORE")
    public void testStructureTokenTypeTwoStore() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.TWO_STORE, 1);
        String structure = _oStatement.structure();

        assertTrue(structure.contains("TWO_STORE"), "Should contain TWO_STORE token type");
    }

    @Test
    @DisplayName("structure should be valid JSON")
    public void testStructureJSON() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        String structure = _oStatement.structure();

        // Basic JSON validation
        assertTrue(structure.contains("{"), "Should have opening brace");
        assertTrue(structure.contains("}"), "Should have closing brace");
        assertTrue(structure.contains(":"), "Should have colons for JSON key-value pairs");
        assertTrue(structure.contains("\""), "Should have quotes for JSON strings");
    }

    // ============================================================================
    // Execute Tests - Basic Operations
    // ============================================================================

    @Test
    @DisplayName("execute should store integer value to variable")
    public void testExecuteStoreInteger() throws Exception {
        // Setup: Define a variable and push value and index to stack
        _oVariables.define("testVar");
        int varIndex = _oVariables.index("testVar");

        _oStack.push(new IntegerValue(42));
        _oStack.push(new IntegerValue(varIndex));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        // Verify the value was stored
        Value stored = _oVariables.get("testVar");
        assertEquals(42, ((IntegerValue) stored).toInteger(), "Should store integer value");
    }

    @Test
    @DisplayName("execute should store string value to variable")
    public void testExecuteStoreString() throws Exception {
        _oVariables.define("stringVar");
        int varIndex = _oVariables.index("stringVar");

        _oStack.push(new StringValue("FORTH"));
        _oStack.push(new IntegerValue(varIndex));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        Value stored = _oVariables.get("stringVar");
        assertEquals("FORTH", stored.toString(), "Should store string value");
    }

    @Test
    @DisplayName("execute should overwrite existing variable value")
    public void testExecuteOverwrite() throws Exception {
        _oVariables.define("counter");
        int varIndex = _oVariables.index("counter");

        // Store first value
        _oStack.push(new IntegerValue(10));
        _oStack.push(new IntegerValue(varIndex));
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        // Verify first value
        assertEquals(10, ((IntegerValue) _oVariables.get("counter")).toInteger());

        // Store second value (overwrite)
        _oStack.reset();
        _oStack.push(new IntegerValue(20));
        _oStack.push(new IntegerValue(varIndex));
        _oStatement.execute();

        // Verify second value overwrote the first
        assertEquals(20, ((IntegerValue) _oVariables.get("counter")).toInteger(),
                   "Should overwrite previous value");
    }

    // ============================================================================
    // Execute Tests - Multiple Variables
    // ============================================================================

    @Test
    @DisplayName("execute should handle multiple variable stores")
    public void testExecuteMultipleVariables() throws Exception {
        _oVariables.define("var1");
        _oVariables.define("var2");
        _oVariables.define("var3");

        int idx1 = _oVariables.index("var1");
        int idx2 = _oVariables.index("var2");
        int idx3 = _oVariables.index("var3");

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);

        // Store to var1
        _oStack.push(new IntegerValue(100));
        _oStack.push(new IntegerValue(idx1));
        _oStatement.execute();

        // Store to var2
        _oStack.push(new IntegerValue(200));
        _oStack.push(new IntegerValue(idx2));
        _oStatement.execute();

        // Store to var3
        _oStack.push(new IntegerValue(300));
        _oStack.push(new IntegerValue(idx3));
        _oStatement.execute();

        // Verify all values
        assertEquals(100, ((IntegerValue) _oVariables.get("var1")).toInteger());
        assertEquals(200, ((IntegerValue) _oVariables.get("var2")).toInteger());
        assertEquals(300, ((IntegerValue) _oVariables.get("var3")).toInteger());
    }

    // ============================================================================
    // Execute Tests - Token Types
    // ============================================================================

    @Test
    @DisplayName("execute should work with STORE token type")
    public void testExecuteWithStoreToken() throws Exception {
        _oVariables.define("x");
        int idx = _oVariables.index("x");

        _oStack.push(new IntegerValue(77));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        assertEquals(77, ((IntegerValue) _oVariables.get("x")).toInteger());
    }

    @Test
    @DisplayName("execute should work with TWO_STORE token type")
    public void testExecuteWithTwoStoreToken() throws Exception {
        _oVariables.define("pair");
        int idx = _oVariables.index("pair");

        _oStack.push(new IntegerValue(99));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.TWO_STORE, 2);
        _oStatement.execute();

        assertEquals(99, ((IntegerValue) _oVariables.get("pair")).toInteger());
    }

    @Test
    @DisplayName("execute should work with CHAR_STORE token type")
    public void testExecuteWithCharStoreToken() throws Exception {
        _oVariables.define("char");
        int idx = _oVariables.index("char");

        _oStack.push(new IntegerValue(65));  // ASCII 'A'
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.CHAR_STORE, 3);
        _oStatement.execute();

        assertEquals(65, ((IntegerValue) _oVariables.get("char")).toInteger());
    }

    // ============================================================================
    // Execute Tests - Edge Cases
    // ============================================================================

    @Test
    @DisplayName("execute should handle zero values")
    public void testExecuteZeroValue() throws Exception {
        _oVariables.define("zero");
        int idx = _oVariables.index("zero");

        _oStack.push(new IntegerValue(0));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        assertEquals(0, ((IntegerValue) _oVariables.get("zero")).toInteger());
    }

    @Test
    @DisplayName("execute should handle negative values")
    public void testExecuteNegativeValue() throws Exception {
        _oVariables.define("negative");
        int idx = _oVariables.index("negative");

        _oStack.push(new IntegerValue(-42));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        assertEquals(-42, ((IntegerValue) _oVariables.get("negative")).toInteger());
    }

    @Test
    @DisplayName("execute should handle large positive values")
    public void testExecuteLargeValue() throws Exception {
        _oVariables.define("large");
        int idx = _oVariables.index("large");

        _oStack.push(new IntegerValue(Integer.MAX_VALUE));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        assertEquals(Integer.MAX_VALUE, ((IntegerValue) _oVariables.get("large")).toInteger());
    }

    @Test
    @DisplayName("execute should handle empty string values")
    public void testExecuteEmptyString() throws Exception {
        _oVariables.define("emptyStr");
        int idx = _oVariables.index("emptyStr");

        _oStack.push(new StringValue(""));
        _oStack.push(new IntegerValue(idx));

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);
        _oStatement.execute();

        assertEquals("", _oVariables.get("emptyStr").toString());
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

        StoreStatement stmt1 = new StoreStatement(ForthTokenType.STORE, 1);
        StoreStatement stmt2 = new StoreStatement(ForthTokenType.STORE, 2);

        // Execute stmt1
        _oStack.push(new IntegerValue(111));
        _oStack.push(new IntegerValue(idxA));
        stmt1.execute();

        // Execute stmt2
        _oStack.push(new IntegerValue(222));
        _oStack.push(new IntegerValue(idxB));
        stmt2.execute();

        assertEquals(111, ((IntegerValue) _oVariables.get("a")).toInteger());
        assertEquals(222, ((IntegerValue) _oVariables.get("b")).toInteger());
    }

    @Test
    @DisplayName("execute should work after multiple content/structure calls")
    public void testExecuteAfterInspection() throws Exception {
        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);

        // Call inspection methods
        _oStatement.content();
        _oStatement.structure();
        _oStatement.content();
        _oStatement.structure();

        // Execute should still work
        _oVariables.define("test");
        int idx = _oVariables.index("test");

        _oStack.push(new IntegerValue(55));
        _oStack.push(new IntegerValue(idx));

        assertDoesNotThrow(() -> _oStatement.execute(), "Execute should work after inspection methods");
        assertEquals(55, ((IntegerValue) _oVariables.get("test")).toInteger());
    }

    @Test
    @DisplayName("statement sequence: define, store, retrieve")
    public void testFullWorkflow() throws Exception {
        // Define variables
        _oVariables.define("x");
        _oVariables.define("y");

        int idxX = _oVariables.index("x");
        int idxY = _oVariables.index("y");

        _oStatement = new StoreStatement(ForthTokenType.STORE, 1);

        // Store value 10 to x
        _oStack.push(new IntegerValue(10));
        _oStack.push(new IntegerValue(idxX));
        _oStatement.execute();

        // Store value 20 to y
        _oStack.push(new IntegerValue(20));
        _oStack.push(new IntegerValue(idxY));
        _oStatement.execute();

        // Verify stored values
        assertEquals(10, ((IntegerValue) _oVariables.get("x")).toInteger());
        assertEquals(20, ((IntegerValue) _oVariables.get("y")).toInteger());
    }
}