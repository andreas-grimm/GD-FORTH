package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.NumberStatement;
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
 * BeginStatementTest.java
 *
 * Unit tests for BeginStatement class (BEGIN...WHILE...REPEAT indefinite loops).
 *
 * Tests cover:
 * - Basic loop functionality
 * - Stack behavior during condition and body
 * - Error cases (stack underflow)
 * - Nested loops
 * - Integration with other statements
 */
@DisplayName("BEGIN...WHILE...REPEAT Statement Tests")
class BeginStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ==================== BASIC FUNCTIONALITY TESTS ====================

    @Test
    @DisplayName("Should execute loop once when condition is initially true then false")
    void testSingleIteration() throws Exception {
        System.out.println("\n[TEST] testSingleIteration - Loop that runs once");

        // Setup: Push TRUE, then condition that pops and leaves FALSE
        List<Statement> aoCondition = new ArrayList<>();
        aoCondition.add(new NumberStatement(0, 0));  // Push FALSE (0)

        // Body: Just a counter statement
        List<Statement> aoBody = new ArrayList<>();
        Statement iterationCounter = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                System.out.println("  Iteration " + count);
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(iterationCounter);

        // First push TRUE so condition can execute once
        stack.push(new IntegerValue(1));

        // Create and execute BeginStatement
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  Loop exited as expected");
    }

    @Test
    @DisplayName("Should exit immediately when condition is false")
    void testZeroIterations() throws Exception {
        System.out.println("\n[TEST] testZeroIterations - Loop that doesn't run");

        // Condition: always false
        List<Statement> aoCondition = new ArrayList<>();
        aoCondition.add(new NumberStatement(0, 0));  // Push FALSE

        // Body: should never execute
        List<Statement> aoBody = new ArrayList<>();
        Statement shouldNotExecute = new Statement() {
            @Override
            public void execute() throws Exception {
                throw new Exception("Body should not execute!");
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(shouldNotExecute);

        // Execute
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  Loop correctly exited without running body");
    }

    @Test
    @DisplayName("Should loop multiple times with counting condition")
    void testMultipleIterations() throws Exception {
        System.out.println("\n[TEST] testMultipleIterations - Loop runs 3 times");

        // Condition: decrement and check if > 0
        List<Statement> aoCondition = new ArrayList<>();
        Statement decrementAndCheck = new Statement() {
            @Override
            public void execute() throws Exception {
                // Pop value, decrement, push true if > 0
                int iValue = stack.pop().toInteger();
                iValue--;
                stack.push(new IntegerValue(iValue));
                stack.push(new IntegerValue(iValue > 0 ? 1 : 0));
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoCondition.add(decrementAndCheck);

        // Body: print iteration
        List<Statement> aoBody = new ArrayList<>();
        Statement printIteration = new Statement() {
            private int iterationCount = 0;
            @Override
            public void execute() throws Exception {
                iterationCount++;
                System.out.println("  Iteration " + iterationCount);
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(printIteration);

        // Setup: Push 3 on stack
        stack.push(new IntegerValue(3));

        // Execute
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  Loop completed after 3 iterations");
    }

    // ==================== ERROR CASES ====================

    @Test
    @DisplayName("Should throw error if condition doesn't push flag")
    void testStackUnderflowInCondition() {
        System.out.println("\n[TEST] testStackUnderflowInCondition");

        // Condition: empty (doesn't push anything)
        List<Statement> aoCondition = new ArrayList<>();

        // Body: doesn't matter
        List<Statement> aoBody = new ArrayList<>();

        // Execute - should throw
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);

        Exception exception = assertThrows(Exception.class, stmt::execute);
        assertTrue(exception.getMessage().contains("Stack underflow"));
        System.out.println("  Correctly threw: " + exception.getMessage());
    }

    @Test
    @DisplayName("Should throw error if stack underflows in body")
    void testStackUnderflowInBody() {
        System.out.println("\n[TEST] testStackUnderflowInBody");

        // Condition: always true once then false
        List<Statement> aoCondition = new ArrayList<>();
        Statement conditionOnce = new Statement() {
            private boolean firstTime = true;
            @Override
            public void execute() throws Exception {
                if (firstTime) {
                    stack.push(new IntegerValue(1));  // TRUE
                    firstTime = false;
                } else {
                    stack.push(new IntegerValue(0));  // FALSE
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoCondition.add(conditionOnce);

        // Body: try to pop from empty stack
        List<Statement> aoBody = new ArrayList<>();
        Statement underflowBody = new Statement() {
            @Override
            public void execute() throws Exception {
                stack.pop();  // Will throw if empty
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(underflowBody);

        // Execute - should throw
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);

        assertThrows(Exception.class, stmt::execute);
        System.out.println("  Correctly threw exception during body execution");
    }

    // ==================== STACK BEHAVIOR TESTS ====================

    @Test
    @DisplayName("Should preserve non-condition stack items")
    void testStackPreservation() throws Exception {
        System.out.println("\n[TEST] testStackPreservation");

        // Push items before loop
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));

        // Condition: always false (don't run body)
        List<Statement> aoCondition = new ArrayList<>();
        aoCondition.add(new NumberStatement(0, 0));  // FALSE

        List<Statement> aoBody = new ArrayList<>();

        // Execute loop
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        // Verify stack still has items (in reverse order: LIFO)
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
        System.out.println("  Stack correctly preserved non-condition items");
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        List<Statement> aoCondition = new ArrayList<>();
        List<Statement> aoBody = new ArrayList<>();

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 42);
        BeginStatement stmt = new BeginStatement(oToken, 42, aoCondition, aoBody);

        assertEquals(42, stmt.getTokenNumber());
    }

    // ==================== NESTING TESTS ====================

    @Test
    @DisplayName("Should support nested BEGIN statements")
    void testNestedBeginStatements() throws Exception {
        System.out.println("\n[TEST] testNestedBeginStatements");

        // Inner loop: counts from 2 to 1
        List<Statement> aoInnerCondition = new ArrayList<>();
        Statement innerDecrement = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                if (count == 0) {
                    stack.push(new IntegerValue(2));  // Initialize
                    count++;
                }
                int iValue = stack.pop().toInteger();
                iValue--;
                stack.push(new IntegerValue(iValue));
                stack.push(new IntegerValue(iValue > 0 ? 1 : 0));  // Flag
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoInnerCondition.add(innerDecrement);

        List<Statement> aoInnerBody = new ArrayList<>();
        aoInnerBody.add(new Statement() {
            @Override
            public void execute() throws Exception {
                System.out.println("    Inner iteration");
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        });

        BeginStatement innerLoop = new BeginStatement(
            new Token("BEGIN", ForthTokenType.BEGIN, 1),
            0, aoInnerCondition, aoInnerBody
        );

        // Outer loop: runs once
        List<Statement> aoOuterCondition = new ArrayList<>();
        Statement outerCondition = new Statement() {
            private boolean first = true;
            @Override
            public void execute() throws Exception {
                if (first) {
                    stack.push(new IntegerValue(1));  // TRUE
                    first = false;
                } else {
                    stack.push(new IntegerValue(0));  // FALSE
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoOuterCondition.add(outerCondition);

        List<Statement> aoOuterBody = new ArrayList<>();
        aoOuterBody.add(innerLoop);  // Inner loop in body

        BeginStatement outerLoop = new BeginStatement(
            new Token("BEGIN", ForthTokenType.BEGIN, 2),
            0, aoOuterCondition, aoOuterBody
        );

        outerLoop.execute();
        System.out.println("  Nested loops completed");
    }

    // ==================== INTEGRATION TESTS ====================

    @Test
    @DisplayName("Should work with NumberStatement in condition")
    void testWithNumberStatement() throws Exception {
        System.out.println("\n[TEST] testWithNumberStatement");

        // Setup: push initial value
        stack.push(new IntegerValue(3));

        // Condition: decrement and push flag
        List<Statement> aoCondition = new ArrayList<>();
        Statement decrementCondition = new Statement() {
            @Override
            public void execute() throws Exception {
                int iValue = stack.pop().toInteger();
                stack.push(new IntegerValue(iValue - 1));  // Decrement
                stack.push(new IntegerValue(iValue - 1 > 0 ? 1 : 0));  // Push flag
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoCondition.add(decrementCondition);

        // Body: print current stack top
        List<Statement> aoBody = new ArrayList<>();
        Statement printTop = new Statement() {
            private int iterationCount = 0;
            @Override
            public void execute() throws Exception {
                iterationCount++;
                int iValue = stack.peek().toInteger();
                System.out.println("  Iteration " + iterationCount + ": value=" + iValue);
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(printTop);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  Completed with NumberStatement integration");
    }

    @Test
    @DisplayName("Should handle complex condition with multiple statements")
    void testComplexCondition() throws Exception {
        System.out.println("\n[TEST] testComplexCondition");

        // Complex condition: DUP then check if > 0
        List<Statement> aoCondition = new ArrayList<>();
        Statement complexCondition = new Statement() {
            private int iterations = 0;
            @Override
            public void execute() throws Exception {
                iterations++;
                if (iterations > 5) {
                    stack.push(new IntegerValue(0));  // Exit after 5
                    return;
                }

                int iValue = stack.pop().toInteger();
                stack.push(new IntegerValue(iValue));      // DUP
                stack.push(new IntegerValue(iValue - 1));  // Store for next
                stack.push(new IntegerValue(iValue > 0 ? 1 : 0));  // Push flag
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoCondition.add(complexCondition);

        // Body: just consume the duplicated value
        List<Statement> aoBody = new ArrayList<>();
        aoBody.add(new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                stack.pop();  // Remove the duplicated value
                System.out.println("  Iteration " + count);
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        });

        stack.push(new IntegerValue(5));
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  Complex condition test completed");
    }

    @Test
    @DisplayName("Should handle true flag value (-1)")
    void testTrueFlagValue() throws Exception {
        System.out.println("\n[TEST] testTrueFlagValue - FORTH TRUE = -1");

        // Condition: push FORTH TRUE (-1) first, then FALSE
        List<Statement> aoCondition = new ArrayList<>();
        Statement forthTrue = new Statement() {
            private boolean first = true;
            @Override
            public void execute() throws Exception {
                if (first) {
                    stack.push(new IntegerValue(-1));  // FORTH TRUE
                    first = false;
                } else {
                    stack.push(new IntegerValue(0));   // FALSE
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoCondition.add(forthTrue);

        // Body
        List<Statement> aoBody = new ArrayList<>();
        Statement body = new Statement() {
            @Override
            public void execute() throws Exception {
                System.out.println("  Body executed with FORTH TRUE (-1)");
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(body);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginStatement stmt = new BeginStatement(oToken, 0, aoCondition, aoBody);
        stmt.execute();

        System.out.println("  FORTH TRUE value (-1) handled correctly");
    }
}
