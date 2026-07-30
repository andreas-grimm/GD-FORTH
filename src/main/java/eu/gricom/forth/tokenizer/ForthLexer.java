package eu.gricom.forth.tokenizer;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.memoryManager.Directory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * ForthLexer.java
 * <p>
 * Description: The ForthLexer class implements the Lexer interface for BASIC source code. It scans the input character
 * by character, recognising keywords, identifiers, numbers, strings, and operators, and produces a list of Token
 * objects for the parser to consume.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class ForthLexer implements Lexer {
    private final Logger _oLogger = new Logger(this.getClass().getName());

    @Override
    public final List<Token> tokenize(final String strSource) throws SyntaxErrorException {
        List<Token> aoTokens = new ArrayList<>();

        // Convert the program into a list of lines
        String[] astrProgramLines = strSource.split("\\s*\n\\s*");

        boolean bIsStringRunning = false;

        // Forth does not have line numbers, so we start at line 1 and add 1 for each line...
        int iLineNumber = 1;

        for (String strProgramLine: astrProgramLines) {
            Token oToken = null;

            iLineNumber++;

            // here we handle all empty lines, e.g. lines that only contain the line number
            if (strProgramLine.length() < 1) {
                aoTokens.add(new Token("empty", ForthTokenType.EMPTY_LINE, iLineNumber));
            } else {
                // normalize the line: put spaces in places where needed, or remove them
                strProgramLine = Normalizer.normalize(strProgramLine);

                // find reserved words: divide the program line into an array of words
                String[] astrWords = strProgramLine.split("\\s");

                // and iterate over them
                for (String strWord : astrWords) {

                    // eliminate all empty strings
                    if (strWord.length() <= 0) {
                        continue;
                    }

                    // this section verifies whether the next word is part of a string (as a string started but did not end yet)
                    // if a string started (bIsStringRunning == true) then the word is added to the string, if the word contains
                    // quotation marks ("), the string is closed.
                    if (bIsStringRunning) {
                        if (oToken == null) {
                            throw new SyntaxErrorException("Syntax Error: Unrecognized character sequence: " + iLineNumber
                                            + " " + strProgramLine);
                        }

                        // if the word ends with a ", then we stop any running string and remove the last character "
                        if (strWord.endsWith("\"")) {
                            strWord = strWord.substring(0, strWord.length() - 1);
                            bIsStringRunning = false;
                            aoTokens.add(oToken);
                        }

                        // add the word to the string in the token
                        oToken.setText(oToken.getText() + " " + strWord);
                    } else {
                        // ok - we know this is not part of a string.

                        // if the found word is part of the word list, then continue with that definition, overwriting the
                        // defined programming code
                        Directory oDirectory = new Directory();
                        List<Token> aoDirectoryContent = oDirectory.getToken(strWord);

                        // compare the word with the list of reserved words
                        int iIndex = ForthReservedWords.getIndex(strWord);

                        if (iIndex != -1) {
                            // we found a reserved word...
                            ForthTokenType oTokenType = ForthReservedWords.getTokenType(iIndex);

                            // this block handles all comments
                            if (oTokenType == ForthTokenType.COMMENT) {
                                aoTokens.add(new Token(strProgramLine, oTokenType, iLineNumber));

                                break;
                            }

                            oToken = new Token(strWord, oTokenType, iLineNumber);

                            // ok - this is not reserved word - so maybe it is a number?
                        } else if (isNumber(strWord)) {
                            oToken = new Token(strWord, ForthTokenType.NUMBER, iLineNumber);

                            // now check whether the word is marked as the beginning of a String
                        } else if (isString(strWord)) {
                            strWord = strWord.substring(1); // remove the "

                            // this section handles single word strings
                            if (strWord.endsWith("\"")) {
                                strWord = strWord.substring(0, strWord.length() - 1);
                                bIsStringRunning = false;
                                oToken = new Token(strWord, ForthTokenType.STRING, iLineNumber);
                                aoTokens.add(oToken);
                            } else {
                                oToken = new Token(strWord, ForthTokenType.STRING, iLineNumber);
                                bIsStringRunning = true;
                            }

                            // now check whether the word is marked as a boolean
                        } else {
                            // as it is neither a number, string, or boolean - it has to be a variable / constant...
                            ForthTokenType oTokenType = ForthTokenType.WORD;

                            oToken = new Token(strWord, oTokenType, iLineNumber);
                        }

                        if (aoDirectoryContent != null) {
                            for (Token oDirectoryToken: aoDirectoryContent) {
                                aoTokens.add(oDirectoryToken);
                            }
                        } else if (oToken.getType() != ForthTokenType.STRING) { // Strings are added after they are completed.
                            aoTokens.add(oToken);
                        }
                    }
                }
            }
        }

        for (Token oToken: aoTokens) {
            _oLogger.debug(" [" + oToken.getLine() + "]  [" + oToken.getType().toString() + "] " + oToken.getText());
        }

        return aoTokens;
    }

    /**
     * isBoolean identifies a boolean in the BASIC program by the keywords "TRUE" and "FALSE".
     *
     * @param strWord argument for the check
     * @return true if argument is a boolean
     */
    private boolean isBoolean(final String strWord) {
        return strWord.toUpperCase(Locale.ROOT).matches("TRUE")
                || strWord.toUpperCase(Locale.ROOT).matches("FALSE");
    }

    /**
     * isString identifies a string in the BASIC program by the trailing quotation marks (").
     *
     * @param strWord argument for the check
     * @return true if argument is a string
     */
    private boolean isString(final String strWord) {
        return strWord.startsWith("\"");
    }

    /**
     * isNumber uses a regular expression to verify that the argument string contains a number.
     *
     * @param strWord argument for the check
     * @return true if argument is a number
     */
    private boolean isNumber(final String strWord) {
        Pattern oPattern = Pattern.compile("-?\\d*(\\.\\d+)?");

        if (strWord == null) {
            return false;
        }
        return oPattern.matcher(strWord).matches();
    }
}
