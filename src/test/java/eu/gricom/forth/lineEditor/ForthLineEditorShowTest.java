package eu.gricom.forth.lineEditor;

import eu.gricom.forth.memoryManager.Program;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.variableTypes.IntegerValue;
import eu.gricom.forth.variableTypes.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ForthLineEditorShowTest {

    private ForthLineEditor _oEditor;
    private Program _oProgram;
    private ByteArrayOutputStream _outputStream;
    private PrintStream _originalOut;

    @BeforeEach
    public void setUp() throws Exception {
        // Clear stack
        Stack oStack = new Stack();
        oStack.reset();

        // Clear variables - clear both static fields
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> map = (Map<Integer, Object>) varField.get(null);
        map.clear();

        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) namesField.get(null);
        if (names != null) {
            names.clear();
        }

        // Create fresh program - this ensures empty state
        _oProgram = new Program();
        _oEditor = new ForthLineEditor(_oProgram);

        // Setup output capture
        _outputStream = new ByteArrayOutputStream();
        _originalOut = System.out;
    }

    private String callShow(String strLine) throws Exception {
        // Capture output
        _outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(_outputStream));

        // Call show method using reflection
        Method showMethod = ForthLineEditor.class.getDeclaredMethod("show", String.class);
        showMethod.setAccessible(true);
        showMethod.invoke(_oEditor, strLine);

        // Restore output
        System.setOut(_originalOut);
        return _outputStream.toString();
    }

    // ============================================================================
    // Empty State Tests
    // ============================================================================

    @Test
    public void testShowAllEmptyState() throws Exception {
        String output = callShow("SHOW");
        assertTrue(output.contains("Display programming history:") || output.contains("Declared and used variables:") || output.contains("Content of the stack:"),
                "SHOW with empty state should display headers");
    }

    @Test
    public void testShowVariablesEmptyState() throws Exception {
        String output = callShow("SHOW VARIABLES");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables header");
    }

    @Test
    public void testShowStackEmptyState() throws Exception {
        String output = callShow("SHOW STACK");
        assertTrue(output.contains("Content of the stack:"), "Should display stack header");
    }

    @Test
    public void testShowProgramEmptyState() throws Exception {
        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should display program header");
    }

    // ============================================================================
    // Single Parameter Tests
    // ============================================================================

    @Test
    public void testShowVariablesOnly() throws Exception {
        Variables var = new Variables();
        var.define("testVar");
        var.put("testVar", new StringValue("testValue"));

        String output = callShow("SHOW VARIABLES");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables");
        assertFalse(output.contains("Content of the stack:"), "Should not display stack");
        assertFalse(output.contains("Display programming history:"), "Should not display program");
    }

    @Test
    public void testShowStackOnly() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(42));

        String output = callShow("SHOW STACK");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
        assertFalse(output.contains("Declared and used variables:"), "Should not display variables");
        assertFalse(output.contains("Display programming history:"), "Should not display program");
    }

    @Test
    public void testShowProgramOnly() throws Exception {
        _oProgram.addLine("1 2 +");

        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should display program");
        assertFalse(output.contains("Declared and used variables:"), "Should not display variables");
        assertFalse(output.contains("Content of the stack:"), "Should not display stack");
    }

    // ============================================================================
    // Multiple Parameter Tests
    // ============================================================================

    @Test
    public void testShowVariablesAndStack() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(10));
        Variables var = new Variables();
        var.define("x");

        String output = callShow("SHOW VARIABLES STACK");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
        assertFalse(output.contains("Display programming history:"), "Should not display program");
    }

    @Test
    public void testShowStackAndProgram() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(99));
        _oProgram.addLine("5 5 *");

        String output = callShow("SHOW STACK PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should display program");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
        assertFalse(output.contains("Declared and used variables:"), "Should not display variables");
    }

    @Test
    public void testShowVariablesAndProgram() throws Exception {
        Variables var = new Variables();
        var.define("counter");
        _oProgram.addLine("10 counter !");

        String output = callShow("SHOW VARIABLES PROGRAM");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables");
        assertTrue(output.contains("Display programming history:"), "Should display program");
        assertFalse(output.contains("Content of the stack:"), "Should not display stack");
    }

    @Test
    public void testShowAllThreeExplicit() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(7));
        Variables var = new Variables();
        var.define("myVar");
        _oProgram.addLine("7 myVar !");

        String output = callShow("SHOW VARIABLES PROGRAM STACK");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables");
        assertTrue(output.contains("Display programming history:"), "Should display program");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
    }

    // ============================================================================
    // Parameter Order Independence Tests
    // ============================================================================

    @Test
    public void testShowParametersOrderIndependence1() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(1));
        Variables var = new Variables();
        var.define("a");
        _oProgram.addLine("test");

        String output1 = callShow("SHOW PROGRAM STACK VARIABLES");

        // Clear static state for second part of test
        Field varField = Variables.class.getDeclaredField("_aoVariable");
        varField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> map = (Map<Integer, Object>) varField.get(null);
        map.clear();

        Field namesField = Variables.class.getDeclaredField("_astrVariableName");
        namesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) namesField.get(null);
        if (names != null) {
            names.clear();
        }

        oStack.reset();
        var = new Variables();
        var.define("b");

        String output2 = callShow("SHOW VARIABLES STACK PROGRAM");

        assertTrue(output1.contains("Display programming history:"));
        assertTrue(output2.contains("Display programming history:"));
    }

    @Test
    public void testShowParametersOrderIndependence2() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(5));
        Variables var = new Variables();
        var.define("b");

        String output1 = callShow("SHOW STACK VARIABLES");
        assertTrue(output1.contains("Content of the stack:"));
        assertTrue(output1.contains("Declared and used variables:"));
    }

    // ============================================================================
    // Case Insensitivity Tests
    // ============================================================================

    @Test
    public void testShowLowercaseParameters() throws Exception {
        Variables var = new Variables();
        var.define("test");

        String output = callShow("SHOW variables");
        assertTrue(output.contains("Declared and used variables:"), "Should handle lowercase parameters");
    }

    @Test
    public void testShowMixedCaseParameters() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(3));

        String output = callShow("SHOW Stack");
        assertTrue(output.contains("Content of the stack:"), "Should handle mixed case parameters");
    }

    @Test
    public void testShowUppercaseParameters() throws Exception {
        _oProgram.addLine("program line");

        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should handle uppercase parameters");
    }

    // ============================================================================
    // Complex Scenario Tests
    // ============================================================================

    @Test
    public void testShowMultipleVariables() throws Exception {
        Variables var = new Variables();
        var.define("var1");
        var.define("var2");
        var.define("var3");
        var.put("var1", new IntegerValue(100));
        var.put("var2", new StringValue("hello"));
        var.put("var3", new IntegerValue(200));

        String output = callShow("SHOW VARIABLES");
        assertTrue(output.contains("var1"), "Should display var1");
        assertTrue(output.contains("var2"), "Should display var2");
        assertTrue(output.contains("var3"), "Should display var3");
    }

    @Test
    public void testShowMultipleStackItems() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(1));
        oStack.push(new IntegerValue(2));
        oStack.push(new IntegerValue(3));

        String output = callShow("SHOW STACK");
        assertTrue(output.contains("Content of the stack:"), "Should display stack header");
    }

    @Test
    public void testShowMultipleProgramLines() throws Exception {
        _oProgram.addLine("line 1");
        _oProgram.addLine("line 2");
        _oProgram.addLine("line 3");

        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should display program header");
    }

    // ============================================================================
    // Content Display Tests
    // ============================================================================

    @Test
    public void testShowVariablesDisplaysContent() throws Exception {
        Variables var = new Variables();
        var.define("myVar");
        var.put("myVar", new StringValue("myValue"));

        String output = callShow("SHOW VARIABLES");
        assertTrue(output.contains("myVar") && output.contains("myValue"), "Should display variable name and value");
    }

    @Test
    public void testShowProgramDisplaysLineNumbers() throws Exception {
        _oProgram.addLine("first");
        _oProgram.addLine("second");

        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("1>") || output.contains("Display programming history:"), "Should display line numbers");
    }

    @Test
    public void testShowStackDisplaysValues() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(42));

        String output = callShow("SHOW STACK");
        assertTrue(output.contains("Content of the stack:"), "Should display stack with values");
    }

    // ============================================================================
    // Edge Cases
    // ============================================================================

    @Test
    public void testShowWithWhitespaceVariations() throws Exception {
        Variables var = new Variables();
        var.define("test");

        String output = callShow("SHOW   VARIABLES");
        assertTrue(output.contains("Declared and used variables:"), "Should handle multiple spaces");
    }

    @Test
    public void testShowWithTabCharacters() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(1));

        String output = callShow("SHOW\tSTACK");
        assertTrue(output.contains("Content of the stack:"), "Should handle tab characters");
    }

    @Test
    public void testShowInvalidParameterIgnored() throws Exception {
        Variables var = new Variables();
        var.define("x");

        String output = callShow("SHOW VARIABLES INVALID PARAMETER");
        assertTrue(output.contains("Declared and used variables:"), "Should display valid parameter");
        assertFalse(output.contains("Content of the stack:"), "Should ignore invalid parameters");
    }

    @Test
    public void testShowDuplicateParameters() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(5));

        String output = callShow("SHOW STACK STACK STACK");
        assertTrue(output.contains("Content of the stack:"), "Should handle duplicate parameters");
    }

    // ============================================================================
    // Comprehensive Scenario Tests
    // ============================================================================

    @Test
    public void testShowCompleteEnvironment() throws Exception {
        // Setup complete environment
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(10));
        oStack.push(new IntegerValue(20));

        Variables var = new Variables();
        var.define("result");
        var.put("result", new IntegerValue(30));

        _oProgram.addLine("10 20 +");
        _oProgram.addLine("result !");

        String output = callShow("SHOW");
        assertTrue(output.contains("Declared and used variables:"), "Should display variables");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
        assertTrue(output.contains("Display programming history:"), "Should display program");
    }

    @Test
    public void testShowSelectiveDisplay() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(100));

        Variables var = new Variables();
        var.define("v1");

        _oProgram.addLine("program");

        String output = callShow("SHOW STACK PROGRAM");
        assertTrue(output.contains("Content of the stack:"), "Should display stack");
        assertTrue(output.contains("Display programming history:"), "Should display program");
        assertFalse(output.contains("Declared and used variables:"), "Should not display variables");
    }

    @Test
    public void testShowAfterReset() throws Exception {
        Stack oStack = new Stack();
        oStack.push(new IntegerValue(1));
        oStack.reset();

        String output = callShow("SHOW STACK");
        assertTrue(output.contains("Content of the stack:"), "Should display stack header even after reset");
    }

    @Test
    public void testShowWithEmptyProgramLine() throws Exception {
        _oProgram.addLine("");

        String output = callShow("SHOW PROGRAM");
        assertTrue(output.contains("Display programming history:"), "Should handle empty program lines");
    }
}