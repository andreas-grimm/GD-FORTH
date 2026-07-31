package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.EmptyStackException;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StackTest {
    private Stack stack;

    @BeforeEach
    public void setUp() {
        stack = new Stack();
        stack.reset();
    }

    @Test
    public void testStack() {
        stack.push(new StringValue("TestValue"));
        stack.push(new IntegerValue(999));

        try {
            IntegerValue oResult = (IntegerValue) stack.pop();
            assertEquals(oResult.toInteger(), 999);

            StringValue strResult = (StringValue) stack.pop();
            assertTrue(strResult.toString().matches("TestValue"));
        } catch (EmptyStackException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testStackWithException() {
        stack.reset();

        assertThrows(EmptyStackException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void testStackSize() {
        Stack oStack = new Stack();
        oStack.reset();

        assertEquals(0, oStack.size(), "Empty stack should have size 0");

        oStack.push(new IntegerValue(1));
        assertEquals(1, oStack.size(), "Stack with one item should have size 1");

        oStack.push(new IntegerValue(2));
        assertEquals(2, oStack.size(), "Stack with two items should have size 2");

        oStack.push(new IntegerValue(3));
        assertEquals(3, oStack.size(), "Stack with three items should have size 3");

        try {
            oStack.pop();
            assertEquals(2, oStack.size(), "After pop, stack size should be 2");

            oStack.pop();
            assertEquals(1, oStack.size(), "After second pop, stack size should be 1");

            oStack.pop();
            assertEquals(0, oStack.size(), "After third pop, stack size should be 0");
        } catch (EmptyStackException e) {
            fail("Unexpected EmptyStackException: " + e.getMessage());
        }
    }

    @Test
    public void testStackGet() {
        Stack oStack = new Stack();
        oStack.reset();

        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(20));
        oStack.push(new IntegerValue(30));

        assertEquals(10, ((IntegerValue) oStack.get(0)).toInteger(), "Index 0 (bottom) should be 10");
        assertEquals(20, ((IntegerValue) oStack.get(1)).toInteger(), "Index 1 (middle) should be 20");
        assertEquals(30, ((IntegerValue) oStack.get(2)).toInteger(), "Index 2 (top) should be 30");
    }

    @Test
    public void testStackGetWithString() {
        Stack oStack = new Stack();
        oStack.reset();

        StringValue strVal1 = new StringValue("First");
        StringValue strVal2 = new StringValue("Second");
        StringValue strVal3 = new StringValue("Third");

        oStack.push(strVal1);
        oStack.push(strVal2);
        oStack.push(strVal3);

        assertEquals("First", oStack.get(0).toString(), "Index 0 (bottom) should be 'First'");
        assertEquals("Second", oStack.get(1).toString(), "Index 1 (middle) should be 'Second'");
        assertEquals("Third", oStack.get(2).toString(), "Index 2 (top) should be 'Third'");
    }

    @Test
    public void testStackReset() {
        Stack oStack = new Stack();

        oStack.push(new IntegerValue(1));
        oStack.push(new IntegerValue(2));
        oStack.push(new IntegerValue(3));

        assertEquals(3, oStack.size(), "Stack should have 3 items");

        oStack.reset();

        assertEquals(0, oStack.size(), "Stack should be empty after reset");

        assertThrows(EmptyStackException.class, () -> {
            oStack.pop();
        }, "Popping from reset stack should throw EmptyStackException");
    }

    @Test
    public void testStackGetSingleItem() {
        Stack oStack = new Stack();
        oStack.reset();

        IntegerValue singleValue = new IntegerValue(42);
        oStack.push(singleValue);

        assertEquals(42, ((IntegerValue) oStack.get(0)).toInteger(), "Single item at index 0 should be 42");
        assertEquals(1, oStack.size(), "Stack size should be 1");
    }
}
