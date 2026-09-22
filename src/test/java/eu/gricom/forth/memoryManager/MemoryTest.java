/**
 * MemoryTest.java
 *
 * (c) 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
package eu.gricom.forth.memoryManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Memory class covering positive and negative test cases,
 * boundary conditions, and exception handling.
 */
public class MemoryTest {
    private Memory oMemory;
    private static final int TEST_SIZE = 1000;

    @BeforeEach
    public void setUp() {
        oMemory = new Memory(TEST_SIZE);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructorWithValidSize() {
        Memory oNewMemory = new Memory(512);
        assertNotNull(oNewMemory, "Memory instance should not be null");
    }

    @Test
    public void testConstructorWithMinimalSize() {
        Memory oNewMemory = new Memory(1);
        assertNotNull(oNewMemory, "Memory instance with size 1 should not be null");
    }

    @Test
    public void testConstructorWithLargeSize() {
        Memory oNewMemory = new Memory(65535);
        assertNotNull(oNewMemory, "Memory instance with large size should not be null");
    }

    // ==================== Peek Tests ====================

    @Test
    public void testPeekFromBeginning() {
        byte bValue = oMemory.peek(0);
        assertEquals(0, bValue, "Peek at index 0 should return 0");
    }

    @Test
    public void testPeekFromMiddle() {
        byte bValue = oMemory.peek(500);
        assertEquals(0, bValue, "Peek at index 500 should return 0");
    }

    @Test
    public void testPeekFromEnd() {
        byte bValue = oMemory.peek(TEST_SIZE - 1);
        assertEquals(0, bValue, "Peek at last valid index should return 0");
    }

    @Test
    public void testPeekAfterWrite() {
        byte bTestValue = 42;
        oMemory.poke(100, bTestValue);
        byte bReadValue = oMemory.peek(100);
        assertEquals(bTestValue, bReadValue, "Peek should return the value written by poke");
    }

    @Test
    public void testPeekNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.peek(-1));
    }

    @Test
    public void testPeekBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.peek(TEST_SIZE));
    }

    @Test
    public void testPeekFarBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.peek(TEST_SIZE + 1000));
    }

    // ==================== Poke Tests ====================

    @Test
    public void testPokeAtBeginning() {
        byte bValue = 10;
        oMemory.poke(0, bValue);
        assertEquals(bValue, oMemory.peek(0), "Poke at index 0 should store value");
    }

    @Test
    public void testPokeAtMiddle() {
        byte bValue = 127;
        oMemory.poke(500, bValue);
        assertEquals(bValue, oMemory.peek(500), "Poke at index 500 should store value");
    }

    @Test
    public void testPokeAtEnd() {
        byte bValue = -1;
        oMemory.poke(TEST_SIZE - 1, bValue);
        assertEquals(bValue, oMemory.peek(TEST_SIZE - 1), "Poke at last valid index should store value");
    }

    @Test
    public void testPokeMultipleValues() {
        byte bValue1 = 25;
        byte bValue2 = 50;
        byte bValue3 = 75;

        oMemory.poke(100, bValue1);
        oMemory.poke(200, bValue2);
        oMemory.poke(300, bValue3);

        assertEquals(bValue1, oMemory.peek(100), "First poke value should be preserved");
        assertEquals(bValue2, oMemory.peek(200), "Second poke value should be preserved");
        assertEquals(bValue3, oMemory.peek(300), "Third poke value should be preserved");
    }

    @Test
    public void testPokeOverwrite() {
        byte bValue1 = 10;
        byte bValue2 = 20;

        oMemory.poke(50, bValue1);
        assertEquals(bValue1, oMemory.peek(50), "Initial poke value should be 10");

        oMemory.poke(50, bValue2);
        assertEquals(bValue2, oMemory.peek(50), "Overwritten value should be 20");
    }

    @Test
    public void testPokeNegativeByteValue() {
        byte bValue = -128;
        oMemory.poke(400, bValue);
        assertEquals(bValue, oMemory.peek(400), "Poke should store negative byte value");
    }

    @Test
    public void testPokeMaxByteValue() {
        byte bValue = 127;
        oMemory.poke(300, bValue);
        assertEquals(bValue, oMemory.peek(300), "Poke should store maximum byte value");
    }

    @Test
    public void testPokeNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.poke(-1, (byte) 0));
    }

    @Test
    public void testPokeBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.poke(TEST_SIZE, (byte) 0));
    }

    @Test
    public void testPokeFarBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.poke(TEST_SIZE + 500, (byte) 0));
    }

    // ==================== IsROM Tests ====================

    @Test
    public void testIsROMWithNoBlockedRanges() {
        assertFalse(oMemory.isROM(0), "isROM should return false when no ranges are blocked");
        assertFalse(oMemory.isROM(500), "isROM should return false for any index when no ranges are blocked");
    }

    @Test
    public void testIsROMInsideRange() {
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(100, 200));

        assertTrue(oMemory.isROM(100), "isROM should return true for index inside range");
        assertTrue(oMemory.isROM(150), "isROM should return true for index in middle of range");
        assertTrue(oMemory.isROM(200), "isROM should return true for index at end of range");
    }

    @Test
    public void testIsROMOutsideRange() {
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(100, 200));

        assertFalse(oMemory.isROM(99), "isROM should return false before range");
        assertFalse(oMemory.isROM(201), "isROM should return false after range");
    }

    @Test
    public void testIsROMMultipleRanges() {
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(50, 100));
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(200, 300));
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(400, 450));

        assertTrue(oMemory.isROM(75), "isROM should find index in first range");
        assertTrue(oMemory.isROM(250), "isROM should find index in second range");
        assertTrue(oMemory.isROM(425), "isROM should find index in third range");

        assertFalse(oMemory.isROM(150), "isROM should not find index between ranges");
        assertFalse(oMemory.isROM(350), "isROM should not find index between ranges");
    }

    @Test
    public void testIsROMSingleByteRange() {
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(100, 100));

        assertTrue(oMemory.isROM(100), "isROM should return true for single-byte blocked range");
        assertFalse(oMemory.isROM(99), "isROM should return false for index before single-byte range");
        assertFalse(oMemory.isROM(101), "isROM should return false for index after single-byte range");
    }

    @Test
    public void testIsROMAdjacentRanges() {
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(100, 149));
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(150, 200));

        assertTrue(oMemory.isROM(125), "isROM should return true for first range");
        assertTrue(oMemory.isROM(175), "isROM should return true for second range");
        assertTrue(oMemory.isROM(149), "isROM should return true for end of first range");
        assertTrue(oMemory.isROM(150), "isROM should return true for start of second range");
    }

    @Test
    public void testIsROMNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.isROM(-1));
    }

    @Test
    public void testIsROMBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.isROM(TEST_SIZE));
    }

    @Test
    public void testIsROMFarBeyondSize() {
        assertThrows(IndexOutOfBoundsException.class, () -> oMemory.isROM(TEST_SIZE + 100));
    }

    // ==================== MemoryRange Tests ====================

    @Test
    public void testMemoryRangeCreation() {
        Memory.MemoryRange oRange = new Memory.MemoryRange(50, 100);
        assertEquals(50, oRange.iStart, "Range start should be set correctly");
        assertEquals(100, oRange.iEnd, "Range end should be set correctly");
    }

    @Test
    public void testMemoryRangeSingleByte() {
        Memory.MemoryRange oRange = new Memory.MemoryRange(100, 100);
        assertEquals(oRange.iStart, oRange.iEnd, "Single byte range start should equal end");
    }

    // ==================== Integration Tests ====================

    @Test
    public void testComplexScenario() {
        // Set up some blocked ranges
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(0, 99));      // System ROM
        oMemory.getBlockedRanges().add(new Memory.MemoryRange(950, 999));   // Interrupt vectors

        // Write to allowed area
        byte bValue = 77;
        oMemory.poke(500, bValue);
        assertEquals(bValue, oMemory.peek(500), "Should write to allowed area");

        // Verify ROM protection
        assertTrue(oMemory.isROM(0), "Address 0 should be in ROM");
        assertTrue(oMemory.isROM(50), "Address 50 should be in ROM");
        assertTrue(oMemory.isROM(975), "Address 975 should be in ROM");

        // Verify writable areas
        assertFalse(oMemory.isROM(100), "Address 100 should not be in ROM");
        assertFalse(oMemory.isROM(500), "Address 500 should not be in ROM");
    }
}
