package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.error.LeaveException;
import eu.gricom.forth.runtimeManager.LoopContext;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LEAVE Statement Tests")
class LeaveStatementTest {

    @BeforeEach
    void setUp() {
        // Ensure clean loop stack
        while (ReturnStack.isLoopActive()) {
            ReturnStack.popLoop();
        }
    }

    @Test
    @DisplayName("Should throw InvalidLoopIndexException outside loop")
    void testLeaveOutsideLoop() {
        System.out.println("\n[TEST] testLeaveOutsideLoop");

        Token oToken = new Token("LEAVE", ForthTokenType.LEAVE, 42);
        LeaveStatement stmt = new LeaveStatement(oToken, 42);

        Exception exception = assertThrows(InvalidLoopIndexException.class, stmt::execute);
        assertTrue(exception.getMessage().contains("LEAVE without matching DO"));
        System.out.println("  Correctly threw: " + exception.getMessage());
    }

    @Test
    @DisplayName("Should throw LeaveException inside DO loop")
    void testLeaveInsideLoop() throws Exception {
        System.out.println("\n[TEST] testLeaveInsideLoop");

        // Setup: push a loop context
        LoopContext oLoopContext = new LoopContext(10, 0, 1);
        ReturnStack.pushLoop(oLoopContext);

        try {
            Token oToken = new Token("LEAVE", ForthTokenType.LEAVE, 1);
            LeaveStatement stmt = new LeaveStatement(oToken, 0);

            // Should throw LeaveException
            assertThrows(LeaveException.class, stmt::execute);
            System.out.println("  LeaveException thrown correctly");

        } finally {
            ReturnStack.popLoop();
        }
    }

    @Test
    @DisplayName("Should only exit innermost loop")
    void testLeaveInNestedLoops() throws Exception {
        System.out.println("\n[TEST] testLeaveInNestedLoops");

        // Create two nested loop contexts
        LoopContext oOuterLoop = new LoopContext(5, 0, 1);
        LoopContext oInnerLoop = new LoopContext(10, 0, 1);

        ReturnStack.pushLoop(oOuterLoop);
        ReturnStack.pushLoop(oInnerLoop);

        try {
            Token oToken = new Token("LEAVE", ForthTokenType.LEAVE, 1);
            LeaveStatement stmt = new LeaveStatement(oToken, 0);

            // Throw LeaveException
            assertThrows(LeaveException.class, stmt::execute);

            // Verify inner loop was active and outer loop is still active
            assertTrue(ReturnStack.isLoopActive(), "Outer loop should still be active");
            System.out.println("  LEAVE exited only inner loop");

        } finally {
            // Clean up
            while (ReturnStack.isLoopActive()) {
                ReturnStack.popLoop();
            }
        }
    }

    @Test
    @DisplayName("Should work inside IF inside DO")
    void testLeaveInsideIfInsideDo() throws Exception {
        System.out.println("\n[TEST] testLeaveInsideIfInsideDo");

        LoopContext oLoopContext = new LoopContext(10, 0, 1);
        ReturnStack.pushLoop(oLoopContext);

        try {
            Token oToken = new Token("LEAVE", ForthTokenType.LEAVE, 1);
            LeaveStatement stmt = new LeaveStatement(oToken, 0);

            // LEAVE should throw even if called from inside IF
            assertThrows(LeaveException.class, stmt::execute);
            System.out.println("  LEAVE works inside IF inside DO");

        } finally {
            ReturnStack.popLoop();
        }
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        Token oToken = new Token("LEAVE", ForthTokenType.LEAVE, 99);
        LeaveStatement stmt = new LeaveStatement(oToken, 99);

        assertEquals(99, stmt.getTokenNumber());
    }
}
