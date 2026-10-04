package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.LeaveException;
import eu.gricom.forth.runtimeManager.LoopContext;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BEGIN...AGAIN Statement Tests")
class BeginAgainStatementTest {

    @BeforeEach
    void setUp() {
        // Ensure clean loop stack
        while (ReturnStack.isLoopActive()) {
            ReturnStack.popLoop();
        }
    }

    @Test
    @DisplayName("Should loop forever until exception")
    void testInfiniteLoopWithException() {
        System.out.println("\n[TEST] testInfiniteLoopWithException");

        List<Statement> aoBody = new ArrayList<>();
        Statement counter = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                System.out.println("  Iteration " + count);
                if (count >= 3) {
                    throw new LeaveException();  // Simulate LEAVE
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(counter);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginAgainStatement stmt = new BeginAgainStatement(oToken, 0, aoBody);

        // AGAIN doesn't catch LeaveException, so it propagates
        assertThrows(LeaveException.class, stmt::execute);
        System.out.println("  LeaveException propagated as expected");
    }

    @Test
    @DisplayName("Should exit when LEAVE inside enclosing DO")
    void testLeaveInsideEnclosingDo() throws Exception {
        System.out.println("\n[TEST] testLeaveInsideEnclosingDo");

        // Create nested: DO BEGIN...AGAIN...LEAVE LOOP
        LoopContext oLoopContext = new LoopContext(10, 0, 1);
        ReturnStack.pushLoop(oLoopContext);

        List<Statement> aoBody = new ArrayList<>();
        Statement leaveAfter2 = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                System.out.println("  Iteration " + count);
                if (count >= 2) {
                    throw new LeaveException();
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoBody.add(leaveAfter2);

        Token oToken = new Token("BEGIN", ForthTokenType.BEGIN, 1);
        BeginAgainStatement stmt = new BeginAgainStatement(oToken, 0, aoBody);

        // When LEAVE throws, it propagates out to enclosing DO
        assertThrows(LeaveException.class, stmt::execute);
        System.out.println("  LEAVE exception propagated to enclosing DO");

        // Clean up
        while (ReturnStack.isLoopActive()) {
            ReturnStack.popLoop();
        }
    }

    @Test
    @DisplayName("Should support nested AGAIN")
    void testNestedAgain() {
        System.out.println("\n[TEST] testNestedAgain");

        List<Statement> aoInnerBody = new ArrayList<>();
        Statement innerCounter = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                System.out.println("    Inner: " + count);
                if (count >= 2) {
                    throw new LeaveException();
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoInnerBody.add(innerCounter);

        BeginAgainStatement innerAgain = new BeginAgainStatement(
            new Token("BEGIN", ForthTokenType.BEGIN, 1),
            0, aoInnerBody
        );

        List<Statement> aoOuterBody = new ArrayList<>();
        Statement outerCounter = new Statement() {
            private int count = 0;
            @Override
            public void execute() throws Exception {
                count++;
                System.out.println("  Outer: " + count);
                if (count >= 2) {
                    throw new LeaveException();
                }
            }
            @Override
            public int getTokenNumber() { return 0; }
            @Override
            public String content() throws Exception { return ""; }
            @Override
            public String structure() throws Exception { return ""; }
        };
        aoOuterBody.add(outerCounter);
        aoOuterBody.add(innerAgain);

        BeginAgainStatement outerAgain = new BeginAgainStatement(
            new Token("BEGIN", ForthTokenType.BEGIN, 2),
            0, aoOuterBody
        );

        // Should throw when inner counter reaches 2, then exit outer
        assertThrows(LeaveException.class, outerAgain::execute);
        System.out.println("  Nested AGAIN handled correctly");
    }
}
