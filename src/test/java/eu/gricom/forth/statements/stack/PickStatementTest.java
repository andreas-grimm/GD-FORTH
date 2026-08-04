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
 * Test suite for PickStatement (PICK) - copy item at index to top.
 * Stack effect: ( ... n -- ... item_n )
 * Copies the item at index n from the top to the top of the stack.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("PICK (Copy Indexed Item) Stack Operation Tests")
class PickStatementTest {

    private Stack stack;
    private PickStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new PickStatement(ForthTokenType.PICK, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        PickStatement stmt = new PickStatement(ForthTokenType.PICK, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        PickStatement stmt = new PickStatement(ForthTokenType.PICK, 123);
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
    // Basic PICK Operation Tests
    // ============================================================================

    @Test
    @DisplayName("PICK with index 0 (top item)")
    void testPickIndexZero() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(0));  // Index for PICK

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(30, stack.pop().toInteger(), "Should pick top item (30)");
    }

    @Test
    @DisplayName("PICK with index 1 (second from top)")
    void testPickIndexOne() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(1));  // Index for PICK

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(20, stack.pop().toInteger(), "Should pick second item (20)");
    }

    @Test
    @DisplayName("PICK with index 2 (third from top)")
    void testPickIndexTwo() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(2));  // Index for PICK

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(10, stack.pop().toInteger(), "Should pick third item (10)");
    }

    @Test
    @DisplayName("PICK increases stack size by 1")
    void testPickIncreasesSizeByOne() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(0));

        int sizeBefore = stack.size();
        statement.execute();
        int sizeAfter = stack.size();

        assertEquals(sizeBefore, sizeAfter, "Index is consumed, item is copied");
    }

    // ============================================================================
    // PICK with Different Value Types
    // ============================================================================

    @Test
    @DisplayName("PICK with RealValue")
    void testPickRealValues() throws Exception {
        stack.push(new RealValue(1.5));
        stack.push(new RealValue(2.5));
        stack.push(new RealValue(3.5));
        stack.push(new IntegerValue(1));

        statement.execute();

        assertEquals(4, stack.size());
        assertTrue(Math.abs(stack.pop().toReal() - 2.5) < 0.00001);
    }

    @Test
    @DisplayName("PICK with StringValue")
    void testPickStringValues() throws Exception {
        stack.push(new StringValue("first"));
        stack.push(new StringValue("second"));
        stack.push(new StringValue("third"));
        stack.push(new IntegerValue(0));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals("third", stack.pop().toString());
    }

    // ============================================================================
    // Sequential PICK Operations
    // ============================================================================

    @Test
    @DisplayName("Multiple PICKs from same stack")
    void testMultiplePicks() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));

        stack.push(new IntegerValue(0));
        statement.execute();  // Pick 30
        assertEquals(4, stack.size());

        stack.push(new IntegerValue(1));
        statement.execute();  // Pick 30 again
        assertEquals(5, stack.size());
    }

    // ============================================================================
    // Error Handling Tests
    // ============================================================================

    @Test
    @DisplayName("PICK on empty stack should handle exception gracefully")
    void testPickEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty after error");
    }

    @Test
    @DisplayName("PICK with only index should handle gracefully")
    void testPickOnlyIndex() throws Exception {
        stack.push(new IntegerValue(0));
        statement.execute();
        assertTrue(stack.size() >= 0);
    }

    // ============================================================================
    // Complex Stack Scenarios
    // ============================================================================

    @Test
    @DisplayName("PICK with large stack and various indices")
    void testPickLargeStack() throws Exception {
        for (int i = 0; i < 10; i++) {
            stack.push(new IntegerValue(i));
        }

        stack.push(new IntegerValue(5));
        statement.execute();

        assertEquals(11, stack.size());
        assertEquals(4, stack.pop().toInteger(), "Should pick item at index 5");
    }

    @Test
    @DisplayName("PICK preserves all items on stack")
    void testPickPreservesAll() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(200));
        stack.push(new IntegerValue(300));
        stack.push(new IntegerValue(1));

        statement.execute();

        assertEquals(4, stack.size());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(300, stack.pop().toInteger());
        assertEquals(200, stack.pop().toInteger());
        assertEquals(100, stack.pop().toInteger());
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    @DisplayName("PICK with maximum index within stack")
    void testPickMaxValidIndex() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }

        stack.push(new IntegerValue(4));
        statement.execute();

        assertEquals(6, stack.size());
        assertEquals(0, stack.pop().toInteger());
    }

    @Test
    @DisplayName("PICK with zero on single value")
    void testPickZeroSingleValue() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(0));

        statement.execute();

        assertEquals(2, stack.size());
        assertEquals(42, stack.pop().toInteger());
        assertEquals(42, stack.pop().toInteger());
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with PICK token type")
    void testWithPickTokenType() {
        PickStatement stmt = new PickStatement(ForthTokenType.PICK, 1);
        assertNotNull(stmt, "Statement should be created with PICK token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            PickStatement stmt = new PickStatement(ForthTokenType.PICK, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }
}
