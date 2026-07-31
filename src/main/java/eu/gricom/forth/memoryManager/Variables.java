package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.AlreadyDeclaredException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.StringValue;
import eu.gricom.forth.variableTypes.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Variables {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final static Map<String, Value> _aoVariable = new HashMap<>();

    public Variables(String strVariableName) throws AlreadyDeclaredException {
        if (!_aoVariable.containsKey(strVariableName)) {
            _aoVariable.put(strVariableName, new StringValue("empty"));
        } else {
            throw (new AlreadyDeclaredException(strVariableName));
        }
    }// Key: Token, Basic Source Line: Value

    public Value get(String strVariableName) throws NoSuchFieldException {
        if (_aoVariable.containsKey(strVariableName)) {
            return(_aoVariable.get(strVariableName));
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }

    public void put(String strVariableName, Value oValue) throws NoSuchFieldException {
        if (_aoVariable.containsKey(strVariableName)) {
            _aoVariable.put(strVariableName, oValue);
        } else {
            throw (new NoSuchFieldException(strVariableName));
        }
    }
}
