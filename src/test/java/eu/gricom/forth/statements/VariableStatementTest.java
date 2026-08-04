package eu.gricom.forth.statements;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VariableStatementTest.java
 * <p>
 * Comprehensive unit test suite for VariableStatement class, testing both variable
 * definition (VARIABLE token) and variable access (WORD token) operations.
 */
@DisplayName("VariableStatement Test Suite")
public class VariableStatementTest {

    private Stack _oStack;
    private Variables _oVariables;

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
    }

    // ============================================================================
    // Constructor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor with 2 arguments for variable access")
    public void testConstructorTwoArguments() {
        Token token = new Token("myVar", ForthTokenType.WORD, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertNotNull(stmt, "VariableStatement should be created");
        assertEquals(1, stmt.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor with 3 arguments for variable definition")
    public void testConstructorThreeArguments() {
        Token token = new Token("myVar", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "myVar");
        assertNotNull(stmt, "VariableStatement should be created");
        assertEquals(1, stmt.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should handle zero token number")
    public void testConstructorWithZeroTokenNumber() {
        Token token = new Token("var", ForthTokenType.VARIABLE, 0);
        VariableStatement stmt = new VariableStatement(token, 0, "var");
        assertEquals(0, stmt.getTokenNumber(), "Token number zero should be valid");
    }

    @Test
    @DisplayName("Constructor should handle large token numbers")
    public void testConstructorWithLargeTokenNumber() {
        Token token = new Token("var", ForthTokenType.VARIABLE, 9999);
        VariableStatement stmt = new VariableStatement(token, 9999, "var");
        assertEquals(9999, stmt.getTokenNumber(), "Large token numbers should be supported");
    }

    // ============================================================================
    // Variable Definition Tests (VARIABLE Token)
    // ============================================================================

    @Test
    @DisplayName("VARIABLE token should define new variable")
    public void testVariableTokenDefinesNewVariable() throws Exception {
        Token token = new Token("myVar", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "myVar");

        stmt.execute();

        assertTrue(_oVariables.isVariable("myVar"), "Variable should be defined after VARIABLE token execution");
    }

    @Test
    @DisplayName("VARIABLE token should initialize with empty")
    public void testVariableTokenInitializesWithEmpty() throws Exception {
        Token token = new Token("testVar", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "testVar");

        stmt.execute();

        assertEquals("empty", _oVariables.get("testVar").toString(), "Variable should be initialized with 'empty'");
    }

    @Test
    @DisplayName("VARIABLE token should throw exception for duplicate variable")
    public void testVariableTokenThrowsExceptionForDuplicate() throws Exception {
        Token token1 = new Token("duplicate", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt1 = new VariableStatement(token1, 1, "duplicate");
        stmt1.execute();

        Token token2 = new Token("duplicate", ForthTokenType.VARIABLE, 2);
        VariableStatement stmt2 = new VariableStatement(token2, 2, "duplicate");

        assertThrows(RuntimeException.class, () -> {
            stmt2.execute();
        }, "Defining duplicate variable should throw exception");
    }

    @Test
    @DisplayName("VARIABLE token with special characters in name")
    public void testVariableTokenWithSpecialCharacterName() throws Exception {
        Token token = new Token("var_with_underscore", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var_with_underscore");

        stmt.execute();

        assertTrue(_oVariables.isVariable("var_with_underscore"), "Variable with underscore should be defined");
    }

    @Test
    @DisplayName("VARIABLE token with numeric name")
    public void testVariableTokenWithNumericName() throws Exception {
        Token token = new Token("var123", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var123");

        stmt.execute();

        assertTrue(_oVariables.isVariable("var123"), "Variable with numeric name should be defined");
    }

    @Test
    @DisplayName("Multiple VARIABLE tokens should define multiple variables")
    public void testMultipleVariableDefinitions() throws Exception {
        Token token1 = new Token("var1", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt1 = new VariableStatement(token1, 1, "var1");
        stmt1.execute();

        Token token2 = new Token("var2", ForthTokenType.VARIABLE, 2);
        VariableStatement stmt2 = new VariableStatement(token2, 2, "var2");
        stmt2.execute();

        assertTrue(_oVariables.isVariable("var1"), "First variable should be defined");
        assertTrue(_oVariables.isVariable("var2"), "Second variable should be defined");
        assertEquals(0, _oVariables.index("var1"), "First variable should have index 0");
        assertEquals(1, _oVariables.index("var2"), "Second variable should have index 1");
    }

    // ============================================================================
    // Variable Access Tests (WORD Token)
    // ============================================================================

    @Test
    @DisplayName("WORD token should push variable index to stack")
    public void testWordTokenPushesVariableIndex() throws Exception {
        // Define a variable first
        Token defToken = new Token("myVar", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "myVar");
        defStmt.execute();

        int expectedIndex = _oVariables.index("myVar");

        // Reset stack for access test
        _oStack.reset();

        // Now access the variable with WORD token
        Token token = new Token("myVar", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        stmt.execute();

        assertEquals(1, _oStack.size(), "Stack should have one item after WORD token");
        int pushedIndex = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(expectedIndex, pushedIndex, "WORD token should push variable index to stack");
    }

    @Test
    @DisplayName("WORD token with first variable should push index 0")
    public void testWordTokenWithFirstVariable() throws Exception {
        Token defToken = new Token("first", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "first");
        defStmt.execute();

        _oStack.reset();

        Token token = new Token("first", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index, "First variable should have index 0");
    }

    @Test
    @DisplayName("WORD token with second variable should push index 1")
    public void testWordTokenWithSecondVariable() throws Exception {
        Token defToken1 = new Token("first", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt1 = new VariableStatement(defToken1, 1, "first");
        defStmt1.execute();

        _oVariables.define("second");

        _oStack.reset();

        Token token = new Token("second", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(1, index, "Second variable should have index 1");
    }

    @Test
    @DisplayName("WORD token should throw exception for undefined variable")
    public void testWordTokenThrowsExceptionForUndefinedVariable() throws Exception {
        Token token = new Token("undefined", ForthTokenType.WORD, 1);
        VariableStatement stmt = new VariableStatement(token, 1);

        assertThrows(RuntimeException.class, () -> {
            stmt.execute();
        }, "WORD token for undefined variable should throw exception");
    }

    @Test
    @DisplayName("WORD token with multiple pushes should maintain stack order")
    public void testWordTokenPushesMultipleValues() throws Exception {
        Token defToken1 = new Token("alpha", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt1 = new VariableStatement(defToken1, 1, "alpha");
        defStmt1.execute();

        _oVariables.define("beta");

        _oStack.reset();

        Token token1 = new Token("alpha", ForthTokenType.WORD, 2);
        VariableStatement stmt1 = new VariableStatement(token1, 2);
        stmt1.execute();

        Token token2 = new Token("beta", ForthTokenType.WORD, 3);
        VariableStatement stmt2 = new VariableStatement(token2, 3);
        stmt2.execute();

        assertEquals(2, _oStack.size(), "Stack should have two items");
        int idx2 = ((IntegerValue) _oStack.pop()).toInteger();
        int idx1 = ((IntegerValue) _oStack.pop()).toInteger();

        assertEquals(1, idx2, "Second push should be index 1");
        assertEquals(0, idx1, "First push should be index 0");
    }

    @Test
    @DisplayName("WORD token with case-sensitive variable name")
    public void testWordTokenCaseSensitiveVariableAccess() throws Exception {
        Token defToken = new Token("MyVar", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "MyVar");
        defStmt.execute();

        _oStack.reset();

        Token token = new Token("MyVar", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        stmt.execute();

        assertEquals(1, _oStack.size(), "Should successfully access case-sensitive variable");
    }

    // ============================================================================
    // GetTokenNumber Tests
    // ============================================================================

    @Test
    @DisplayName("getTokenNumber should return constructor token number")
    public void testGetTokenNumber() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 42);
        VariableStatement stmt = new VariableStatement(token, 42, "var");

        assertEquals(42, stmt.getTokenNumber(), "Token number should match constructor parameter");
    }

    @Test
    @DisplayName("getTokenNumber for different token numbers")
    public void testGetTokenNumberMultiple() {
        VariableStatement stmt1 = new VariableStatement(new Token("var1", ForthTokenType.VARIABLE, 1), 1, "var1");
        VariableStatement stmt2 = new VariableStatement(new Token("var2", ForthTokenType.VARIABLE, 2), 2, "var2");
        VariableStatement stmt3 = new VariableStatement(new Token("var3", ForthTokenType.WORD, 3), 3);

        assertEquals(1, stmt1.getTokenNumber());
        assertEquals(2, stmt2.getTokenNumber());
        assertEquals(3, stmt3.getTokenNumber());
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    @DisplayName("content should return empty string")
    public void testContentReturnsEmptyString() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var");

        assertEquals("", stmt.content(), "Content should return empty string");
    }

    @Test
    @DisplayName("structure should return empty string")
    public void testStructureReturnsEmptyString() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var");

        assertEquals("", stmt.structure(), "Structure should return empty string");
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    @DisplayName("Variable definition followed by access")
    public void testVariableDefinitionFollowedByAccess() throws Exception {
        Token defineToken = new Token("counter", ForthTokenType.VARIABLE, 1);
        VariableStatement defineStmt = new VariableStatement(defineToken, 1, "counter");
        defineStmt.execute();

        _oStack.reset();

        Token accessToken = new Token("counter", ForthTokenType.WORD, 2);
        VariableStatement accessStmt = new VariableStatement(accessToken, 2);
        accessStmt.execute();

        assertEquals(1, _oStack.size(), "Stack should contain variable index");
        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index, "Variable index should be 0");
    }

    @Test
    @DisplayName("Multiple variable definitions and access")
    public void testMultipleVariableDefinitionsAndAccess() throws Exception {
        Token defToken1 = new Token("alpha", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt1 = new VariableStatement(defToken1, 1, "alpha");
        defStmt1.execute();

        _oVariables.define("beta");
        _oVariables.define("gamma");

        _oStack.reset();

        Token token1 = new Token("alpha", ForthTokenType.WORD, 2);
        VariableStatement stmt1 = new VariableStatement(token1, 2);
        stmt1.execute();

        Token token2 = new Token("gamma", ForthTokenType.WORD, 3);
        VariableStatement stmt2 = new VariableStatement(token2, 3);
        stmt2.execute();

        assertEquals(2, _oStack.size(), "Stack should have two indices");
        int idx2 = ((IntegerValue) _oStack.pop()).toInteger();
        int idx1 = ((IntegerValue) _oStack.pop()).toInteger();

        assertEquals(2, idx2, "Gamma should have index 2");
        assertEquals(0, idx1, "Alpha should have index 0");
    }

    @Test
    @DisplayName("Variable and WORD token sequence")
    public void testVariableAndWordTokenSequence() throws Exception {
        Token varToken1 = new Token("x", ForthTokenType.VARIABLE, 1);
        VariableStatement varStmt1 = new VariableStatement(varToken1, 1, "x");
        varStmt1.execute();

        _oVariables.define("y");

        _oStack.reset();

        Token wordToken = new Token("x", ForthTokenType.WORD, 2);
        VariableStatement wordStmt = new VariableStatement(wordToken, 2);
        wordStmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index, "Should retrieve index of first defined variable");
    }

    @Test
    @DisplayName("Full workflow: define, access multiple variables")
    public void testFullWorkflow() throws Exception {
        // Define three variables
        VariableStatement stmt1 = new VariableStatement(new Token("x", ForthTokenType.VARIABLE, 1), 1, "x");
        stmt1.execute();

        VariableStatement stmt2 = new VariableStatement(new Token("y", ForthTokenType.VARIABLE, 2), 2, "y");
        stmt2.execute();

        VariableStatement stmt3 = new VariableStatement(new Token("z", ForthTokenType.VARIABLE, 3), 3, "z");
        stmt3.execute();

        // Access all three variables
        _oStack.reset();

        VariableStatement access1 = new VariableStatement(new Token("x", ForthTokenType.WORD, 4), 4);
        access1.execute();

        VariableStatement access2 = new VariableStatement(new Token("y", ForthTokenType.WORD, 5), 5);
        access2.execute();

        VariableStatement access3 = new VariableStatement(new Token("z", ForthTokenType.WORD, 6), 6);
        access3.execute();

        // Verify stack contains indices in correct order
        assertEquals(3, _oStack.size());
        assertEquals(2, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("Variable index correctness for use with fetch/store")
    public void testVariableIndexCorrectness() throws Exception {
        Token defToken = new Token("data", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "data");
        defStmt.execute();

        // Get the index through WORD token
        _oStack.reset();

        Token accessToken = new Token("data", ForthTokenType.WORD, 2);
        VariableStatement accessStmt = new VariableStatement(accessToken, 2);
        accessStmt.execute();

        int indexFromWord = ((IntegerValue) _oStack.pop()).toInteger();
        int indexFromVariables = _oVariables.index("data");

        assertEquals(indexFromVariables, indexFromWord, "Index from WORD token should match Variables.index()");
    }
}
