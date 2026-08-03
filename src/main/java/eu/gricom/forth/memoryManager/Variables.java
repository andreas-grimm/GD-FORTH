package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.StringValue;
import eu.gricom.forth.variableTypes.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Variables.java
 * <p>
 * Description: The Variables class manages all variable definitions and values in the FORTH interpreter.
 * It uses a dual-storage architecture:
 * - Static list (_astrVariableName) to store variable names by index
 * - Static map (_aoVariable) to store variable values by index
 * <p>
 * This provides index-based access for fast retrieval and supports both name-based and index-based operations.
 * Variables are initialized with default "empty" StringValue when defined.
 * <p>
 * Supports two interfaces:
 * - Name-based: define(name), get(name), put(name, value), isVariable(name), index(name)
 * - Index-based: get(index), put(index, value)
 * <p>
 * (c) = 2026,.., by Andreas Grimm, The Netherlands / Norway
 */
public class Variables {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final static List<String> _astrVariableName = new ArrayList<>();
    private final static Map<Integer, Value> _aoVariable = new HashMap<>();

    /**
     * Constructor.
     * <p>
     * Creates a new Variables instance. Note: Variables uses static storage, so all instances
     * share the same variable definitions and values.
     */
    public Variables() {
    }

    /**
     * Define a new variable with the given name.
     * <p>
     * Adds the variable to the static list and initializes its value to "empty" StringValue.
     * Variables are indexed by their position in the _astrVariableName list.
     *
     * @param strVariableName The name of the variable to define
     * @throws AlreadyDeclaredException if a variable with this name already exists
     */
    public void define(String strVariableName) throws AlreadyDeclaredException {
        if (!_astrVariableName.contains(strVariableName)) {
            _astrVariableName.add(strVariableName);
            int iIndex = _astrVariableName.indexOf(strVariableName);
            _aoVariable.put(iIndex, new StringValue("empty"));
        } else {
            throw (new AlreadyDeclaredException(strVariableName));
        }
    }

    /**
     * Get the index of a variable by name.
     * <p>
     * Returns the position of the variable in the _astrVariableName list, which corresponds
     * to the key in the _aoVariable map.
     *
     * @param strVariableName The name of the variable
     * @return The index/position of the variable
     * @throws NoSuchFieldException if the variable is not defined
     */
    public int index(String strVariableName) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            return(_astrVariableName.indexOf(strVariableName));
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    /**
     * Check if a variable with the given name exists.
     *
     * @param strVariableName The name to check
     * @return true if the variable is defined, false otherwise
     */
    public boolean isVariable(String strVariableName) {
        if (_astrVariableName.contains(strVariableName)) {
            return(true);
        }

        return(false);
    }


    /**
     * Get the value of a variable by name.
     * <p>
     * Retrieves the current value stored in the variable without modifying the stack.
     *
     * @param strVariableName The name of the variable
     * @return The value stored in the variable
     * @throws NoSuchFieldException if the variable is not defined
     */
    public Value get(String strVariableName) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            return(_aoVariable.get(index(strVariableName)));
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    /**
     * Fetch a variable value by index and push it to the stack.
     * <p>
     * Implements the @ (fetch) operation in FORTH. Retrieves the value stored at the given
     * variable index and pushes it onto the stack. This is used by FetchStatement.
     *
     * @param iIndex The variable index
     * @throws NoSuchFieldException if the index is not valid
     */
    public void get(int iIndex) throws NoSuchFieldException {
        try {
            Value oValue = _aoVariable.get(iIndex);
            Stack oStack = new Stack();
            oStack.push(oValue);
        } catch (Exception e) {
            throw new NoSuchFieldException(e.getMessage());
        }
    }

    /**
     * Store a value in a variable by name.
     * <p>
     * Updates the value stored in the variable. The variable must already be defined.
     *
     * @param strVariableName The name of the variable
     * @param oValue The new value to store
     * @throws NoSuchFieldException if the variable is not defined
     */
    public void put(String strVariableName, Value oValue) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            _aoVariable.put(index(strVariableName), oValue);
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    /**
     * Store a value in a variable by index.
     * <p>
     * Implements the ! (store) operation in FORTH. Stores the value at the given
     * variable index. This is used by StoreStatement.
     *
     * @param iIndex The variable index
     * @param oValue The value to store
     * @throws NoSuchFieldException if the index is not valid
     */
    public void put(int iIndex, Value oValue) throws NoSuchFieldException {
        if (iIndex < _aoVariable.size()) {
            _aoVariable.put(iIndex, oValue);
        } else {
            throw (new NoSuchFieldException("Index: " + iIndex));
        }
    }

    /**
     * Get a formatted string representation of all defined variables.
     * <p>
     * Returns a formatted display of all variables and their current values,
     * one per line in the format: "name[index]: value"
     *
     * @return A formatted string showing all variables and values
     */
    public String retrieveContent() {
        String strReturn = new String();
        for (int iIndex = 0; iIndex < _astrVariableName.size(); iIndex++) {
            String strVariableName = _astrVariableName.get(iIndex);
            String strVariableValue = _aoVariable.get(iIndex).toString();
            strReturn += strVariableName + "["+ iIndex + "]: " + strVariableValue + "\n";
        }
        return (strReturn);
    }
}
