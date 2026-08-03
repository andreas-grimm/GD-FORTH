package eu.gricom.forth.statements;

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
 * WordStatementTest.java
 * <p>
 * Comprehensive unit test suite for WordStatement class, testing variable access
 * (WORD token) operations that push variable indices to the stack.
 */
@DisplayName("WordStatement Test Suite")
public class WordStatementTest {

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
    @DisplayName("Constructor should initialize with token and token number")
    public void testConstructor() {
        Token token = new Token("myVar", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        assertNotNull(stmt, "WordStatement should be created");
        assertEquals(1, stmt.getTokenNumber(), "Token number should match");
    }

    @Test
    @DisplayName("Constructor should handle zero token number")
    public void testConstructorWithZeroTokenNumber() {
        Token token = new Token("var", ForthTokenType.WORD, 0);
        WordStatement stmt = new WordStatement(token, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number zero should be valid");
    }

    @Test
    @DisplayName("Constructor should handle large token numbers")
    public void testConstructorWithLargeTokenNumber() {
        Token token = new Token("var", ForthTokenType.WORD, 9999);
        WordStatement stmt = new WordStatement(token, 9999);
        assertEquals(9999, stmt.getTokenNumber(), "Large token numbers should be supported");
    }

    // ============================================================================
    // Variable Access Tests (WORD Token)
    // ============================================================================

    @Test
    @DisplayName("Should push variable index to stack")
    public void testWordTokenPushesVariableIndex() throws Exception {
        // Define a variable first
        _oVariables.define("myVar");
        int expectedIndex = _oVariables.index("myVar");

        // Access the variable with WORD token
        Token token = new Token("myVar", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        assertEquals(1, _oStack.size(), "Stack should have one item after WORD token");
        int pushedIndex = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(expectedIndex, pushedIndex, "WORD token should push variable index to stack");
    }

    @Test
    @DisplayName("Should push correct index for first variable")
    public void testWordTokenWithFirstVariable() throws Exception {
        _oVariables.define("first");

        Token token = new Token("first", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index, "First variable should have index 0");
    }

    @Test
    @DisplayName("Should push correct index for second variable")
    public void testWordTokenWithSecondVariable() throws Exception {
        _oVariables.define("first");
        _oVariables.define("second");

        Token token = new Token("second", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(1, index, "Second variable should have index 1");
    }

    @Test
    @DisplayName("Should push correct index for third variable")
    public void testWordTokenWithThirdVariable() throws Exception {
        _oVariables.define("var1");
        _oVariables.define("var2");
        _oVariables.define("var3");

        Token token = new Token("var3", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(2, index, "Third variable should have index 2");
    }

    @Test
    @DisplayName("Should throw exception for undefined variable")
    public void testWordTokenThrowsExceptionForUndefinedVariable() throws Exception {
        Token token = new Token("undefined", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);

        assertThrows(RuntimeException.class, () -> {
            stmt.execute();
        }, "WORD token for undefined variable should throw exception");
    }

    @Test
    @DisplayName("Should handle case-sensitive variable names")
    public void testWordTokenCaseSensitiveVariableAccess() throws Exception {
        _oVariables.define("MyVar");
        _oVariables.define("myvar");

        // Access uppercase version
        Token token1 = new Token("MyVar", ForthTokenType.WORD, 1);
        WordStatement stmt1 = new WordStatement(token1, 1);
        stmt1.execute();

        int index1 = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index1, "Should access correct variable with case-sensitive name");

        // Access lowercase version
        Token token2 = new Token("myvar", ForthTokenType.WORD, 2);
        WordStatement stmt2 = new WordStatement(token2, 2);
        stmt2.execute();

        int index2 = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(1, index2, "Should access different variable with different case");
    }

    @Test
    @DisplayName("Should handle variable names with special characters")
    public void testWordTokenWithSpecialCharacterNames() throws Exception {
        _oVariables.define("var_with_underscore");
        _oVariables.define("var-with-dash");

        Token token = new Token("var_with_underscore", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        assertEquals(1, _oStack.size(), "Stack should contain one item");
    }

    @Test
    @DisplayName("Should handle numeric variable names")
    public void testWordTokenWithNumericVariableName() throws Exception {
        _oVariables.define("var123");

        Token token = new Token("var123", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);
        stmt.execute();

        int index = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, index, "Should access variable with numeric name");
    }

    // ============================================================================
    // Multiple Access Tests
    // ============================================================================

    @Test
    @DisplayName("Should push correct indices for multiple accesses")
    public void testWordTokenMultiplePushes() throws Exception {
        _oVariables.define("alpha");
        _oVariables.define("beta");

        Token token1 = new Token("alpha", ForthTokenType.WORD, 1);
        WordStatement stmt1 = new WordStatement(token1, 1);
        stmt1.execute();

        Token token2 = new Token("beta", ForthTokenType.WORD, 2);
        WordStatement stmt2 = new WordStatement(token2, 2);
        stmt2.execute();

        assertEquals(2, _oStack.size(), "Stack should have two items");
        int idx2 = ((IntegerValue) _oStack.pop()).toInteger();
        int idx1 = ((IntegerValue) _oStack.pop()).toInteger();

        assertEquals(1, idx2, "Second push should be index 1");
        assertEquals(0, idx1, "First push should be index 0");
    }

    @Test
    @DisplayName("Should maintain stack order with multiple accesses")
    public void testWordTokenMultipleAccessesStackOrder() throws Exception {
        _oVariables.define("x");
        _oVariables.define("y");
        _oVariables.define("z");

        WordStatement stmt1 = new WordStatement(new Token("x", ForthTokenType.WORD, 1), 1);
        stmt1.execute();

        WordStatement stmt2 = new WordStatement(new Token("y", ForthTokenType.WORD, 2), 2);
        stmt2.execute();

        WordStatement stmt3 = new WordStatement(new Token("z", ForthTokenType.WORD, 3), 3);
        stmt3.execute();

        assertEquals(3, _oStack.size());
        assertEquals(2, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("Should allow repeated access to same variable")
    public void testWordTokenRepeatedAccess() throws Exception {
        _oVariables.define("counter");
        int expectedIndex = _oVariables.index("counter");

        Token token = new Token("counter", ForthTokenType.WORD, 1);
        WordStatement stmt1 = new WordStatement(token, 1);
        WordStatement stmt2 = new WordStatement(token, 2);
        WordStatement stmt3 = new WordStatement(token, 3);

        stmt1.execute();
        stmt2.execute();
        stmt3.execute();

        assertEquals(3, _oStack.size(), "Stack should have three items");
        assertEquals(expectedIndex, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(expectedIndex, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(expectedIndex, ((IntegerValue) _oStack.pop()).toInteger());
    }

    // ============================================================================
    // GetTokenNumber Tests
    // ============================================================================

    @Test
    @DisplayName("getTokenNumber should return constructor token number")
    public void testGetTokenNumber() throws Exception {
        Token token = new Token("var", ForthTokenType.WORD, 42);
        WordStatement stmt = new WordStatement(token, 42);

        assertEquals(42, stmt.getTokenNumber(), "Token number should match constructor parameter");
    }

    @Test
    @DisplayName("getTokenNumber for different token numbers")
    public void testGetTokenNumberMultiple() {
        WordStatement stmt1 = new WordStatement(new Token("var1", ForthTokenType.WORD, 1), 1);
        WordStatement stmt2 = new WordStatement(new Token("var2", ForthTokenType.WORD, 2), 2);
        WordStatement stmt3 = new WordStatement(new Token("var3", ForthTokenType.WORD, 3), 3);

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
        Token token = new Token("var", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);

        assertEquals("", stmt.content(), "Content should return empty string");
    }

    @Test
    @DisplayName("structure should return empty string")
    public void testStructureReturnsEmptyString() throws Exception {
        Token token = new Token("var", ForthTokenType.WORD, 1);
        WordStatement stmt = new WordStatement(token, 1);

        assertEquals("", stmt.structure(), "Structure should return empty string");
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    @DisplayName("Variable index correctness for use with fetch/store")
    public void testVariableIndexCorrectness() throws Exception {
        _oVariables.define("data");

        Token accessToken = new Token("data", ForthTokenType.WORD, 1);
        WordStatement accessStmt = new WordStatement(accessToken, 1);
        accessStmt.execute();

        int indexFromWord = ((IntegerValue) _oStack.pop()).toInteger();
        int indexFromVariables = _oVariables.index("data");

        assertEquals(indexFromVariables, indexFromWord, "Index from WORD token should match Variables.index()");
    }

    @Test
    @DisplayName("Sequence of definition and access operations")
    public void testDefinitionAndAccessSequence() throws Exception {
        // Define multiple variables
        _oVariables.define("x");
        _oVariables.define("y");
        _oVariables.define("z");

        // Access all variables
        WordStatement stmtX = new WordStatement(new Token("x", ForthTokenType.WORD, 1), 1);
        WordStatement stmtY = new WordStatement(new Token("y", ForthTokenType.WORD, 2), 2);
        WordStatement stmtZ = new WordStatement(new Token("z", ForthTokenType.WORD, 3), 3);

        stmtX.execute();
        stmtY.execute();
        stmtZ.execute();

        assertEquals(3, _oStack.size());
        assertEquals(2, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) _oStack.pop()).toInteger());
        assertEquals(0, ((IntegerValue) _oStack.pop()).toInteger());
    }

    @Test
    @DisplayName("Multiple statements with different token numbers")
    public void testMultipleStatementsWithDifferentTokenNumbers() throws Exception {
        _oVariables.define("var1");
        _oVariables.define("var2");

        WordStatement stmt1 = new WordStatement(new Token("var1", ForthTokenType.WORD, 10), 10);
        WordStatement stmt2 = new WordStatement(new Token("var2", ForthTokenType.WORD, 20), 20);

        assertEquals(10, stmt1.getTokenNumber());
        assertEquals(20, stmt2.getTokenNumber());

        stmt1.execute();
        stmt2.execute();

        assertEquals(2, _oStack.size());
    }

    @Test
    @DisplayName("Full workflow: define then access for fetch/store operations")
    public void testFullWorkflowForFetchStore() throws Exception {
        // Simulate complete workflow: define, access index, use with fetch/store
        _oVariables.define("myData");

        // Get variable index via WORD token
        WordStatement accessStmt = new WordStatement(new Token("myData", ForthTokenType.WORD, 1), 1);
        accessStmt.execute();

        // Index is now on stack, ready for fetch (@) or store (!)
        assertEquals(1, _oStack.size(), "Stack should have variable index");
        int varIndex = ((IntegerValue) _oStack.pop()).toInteger();
        assertEquals(0, varIndex, "Variable index should be 0");

        // Verify the index matches the variable
        assertEquals(varIndex, _oVariables.index("myData"), "Index should match Variables.index()");
    }
}
