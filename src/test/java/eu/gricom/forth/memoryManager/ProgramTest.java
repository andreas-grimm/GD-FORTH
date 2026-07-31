package eu.gricom.forth.memoryManager;

import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.tokenizer.ForthTokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProgramTest.java
 * <p>
 * Test suite for Program class, testing program storage and management functionality.
 */
@DisplayName("Program Test Suite")
public class ProgramTest {

    private Program _oProgram;

    @BeforeEach
    public void setUp() {
        _oProgram = new Program();
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    @DisplayName("Constructor should create program without exception")
    public void testConstructor() {
        assertNotNull(_oProgram, "Program object should be created");
    }

    @Test
    @DisplayName("Constructor should initialize with null values")
    public void testConstructorInitialization() {
        assertNull(_oProgram.getProgramName(), "Program name should be null initially");
        assertNull(_oProgram.getProgram(), "Program source should be null initially");
        assertNull(_oProgram.getTokens(), "Tokens should be null initially");
        assertNull(_oProgram.getStatements(), "Statements should be null initially");
    }

    // ===== LOAD TESTS =====

    @Test
    @DisplayName("load should store program name and source correctly")
    public void testLoadAndGetters() {
        String name = "test.fs";
        String source = ": HELLO .\" Hello\" CR ;";
        _oProgram.load(name, source);

        assertEquals(name, _oProgram.getProgramName(), "Program name should be stored correctly");
        assertEquals(source, _oProgram.getProgram(), "Program source should be stored correctly");
    }

    @Test
    @DisplayName("load should handle null program name")
    public void testLoadNullName() {
        String source = ": HELLO .\" Hello\" CR ;";
        _oProgram.load(null, source);

        assertNull(_oProgram.getProgramName(), "Program name should be null");
        assertEquals(source, _oProgram.getProgram(), "Program source should be stored");
    }

    @Test
    @DisplayName("load should handle null program source")
    public void testLoadNullSource() {
        String name = "test.fs";
        _oProgram.load(name, null);

        assertEquals(name, _oProgram.getProgramName(), "Program name should be stored");
        assertNull(_oProgram.getProgram(), "Program source should be null");
    }

    @Test
    @DisplayName("load should handle both null parameters")
    public void testLoadBothNull() {
        _oProgram.load(null, null);

        assertNull(_oProgram.getProgramName(), "Program name should be null");
        assertNull(_oProgram.getProgram(), "Program source should be null");
    }

    @Test
    @DisplayName("load should handle empty strings")
    public void testLoadEmptyStrings() {
        _oProgram.load("", "");

        assertEquals("", _oProgram.getProgramName(), "Empty program name should be stored");
        assertEquals("", _oProgram.getProgram(), "Empty program source should be stored");
    }

    @Test
    @DisplayName("load should handle very long program source")
    public void testLoadLongSource() {
        StringBuilder longSource = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longSource.append(": word").append(i).append(" EMIT ;\n");
        }

        _oProgram.load("long_program.forth", longSource.toString());
        assertEquals(longSource.toString(), _oProgram.getProgram(), "Long source should be stored");
    }

    @Test
    @DisplayName("load should allow overwriting program multiple times")
    public void testLoadMultipleTimes() {
        _oProgram.load("first.forth", "EMIT DROP");
        assertEquals("first.forth", _oProgram.getProgramName());

        _oProgram.load("second.forth", "KEY SWAP");
        assertEquals("second.forth", _oProgram.getProgramName());
        assertEquals("KEY SWAP", _oProgram.getProgram());
    }

    // ===== SET/GET PROGRAM TESTS =====

    @Test
    @DisplayName("setProgram should update program source")
    public void testSetProgram() {
        String source = "DUP DROP SWAP";
        _oProgram.setProgram(source);
        assertEquals(source, _oProgram.getProgram(), "Program source should be updated");
    }

    @Test
    @DisplayName("setProgram should handle null")
    public void testSetProgramNull() {
        _oProgram.load("test.fs", ": HELLO .\" Hello\" CR ;");
        _oProgram.setProgram(null);
        assertNull(_oProgram.getProgram(), "Program source should be null");
    }

    @Test
    @DisplayName("setProgram should handle empty string")
    public void testSetProgramEmpty() {
        _oProgram.load("test.fs", ": HELLO .\" Hello\" CR ;");
        _oProgram.setProgram("");
        assertEquals("", _oProgram.getProgram(), "Program source should be empty");
    }

    @Test
    @DisplayName("setProgram should overwrite existing source")
    public void testSetProgramOverwrite() {
        _oProgram.load("test.fs", ": FIRST .\" First\" CR ;");
        _oProgram.setProgram(": SECOND .\" Second\" CR ;");
        assertEquals(": SECOND .\" Second\" CR ;", _oProgram.getProgram());
    }

