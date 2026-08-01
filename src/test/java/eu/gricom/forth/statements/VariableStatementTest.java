package eu.gricom.forth.statements;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VariableStatementTest {

    private Stack stack;

    @BeforeEach
    public void setUp() throws Exception {
        stack = new Stack();
        stack.reset();

        // Clear the static map in Variables
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        Map<Integer, Object> map = (Map<Integer, Object>) varField.get(null);
        map.clear();

        // Clear variable names list by creating a fresh Variables instance with cleared state
        // This ensures each test starts with a clean slate
        Variables testVars = new Variables();
        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) namesField.get(testVars);
        if (names != null) {
            names.clear();
        }
    }

    private Variables getVariablesFromStatement(VariableStatement stmt) throws Exception {
        Field field = VariableStatement.class.getDeclaredField("_oVariables");
        field.setAccessible(true);
        return (Variables) field.get(stmt);
    }

    private void setVariablesForStatement(VariableStatement stmt, Variables vars) throws Exception {
        Field field = VariableStatement.class.getDeclaredField("_oVariables");
        field.setAccessible(true);
        field.set(stmt, vars);
    }

    // ============================================================================
    // VARIABLE Token Tests
    // ============================================================================

    @Test
    public void testVariableTokenDefinesNewVariable() throws Exception {
        Token token = new Token("myVar", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "myVar");

        stmt.execute();

        Variables variables = getVariablesFromStatement(stmt);
        assertTrue(variables.isVariable("myVar"), "Variable should be defined after VARIABLE token execution");
    }

    @Test
    public void testVariableTokenInitializesWithEmpty() throws Exception {
        Token token = new Token("testVar", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "testVar");

        stmt.execute();

        Variables variables = getVariablesFromStatement(stmt);
        assertEquals("empty", variables.get("testVar").toString(), "Variable should be initialized with 'empty'");
    }

    @Test
    public void testMultipleVariableTokensInSameStatement() throws Exception {
        Token token1 = new Token("var1", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt1 = new VariableStatement(token1, 1, "var1");
        stmt1.execute();

        Variables variables = getVariablesFromStatement(stmt1);
        assertTrue(variables.isVariable("var1"), "Variable should be defined");

        Token token2 = new Token("var2", ForthTokenType.VARIABLE, 2);
        VariableStatement stmt2 = new VariableStatement(token2, 2, "var2");
        stmt2.execute();

        Variables variables2 = getVariablesFromStatement(stmt2);
        assertTrue(variables2.isVariable("var2"), "Second statement's variable should be defined");
    }

    @Test
    public void testVariableTokenThrowsExceptionForDuplicate() throws Exception {
        Token token1 = new Token("duplicate", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt1 = new VariableStatement(token1, 1, "duplicate");
        stmt1.execute();

        Variables variables = getVariablesFromStatement(stmt1);

        Token token2 = new Token("duplicate", ForthTokenType.VARIABLE, 2);
        VariableStatement stmt2 = new VariableStatement(token2, 2, "duplicate");
        setVariablesForStatement(stmt2, variables);

        assertThrows(RuntimeException.class, () -> {
            stmt2.execute();
        }, "Defining duplicate variable should throw exception");
    }

    @Test
    public void testVariableTokenWithSpecialCharacterName() throws Exception {
        Token token = new Token("var_with_underscore", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var_with_underscore");

        stmt.execute();

        Variables variables = getVariablesFromStatement(stmt);
        assertTrue(variables.isVariable("var_with_underscore"));
    }

    @Test
    public void testVariableTokenWithNumericName() throws Exception {
        Token token = new Token("var123", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "var123");

        stmt.execute();

        Variables variables = getVariablesFromStatement(stmt);
        assertTrue(variables.isVariable("var123"));
    }

    @Test
    public void testVariableTokenWithEmptyString() throws Exception {
        Token token = new Token("", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1, "");

        stmt.execute();

        Variables variables = getVariablesFromStatement(stmt);
        assertTrue(variables.isVariable(""));
    }

    // ============================================================================
    // WORD Token Tests (Variable Access)
    // ============================================================================

    @Test
    public void testWordTokenPushesVariableIndex() throws Exception {
        Token defToken = new Token("myVar", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "myVar");
        defStmt.execute();

        Variables variables = getVariablesFromStatement(defStmt);
        int expectedIndex = variables.index("myVar");

        Token token = new Token("myVar", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        setVariablesForStatement(stmt, variables);
        stmt.execute();

        assertEquals(1, stack.size(), "Stack should have one item after WORD token");
        int pushedIndex = ((IntegerValue) stack.pop()).toInteger();
        assertEquals(expectedIndex, pushedIndex, "WORD token should push variable index to stack");
    }

    @Test
    public void testWordTokenWithFirstVariable() throws Exception {
        Token defToken = new Token("first", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "first");
        defStmt.execute();

        Variables variables = getVariablesFromStatement(defStmt);

        Token token = new Token("first", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        setVariablesForStatement(stmt, variables);
        stmt.execute();

        int index = ((IntegerValue) stack.pop()).toInteger();
        assertEquals(0, index, "First variable should have index 0");
    }

    @Test
    public void testWordTokenWithSecondVariable() throws Exception {
        Token defToken1 = new Token("first", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt1 = new VariableStatement(defToken1, 1, "first");
        defStmt1.execute();

        Variables variables = getVariablesFromStatement(defStmt1);
        variables.define("second");

        Token token = new Token("second", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        setVariablesForStatement(stmt, variables);
        stmt.execute();

        int index = ((IntegerValue) stack.pop()).toInteger();
        assertEquals(1, index, "Second variable should have index 1");
    }

    @Test
    public void testWordTokenThrowsExceptionForUndefinedVariable() throws Exception {
        Token token = new Token("undefined", ForthTokenType.WORD, 1);
        VariableStatement stmt = new VariableStatement(token, 1);

        assertThrows(RuntimeException.class, () -> {
            stmt.execute();
        }, "WORD token for undefined variable should throw exception");
    }

    @Test
    public void testWordTokenPushesMultipleValues() throws Exception {
        Token defToken = new Token("alpha", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "alpha");
        defStmt.execute();

        Variables variables = getVariablesFromStatement(defStmt);
        variables.define("beta");

        Token token1 = new Token("alpha", ForthTokenType.WORD, 2);
        VariableStatement stmt1 = new VariableStatement(token1, 2);
        setVariablesForStatement(stmt1, variables);
        stmt1.execute();

        Token token2 = new Token("beta", ForthTokenType.WORD, 3);
        VariableStatement stmt2 = new VariableStatement(token2, 3);
        setVariablesForStatement(stmt2, variables);
        stmt2.execute();

        assertEquals(2, stack.size(), "Stack should have two items");
        int idx2 = ((IntegerValue) stack.pop()).toInteger();
        int idx1 = ((IntegerValue) stack.pop()).toInteger();

        assertEquals(1, idx2, "Second push should be index 1");
        assertEquals(0, idx1, "First push should be index 0");
    }

    // ============================================================================
    // Other Token Tests
    // ============================================================================

    @Test
    public void testStoreTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.STORE, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "STORE token should not throw");
    }

    @Test
    public void testFetchTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.FETCH, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "FETCH token should not throw");
    }

    @Test
    public void testTwoStoreTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.TWO_STORE, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "2STORE token should not throw");
    }

    @Test
    public void testTwoFetchTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.TWO_FETCH, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "2FETCH token should not throw");
    }

    @Test
    public void testCharStoreTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.CHAR_STORE, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "CHAR_STORE token should not throw");
    }

    @Test
    public void testCharFetchTokenDoesNotThrow() throws Exception {
        Token token = new Token("", ForthTokenType.CHAR_FETCH, 1);
        VariableStatement stmt = new VariableStatement(token, 1);
        assertDoesNotThrow(() -> stmt.execute(), "CHAR_FETCH token should not throw");
    }

    @Test
    public void testUnknownTokenThrowsException() throws Exception {
        Token token = new Token("", ForthTokenType.NUMBER, 1);
        VariableStatement stmt = new VariableStatement(token, 1);

        assertThrows(RuntimeException.class, () -> {
            stmt.execute();
        }, "Unknown token type should throw exception");
    }

    // ============================================================================
    // Token Number Tests
    // ============================================================================

    @Test
    public void testGetTokenNumber() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 42);
        VariableStatement stmt = new VariableStatement(token, 42);

        assertEquals(42, stmt.getTokenNumber(), "Token number should match constructor parameter");
    }

    @Test
    public void testGetTokenNumberZero() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 0);
        VariableStatement stmt = new VariableStatement(token, 0);

        assertEquals(0, stmt.getTokenNumber(), "Token number can be zero");
    }

    @Test
    public void testGetTokenNumberLarge() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 9999);
        VariableStatement stmt = new VariableStatement(token, 9999);

        assertEquals(9999, stmt.getTokenNumber(), "Token number can be large");
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    public void testContentReturnsEmptyString() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1);

        assertEquals("", stmt.content(), "Content should return empty string");
    }

    @Test
    public void testStructureReturnsEmptyString() throws Exception {
        Token token = new Token("var", ForthTokenType.VARIABLE, 1);
        VariableStatement stmt = new VariableStatement(token, 1);

        assertEquals("", stmt.structure(), "Structure should return empty string");
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    public void testVariableDefinitionFollowedByAccess() throws Exception {
        Token defineToken = new Token("counter", ForthTokenType.VARIABLE, 1);
        VariableStatement defineStmt = new VariableStatement(defineToken, 1, "counter");
        defineStmt.execute();

        Variables variables = getVariablesFromStatement(defineStmt);

        Token accessToken = new Token("counter", ForthTokenType.WORD, 2);
        VariableStatement accessStmt = new VariableStatement(accessToken, 2);
        setVariablesForStatement(accessStmt, variables);
        accessStmt.execute();

        assertEquals(1, stack.size(), "Stack should contain variable index");
        int index = ((IntegerValue) stack.pop()).toInteger();
        assertEquals(0, index, "Variable index should be 0");
    }

    @Test
    public void testMultipleVariableDefinitionsAndAccess() throws Exception {
        Token defToken1 = new Token("alpha", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt1 = new VariableStatement(defToken1, 1, "alpha");
        defStmt1.execute();

        Variables variables = getVariablesFromStatement(defStmt1);
        variables.define("beta");
        variables.define("gamma");

        Token token1 = new Token("alpha", ForthTokenType.WORD, 2);
        VariableStatement stmt1 = new VariableStatement(token1, 2);
        setVariablesForStatement(stmt1, variables);
        stmt1.execute();

        Token token2 = new Token("gamma", ForthTokenType.WORD, 3);
        VariableStatement stmt2 = new VariableStatement(token2, 3);
        setVariablesForStatement(stmt2, variables);
        stmt2.execute();

        assertEquals(2, stack.size(), "Stack should have two indices");
        int idx2 = ((IntegerValue) stack.pop()).toInteger();
        int idx1 = ((IntegerValue) stack.pop()).toInteger();

        assertEquals(2, idx2, "Gamma should have index 2");
        assertEquals(0, idx1, "Alpha should have index 0");
    }

    @Test
    public void testVariableAndWordTokenSequence() throws Exception {
        Token varToken1 = new Token("x", ForthTokenType.VARIABLE, 1);
        VariableStatement varStmt1 = new VariableStatement(varToken1, 1, "x");
        varStmt1.execute();

        Variables variables = getVariablesFromStatement(varStmt1);
        variables.define("y");

        Token wordToken = new Token("x", ForthTokenType.WORD, 2);
        VariableStatement wordStmt = new VariableStatement(wordToken, 2);
        setVariablesForStatement(wordStmt, variables);
        wordStmt.execute();

        int index = ((IntegerValue) stack.pop()).toInteger();
        assertEquals(0, index, "Should retrieve index of first defined variable");
    }

    @Test
    public void testCaseSensitiveVariableAccess() throws Exception {
        Token defToken = new Token("MyVar", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "MyVar");
        defStmt.execute();

        Variables variables = getVariablesFromStatement(defStmt);

        Token token = new Token("MyVar", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        setVariablesForStatement(stmt, variables);
        stmt.execute();

        assertEquals(1, stack.size(), "Should successfully access case-sensitive variable");
    }

    @Test
    public void testVariableStackPush() throws Exception {
        Token defToken = new Token("var1", ForthTokenType.VARIABLE, 1);
        VariableStatement defStmt = new VariableStatement(defToken, 1, "var1");
        defStmt.execute();

        Variables variables = getVariablesFromStatement(defStmt);
        stack.reset();

        Token token = new Token("var1", ForthTokenType.WORD, 2);
        VariableStatement stmt = new VariableStatement(token, 2);
        setVariablesForStatement(stmt, variables);
        stmt.execute();

        assertEquals(1, stack.size(), "Statement should push to stack");
    }
}
