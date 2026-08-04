package eu.gricom.forth.statements.stack;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.RealValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DepthStatement (DEPTH) - get stack depth.
 * Stack effect: ( -- depth )
 * Pushes the current stack depth onto the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("DEPTH (Get Stack Depth) Stack Operation Tests")
class DepthStatementTest {

    private Stack stack;
    private DepthStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new DepthStatement(ForthTokenType.DEPTH, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        DepthStatement stmt = new DepthStatement(ForthTokenType.DEPTH, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        DepthStatement stmt = new DepthStatement(ForthTokenType.DEPTH, 123);
        assertEquals(123, stmt.getTokenNumber(), "Token number should be 123");
    }

    @Test
    @DisplayName("content() should return empty string")
    void testContent() throws Exception {
        assertEquals("", statement.content(), "Content should be empty string");
    }

    @Test
    @DisplayName("structure() should return empty string")
    void testStructure() throws Exception {
        assertEquals("", statement.structure(), "Structure should be empty string");
    }

    // ============================================================================
    // Basic DEPTH Operation Tests
    // ============================================================================

    @Test
    @DisplayName("DEPTH on empty stack pushes 0")
    void testDepthEmptyStack() throws Exception {
        statement.execute();

        assertEquals(1, stack.size(), "Stack should have 1 item (the depth value)");
        assertEquals(0, stack.pop().toInteger(), "Depth should be 0");
    }

    @Test
    @DisplayName("DEPTH with one item pushes 1")
    void testDepthSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();

        assertEquals(2, stack.size(), "Stack should have 2 items");
        assertEquals(1, stack.pop().toInteger(), "Depth should be 1");
        assertEquals(5, stack.pop().toInteger(), "Original value should be preserved");
    }

    @Test
    @DisplayName("DEPTH with two items pushes 2")
    void testDepthTwoItems() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger(), "Depth should be 2");
        assertEquals(20, stack.pop().toInteger());
        assertEquals(10, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DEPTH with three items pushes 3")
    void testDepthThreeItems() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(3, stack.pop().toInteger(), "Depth should be 3");
    }

    @Test
    @DisplayName("DEPTH increases stack size by 1")
    void testDepthIncreasesSizeByOne() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    @DisplayName("DEPTH preserves all items on stack")
    void testDepthPreservesItems() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        statement.execute();

        assertEquals(4, stack.size());
        int depth = stack.pop().toInteger();
        assertEquals(3, depth);
        assertEquals(300, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // DEPTH with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("DEPTH with RealValue items")
    void testDepthRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DEPTH with StringValue items")
    void testDepthStringValues() throws Exception {
        stack.push(new StringValue("hello"));
        stack.push(new StringValue("world"));
        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DEPTH with mixed value types")
    void testDepthMixedTypes() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new RealValue(3.14));
        stack.push(new StringValue("test"));
        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(3, stack.pop().toInteger());
    }

    // ============================================================================
    // Sequential DEPTH Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple DEPTHs track growing stack")
    void testMultipleDepths() throws Exception {
        statement.execute();  // Empty stack
        assertEquals(0, stack.pop().toInteger());

        stack.push(new IntegerValue(1));
        statement.execute();
        assertEquals(1, stack.pop().toInteger());

        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        statement.execute();
        assertEquals(3, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DEPTH after DEPTH")
    void testDepthAfterDepth() throws Exception {
        stack.push(new IntegerValue(100));
        statement.execute();  // Stack: [100, 1]

        statement.execute();  // Stack: [100, 1, 2]

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("DEPTH always works even on empty stack")
    void testDepthAlwaysWorks() throws Exception {
        for (int i = 0; i < 3; i++) {
            statement.execute();
        }

        // Stack should have depth values 0, 1, 2
        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
        assertEquals(1, stack.pop().toInteger());
        assertEquals(0, stack.pop().toInteger());
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("DEPTH with large stack")
    void testDepthLargeStack() throws Exception {
        for (int i = 0; i < 100; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(101, stack.size());
        assertEquals(100, stack.pop().toInteger(), "Depth should be 100");
    }

    @Test
    @DisplayName("DEPTH after multiple operations")
    void testDepthAfterOperations() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        // Stack: [1, 2, 3]

        DupeStatement dup = new DupeStatement(ForthTokenType.DUPE, 1);
        dup.execute();
        // Stack: [1, 2, 3, 3]

        statement.execute();

        assertEquals(5, stack.size());
        assertEquals(4, stack.pop().toInteger(), "Depth should be 4");
    }

    @Test
    @DisplayName("DEPTH with DROP operations")
    void testDepthWithDrops() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }

        DropStatement drop = new DropStatement(ForthTokenType.DROP, 1);
        drop.execute();  // Remove top
        // Stack: [0, 1, 2, 3]

        statement.execute();

        assertEquals(5, stack.size());
        assertEquals(4, stack.pop().toInteger(), "Depth should be 4");
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("DEPTH with very large number of items")
    void testDepthVeryLargeStack() throws Exception {
        int itemCount = 1000;
        for (int i = 0; i < itemCount; i++) {
            stack.push(new IntegerValue(i));
        }

        statement.execute();

        assertEquals(itemCount + 1, stack.size());
        assertEquals(itemCount, stack.pop().toInteger());
    }

    @Test
    @DisplayName("DEPTH is non-destructive")
    void testDepthNonDestructive() throws Exception {
        stack.push(new IntegerValue(42));
        statement.execute();

        assertEquals(2, stack.size());
        int depth = stack.pop().toInteger();
        assertEquals(1, depth);

        // Original value still there
        assertEquals(42, stack.pop().toInteger());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("DEPTH returns correct count after SWAP")
    void testDepthAfterSwap() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));

        SwapStatement swap = new SwapStatement(ForthTokenType.SWAP, 1);
        swap.execute();

        statement.execute();

        assertEquals(3, stack.size());
        assertEquals(2, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with DEPTH token type")
    void testWithDepthTokenType() {
        DepthStatement stmt = new DepthStatement(ForthTokenType.DEPTH, 1);
        assertNotNull(stmt, "Statement should be created with DEPTH token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            DepthStatement stmt = new DepthStatement(ForthTokenType.DEPTH, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    @DisplayName("DEPTH can be used for conditional logic")
    void testDepthForConditional() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));

        statement.execute();

        // Top of stack is now the depth (2)
        int depth = stack.peek().toInteger();
        assertEquals(2, depth);

        // Can use this for conditional operations
        if (depth > 1) {
            DropStatement drop = new DropStatement(ForthTokenType.DROP, 1);
            drop.execute();  // Remove the depth value
        }

        assertEquals(2, stack.size());
    }
}
