package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.InvalidLoopIndexException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.runtimeManager.LoopContext;
import eu.gricom.forth.runtimeManager.ReturnStack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CurrentLoopIndexStatementTest.java
 *
 * Test suite for CurrentLoopIndexStatement (I command).
 * Tests pushing current loop index to stack.
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("CurrentLoopIndexStatement (I) Tests")
class CurrentLoopIndexStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        ReturnStack.reset();
    }

    @Test
    @DisplayName("Should push current loop index to stack")
    void testPushCurrentIndex() throws Exception {
        // Setup: create loop context
        LoopContext oLoop = new LoopContext(5, 0, 1);
        ReturnStack.pushLoop(oLoop);

        // Execute I statement
        Token oToken = new Token("I", ForthTokenType.I, 1);
        CurrentLoopIndexStatement stmt = new CurrentLoopIndexStatement(oToken, 0);
        stmt.execute();

        // Verify index is on stack
        assertEquals(1, stack.size());
        assertEquals(0, stack.pop().toInteger());
    }

    @Test
    @DisplayName("Should push correct index during loop iteration")
    void testIndexDuringIteration() throws Exception {
        // Setup: create loop context and increment
        LoopContext oLoop = new LoopContext(5, 0, 1);
        ReturnStack.pushLoop(oLoop);

        oLoop.increment();
        oLoop.increment();

        // Execute I statement (should push index 2)
        Token oToken = new Token("I", ForthTokenType.I, 1);
        CurrentLoopIndexStatement stmt = new CurrentLoopIndexStatement(oToken, 0);
        stmt.execute();

        // Verify correct index is on stack
        assertEquals(2, stack.pop().toInteger());
    }

    @Test
    @DisplayName("Should throw exception when used outside loop")
    void testOutsideLoop() throws Exception {
        // Don't set up loop context
        Token oToken = new Token("I", ForthTokenType.I, 1);
        CurrentLoopIndexStatement stmt = new CurrentLoopIndexStatement(oToken, 0);

        // Should throw InvalidLoopIndexException
        assertThrows(InvalidLoopIndexException.class, stmt::execute);
    }

    @Test
    @DisplayName("Should work with nested loops (innermost)")
    void testNestedLoopInner() throws Exception {
        // Setup: two nested loops
        LoopContext oOuterLoop = new LoopContext(3, 0, 1);
        LoopContext oInnerLoop = new LoopContext(2, 1, 1);
        ReturnStack.pushLoop(oOuterLoop);
        ReturnStack.pushLoop(oInnerLoop);

        // Execute I statement (should access innermost loop)
        Token oToken = new Token("I", ForthTokenType.I, 1);
        CurrentLoopIndexStatement stmt = new CurrentLoopIndexStatement(oToken, 0);
        stmt.execute();

        // Verify we get inner loop index (1)
        assertEquals(1, stack.pop().toInteger());
    }

    @Test
    @DisplayName("Should preserve loop context after execution")
    void testPreservesLoopContext() throws Exception {
        // Setup loop
        LoopContext oLoop = new LoopContext(5, 2, 1);
        ReturnStack.pushLoop(oLoop);

        // Execute I statement
        Token oToken = new Token("I", ForthTokenType.I, 1);
        CurrentLoopIndexStatement stmt = new CurrentLoopIndexStatement(oToken, 0);
        stmt.execute();

        // Verify loop context still exists
        assertTrue(ReturnStack.isLoopActive());
        assertEquals(2, ReturnStack.peekLoop().getCurrentIndex());
    }
}
