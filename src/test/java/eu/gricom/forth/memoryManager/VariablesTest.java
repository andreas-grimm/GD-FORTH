package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import eu.gricom.forth.variableTypes.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesTest {

    @BeforeEach
    public void setUp() throws Exception {
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        Map<Integer, Value> map = (Map<Integer, Value>) varField.get(null);
        map.clear();

        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        List<String> names = (List<String>) namesField.get(Variables.class.getConstructor().newInstance());
        names.clear();
    }

    // ============================================================================
    // Constructor Tests
    // ============================================================================

    @Test
    public void testConstructorCreatesVariablesInstance() {
        Variables var = new Variables();
        assertNotNull(var, "Variables instance should be created");
    }

    @Test
    public void testMultipleVariablesInstancesCreated() {
        Variables var1 = new Variables();
        Variables var2 = new Variables();
        assertNotNull(var1);
        assertNotNull(var2);
    }

    // ============================================================================
    // Define Tests
    // ============================================================================

    @Test
    public void testDefineCreatesNewVariable() throws Exception {
        Variables var = new Variables();
        var.define("testVar");
        assertTrue(var.isVariable("testVar"), "Variable should be defined");
    }

    @Test
    public void testDefineInitializesWithEmptyString() throws Exception {
        Variables var = new Variables();
        var.define("myVar");
        Value value = var.get("myVar");
        assertEquals("empty", value.toString(), "New variable should be initialized with 'empty'");
    }

    @Test
    public void testDefineThrowsAlreadyDeclaredException() throws Exception {
        Variables var = new Variables();
        var.define("duplicateVar");
        assertThrows(AlreadyDeclaredException.class, () -> {
            var.define("duplicateVar");
        }, "Defining duplicate variable should throw AlreadyDeclaredException");
    }

    @Test
    public void testDefineAllowsMultipleDifferentVariables() throws Exception {
        Variables var = new Variables();
        var.define("var1");
        var.define("var2");
        var.define("var3");

        assertTrue(var.isVariable("var1"));
        assertTrue(var.isVariable("var2"));
        assertTrue(var.isVariable("var3"));
        assertEquals("empty", var.get("var1").toString());
        assertEquals("empty", var.get("var2").toString());
        assertEquals("empty", var.get("var3").toString());
    }

    // ============================================================================
    // Index Tests
    // ============================================================================

    @Test
    public void testIndexReturnsCorrectValue() throws Exception {
        Variables var = new Variables();
        var.define("var1");
        var.define("var2");
        var.define("var3");

        int idx1 = var.index("var1");
        int idx2 = var.index("var2");
        int idx3 = var.index("var3");

        assertEquals(0, idx1, "First variable should have index 0");
        assertEquals(1, idx2, "Second variable should have index 1");
        assertEquals(2, idx3, "Third variable should have index 2");
    }

    @Test
    public void testIndexThrowsNoSuchFieldExceptionForNonExistentVariable() throws Exception {
        Variables var = new Variables();
        var.define("existingVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.index("nonExistentVar");
        }, "Index should throw NoSuchFieldException for non-existent variable");
    }

    @Test
    public void testIndexConsistency() throws Exception {
        Variables var = new Variables();
        var.define("alpha");
        int idx1 = var.index("alpha");
        int idx2 = var.index("alpha");
        assertEquals(idx1, idx2, "Index should be consistent for same variable");
    }

    // ============================================================================
    // IsVariable Tests
    // ============================================================================

    @Test
    public void testIsVariableReturnsTrueForDefinedVariable() throws Exception {
        Variables var = new Variables();
        var.define("testVar");
        assertTrue(var.isVariable("testVar"), "isVariable should return true for defined variable");
    }

    @Test
    public void testIsVariableReturnsFalseForUndefinedVariable() {
        Variables var = new Variables();
        assertFalse(var.isVariable("undefinedVar"), "isVariable should return false for undefined variable");
    }

    @Test
    public void testIsVariableCaseSensitive() throws Exception {
        Variables var = new Variables();
        var.define("MyVar");
        assertTrue(var.isVariable("MyVar"));
        assertFalse(var.isVariable("myvar"), "isVariable should be case-sensitive");
    }

    // ============================================================================
    // Get Tests
    // ============================================================================

    @Test
    public void testGetRetrievesDefinedVariable() throws Exception {
        Variables var = new Variables();
        var.define("testVar");
        Value result = var.get("testVar");
        assertNotNull(result, "Get should return the variable value");
        assertEquals("empty", result.toString(), "Get should return the initial 'empty' value");
    }

    @Test
    public void testGetThrowsNoSuchFieldExceptionForUndefinedVariable() throws Exception {
        Variables var = new Variables();
        var.define("existingVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.get("nonExistentVar");
        }, "Get should throw NoSuchFieldException for undefined variable");
    }

    @Test
    public void testGetWithSingleCharacterVariableName() throws Exception {
        Variables var = new Variables();
        var.define("x");
        Value result = var.get("x");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testGetWithLongVariableName() throws Exception {
        String longName = "thisIsAVeryLongVariableNameForTesting";
        Variables var = new Variables();
        var.define(longName);
        Value result = var.get(longName);
        assertEquals("empty", result.toString());
    }

    // ============================================================================
    // Put Tests
    // ============================================================================

    @Test
    public void testPutUpdatesDefinedVariable() throws Exception {
        Variables var = new Variables();
        var.define("myVar");
        var.put("myVar", new StringValue("updated"));
        Value result = var.get("myVar");
        assertEquals("updated", result.toString(), "Put should update the variable value");
    }

    @Test
    public void testPutWithIntegerValue() throws Exception {
        Variables var = new Variables();
        var.define("numVar");
        var.put("numVar", new IntegerValue(42));
        Value result = var.get("numVar");
        assertEquals(42, ((IntegerValue) result).toInteger(), "Put should store integer values");
    }

    @Test
    public void testPutThrowsNoSuchFieldExceptionForUndefinedVariable() throws Exception {
        Variables var = new Variables();
        var.define("existingVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.put("nonExistentVar", new StringValue("value"));
        }, "Put should throw NoSuchFieldException for undefined variable");
    }

    @Test
    public void testPutMultipleTimesUpdatesVariable() throws Exception {
        Variables var = new Variables();
        var.define("counter");
        var.put("counter", new StringValue("first"));
        assertEquals("first", var.get("counter").toString());

        var.put("counter", new StringValue("second"));
        assertEquals("second", var.get("counter").toString());

        var.put("counter", new StringValue("third"));
        assertEquals("third", var.get("counter").toString());
    }

    // ============================================================================
    // Integration Tests
    // ============================================================================

    @Test
    public void testDefineGetAndUpdate() throws Exception {
        Variables var = new Variables();
        var.define("testVar");
        assertEquals("empty", var.get("testVar").toString());

        var.put("testVar", new StringValue("new value"));
        assertEquals("new value", var.get("testVar").toString());
    }

    @Test
    public void testMultipleVariablesIndependent() throws Exception {
        Variables var = new Variables();
        var.define("var1");
        var.define("var2");
        var.define("var3");

        var.put("var1", new StringValue("value1"));
        var.put("var2", new StringValue("value2"));
        var.put("var3", new StringValue("value3"));

        assertEquals("value1", var.get("var1").toString());
        assertEquals("value2", var.get("var2").toString());
        assertEquals("value3", var.get("var3").toString());
    }

    @Test
    public void testVariableNameCaseSensitivity() throws Exception {
        Variables var = new Variables();
        var.define("MyVar");
        assertThrows(NoSuchFieldException.class, () -> {
            var.get("myvar");
        }, "Variable names should be case-sensitive");
    }

    @Test
    public void testGetAfterMultipleUpdates() throws Exception {
        Variables var = new Variables();
        var.define("counter");
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
        Variables var = new Variables();
        var.define("var_with_underscore");
        Value result = var.get("var_with_underscore");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testVariableNameWithNumbers() throws Exception {
        Variables var = new Variables();
        var.define("var123");
        Value result = var.get("var123");
        assertEquals("empty", result.toString());
    }

    @Test
    public void testEmptyStringVariableName() throws Exception {
        Variables var = new Variables();
        var.define("");
        Value result = var.get("");
        assertEquals("empty", result.toString(), "Empty string should be a valid variable name");
    }

    @Test
    public void testPutWithEmptyStringValue() throws Exception {
        Variables var = new Variables();
        var.define("emptyVar");
        var.put("emptyVar", new StringValue(""));
        Value result = var.get("emptyVar");
        assertEquals("", result.toString(), "Should be able to store empty string value");
    }

    @Test
    public void testPutSameValueMultipleTimes() throws Exception {
        Variables var = new Variables();
        var.define("sameVal");
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
        Variables var = new Variables();
        var.define("strVar");
        var.put("strVar", new StringValue("Hello FORTH"));
        Value result = var.get("strVar");
        assertEquals("Hello FORTH", result.toString());
    }

    @Test
    public void testStoreAndRetrieveIntegerValue() throws Exception {
        Variables var = new Variables();
        var.define("intVar");
        var.put("intVar", new IntegerValue(999));
        Value result = var.get("intVar");
        assertEquals(999, ((IntegerValue) result).toInteger());
    }

    // ============================================================================
    // Index-Based Operations
    // ============================================================================

    @Test
    public void testIndexAfterDefine() throws Exception {
        Variables var = new Variables();
        var.define("first");
        var.define("second");
        var.define("third");

        assertEquals(0, var.index("first"));
        assertEquals(1, var.index("second"));
        assertEquals(2, var.index("third"));
    }

    @Test
    public void testGetByIndexConsistency() throws Exception {
        Variables var = new Variables();
        var.define("alpha");
        var.put("alpha", new StringValue("alphaValue"));

        int idx = var.index("alpha");
        Value value = var.get("alpha");
        assertEquals("alphaValue", value.toString());
    }

    @Test
    public void testMultipleVariablesWithSeparateInstances() throws Exception {
        Variables var1 = new Variables();
        Variables var2 = new Variables();

        var1.define("var1");
        var2.define("var2");

        assertTrue(var1.isVariable("var1"));
        assertTrue(var2.isVariable("var2"));
    }
}
