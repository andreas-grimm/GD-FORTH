package eu.gricom.forth;

import eu.gricom.forth.helper.EnvParam;
import eu.gricom.forth.helper.FileHandler;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.helper.Printer;
import eu.gricom.forth.lineEditor.ForthLineEditor;
import eu.gricom.forth.memoryManager.Program;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Locale;


/**
 * Forth.java
 * <p>
 * Description: The Forth class is the main entry point for the GD-FORTH interpreter. It orchestrates the
 * complete execution pipeline including file loading, lexical analysis (tokenisation), parsing, and
 * interpretation of FORTH programs.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
@SuppressWarnings("SpellCheckingInspection")
public class Forth {
    private Program _oProgram = new Program();
    private final transient Logger _oLogger = new Logger(this.getClass().getName());
    private static String _strVersion = EnvParam.getString("version");
    private static boolean _bEditorFlag = true;

    /**
     * Constructs a new Basic instance. The instance stores the global state of the interpreter, such as the values of
     * all the variables and the current statement.
     */
    public Forth() {
    }

    /**
     * Process.
     * This is where the magic happens. This runs the code through the parsing pipeline to generate the AST. Then it
     * executes each statement. It keeps track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply setting the index of the current statement.
     * <p>
     * In an interpreter that didn't mix the interpretation logic in with the AST node classes, this would be doing a lot more work.
     *
     * @param oProgram The program object, containing the source code of a .bas script to interpret.
     */
    public final void process(final Program oProgram) {
        _oLogger.info("Processing program...");

        /*
        // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new ForthLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException eSyntaxErrorException) {
            // This syntax error has to generate due to the use of the macro. Original code errors in the lexer are
            // discovered in the previous step.
            System.out.println(eSyntaxErrorException.getMessage());
            System.exit(1);
        }

        int iCounter = 0;
        for (Token oToken: _oProgram.getTokens()) {
            if (oToken.getType().toString().contains("LINE")) {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: []");
            } else {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: ["
                                       + oToken.getText() + "]");
            }
            iCounter++;
        }

        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            BasicParser oParser = new BasicParser(oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            _oLogger.error(eSyntaxError.getMessage());
        }

         */
    }

    /**
     * Interpret.
     * This is where the magic happens. This runs the code through the parsing pipeline to generator the AST. Then it
     * executes each statement. It keeps track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply setting the index of the current statement.
     * <p>
     * In an interpreter that didn't mix the interpretation logic in with the AST node classes, this would be doing a lot more work.
     *
     * @param oProgram The program object, containing the source code of a .bas script to interpret.
     */
    public final void interpret(final Program oProgram) {

        // Find and process Macros.
        _oLogger.info("Processing macros...");
        _oProgram = oProgram;

        /*
        // Tokenize. At the end of the tokenization, I have the program transferred into a list of tokens and parameters
        _oLogger.info("Starting tokenization...");

        Lexer oTokenizer = new ForthLexer();

        try {
            _oProgram.setTokens(oTokenizer.tokenize(oProgram.getProgram()));

        } catch (SyntaxErrorException eSyntaxErrorException) {
            // This syntax error has to generate due to the use of the macro. Original code errors in the lexer are
            // discovered in the previous step.
            System.out.println(eSyntaxErrorException.getMessage());
            System.exit(1);
        }

        int iCounter = 0;
        for (Token oToken: _oProgram.getTokens()) {
            if (oToken.getType().toString().contains("LINE")) {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: []");
            } else {
                _oLogger.debug("[" + oToken.getLine() + "] Token # <" + iCounter + ">: [" + oToken.getType() + "]: ["
                        + oToken.getText() + "]");
            }
            iCounter++;
        }
*/
        if (_bEditorFlag) {
            Printer.println("Starting line editor");
            ForthLineEditor oLineEditor = new ForthLineEditor(_oProgram);
            oLineEditor.execute();
        } else {
/*
        // Parse.
        _oLogger.info("Starting parsing...");
        try {
            BasicParser oParser = new BasicParser(oProgram.getTokens(), _bDartmouthFlag);
            _oProgram.setPreRunStatements(oParser.parsePreRun());
            _oProgram.setStatements(oParser.parse());
        } catch (SyntaxErrorException eSyntaxError) {
            _oLogger.error(eSyntaxError.getMessage());
        }

        // Run.
        Execute oRun = new Execute(_oProgram);

        // load the environment for the execution
        oRun.loadEnvironment();

        // run the program
        oRun.runProgram();
*/
        }

        System.exit(0);
    }


    // Utility stuff -----------------------------------------------------------

    /**
     * Runs the interpreter as a command-line app. Takes one argument: a path
     * to a script file to load and run. The script should contain one
     * statement per line.
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        Logger oLogger = new Logger("main");
        Program oProgram = new Program();
        oLogger.setLogLevel(EnvParam.getString("log_level"));

        CommandLine oCommandLine = null;

        // create the Options object
        Options oOptions = new Options();

        try {
            oOptions.addOption("h", false, "help (This screen)");
            oOptions.addOption("q", false, "quiet mode");
            oOptions.addOption("v", true, "verbose level: (info, debug, trace, or error)");
            oOptions.addOption("r", false, "execute the loaded program directly, do not use the line editor");

            CommandLineParser parser = new DefaultParser();
            oCommandLine = parser.parse(oOptions, args);
        } catch (ParseException eParseException) {
            System.out.println(eParseException.getMessage());
            System.exit(-1);
        }

        if (oCommandLine != null && !oCommandLine.hasOption("q")) {

            long lMaxMemory = Runtime.getRuntime().maxMemory() / 1024;
            Printer.println();
            Printer.println("  ____ ____        _____          _   _");
            Printer.println(" / ___|  _ \\      |  ___|__  _ __| |_| |__     GriCom Forth Interpreter Version " + _strVersion);
            Printer.println("| |  _| | | |_____| |_ / _ \\| '__| __| '_ \\    (c) Copyright A.Grimm 2026");
            Printer.println("| |_| | |_| |_____|  _| (_) | |  | |_| | | |  ");
            Printer.println(" \\____|____/      |_|  \\___/|_|   \\__|_| |_|   Maximum memory (KBytes): " + lMaxMemory);
            Printer.println();
        }

        if (oCommandLine != null && oCommandLine.hasOption("v")) {
            String strLogLevel = oCommandLine.getOptionValue("v");
            String strLogLevelList = "trace|debug|info|warning";

            oLogger.setLogLevel(EnvParam.getString("log_level"));

            if (strLogLevelList.contains(strLogLevel.toLowerCase(Locale.ROOT))) {
                oLogger.setLogLevel(strLogLevel);
            }

            oLogger.debug("Log Level set:" + strLogLevel + "...");
        }

        if (oCommandLine != null && oCommandLine.hasOption("h")) {
            // automatically generator the help statement
            oLogger.debug("Display help message...");

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar "+ EnvParam.getString("app_name") + "-" + _strVersion + ".jar [<filename.fhs>]", oOptions);        }

        if (oCommandLine != null && oCommandLine.hasOption("r")) {
            _bEditorFlag = false;
            oLogger.debug("Direct execution mode selected...");
        }

        if (oCommandLine != null) {
            List<String> astrArguments = oCommandLine.getArgList();

            if (astrArguments.isEmpty()) {
                // No program file provided; start with empty program in line editor
                oLogger.info("Starting with empty program in line editor mode...");
                _bEditorFlag = true;
                oProgram.load("<empty>", "");

                Forth oForth = new Forth();
                oLogger.info("Run the interpreter...");
                oForth.interpret(oProgram);

                System.exit(-1);
            }

            String strFileName = astrArguments.getLast();

            // Read the file.
            oLogger.info("Read file: " + strFileName + "...");
            oProgram.load(strFileName, FileHandler.readFile(strFileName));

            // Run it.
            Forth oForth = new Forth();

            oLogger.info("Run the interpreter...");
            oForth.interpret(oProgram);

            System.exit(-1);
        }
    }
}
