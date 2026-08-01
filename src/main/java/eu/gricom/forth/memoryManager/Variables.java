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

public class Variables {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final static List<String> _astrVariableName = new ArrayList<>();
    private final static Map<Integer, Value> _aoVariable = new HashMap<>();

    public Variables() {
    }

    public void define(String strVariableName) throws AlreadyDeclaredException {
        if (!_astrVariableName.contains(strVariableName)) {
            _astrVariableName.add(strVariableName);
            int iIndex = _astrVariableName.indexOf(strVariableName);
            _aoVariable.put(iIndex, new StringValue("empty"));
        } else {
            throw (new AlreadyDeclaredException(strVariableName));
        }
    }

    public int index(String strVariableName) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            return(_astrVariableName.indexOf(strVariableName));
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    public boolean isVariable(String strVariableName) {
        if (_astrVariableName.contains(strVariableName)) {
            return(true);
        }

        return(false);
    }


    public Value get(String strVariableName) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            return(_aoVariable.get(index(strVariableName)));
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    public void put(String strVariableName, Value oValue) throws NoSuchFieldException {
        if (_astrVariableName.contains(strVariableName)) {
            _aoVariable.put(index(strVariableName), oValue);
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    public void put(int iIndex, Value oValue) throws NoSuchFieldException {
        if (iIndex < _aoVariable.size()) {
            _aoVariable.put(iIndex, oValue);
        } else {
            throw (new NoSuchFieldException("Index: " + iIndex));
        }
    }

    /**
     * Display the variables.
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
