package eu.gricom.forth.statements;

import eu.gricom.forth.error.EmptyStackException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StackStatement class.
 * Tests all FORTH stack operations according to the FORTH standard.
 * Stack notation: top of stack is on the right, e.g., ( a b c -- b a c ) means:
 * Before: [a, b, c] with c on top
 * After: [b, a, c] with c on top
 */
public class StackStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ============================================================================
    // DUP Tests ( a -- a a )
    // ============================================================================

    @Test
    void testDupSingleValue() throws Exception {
        stack.push(new IntegerValue(5));

        StackStatement stmt = new StackStatement(ForthTokenType.DUPE, 1);
        stmt.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items after DUP");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Top should be 5");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Second should be 5");
    }

    @Test
    void testDupMultipleValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        StackStatement stmt = new StackStatement(ForthTokenType.DUPE, 1);
        stmt.execute();

        assertEquals(3, stack.size(), "Stack should have 3 items");
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger(), "Top should be 20");
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger(), "Second should be 20");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Third should be 10");
    }

    // ============================================================================
    // ?DUP Tests ( a -- a a | a -- a based on value )
    // ============================================================================

    @Test
    void testQuestionDupNonZero() throws Exception {
        stack.push(new IntegerValue(5));

        StackStatement stmt = new StackStatement(ForthTokenType.QUESTION_DUPE, 1);
        stmt.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items when value is non-zero");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    void testQuestionDupZero() throws Exception {
        stack.push(new IntegerValue(0));

        StackStatement stmt = new StackStatement(ForthTokenType.QUESTION_DUPE, 1);
        stmt.execute();

        assertEquals(0, stack.size(), "Stack should be empty when value is zero (?DUP pops but doesn't push)");
    }

    @Test
    void testQuestionDupNegative() throws Exception {
        stack.push(new IntegerValue(-5));

        StackStatement stmt = new StackStatement(ForthTokenType.QUESTION_DUPE, 1);
        stmt.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items for negative value");
        assertEquals(-5, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(-5, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // DROP Tests ( a -- )
    // ============================================================================

    @Test
    void testDropSingleValue() throws Exception {
        stack.push(new IntegerValue(5));

        StackStatement stmt = new StackStatement(ForthTokenType.DROP, 1);
        stmt.execute();

        assertEquals(0, stack.size(), "Stack should be empty after DROP");
    }

    @Test
    void testDropMultipleValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        StackStatement stmt = new StackStatement(ForthTokenType.DROP, 1);
        stmt.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item after DROP");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Remaining value should be 10");
    }

    // ============================================================================
    // 2DROP Tests ( a b -- )
    // ============================================================================

    @Test
    void testTwoDropTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(6));

        StackStatement stmt = new StackStatement(ForthTokenType.TWO_DROP, 1);
        stmt.execute();

        assertEquals(0, stack.size(), "Stack should be empty after 2DROP");
    }

    @Test
    void testTwoDropFromLargeStack() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));

        StackStatement stmt = new StackStatement(ForthTokenType.TWO_DROP, 1);
        stmt.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item after 2DROP");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // SWAP Tests ( a b -- b a )
    // ============================================================================

    @Test
    void testSwapTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        StackStatement stmt = new StackStatement(ForthTokenType.SWAP, 1);
        stmt.execute();

        assertEquals(2, stack.size(), "Stack should still have 2 items");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Top should now be 5");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Second should be 10");
    }

    @Test
    void testSwapMultipleValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.SWAP, 1);
        stmt.execute();

        assertEquals(3, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger(), "Top should be 2");
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger(), "Second should be 3");
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger(), "Third should be 1");
    }

    // ============================================================================
    // 2SWAP Tests ( a b c d -- c d a b )
    // ============================================================================

    @Test
    void testTwoSwapFourValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        StackStatement stmt = new StackStatement(ForthTokenType.TWO_SWAP, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(4, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // OVER Tests ( a b -- a b a )
    // ============================================================================

    @Test
    void testOverTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        StackStatement stmt = new StackStatement(ForthTokenType.OVER, 1);
        stmt.execute();

        assertEquals(3, stack.size(), "Stack should have 3 items");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Top should be 5");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Second should be 10");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Third should be 5");
    }

    @Test
    void testOverMultipleValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.OVER, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // 2OVER Tests ( a b c d -- a b c d a b )
    // ============================================================================

    @Test
    void testTwoOverFourValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        StackStatement stmt = new StackStatement(ForthTokenType.TWO_OVER, 1);
        stmt.execute();

        assertEquals(6, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(4, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // ROT Tests ( a b c -- b c a )
    // ============================================================================

    @Test
    void testRotThreeValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.ROT, 1);
        stmt.execute();

        assertEquals(3, stack.size());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger(), "Top should be 1");
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger(), "Second should be 3");
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger(), "Third should be 2");
    }

    @Test
    void testRotMultipleValues() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(40));

        StackStatement stmt = new StackStatement(ForthTokenType.ROT, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger(), "Top should be 20");
        assertEquals(40, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(30, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // 2ROT Tests ( a b c d e f -- c d e f a b )
    // ============================================================================

    @Test
    void testTwoRotSixValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(6));

        StackStatement stmt = new StackStatement(ForthTokenType.TWO_ROT, 1);
        stmt.execute();

        assertEquals(6, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(6, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(4, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // -ROT Tests ( a b c -- c a b )
    // ============================================================================

    @Test
    void testMinusRotThreeValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.MINUS_ROT, 1);
        stmt.execute();

        assertEquals(3, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger(), "Top should be 2");
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger(), "Second should be 1");
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger(), "Third should be 3");
    }

    // ============================================================================
    // NIP Tests ( a b -- b )
    // ============================================================================

    @Test
    void testNipTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        StackStatement stmt = new StackStatement(ForthTokenType.NIP, 1);
        stmt.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Remaining value should be 10");
    }

    @Test
    void testNipMultipleValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.NIP, 1);
        stmt.execute();

        assertEquals(2, stack.size());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // TUCK Tests ( a b -- b a b )
    // ============================================================================

    @Test
    void testTuckTwoValues() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        StackStatement stmt = new StackStatement(ForthTokenType.TUCK, 1);
        stmt.execute();

        assertEquals(3, stack.size());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Top should be 10 (b)");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Second should be 5 (a)");
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Third should be 10 (b)");
    }

    @Test
    void testTuckMultipleValues() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement stmt = new StackStatement(ForthTokenType.TUCK, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger(), "Top should be 3 (b)");
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger(), "Second should be 2 (a)");
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger(), "Third should be 3 (b)");
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger(), "Fourth should be 1");
    }

    // ============================================================================
    // PICK Tests ( ... n -- ... item )
    // ============================================================================

    @Test
    void testPickFirstItem() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(0)); // PICK index 0 (top)

        StackStatement stmt = new StackStatement(ForthTokenType.PICK, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(30, ((IntegerValue) stack.pop()).toInteger(), "Picked item should be 30");
    }

    @Test
    void testPickSecondItem() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(1)); // PICK index 1 (second from top)

        StackStatement stmt = new StackStatement(ForthTokenType.PICK, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger(), "Picked item should be 20");
    }

    @Test
    void testPickThirdItem() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(2)); // PICK index 2 (third from top)

        StackStatement stmt = new StackStatement(ForthTokenType.PICK, 1);
        stmt.execute();

        assertEquals(4, stack.size());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger(), "Picked item should be 10");
    }

    // ============================================================================
    // DEPTH Tests ( ... -- ... n )
    // ============================================================================

    @Test
    void testDepthEmptyStack() throws Exception {
        StackStatement stmt = new StackStatement(ForthTokenType.DEPTH, 1);
        stmt.execute();

        assertEquals(1, stack.size());
        assertEquals(0, ((IntegerValue) stack.pop()).toInteger(), "Depth should be 0");
    }

    @Test
    void testDepthSingleItem() throws Exception {
        stack.push(new IntegerValue(5));

        StackStatement stmt = new StackStatement(ForthTokenType.DEPTH, 1);
        stmt.execute();

        assertEquals(2, stack.size());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger(), "Depth should be 1");
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger(), "Original value should be preserved");
    }

    @Test
    void testDepthMultipleItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(4));

        StackStatement stmt = new StackStatement(ForthTokenType.DEPTH, 1);
        stmt.execute();

        assertEquals(5, stack.size());
        assertEquals(4, ((IntegerValue) stack.pop()).toInteger(), "Depth should be 4");
        assertEquals(4, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    // ============================================================================
    // Token Number Tests
    // ============================================================================

    @Test
    void testGetTokenNumber() throws Exception {
        StackStatement stmt = new StackStatement(ForthTokenType.DUPE, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    void testGetTokenNumberZero() throws Exception {
        StackStatement stmt = new StackStatement(ForthTokenType.DROP, 0);
        assertEquals(0, stmt.getTokenNumber(), "Token number should be 0");
    }

    // ============================================================================
    // Content and Structure Tests
    // ============================================================================

    @Test
    void testContent() throws Exception {
        StackStatement stmt = new StackStatement(ForthTokenType.DUPE, 1);
        assertEquals("", stmt.content(), "Content should be empty string");
    }

    @Test
    void testStructure() throws Exception {
        StackStatement stmt = new StackStatement(ForthTokenType.DUPE, 1);
        assertEquals("", stmt.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Combination Tests (Testing realistic FORTH stack sequences)
    // ============================================================================

    @Test
    void testDupThenSwap() throws Exception {
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(10));

        StackStatement dup = new StackStatement(ForthTokenType.DUPE, 1);
        dup.execute(); // Stack: [5, 10, 10]

        StackStatement swap = new StackStatement(ForthTokenType.SWAP, 2);
        swap.execute(); // Stack: [5, 10, 10] -> [5, 10, 10] (swap top two 10s)

        assertEquals(3, stack.size());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(5, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    void testOverThenDrop() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));

        StackStatement over = new StackStatement(ForthTokenType.OVER, 1);
        over.execute(); // Stack: [1, 2, 3, 2]

        assertEquals(4, stack.size());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    void testDepthWithOperations() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        StackStatement depth = new StackStatement(ForthTokenType.DEPTH, 1);
        depth.execute(); // Stack: [10, 20, 2]

        StackStatement drop = new StackStatement(ForthTokenType.DROP, 2);
        drop.execute(); // Stack: [10, 20]

        assertEquals(2, stack.size());
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
    }
}
