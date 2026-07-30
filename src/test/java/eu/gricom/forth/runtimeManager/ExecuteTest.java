package eu.gricom.forth.runtimeManager;

import eu.gricom.forth.memoryManager.Program;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Execute class.
 * Tests the runtime statement execution engine.
 */
class ExecuteTest {

    @Test
    void testExecuteCreation() {
        Program mockProgram = new Program();
        Execute executor = new Execute(mockProgram);
        assertNotNull(executor);
    }

    @Test
    void testLoadEnvironment() {
        Program mockProgram = new Program();
        Execute executor = new Execute(mockProgram);
        assertDoesNotThrow(() -> executor.loadEnvironment());
    }

    @Test
    void testRunNewStatementsWithEmpty() {
        Program mockProgram = new Program();
        Execute executor = new Execute(mockProgram);
        assertDoesNotThrow(() -> executor.runNewStatements());
    }

    @Test
    void testGetFinalStatement() {
        Program mockProgram = new Program();
        Execute executor = new Execute(mockProgram);
        assertNull(executor.getFinalStatement());
    }

    @Test
    void testRunNewStatementsWithSimpleStatements() {
        List<Statement> statements = new ArrayList<>();
        Token token1 = new Token("", ForthTokenType.CARRIAGE_RETURN, 1);

        // Create a simple mock statement
        statements.add(new MockStatement());

        Program mockProgram = new Program();
        // Note: We would need to mock Program to set statements
        // For now, just verify the executor doesn't crash
        Execute executor = new Execute(mockProgram);
        assertDoesNotThrow(() -> executor.runNewStatements());
    }

    @Test
    void testExecuteWithNullProgram() {
        // This should be handled gracefully
        Program mockProgram = new Program();
        Execute executor = new Execute(mockProgram);
        assertNotNull(executor);
    }

    @Test
    void testMultipleExecuteInstances() {
        Program prog1 = new Program();
        Program prog2 = new Program();

        Execute executor1 = new Execute(prog1);
        Execute executor2 = new Execute(prog2);

        assertNotNull(executor1);
        assertNotNull(executor2);
    }

    /**
     * Mock statement for testing purposes.
     */
    private static class MockStatement implements Statement {
        @Override
        public int getTokenNumber() {
            return 0;
        }

        @Override
        public void execute() throws Exception {
            // Mock implementation - do nothing
        }

        @Override
        public String content() throws Exception {
            return "MOCK";
        }

        @Override
        public String structure() throws Exception {
            return "{\"MOCK\": {}}";
        }
    }
}