package eu.gricom.forth.lineEditor;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.ConsoleColors;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.memoryManager.Directory;
import eu.gricom.forth.memoryManager.Program;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.memoryManager.Variables;
import eu.gricom.forth.parser.ForthParser;
import eu.gricom.forth.runtimeManager.Execute;
import eu.gricom.forth.tokenizer.ForthLexer;
import eu.gricom.forth.tokenizer.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ForthLineEditor {
    private Program _oProgram;
    private BufferedReader _oReader;
    private static final Logger _oLogger = new Logger(ForthLineEditor.class.getName());


    public ForthLineEditor(Program oProgram) {
        _oProgram = oProgram;
        _oReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void execute() {
        boolean bExit = false;
        Printer.println("Welcome");

        try {
            while (!bExit) {
                Printer.print(">");
                String strEnteredLine = _oReader.readLine();
                if (strEnteredLine == null) {
                    _oLogger.debug("End of input detected (EOF)");
                    bExit = true;
                } else {
                    _oLogger.debug("Entered line: [" + strEnteredLine + "]");
                    bExit = processLine(strEnteredLine);
                }
            }
        } catch (IOException eException) {
            _oLogger.error("Catastrophic error: " + eException.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Printer.println("Good bye.");
    }

    private boolean processLine(String strLine) throws Exception {
        boolean bExit = false;
        String strFirstWord;

        //not an empty line, we need to process:
        if (strLine.length() >= 1) {

            // let's check whether we talk about one or more words...
            if (strLine.indexOf(" ") > 0) {
                strFirstWord = strLine.substring(0, strLine.indexOf(" "));
            } else {
                strFirstWord = strLine;
            }

            // we start defining a new word for the language
            if (strFirstWord.equals(":")) {
                try {
                    defineNewWord(strLine);
                } catch (SyntaxErrorException eException) {
                    Printer.println("Syntax error: " + eException);
                }
            } else {
                // let's interpret the entered command:
                strFirstWord = strFirstWord.toUpperCase();
                switch (strFirstWord) {
                    case "BYE":
                    case "EXIT":
                    case "QUIT":
                        bExit = true;
                        break;
                    case "RERUN":
                        run();
                        break;
                    case "SHOW":
                        show(strLine);
                        break;
                    case "DELETE":
                        deleteLines(strLine);
                        break;
                    case "LOAD":
                        load(strLine);
                        break;
                    case "SAVE":
                        save(strLine);
                        break;
                    case "HELP":
                        printHelp();
                        break;
                    default: {
                        interpretLine(strLine);
                        run();
                    }
                }
            }
        }
        return bExit;
    }

    private void defineNewWord(String strLine) throws IOException, SyntaxErrorException {
        // first remove the leading ":"
        strLine = strLine.substring(strLine.indexOf(" ") + 1);
        if (strLine.contains(":")) {
            strLine.substring(strLine.indexOf(":"));
        }

        // new loop until the entered line contains a semicolon (;)
        while (!strLine.contains(";")) {
            Printer.print(">>");
            strLine += " " + _oReader.readLine();
        }

        // make sure that there is a space between word name and word body
        if (strLine.indexOf(" ") < 1) {
            throw new SyntaxErrorException("New Word not correctly defined");
        }

        // split the string into word name, body and rest
        String strWord = strLine.substring(0, strLine.indexOf(" "));
        String strBody = strLine.substring(strLine.indexOf(" ") + 1, strLine.indexOf(" ;"));
        strLine = strLine.substring(strLine.indexOf(" ;"));

        _oLogger.debug("Word: " + strWord);
        _oLogger.debug("Body: " + strBody);
        _oLogger.debug("Rest: " + strLine);

        Directory oWords = new Directory();
        oWords.storeWord(strWord, strBody);
        _oProgram.addLine(" : " + strWord + " " + strBody + " ;");
    }

    private void interpretLine (String strLine) throws SyntaxErrorException {

        ForthLexer oTokenizer = new ForthLexer();
        try {
            List<Token> aoLineTokens = oTokenizer.tokenize(strLine);
            ForthParser oParser = new ForthParser(aoLineTokens);

            _oProgram.addLine(strLine);
            _oProgram.addTokens(oTokenizer.tokenize(strLine));
            _oProgram.setNewStatements(oParser.parse());

        } catch (SyntaxErrorException e) {
            Printer.println("Syntax error: " + e.getMessage());
        }
    }

    private void run() {
        try {
            if (!_oProgram.hasContent()) {
                Printer.println("RUN: No program loaded. Use LOAD command or enter program lines.");
                return;
            }

            boolean bSuccessfulCompleted = false;
            // Parse.
            _oLogger.info("Starting parsing...");

            // Run.
            Execute oRun = new Execute(_oProgram);

            // run the program
            oRun.runNewStatements();
            _oProgram.deleteNewStatements();
        } catch (Exception eException) {
            String strExceptionMessage = eException.getMessage();
            Printer.println(ConsoleColors.RED + strExceptionMessage + ConsoleColors.RESET);
        }
    }

    /**
     * Parse and execute the DELETE command.
     *
     * Syntax: DELETE lineNumber
     *         DELETE lineBegin lineEnd
     *         DELETE lineBegin,lineEnd
     *         DELETE lineBegin, lineEnd
     *
     * @param strLine The complete command line including "DELETE" and parameters
     */
    private void deleteLines(String strLine) {
        String strRemainder = strLine.substring(6).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("DELETE: missing line number(s)");
            return;
        }

        String[] astrNumbers = strRemainder.replaceAll(",", " ").split("\\s+");

        if (astrNumbers.length == 0) {
            Printer.println("DELETE: invalid syntax");
            return;
        }
/*
        try {
            if (astrNumbers.length == 1) {
                int iLineNumber = Integer.parseInt(astrNumbers[0]);
                _oProgram.deleteLines(iLineNumber, iLineNumber);
                Printer.println("Deleted line " + iLineNumber);
            } else if (astrNumbers.length >= 2) {
                int iBegin = Integer.parseInt(astrNumbers[0]);
                int iEnd = Integer.parseInt(astrNumbers[1]);
                int iMin = Math.min(iBegin, iEnd);
                int iMax = Math.max(iBegin, iEnd);
                _oProgram.deleteLines(iMin, iMax);
                Printer.println("Deleted lines " + iMin + " to " + iMax);
            }
        } catch (NumberFormatException e) {
            Printer.println("DELETE: invalid line number(s)");
        } catch (SyntaxErrorException e) {
            Printer.println("DELETE error: " + e.getMessage());
        }
 */
    }

    /**
     * Parse and execute the LOAD command.
     *
     * Syntax: LOAD filename
     *
     * @param strLine The complete command line including "LOAD" and the filename
     */
    private void load(String strLine) {
        String strRemainder = strLine.substring(4).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("LOAD: missing filename");
            return;
        }
/*
        try {
            _oProgram.loadProgram(strRemainder);
            Printer.println("Program loaded from " + strRemainder);
        } catch (eu.gricom.forth.error.FileNotFoundException e) {
            Printer.println("LOAD error: " + e.getMessage());
        } catch (eu.gricom.forth.error.EmptyProgramException e) {
            Printer.println("LOAD error: " + e.getMessage());
        } catch (SyntaxErrorException e) {
            Printer.println("LOAD error: " + e.getMessage());
        }
 */
    }

    /**
     * Parse and execute the SHOW command with support for multiple parameters.
     *
     * Syntax: SHOW                              - Display VARIABLES, STACK, and PROGRAM
     *         SHOW VARIABLES                    - Display only VARIABLES
     *         SHOW STACK                        - Display only STACK
     *         SHOW PROGRAM                      - Display only PROGRAM
     *         SHOW VARIABLES STACK              - Display VARIABLES and STACK
     *         SHOW STACK PROGRAM                - Display STACK and PROGRAM
     *         SHOW VARIABLES PROGRAM STACK      - Display all three (order independent)
     *
     * @param strLine The complete command line including "SHOW" and optional parameters
     */
    private void show(String strLine) {
        String[] astrWords = strLine.trim().split("\\s+");

        boolean bShowVariables = true;
        boolean bShowStack = true;
        boolean bShowProgram = true;

        if (astrWords.length > 1) {
            // If any parameter is provided, start with all false
            bShowVariables = false;
            bShowStack = false;
            bShowProgram = false;

            // Process all words after "SHOW"
            for (int i = 1; i < astrWords.length; i++) {
                String strWord = astrWords[i].toUpperCase();
                if (strWord.equals("VARIABLES")) {
                    bShowVariables = true;
                } else if (strWord.equals("STACK")) {
                    bShowStack = true;
                } else if (strWord.equals("PROGRAM")) {
                    bShowProgram = true;
                }
            }
        }
        // If no second word, all flags remain true (show all)

        if (bShowProgram) {
            Printer.println(ConsoleColors.GREEN + "Display programming history:");
            String strProgram = _oProgram.getProgram();
            if (strProgram != null && !strProgram.isEmpty()) {
                String[] astrProgramLines = strProgram.split("\\s*\n\\s*");
                int iLineNumber = 0;
                for (String strProgramLine: astrProgramLines) {
                    Printer.print(ConsoleColors.YELLOW + ++iLineNumber + "> " + ConsoleColors.RESET);
                    Printer.println(strProgramLine);
                }
            }
            Printer.println(ConsoleColors.RESET);
        }

        if (bShowVariables) {
            Printer.println(ConsoleColors.GREEN + "Declared and used variables:");
            Variables oVariables = new Variables();
            String strVariables = oVariables.retrieveContent();
            Printer.print(strVariables);
            Printer.println(ConsoleColors.RESET);
        }

        // Display STACK if requested
        if (bShowStack) {
            Printer.println(ConsoleColors.GREEN + "Content of the stack:");
            Stack oStack = new Stack();
            String strStackContent = oStack.retrieveContent();
            Printer.println(strStackContent);
            Printer.println(ConsoleColors.RESET);
        }
    }

    /**
     * Parse and execute the SAVE command.
     *
     * Syntax: SAVE filename
     *
     * @param strLine The complete command line including "SAVE" and the filename
     */
    private void save(String strLine) {
        String strRemainder = strLine.substring(4).trim();

        if (strRemainder.isEmpty()) {
            Printer.println("SAVE: missing filename");
            return;
        }

        try {
            _oProgram.save(strRemainder);
            Printer.println("Program saved to " + strRemainder);
        } catch (eu.gricom.forth.error.FileAlreadyExistsException e) {
            Printer.println("SAVE error: " + e.getMessage());
        } catch (java.io.IOException e) {
            Printer.println("SAVE error: I/O exception - " + e.getMessage());
        }
    }

    /**
     * Print help information from help.txt resource file.
     */
    private void printHelp() {
        try {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            InputStream oInputStream = oClassLoader.getResourceAsStream("help.txt");

            if (oInputStream == null) {
                Printer.println("Help file not found");
                return;
            }

            BufferedReader oReader = new BufferedReader(new InputStreamReader(oInputStream));
            StringBuilder oBuilder = new StringBuilder();
            String strLine;

            while ((strLine = oReader.readLine()) != null) {
                oBuilder.append(strLine).append("\n");
            }

            oReader.close();

            if (oBuilder.length() > 0) {
                Printer.print(oBuilder.toString());
            } else {
                Printer.println("Help file is empty");
            }
        } catch (Exception e) {
            Printer.println("Error reading help file: " + e.getMessage());
        }
    }
}
