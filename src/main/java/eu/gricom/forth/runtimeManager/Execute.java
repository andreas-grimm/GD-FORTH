package eu.gricom.forth.runtimeManager;

import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.helper.Trace;
import eu.gricom.forth.memoryManager.LineNumberXRef;
import eu.gricom.forth.memoryManager.Program;
//import eu.gricom.forth.memoryManager.ProgramPointer;
import eu.gricom.forth.statements.EmptyStatement;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;

import java.util.List;

public class Execute {
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private final transient LineNumberXRef _oLineNumbers = new LineNumberXRef();
//    private final ProgramPointer _oProgramPointer = new ProgramPointer();
    private final List<Statement> _aoAllStatements;
    private final List<Statement> _aoNewStatements;
    private final Trace _oTrace = new Trace(false);

    private Statement _oStatement = null;

    public Execute(Program oProgram) {
        _aoNewStatements = oProgram.getStatements();
        _aoAllStatements = oProgram.getAllStatements();
    }

    public void loadEnvironment() {
        _oLogger.info("Pre-load environment...");
    }

    public void runAllStatements() {
/*
        _oLogger.info("Starting execution...");
        try {
            if (_aoStatements != null) {
                int iSourceCodeLineNumber = -1;
                _oProgramPointer.setCurrentStatement(0);

                while (_oProgramPointer.getCurrentStatement() < _aoStatements.size()) {
                    // as long as we have not reached the end of the code
                    int iThisStatement = _oProgramPointer.getCurrentStatement();

                    iSourceCodeLineNumber =
                            _oLineNumbers.getLineNumberFromToken(_aoStatements.get(iThisStatement).getTokenNumber());

                    _oProgramPointer.calcNextStatement();

                    _oLogger.debug(
                            "Basic Source Code Line [" + iSourceCodeLineNumber + "] Statement [ "
                                    + _aoStatements.get(iThisStatement).getTokenNumber() + "]: "
                                    + _aoStatements.get(iThisStatement).content());

                    _oTrace.trace(iSourceCodeLineNumber);

                    _oStatement = _aoStatements.get(iThisStatement);
                    _aoStatements.get(iThisStatement).execute();
                }
            } else {
                _oLogger.error("Parsing delivered empty program");
                System.exit(-1);
            }
        } catch (Exception eException) {
            if (!eException.getClass().getName().contains("EndOfProgramException")) {
                eException.printStackTrace();
            }
        }

 */
    }

    public void runNewStatements() {
        _oLogger.info("Starting execution...");
        try {
            if (_aoNewStatements != null) {
                for (Statement oStatement: _aoNewStatements) {
                    // as long as we have not reached the end of the code
                    oStatement.execute();
                }
            } else {
                Printer.println("Parsing delivered empty program");
            }
        } catch (Exception eException) {
            if (!eException.getClass().getName().contains("EndOfProgramException")) {
                eException.printStackTrace();
            }
        }
    }

    public Statement getFinalStatement() {
        return _oStatement;
    }
}