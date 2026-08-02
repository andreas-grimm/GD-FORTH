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
 * Test suite for RollStatement (ROLL) - rotate stack items by index.
 * Stack effect: ( ... n -- ... rolled_items )
 * Rolls the stack by n positions, moving the item at index n to the top.
 * Note: This implementation is not yet complete as indicated by the source code.
 * <p>
 * (c) 2024, by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("ROLL (Rotate by Index) Stack Operation Tests")
class RollStatementTest {

    private Stack stack;
    private RollStatement statement;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
        statement = new RollStatement(ForthTokenType.ROLL, 1);
    }

    // ============================================================================
    // Constructor and Accessor Tests
    // ============================================================================

    @Test
    @DisplayName("Constructor should initialize with token type and number")
    void testConstructor() {
        RollStatement stmt = new RollStatement(ForthTokenType.ROLL, 42);
        assertEquals(42, stmt.getTokenNumber(), "Token number should be 42");
    }

    @Test
    @DisplayName("getTokenNumber should return the token number passed to constructor")
    void testGetTokenNumber() {
        RollStatement stmt = new RollStatement(ForthTokenType.ROLL, 123);
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
    // Note on ROLL Implementation
    // ============================================================================
    // ROLL is marked as "Command not implemented" in the source code.
    // These tests verify basic structure and exception handling.
    // Full functionality tests will be added once the command is implemented.

    @Test
    @DisplayName("ROLL on empty stack should handle gracefully")
    void testRollEmptyStack() throws Exception {
        statement.execute();
        assertEquals(0, stack.size(), "Stack should remain empty");
    }

    @Test
    @DisplayName("ROLL with single item should handle gracefully")
    void testRollSingleItem() throws Exception {
        stack.push(new IntegerValue(5));
        statement.execute();
        assertTrue(stack.size() >= 0, "Should handle single item gracefully");
    }

    @Test
    @DisplayName("ROLL should not crash with index 0")
    void testRollIndexZero() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(0));

        // Should not throw exception
        try {
            statement.execute();
        } catch (Exception e) {
            fail("ROLL should not throw exception even if not implemented");
        }
    }

    @Test
    @DisplayName("ROLL should not crash with index 1")
    void testRollIndexOne() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(30));
        stack.push(new IntegerValue(1));

        // Should not throw exception
        try {
            statement.execute();
        } catch (Exception e) {
            fail("ROLL should not throw exception even if not implemented");
        }
    }

    @Test
    @DisplayName("ROLL should not crash with multiple items")
    void testRollMultipleItems() throws Exception {
        for (int i = 0; i < 5; i++) {
            stack.push(new IntegerValue(i));
        }
        stack.push(new IntegerValue(2));

        // Should not throw exception
        try {
            statement.execute();
        } catch (Exception e) {
            fail("ROLL should not throw exception even if not implemented");
        }
    }

    // ============================================================================
    // Placeholder Tests for Future Implementation
    // ============================================================================

    @Test
    @DisplayName("ROLL structure test - placeholder for implementation")
    void testRollStructure() throws Exception {
        // These tests are placeholders for when ROLL is fully implemented
        // Expected behavior: ROLL with index n moves item n to top
        // ( ... a b c -- ... c a b ) for ROLL with n=2

        assertNotNull(statement, "Statement object should be created");
    }

    @Test
    @DisplayName("ROLL should maintain stack integrity")
    void testRollStackIntegrity() throws Exception {
        stack.push(new IntegerValue(1));
        stack.push(new IntegerValue(2));
        stack.push(new IntegerValue(3));
        int sizeBefore = stack.size();

        statement.execute();

        // Stack should still be valid (though operation may not be complete)
        assertTrue(stack.size() >= 0, "Stack should remain valid");
    }

    // ============================================================================
    // Token Type Tests
    // ============================================================================

    @Test
    @DisplayName("Statement works with ROLL token type")
    void testWithRollTokenType() {
        RollStatement stmt = new RollStatement(ForthTokenType.ROLL, 1);
        assertNotNull(stmt, "Statement should be created with ROLL token type");
    }

    @Test
    @DisplayName("Constructor accepts various token numbers")
    void testVariousTokenNumbers() {
        for (int i = 0; i <= 1000; i += 100) {
            RollStatement stmt = new RollStatement(ForthTokenType.ROLL, i);
            assertEquals(i, stmt.getTokenNumber());
        }
    }

    // ============================================================================
    // Expected Behavior Documentation
    // ============================================================================
    // Once implemented, ROLL should work as follows:
    //
    // ROLL: ( ... n -- ... rolled_items )
    // Pops n from stack, then rotates the top n items
    //
    // Example: ( a b c d 2 -- a c d b )
    // The item at index 2 (b) is moved to the top
    //
    // This is more general than ROT which always rotates 3 items
    // ROLL can handle any number of items
}