    @Test
    @DisplayName("getProgram should return initially null")
    public void testGetProgramInitial() {
        assertNull(_oProgram.getProgram(), "Program should be null initially");
    }

    // ===== TOKENS TESTS =====

    @Test
    @DisplayName("setTokens and getTokens should work with empty list")
    public void testTokensEmpty() {
        List<Token> tokens = new ArrayList<>();
        _oProgram.setTokens(tokens);
        assertEquals(tokens, _oProgram.getTokens(), "Empty token list should be stored");
    }

    @Test
    @DisplayName("setTokens and getTokens should work with populated list")
    public void testTokensPopulated() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("EMIT", ForthTokenType.EMIT, 10));
        tokens.add(new Token("DOT", ForthTokenType.PRINT, 10));

        _oProgram.setTokens(tokens);
        assertEquals(tokens, _oProgram.getTokens(), "Token list should be stored correctly");
        assertEquals(2, _oProgram.getTokens().size(), "Should have 2 tokens");
    }

    @Test
    @DisplayName("setTokens should handle null")
    public void testTokensNull() {
        _oProgram.setTokens(null);
        assertNull(_oProgram.getTokens(), "Null token list should be stored");
    }

    @Test
    @DisplayName("setTokens should overwrite previous tokens")
    public void testTokensOverwrite() {
        List<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("EMIT", ForthTokenType.EMIT, 10));
        _oProgram.setTokens(tokens1);

        List<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("KEY", ForthTokenType.KEY, 20));
        _oProgram.setTokens(tokens2);

        assertEquals(1, _oProgram.getTokens().size());
        assertEquals("KEY", _oProgram.getTokens().get(0).getText());
    }

    @Test
    @DisplayName("getTokens should return initially null")
    public void testGetTokensInitial() {
        assertNull(_oProgram.getTokens(), "Tokens should be null initially");
    }

    // ===== STATEMENTS TESTS =====

    @Test
    @DisplayName("setStatements and getStatements should work with empty list")
    public void testStatementsEmpty() {
        List<Statement> statements = new ArrayList<>();
        _oProgram.setStatements(statements);
        assertEquals(statements, _oProgram.getStatements(), "Empty statement list should be stored");
    }

    @Test
    @DisplayName("setStatements should handle null")
    public void testStatementsNull() {
        _oProgram.setStatements(null);
        assertNull(_oProgram.getStatements(), "Null statement list should be stored");
    }

    @Test
    @DisplayName("setStatements should overwrite previous statements")
    public void testStatementsOverwrite() {
        List<Statement> statements1 = new ArrayList<>();
        _oProgram.setStatements(statements1);
        assertNotNull(_oProgram.getStatements());

        List<Statement> statements2 = new ArrayList<>();
        _oProgram.setStatements(statements2);
        assertEquals(statements2, _oProgram.getStatements());
    }

    @Test
    @DisplayName("getStatements should return initially null")
    public void testGetStatementsInitial() {
        assertNull(_oProgram.getStatements(), "Statements should be null initially");
    }

    // ===== EQUALS TESTS - POSITIVE CASES =====

    @Test
    @DisplayName("equals should return true for identical programs with null values")
    public void testEqualsPositiveAllNull() {
        Program p1 = new Program();
        Program p2 = new Program();

        assertTrue(p1.equals(p2), "Programs with all null values should be equal");
    }

    @Test
    @DisplayName("equals should return true for identical programs with same values")
    public void testEqualsPositiveAllSet() {
        Program p1 = new Program();
        p1.load("test", "source");
        p1.setTokens(new ArrayList<>());
        p1.setStatements(new ArrayList<>());

        Program p2 = new Program();
        p2.load("test", "source");
        p2.setTokens(new ArrayList<>());
        p2.setStatements(new ArrayList<>());

        assertTrue(p1.equals(p2), "Programs with identical values should be equal");
    }

    @Test
    @DisplayName("equals should return true for programs with null name and source")
    public void testEqualsPositiveNullNameAndSource() {
        Program p1 = new Program();
        Program p2 = new Program();

        assertTrue(p1.equals(p2), "Programs with null name and source should be equal");
    }

    // ===== EQUALS TESTS - NEGATIVE CASES =====

    @Test
    @DisplayName("equals should return false when comparing with null")
    public void testEqualsNull() {
        assertFalse(_oProgram.equals(null), "Program should not equal null");
    }

    @Test
    @DisplayName("equals should return false for different program names")
    public void testEqualsNegativeName() {
        Program p1 = new Program();
        p1.load("test1", "source");

        Program p2 = new Program();
        p2.load("test2", "source");

        assertFalse(p1.equals(p2), "Programs with different names should not be equal");
    }

    @Test
    @DisplayName("equals should return false for different program sources")
    public void testEqualsNegativeSource() {
        Program p1 = new Program();
        p1.load("test", "source1");

        Program p2 = new Program();
        p2.load("test", "source2");

        assertFalse(p1.equals(p2), "Programs with different sources should not be equal");
    }

    @Test
    @DisplayName("equals should return false when one has tokens and other doesn't")
    public void testEqualsDifferentTokens() {
        Program p1 = new Program();
        p1.load("test", "source");

        Program p2 = new Program();
        p2.load("test", "source");
        p2.setTokens(new ArrayList<>());

        assertFalse(p1.equals(p2), "Programs with different token lists should not be equal");
    }

    @Test
    @DisplayName("equals should return false when one has statements and other doesn't")
    public void testEqualsDifferentStatements() {
        Program p1 = new Program();
        p1.load("test", "source");

        Program p2 = new Program();
        p2.load("test", "source");
        p2.setStatements(new ArrayList<>());

        assertFalse(p1.equals(p2), "Programs with different statement lists should not be equal");
    }

    @Test
    @DisplayName("equals should return false when name is null in one program")
    public void testEqualsNameOneNull() {
        Program p1 = new Program();
        p1.load(null, "source");

        Program p2 = new Program();
        p2.load("test", "source");

        assertFalse(p1.equals(p2), "Programs with null vs non-null names should not be equal");
    }

    @Test
    @DisplayName("equals should return false when source is null in one program")
    public void testEqualsSourceOneNull() {
        Program p1 = new Program();
        p1.load("test", null);

        Program p2 = new Program();
        p2.load("test", "source");

        assertFalse(p1.equals(p2), "Programs with null vs non-null sources should not be equal");
    }

    // ===== SET LINE NUMBER TESTS =====

    @Test
    @DisplayName("setLineNumber should accept LineNumberXRef object")
    public void testSetLineNumber() {
        LineNumberXRef lineNumbers = new LineNumberXRef();
        assertDoesNotThrow(() -> _oProgram.setLineNumber(lineNumbers),
            "setLineNumber should accept valid LineNumberXRef object");
    }

    @Test
    @DisplayName("setLineNumber should handle null")
    public void testSetLineNumberNull() {
        assertDoesNotThrow(() -> _oProgram.setLineNumber(null),
            "setLineNumber should handle null gracefully");
    }

    // ===== INTEGRATION TESTS =====

    @Test
    @DisplayName("full program workflow: load, set tokens, statements, and compare")
    public void testFullProgramWorkflow() {
        String programName = "myprogram.forth";
        String programSource = "EMIT KEY DROP\n";

        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("EMIT", ForthTokenType.EMIT, 10));
        tokens.add(new Token("KEY", ForthTokenType.KEY, 10));
        tokens.add(new Token("DROP", ForthTokenType.DROP, 10));

        List<Statement> statements = new ArrayList<>();

        _oProgram.load(programName, programSource);
        _oProgram.setTokens(tokens);
        _oProgram.setStatements(statements);

        assertEquals(programName, _oProgram.getProgramName());
        assertEquals(programSource, _oProgram.getProgram());
        assertEquals(tokens, _oProgram.getTokens());
        assertEquals(statements, _oProgram.getStatements());
    }

    @Test
    @DisplayName("program should maintain state through multiple operations")
    public void testProgramStateManagement() {
        _oProgram.load("test.forth", "EMIT KEY");

        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("EMIT", ForthTokenType.EMIT, 10));
        _oProgram.setTokens(tokens);

        // Verify initial state
        assertEquals("test.forth", _oProgram.getProgramName());
        assertEquals("EMIT KEY", _oProgram.getProgram());
        assertEquals(1, _oProgram.getTokens().size());

        // Update program
        _oProgram.setProgram("DROP DUP");

        // Verify updated state
        assertEquals("DROP DUP", _oProgram.getProgram());
        assertEquals("test.forth", _oProgram.getProgramName(), "Name should remain unchanged");
        assertEquals(1, _oProgram.getTokens().size(), "Tokens should remain unchanged");
    }

    @Test
    @DisplayName("two identical programs should be equal but independent")
    public void testProgramIndependence() {
        Program p1 = new Program();
        p1.load("test.forth", "EMIT KEY DROP");

        Program p2 = new Program();
        p2.load("test.forth", "EMIT KEY DROP");

        assertTrue(p1.equals(p2), "Identical programs should be equal");

        // Modify p1
        p1.setProgram("SWAP OVER");

        // p2 should not be affected
        assertEquals("EMIT KEY DROP", p2.getProgram(), "p2 should be unaffected by p1 modification");
        assertFalse(p1.equals(p2), "Programs should not be equal after p1 modification");
    }
}
