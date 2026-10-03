package eu.gricom.forth.runtimeManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoopContextTest.java
 *
 * Comprehensive test suite for LoopContext class.
 * Tests loop state management, index incrementation, and completion conditions.
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
@DisplayName("LoopContext Tests")
class LoopContextTest {

    // ================= CONSTRUCTOR & BASIC INITIALIZATION =================

    @Test
    @DisplayName("Should create LoopContext with correct initial values")
    void testConstructor() {
        LoopContext oLoop = new LoopContext(5, 0, 1);

        assertEquals(0, oLoop.getCurrentIndex());
        assertEquals(5, oLoop.getLimit());
        assertEquals(1, oLoop.getStepSize());
    }

    @Test
    @DisplayName("Should initialize with negative index")
    void testConstructorNegativeIndex() {
        LoopContext oLoop = new LoopContext(10, -5, 1);

        assertEquals(-5, oLoop.getCurrentIndex());
        assertEquals(10, oLoop.getLimit());
    }

    @Test
    @DisplayName("Should initialize with negative step size")
    void testConstructorNegativeStep() {
        LoopContext oLoop = new LoopContext(0, 10, -1);

        assertEquals(10, oLoop.getCurrentIndex());
        assertEquals(0, oLoop.getLimit());
        assertEquals(-1, oLoop.getStepSize());
    }

    // ================= INCREMENT BEHAVIOR =================

    @Test
    @DisplayName("Should increment index by step size")
    void testIncrement() {
        LoopContext oLoop = new LoopContext(5, 0, 1);

        oLoop.increment();
        assertEquals(1, oLoop.getCurrentIndex());

        oLoop.increment();
        assertEquals(2, oLoop.getCurrentIndex());
    }

    @Test
    @DisplayName("Should increment by custom step size")
    void testIncrementCustomStep() {
        LoopContext oLoop = new LoopContext(10, 0, 2);

        oLoop.increment();
        assertEquals(2, oLoop.getCurrentIndex());

        oLoop.increment();
        assertEquals(4, oLoop.getCurrentIndex());
    }

    @Test
    @DisplayName("Should increment by negative step size")
    void testIncrementNegativeStep() {
        LoopContext oLoop = new LoopContext(0, 10, -1);

        oLoop.increment();
        assertEquals(9, oLoop.getCurrentIndex());

        oLoop.increment();
        assertEquals(8, oLoop.getCurrentIndex());
    }

    // ================= COMPLETION TESTS (POSITIVE STEP) =================

    @Test
    @DisplayName("Should not be complete when index < limit (positive step)")
    void testNotCompletePositive() {
        LoopContext oLoop = new LoopContext(5, 0, 1);
        assertFalse(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should be complete when index >= limit (positive step)")
    void testCompletePositive() {
        LoopContext oLoop = new LoopContext(5, 5, 1);
        assertTrue(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should be complete when index > limit (positive step)")
    void testCompleteGreaterThanPositive() {
        LoopContext oLoop = new LoopContext(5, 10, 1);
        assertTrue(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should complete after correct number of iterations (positive step)")
    void testCompletionIterationCountPositive() {
        LoopContext oLoop = new LoopContext(5, 0, 1);

        // 0, 1, 2, 3, 4 should be valid indices (5 iterations)
        // Index 5 should be complete
        assertFalse(oLoop.isComplete()); // index = 0
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 1
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 2
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 3
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 4
        oLoop.increment();
        assertTrue(oLoop.isComplete());  // index = 5, loop complete
    }

    // ================= COMPLETION TESTS (NEGATIVE STEP) =================

    @Test
    @DisplayName("Should not be complete when index > limit (negative step)")
    void testNotCompleteNegative() {
        LoopContext oLoop = new LoopContext(0, 5, -1);
        assertFalse(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should be complete when index <= limit (negative step)")
    void testCompleteNegative() {
        LoopContext oLoop = new LoopContext(5, 5, -1);
        assertTrue(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should be complete when index < limit (negative step)")
    void testCompleteLessThanNegative() {
        LoopContext oLoop = new LoopContext(5, 0, -1);
        assertTrue(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should complete after correct number of iterations (negative step)")
    void testCompletionIterationCountNegative() {
        LoopContext oLoop = new LoopContext(0, 5, -1);

        // 5, 4, 3, 2, 1 should be valid indices (5 iterations)
        // Index 0 should be complete
        assertFalse(oLoop.isComplete()); // index = 5
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 4
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 3
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 2
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 1
        oLoop.increment();
        assertTrue(oLoop.isComplete());  // index = 0, loop complete
    }

    // ================= ZERO STEP EDGE CASE =================

    @Test
    @DisplayName("Should be complete with zero step size (infinite loop prevention)")
    void testZeroStepComplete() {
        LoopContext oLoop = new LoopContext(5, 0, 0);
        assertTrue(oLoop.isComplete());
    }

    // ================= STEP SIZE MODIFICATION =================

    @Test
    @DisplayName("Should allow changing step size")
    void testSetStepSize() {
        LoopContext oLoop = new LoopContext(10, 0, 1);
        assertEquals(1, oLoop.getStepSize());

        oLoop.setStepSize(2);
        assertEquals(2, oLoop.getStepSize());

        oLoop.increment();
        assertEquals(2, oLoop.getCurrentIndex());
    }

    @Test
    @DisplayName("Should support +LOOP: increment with custom step")
    void testPlusLoopBehavior() {
        LoopContext oLoop = new LoopContext(20, 0, 1);

        // Simulate: 0 20 DO 2 +LOOP (increment by 2 each time)
        assertFalse(oLoop.isComplete()); // index = 0
        oLoop.setStepSize(2);
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 2
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 4
        oLoop.increment();
        assertFalse(oLoop.isComplete()); // index = 6
        // ... continue until >= 20
        while (!oLoop.isComplete()) {
            oLoop.increment();
        }
        assertTrue(oLoop.isComplete());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle large numbers")
    void testLargeNumbers() {
        LoopContext oLoop = new LoopContext(1_000_000_000L, 999_999_999L, 1);
        assertFalse(oLoop.isComplete());
        oLoop.increment();
        assertTrue(oLoop.isComplete());
    }

    @Test
    @DisplayName("Should handle same index and limit")
    void testSameIndexAndLimit() {
        LoopContext oLoop = new LoopContext(5, 5, 1);
        assertTrue(oLoop.isComplete()); // Loop doesn't execute (index >= limit)
    }

    @Test
    @DisplayName("Should handle backwards loop (empty iteration)")
    void testBackwardsLoop() {
        LoopContext oLoop = new LoopContext(10, 0, 1);
        // This loop should execute normally from 0 to 9
        assertFalse(oLoop.isComplete());

        // But if we start at index higher than limit:
        LoopContext oLoop2 = new LoopContext(5, 10, 1);
        assertTrue(oLoop2.isComplete()); // Doesn't iterate (index > limit)
    }
}
