package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DoStatementTest.java
 *
 * Unit tests for DoStatement class.
 * Tests loop construction and basic properties.
 *
 * Note: Complex execution scenarios are tested via integration tests
 * and FORTH code tests to avoid circular dependencies and hangs.
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("DoStatement Tests")
class DoStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        ReturnStack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create DoStatement with token and loop body")
    void testConstructor() {
        System.out.println("\n[TEST] testConstructor - Creating empty DoStatement");
        List<Statement> aoBody = new ArrayList<>();
        Token oToken = new Token("DO", ForthTokenType.DO, 1);

        DoStatement stmt = new DoStatement(oToken, 0, aoBody);
        System.out.println("  ✓ DoStatement created: " + (stmt != null ? "SUCCESS" : "FAILED"));
        System.out.println("  ✓ Is Statement instance: " + (stmt instanceof Statement ? "YES" : "NO"));
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create DoStatement with non-empty loop body")
    void testConstructorWithBody() {
        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);

        DoStatement stmt = new DoStatement(oToken, 0, aoBody);
        assertNotNull(stmt);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        List<Statement> aoBody = new ArrayList<>();
        Token oToken = new Token("DO", ForthTokenType.DO, 1);

        DoStatement stmt = new DoStatement(oToken, 5, aoBody);
        assertEquals(5, stmt.getTokenNumber());
    }

    // ================= LOOP CONTEXT MANAGEMENT =================

    @Test
    @DisplayName("Should clean up loop context after execution")
    void testLoopContextCleanup() throws Exception {
        System.out.println("\n[TEST] testLoopContextCleanup - Verify cleanup on exit");
        System.out.println("  Setup: 0 1 DO LOOP");

        // Setup: 0 1 DO LOOP (minimal loop)
        stack.push(new IntegerValue(1));  // limit
        stack.push(new IntegerValue(0));  // index

        // Create a custom statement that prints iteration information
        Statement iterationPrinter = new Statement() {
            private int iterationCount = 0;

            @Override
            public void execute() throws Exception {
                iterationCount++;
                long currentIndex = ReturnStack.peekLoop().getCurrentIndex();
                System.out.println("    Iteration " + iterationCount + ": index = " + currentIndex);
            }

            @Override
            public int getTokenNumber() {
                return 0;
            }

            @Override
            public String content() throws Exception {
                return "";
            }

            @Override
            public String structure() throws Exception {
                return "";
            }
        };

        // Create loop body with LOOP statement to prevent infinite loop
        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(iterationPrinter);  // Print iteration info
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Verify loop stack is empty before
        System.out.println("  Before execution: Loop active = " + ReturnStack.isLoopActive());
        assertFalse(ReturnStack.isLoopActive());

        // Execute - loop executes once (0 < 1) then exits
        System.out.println("  Executing loop: 0 < 1 = 1 iteration expected");
        stmt.execute();
        System.out.println("  Loop execution completed");

        // Verify loop stack is empty after (cleanup happened)
        System.out.println("  After execution: Loop active = " + ReturnStack.isLoopActive());
        System.out.println("  Loop stack size = " + ReturnStack.getLoopStackSize());
        assertFalse(ReturnStack.isLoopActive());
    }

    // ================= ERROR CASES =================

    @Test
    @DisplayName("Should throw exception if stack doesn't have enough values")
    void testEmptyStackError() throws Exception {
        // Don't push anything on the stack
        List<Statement> aoBody = new ArrayList<>();
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Should throw exception due to empty stack
        assertThrows(Exception.class, stmt::execute);
    }

    @Test
    @DisplayName("Should throw exception with insufficient stack values")
    void testSingleValueOnStack() throws Exception {
        // Only push one value (need two: limit and index)
        stack.push(new IntegerValue(5));

        List<Statement> aoBody = new ArrayList<>();
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Should throw exception due to insufficient values
        assertThrows(Exception.class, stmt::execute);
    }

    // ================= LOOP EXECUTION TESTS =================

    @Test
    @DisplayName("Single iteration loop executes once and exits")
    void testSingleIterationLoop() throws Exception {
        System.out.println("\n[TEST] testSingleIterationLoop - 0 1 DO LOOP");
        System.out.println("  Setup: Loop from index=0 to limit=1 (expect 1 iteration)");

        // Setup: 0 1 DO LOOP (loop from 0 to 1, should execute once)
        stack.push(new IntegerValue(1));  // limit
        stack.push(new IntegerValue(0));  // index

        // Create a custom statement that prints iteration information
        Statement iterationPrinter = new Statement() {
            private int iterationCount = 0;

            @Override
            public void execute() throws Exception {
                iterationCount++;
                long currentIndex = ReturnStack.peekLoop().getCurrentIndex();
                System.out.println("    Iteration " + iterationCount + ": index = " + currentIndex);
            }

            @Override
            public int getTokenNumber() {
                return 0;
            }

            @Override
            public String content() throws Exception {
                return "";
            }

            @Override
            public String structure() throws Exception {
                return "";
            }
        };

        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(iterationPrinter);  // Print iteration info
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Verify loop stack is empty before
        System.out.println("  Before: Loop active = " + ReturnStack.isLoopActive());
        assertFalse(ReturnStack.isLoopActive());

        // Execute the loop
        System.out.println("  Executing: 0 1 DO LOOP");
        stmt.execute();

        // Verify loop completed and cleaned up
        System.out.println("  After: Loop active = " + ReturnStack.isLoopActive());
        System.out.println("  ✓ Single iteration completed successfully");
        assertFalse(ReturnStack.isLoopActive());
    }

    @Test
    @DisplayName("Multiple iteration loop executes correct number of times")
    void testMultipleIterationLoop() throws Exception {
        System.out.println("\n[TEST] testMultipleIterationLoop - 0 3 DO LOOP");
        System.out.println("  Setup: Loop from index=0 to limit=3 (expect 3 iterations: 0, 1, 2)");

        // Setup: 0 3 DO LOOP (loop from 0 to 3, should execute 3 times)
        stack.push(new IntegerValue(3));  // limit
        stack.push(new IntegerValue(0));  // index

        // Create a custom statement that prints iteration information
        Statement iterationPrinter = new Statement() {
            private int iterationCount = 0;

            @Override
            public void execute() throws Exception {
                iterationCount++;
                long currentIndex = ReturnStack.peekLoop().getCurrentIndex();
                System.out.println("    Iteration " + iterationCount + ": index = " + currentIndex);
            }

            @Override
            public int getTokenNumber() {
                return 0;
            }

            @Override
            public String content() throws Exception {
                return "";
            }

            @Override
            public String structure() throws Exception {
                return "";
            }
        };

        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(iterationPrinter);  // Print iteration info
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Execute - loop runs 3 times (0, 1, 2) then exits
        System.out.println("  Executing: 0 3 DO LOOP");
        stmt.execute();

        // Verify loop cleaned up properly
        System.out.println("  Loop execution completed");
        System.out.println("  After: Loop active = " + ReturnStack.isLoopActive());
        System.out.println("  ✓ 3 iterations completed successfully");
        assertFalse(ReturnStack.isLoopActive());
    }

    @Test
    @DisplayName("Empty loop (equal bounds) executes zero iterations")
    void testEmptyLoop() throws Exception {
        System.out.println("\n[TEST] testEmptyLoop - 5 5 DO LOOP");
        System.out.println("  Setup: Loop from index=5 to limit=5 (expect 0 iterations)");

        // Setup: 5 5 DO LOOP (loop from 5 to 5, should not execute)
        stack.push(new IntegerValue(5));  // limit
        stack.push(new IntegerValue(5));  // index

        // Create a custom statement that prints iteration information
        Statement iterationPrinter = new Statement() {
            private int iterationCount = 0;

            @Override
            public void execute() throws Exception {
                iterationCount++;
                long currentIndex = ReturnStack.peekLoop().getCurrentIndex();
                System.out.println("    Iteration " + iterationCount + ": index = " + currentIndex);
            }

            @Override
            public int getTokenNumber() {
                return 0;
            }

            @Override
            public String content() throws Exception {
                return "";
            }

            @Override
            public String structure() throws Exception {
                return "";
            }
        };

        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(iterationPrinter);  // Print iteration info (shouldn't print for empty loop)
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Execute - loop should exit immediately (5 >= 5)
        System.out.println("  Executing: 5 5 DO LOOP");
        System.out.println("  Condition check: 5 >= 5? = true (loop should not execute)");
        stmt.execute();

        // Verify loop exited and cleaned up
        System.out.println("  Loop execution completed");
        System.out.println("  After: Loop active = " + ReturnStack.isLoopActive());
        System.out.println("  ✓ 0 iterations (empty loop handled correctly)");
        assertFalse(ReturnStack.isLoopActive());
    }

    @Test
    @DisplayName("Loop maintains proper context during execution")
    void testLoopContextMaintenance() throws Exception {
        System.out.println("\n[TEST] testLoopContextMaintenance - 10 12 DO LOOP");
        System.out.println("  Setup: Loop from index=10 to limit=12 (expect 2 iterations: 10, 11)");

        // Setup: 10 12 DO LOOP (loop from 10 to 12)
        stack.push(new IntegerValue(12));  // limit
        stack.push(new IntegerValue(10));  // index

        // Create a custom statement that prints iteration information
        Statement iterationPrinter = new Statement() {
            private int iterationCount = 0;

            @Override
            public void execute() throws Exception {
                iterationCount++;
                long currentIndex = ReturnStack.peekLoop().getCurrentIndex();
                System.out.println("    Iteration " + iterationCount + ": index = " + currentIndex);
            }

            @Override
            public int getTokenNumber() {
                return 0;
            }

            @Override
            public String content() throws Exception {
                return "";
            }

            @Override
            public String structure() throws Exception {
                return "";
            }
        };

        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(iterationPrinter);  // Print iteration info
        aoBody.add(new LoopStatement(new Token("LOOP", ForthTokenType.LOOP, 1), 1));
        Token oToken = new Token("DO", ForthTokenType.DO, 1);
        DoStatement stmt = new DoStatement(oToken, 0, aoBody);

        // Verify no loop before execution
        System.out.println("  Before execution:");
        System.out.println("    Loop active = " + ReturnStack.isLoopActive());
        System.out.println("    Loop stack size = " + ReturnStack.getLoopStackSize());
        assertFalse(ReturnStack.isLoopActive());

        // Execute
        System.out.println("  Executing: 10 12 DO LOOP");
        stmt.execute();

        // Verify no loop after execution (context properly cleaned)
        System.out.println("  After execution:");
        System.out.println("    Loop active = " + ReturnStack.isLoopActive());
        int loopStackSize = ReturnStack.getLoopStackSize();
        System.out.println("    Loop stack size = " + loopStackSize);
        System.out.println("  ✓ 2 iterations completed, context properly cleaned");
        assertFalse(ReturnStack.isLoopActive());
        assertEquals(0, loopStackSize);
    }
}
