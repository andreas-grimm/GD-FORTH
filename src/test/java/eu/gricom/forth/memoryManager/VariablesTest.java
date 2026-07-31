package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import eu.gricom.forth.variableTypes.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesTest {

    @BeforeEach
    public void setUp() throws Exception {
        Field field = Variables.class.getDeclaredField("_aoVariable");
        field.setAccessible(true);
        Map<String, Value> map = (Map<String, Value>) field.get(null);
        map.clear();
    }

    // ============================================================================
    // Constructor Tests
    // ============================================================================

    @Test
    public void testConstructorCreatesNewVariable() throws Exception {
        Variables var = new Variables("testVar");
        assertNotNull(var, "Variable should be created");
    }

    @Test
    public void testConstructorInitializesWithEmptyString() throws Exception {
        Variables var = new Variables("myVar");
        Value retrievedValue = var.get("myVar");
        assertEquals("empty", retrievedValue.toString(), "New variable should be initialized with 'empty'");
    }

    @Test
    public void testConstructorThrowsAlreadyDeclaredException() throws Exception {
        Variables var1 = new Variables("duplicateVar");
        assertThrows(AlreadyDeclaredException.class, () -> {
            new Variables("duplicateVar");
        }, "Creating a variable with the same name should throw AlreadyDeclaredException");
    }

    @Test
    public void testConstructorAllowsMultipleDifferentVariables() throws Exception {
        Variables var1 = new Variables("var1");
        Variables var2 = new Variables("var2");
        Variables var3 = new Variables("var3");

        assertEquals("empty", var1.get("var1").toString());
        assertEquals("empty", var2.get("var2").toString());
        assertEquals("empty", var3.get("var3").toString());
    }

    // ============================================================================
    // Get Tests
    // ============================================================================

    @Test
    public void testGetRetrievesExistingVariable() throws Exception {
        Variables var = new Variables("testVar");
        Value result = var.get("testVar");
        assertNotNull(result, "Get should return the variable value");
        assertEquals("empty", result.toString(), "Get should return the initial 'empty' value");
    }

    @Test
    public void testGetThrowsNoSuchFieldExceptionForNonExistentVariable() throws Exception {
        Variables var = new Variables("existingVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.get("nonExistentVar");
        }, "Get should throw NoSuchFieldException for non-existent variable");
    }

    @Test
    public void testGetWithSingleCharacterVariableName() throws Exception {
        Variables var = new Variables("x");
        Value result = var.get("x");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testGetWithLongVariableName() throws Exception {
        String longName = "thisIsAVeryLongVariableNameForTesting";
        Variables var = new Variables(longName);
        Value result = var.get(longName);
        assertEquals("empty", result.toString());
    }

    // ============================================================================
    // Put Tests
    // ============================================================================

    @Test
    public void testPutUpdatesExistingVariable() throws Exception {
        Variables var = new Variables("myVar");
        var.put("myVar", new StringValue("updated"));
        Value result = var.get("myVar");
        assertEquals("updated", result.toString(), "Put should update the variable value");
    }

    @Test
    public void testPutWithIntegerValue() throws Exception {
        Variables var = new Variables("numVar");
        var.put("numVar", new IntegerValue(42));
        Value result = var.get("numVar");
        assertEquals(42, ((IntegerValue) result).toInteger(), "Put should store integer values");
    }

    @Test
    public void testPutThrowsNoSuchFieldExceptionForNonExistentVariable() throws Exception {
        Variables var = new Variables("existingVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.put("nonExistentVar", new StringValue("value"));
        }, "Put should throw NoSuchFieldException for non-existent variable");
    }

    @Test
    public void testPutMultipleTimesUpdatesVariable() throws Exception {
        Variables var = new Variables("counter");
        var.put("counter", new StringValue("first"));
        assertEquals("first", var.get("counter").toString());

        var.put("counter", new StringValue("second"));
        assertEquals("second", var.get("counter").toString());

        var.put("counter", new StringValue("third"));
        assertEquals("third", var.get("counter").toString());
    }

    // ============================================================================
    // Integration Tests (Constructor + Get + Put)
    // ============================================================================

    @Test
    public void testCreateVariableGetAndUpdate() throws Exception {
        Variables var = new Variables("testVar");
        assertEquals("empty", var.get("testVar").toString());

        var.put("testVar", new StringValue("new value"));
        assertEquals("new value", var.get("testVar").toString());
    }

    @Test
    public void testMultipleVariablesIndependent() throws Exception {
        Variables var1 = new Variables("var1");
        Variables var2 = new Variables("var2");
        Variables var3 = new Variables("var3");

        var1.put("var1", new StringValue("value1"));
        var2.put("var2", new StringValue("value2"));
        var3.put("var3", new StringValue("value3"));

        assertEquals("value1", var1.get("var1").toString());
        assertEquals("value2", var2.get("var2").toString());
        assertEquals("value3", var3.get("var3").toString());
    }

    @Test
    public void testVariableNameCaseSensitivity() throws Exception {
        Variables var = new Variables("MyVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.get("myvar");
        }, "Variable names should be case-sensitive");
    }

    @Test
    public void testGetAfterMultipleUpdates() throws Exception {
        Variables var = new Variables("counter");
        var.put("counter", new IntegerValue(1));
        var.put("counter", new IntegerValue(2));
        var.put("counter", new IntegerValue(3));
        var.put("counter", new IntegerValue(10));

        Value result = var.get("counter");
        assertEquals(10, ((IntegerValue) result).toInteger(), "Should return the most recent value");
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    public void testVariableNameWithSpecialCharacters() throws Exception {
        Variables var = new Variables("var_with_underscore");
        Value result = var.get("var_with_underscore");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testVariableNameWithNumbers() throws Exception {
        Variables var = new Variables("var123");
        Value result = var.get("var123");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testEmptyStringVariableName() throws Exception {
        Variables var = new Variables("");
        Value result = var.get("");
        assertEquals("empty", result.toString(), "Empty string should be a valid variable name");
    }

    @Test
    public void testPutWithEmptyStringValue() throws Exception {
        Variables var = new Variables("emptyVar");
        var.put("emptyVar", new StringValue(""));
        Value result = var.get("emptyVar");
        assertEquals("", result.toString(), "Should be able to store empty string value");
    }

    @Test
    public void testPutSameValueMultipleTimes() throws Exception {
        Variables var = new Variables("sameVal");
        var.put("sameVal", new StringValue("same"));
        var.put("sameVal", new StringValue("same"));
        var.put("sameVal", new StringValue("same"));

        Value result = var.get("sameVal");
        assertEquals("same", result.toString(), "Should handle repeated identical values");
    }

    // ============================================================================
    // Value Type Tests
    // ============================================================================

    @Test
    public void testStoreAndRetrieveStringValue() throws Exception {
        Variables var = new Variables("strVar");
        var.put("strVar", new StringValue("Hello FORTH"));
        Value result = var.get("strVar");
        assertEquals("Hello FORTH", result.toString());
    }

    @Test
    public void testStoreAndRetrieveIntegerValue() throws Exception {
        Variables var = new Variables("intVar");
        var.put("intVar", new IntegerValue(999));
        Value result = var.get("intVar");
        assertEquals(999, ((IntegerValue) result).toInteger());
    }

    // ============================================================================
    // Constructor Variations
    // ============================================================================

    @Test
    public void testMultipleConstructorCalls() throws Exception {
        for (int i = 0; i < 5; i++) {
            new Variables("var" + i);
        }

        Variables var0 = new Variables("var0_check");
        assertNotNull(var0);
    }

    @Test
    public void testConstructorExceptionPreservesExistingVariables() throws Exception {
        Variables var1 = new Variables("keepMe");
        var1.put("keepMe", new StringValue("preserved"));

        assertThrows(AlreadyDeclaredException.class, () -> {
            new Variables("keepMe");
        });

        Value result = var1.get("keepMe");
        assertEquals("preserved", result.toString(), "Existing variable should be preserved after exception");
    }
}
