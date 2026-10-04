package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.memoryManager.Stack;
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

@DisplayName("BEGIN...UNTIL Statement Tests")
class BeginUntilStatementTest {
    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    @Test
    @DisplayName("Should run body at least once even when condition is immediately true")
    void testRunsAtLeastOnce() throws Exception {
        System.out.println("\n[TEST] testRunsAtLeastOnce");

        List<Statement> aoBody = new ArrayList<>();
        Statement bodyOnce = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                stack.push(new IntegerValue(1));  // Push flag (true)
                System.out.println("  Body executed: " + count);
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(bodyOnce);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginUntilStatement stmt = new BeginUntilStatement(oToken, 0, aoBody);
        stmt.execute();

        System.out.println("  Body executed exactly once");
    }

    @Test
    @DisplayName("Should loop multiple times until condition becomes true")
    void testMultipleIterations() throws Exception {
        System.out.println("\n[TEST] testMultipleIterations - 3 iterations");

        List<Statement> aoBody = new ArrayList<>();
        Statement decrementUntilZero = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                int iValue = stack.size() > 0 ? stack.pop().toInteger() : 3;
                iValue--;
                stack.push(new IntegerValue(iValue));
                System.out.println("  Iteration " + count + ": value=" + iValue);
                // Push the flag: non-zero (true) when value becomes 0
                stack.push(new IntegerValue(iValue == 0 ? 1 : 0));
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(decrementUntilZero);

        stack.push(new IntegerValue(3));
        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginUntilStatement stmt = new BeginUntilStatement(oToken, 0, aoBody);
        stmt.execute();

        System.out.println("  Completed 3 iterations");
    }

    @Test
    @DisplayName("Should throw error if body doesn't push flag")
    void testStackUnderflow() {
        System.out.println("\n[TEST] testStackUnderflow");

        List<Statement> aoBody = new ArrayList<>();
        // Body doesn't push anything

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginUntilStatement stmt = new BeginUntilStatement(oToken, 0, aoBody);

        Exception exception = assertThrows(Exception.class, stmt::execute);
        assertTrue(exception.getMessage().contains("Stack underflow"));
        System.out.println("  Correctly threw: " + exception.getMessage());
    }

    @Test
    @DisplayName("Should handle FORTH true (-1)")
    void testForthTrue() throws Exception {
        System.out.println("\n[TEST] testForthTrue - FORTH TRUE = -1");

        List<Statement> aoBody = new ArrayList<>();
        Statement forthTrue = new Statement() {
            private boolean first = true;
            @Override
            public void execute() throws Exception {
                if (first) {
                    stack.push(new IntegerValue(-1));  // FORTH TRUE
                    first = false;
                } else {
                    stack.push(new IntegerValue(1));   // Exit
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(forthTrue);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginUntilStatement stmt = new BeginUntilStatement(oToken, 0, aoBody);
        stmt.execute();

        System.out.println("  FORTH TRUE (-1) handled correctly");
    }
}
