package eu.gricom.forth.tokenizer;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * ForthReservedWords.java
 * <p>
 * Description: The ForthReservedWords class maintains the dictionary of all FORTH language keywords and their corresponding
 * token types. During lexical analysis, the lexer uses this class to identify whether a word is a reserved keyword
 * (such as DUP, DROP, SWAP) or a user-defined identifier.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public final class ForthReservedWords {

    /**
     * Default constructor.
     */
    private ForthReservedWords() { }

    /**
     * This defines the different kinds of tokens for the FORTH language standard.
     */
    private final static String[] _astrReservedWords = {
            "!",
            "#", "#>", "#S",
            "'",
            "(", "(LOCAL)",
            "*", "*/", "*/MOD",
            "+", "+!", "+FIELD", "+LOOP", "+X/STRING",
            ",", "-", "-TRAILING", "-TRAILING-GARBAGE",
            ".", ".\"", ".(", ".R", ".S",
            "/", "/MOD", "/STRING",
            "0<", "0<>", "0=", "0>",
            "1+", "1-",
            "2!", "2*", "2/", "2>R",
            "2@", "2CONSTANT", "2DROP", "2DUP",
            "2LITERAL", "2OVER", "2R>", "2R@",
            "2ROT", "2SWAP", "2VALUE", "2VARIABLE",
            ":", ":NONAME",
            ";", ";CODE",
            "<", "<#", "<>", "=",
            ">", "<=", ">=",
            ">BODY", ">FLOAT", ">IN", ">NUMBER", ">R",
            "?", "?DO", "?DUP", "@",
            "ABORT", "ABORT\"", "ABS", "ACCEPT", "ACTION-OF",
            "AGAIN", "AHEAD", "ALIGN", "ALIGNED", "ALLOCATE",
            "ALLOT", "ALSO", "AND", "ASSEMBLER", "AT-XY",
            "BASE", "BEGIN", "BEGIN-STRUCTURE", "BIN", "BL",
            "BLANK", "BLK", "BLOCK", "BUFFER", "BUFFER:",
            "BYE",
            "C!", "C\"", "C,", "C@",
            "CASE", "CATCH", "CELL+", "CELLS",
            "CFIELD:", "CHAR", "CHAR+", "CHARS",
            "CLOSE-FILE", "CMOVE", "CMOVE>", "CODE",
            "COMPARE", "COMPILE,", "CONSTANT", "COUNT",
            "CR", "CREATE", "CREATE-FILE", "CS-PICK",
            "CS-ROLL",
            "D+", "D-", "D.", "D.R", "D0<",
            "D0=", "D2*", "D2/", "D<",
            "D=", "D>F", "D>S", "DABS", "DECIMAL",
            "DEFER", "DEFER!", "DEFER@","DEFINITIONS",
            "DELETE-FILE", "DEPTH", "DF!", "DF@",
            "DFALIGN", "DFALIGNED", "DFFIELD:", "DFLOAT+",
            "DFLOATS", "DMAX", "DMIN", "DNEGATE", "DO",
            "DOES>", "DROP", "DU<", "DUMP", "DUP",
            "EDITOR", "EKEY", "EKEY>CHAR", "EKEY>FKEY",
            "EKEY>XCHAR", "EKEY?", "ELSE", "EMIT",
            "EMIT?", "EMPTY-BUFFERS", "END-STRUCTURE", "ENDCASE",
            "ENDOF", "ENVIRONMENT?", "ERASE", "EVALUATE",
            "EXECUTE", "EXIT",
            "F!", "F*", "F**", "F+", "F-",
            "F.", "F/", "F0<", "F0=",
            "F<", "F>D", "F>S", "F@",
            "FABS", "FACOS", "FACOSH", "FALIGN",
            "FALIGNED", "FALOG", "FALSE",
            "FASIN", "FASINH", "FATAN", "FATAN2",
            "FATANH", "FCONSTANT", "FCOS", "FCOSH",
            "FDEPTH", "FDROP", "FDUP", "FE.", "FEXP",
            "FEXPM1", "FFIELD:",
            "FIELD:", "FILE-POSITION", "FILE-SIZE", "FILE-STATUS",
            "FILL", "FIND", "FLITERAL", "FLN", "FLNP1",
            "FLOAT+", "FLOATS", "FLOG", "FLOOR", "FLUSH",
            "FLUSH-FILE", "FM/MOD", "FMAX", "FMIN",
            "FNEGATE", "FORGET", "FORTH", "FORTH-WORDLIST", "FOVER",
            "FREE", "FROT", "FROUND",
            "FS.", "FSIN", "FSINCOS", "FSINH", "FSQRT",
            "FSWAP", "FTAN", "FTANH", "FTRUNC",
            "FVALUE", "FVARIABLE", "F~",
            "GET-CURRENT", "GET_ORDER",
            "HERE", "HEX", "HOLD", "HOLDS",
            "I", "IF", "IMMEDIATE", "INCLUDE", "INCLUDE-FILE",
            "INCLUDED", "INVERT", "IS", "J",
            "K-ALT-MASK", "K-CTRL-MASK", "K-DELETE", "K-DOWN",
            "K-END", "K-F1", "K-F10", "K-F11", "K-F12",
            "K-F2", "K-F3", "K-F4", "K-F5", "K-F6",
            "K-F7", "K-F8", "K-F9", "K-HOME", "K-INSERT",
            "K-LEFT", "K-NEXT", "K-PRIOR", "K-RIGHT", "K-SHIFT-MASK",
            "K-UP", "KEY", "KEY?",
            "LEAVE", "LIST", "LITERAL", "LOAD", "LOCALS|",
            "LOOP", "LSHIFT",
            "M*", "M*/", "M+", "MARKER", "MAX",
            "MIN", "MOD", "MOVE", "MS",
            "N>R", "NAME>COMPILE", "NAME>INTERPRET", "NAME>STRING",
            "NEGATE", "NIP", "NR>",
            "OF", "ONLY", "OPEN_FILE", "ORDER", "OVER",
            "PAD", "PAGE", "PARSE", "PARSE-NAME", "PICK",
            "POSTPONE", "PRECISION", "PREVIOUS", "QUIT",
            "R/O", "R/W", "R>", "R@", "READ-FILE",
            "READ-LINE", "RECURSE", "REFILL", "RENAME-FILE",
            "REPEAT", "REPLACES", "REPOSITION-FILE", "REPRESENT",
            "REQUIRE", "REQUIRED", "RESIZE", "RESIZE-FILE", "RESTORE-INPUT",
            "ROLL", "ROT", "RSHIFT",
            "S\"", "S>D", "S>F", "SAVE-BUFFERS", "SAVE-INPUT",
            "SCR", "SEARCH", "SEARCH-WORDLIST", "SEE", "SET-CURRENT",
            "SET-ORDER", "SET-PRECISION", "SF!", "SF@",
            "SFALIGN", "SFALIGNED", "SFFIELD:", "SFLOAT+",
            "SFLOATS", "SIGN", "SLITERAL", "SM/REM",
            "SOURCE", "SOURCE-ID", "SPACE", "SPACES", "STATE",
            "SUBSTITUTE", "SWAP", "SYNONYM", "S\\\"",
            "THEN", "THROW", "THRU", "TIME&DATE", "TO",
            "TRAVERSE-WORDLIST", "TRUE", "TUCK", "TYPE",
            "U.", "U.R", "U<", "U>",
            "UM*", "UM/MOD", "UNESCAPE", "UNLOOP",
            "UNTIL", "UNUSED", "UPDATE",
            "VALUE", "VARIABLE",
            "W/O", "WHILE", "WITHIN", "WORD", "WORDLIST",
            "WORDS", "WRITE-FILE", "WRITE-LINE",
            "X-SIZE", "X-WIDTH", "XC!+", "XC!+?",
            "XC,", "XC-SIZE", "XC-WIDTH", "XC@+",
            "XCHAR+", "XCHAR-", "XEMIT", "XHOLD",
            "XKEY", "XKEY?", "XOR", "X\\STRING-",
            "[", "[']", "[CHAR]", "[COMPILE]",
            "[DEFINED]", "[ELSE]", "[IF]", "[THEN]",
            "[UNDEFINED]", "\\", "]", "{:"
    };

    private final static ForthTokenType[] _aeTokenTypes = {
            ForthTokenType.STORE,
            ForthTokenType.HASH_TAG, ForthTokenType.HASH_TAG_GREATER, ForthTokenType.HASH_TAG_S,
            ForthTokenType.TICK,
            ForthTokenType.PAREN, ForthTokenType.PAREN_LOCAL_PAREN,
            ForthTokenType.MULTIPLY, ForthTokenType.STAR_SLASH, ForthTokenType.STAR_SLASH_MOD,
            ForthTokenType.PLUS, ForthTokenType.PLUS_STORE, ForthTokenType.PLUS_FIELD, ForthTokenType.PLUS_LOOP, ForthTokenType.PLUS_X_STRING,
              ForthTokenType.COMMA, ForthTokenType.MINUS, ForthTokenType.DASH_TRAILING, ForthTokenType.MINUS_TRAILING_GARBAGE,
            ForthTokenType.PRINT, ForthTokenType.DOT_QUOTE, ForthTokenType.DOT_PAREN, ForthTokenType.DOT_R, ForthTokenType.PRINT_KEEP_STACK,
            ForthTokenType.DIVIDE, ForthTokenType.SLASH_MOD, ForthTokenType.SLASH_STRING,
            ForthTokenType.ZERO_LESS, ForthTokenType.ZERO_NOT_EQUALS, ForthTokenType.ZERO_EQUALS, ForthTokenType.ZERO_GREATER,
            ForthTokenType.ONE_PLUS, ForthTokenType.ONE_MINUS,
            ForthTokenType.TWO_STORE, ForthTokenType.TWO_STAR, ForthTokenType.TWO_SLASH, ForthTokenType.TWO_TO_R,
              ForthTokenType.TWO_FETCH, ForthTokenType.TWO_CONSTANT, ForthTokenType.TWO_DROP, ForthTokenType.TWO_DUPE,
              ForthTokenType.TWO_LITERAL, ForthTokenType.TWO_OVER, ForthTokenType.TWO_R_FROM, ForthTokenType.TWO_R_FETCH,
              ForthTokenType.TWO_ROTE, ForthTokenType.TWO_SWAP, ForthTokenType.TWO_VALUE, ForthTokenType.TWO_VARIABLE,
            ForthTokenType.COLON, ForthTokenType.COLON_NO_NAME,
            ForthTokenType.SEMICOLON, ForthTokenType.SEMICOLON_CODE,
            ForthTokenType.LESS_THAN, ForthTokenType.LESS_NUMBER_SIGN, ForthTokenType.NOT_EQUALS, ForthTokenType.EQUALS,
              ForthTokenType.GREATER_THAN, ForthTokenType.LESS_EQUAL, ForthTokenType.GREATER_EQUAL,
            ForthTokenType.TO_BODY, ForthTokenType.TO_FLOAT, ForthTokenType.TO_IN, ForthTokenType.TO_NUMBER, ForthTokenType.TO_R,
            ForthTokenType.QUESTION, ForthTokenType.QUESTION_DO, ForthTokenType.QUESTION_DUPE, ForthTokenType.FETCH,
            ForthTokenType.ABORT, ForthTokenType.ABORT_QUOTE, ForthTokenType.ABS, ForthTokenType.ACCEPT, ForthTokenType.ACTION_OF,
              ForthTokenType.AGAIN, ForthTokenType.AHEAD, ForthTokenType.ALIGN, ForthTokenType.ALIGNED, ForthTokenType.ALLOCATE,
              ForthTokenType.ALLOT, ForthTokenType.ALSO, ForthTokenType.AND, ForthTokenType.ASSEMBLER, ForthTokenType.AT_XY,
            ForthTokenType.BASE, ForthTokenType.BEGIN, ForthTokenType.BEGIN_STRUCTURE, ForthTokenType.BIN, ForthTokenType.BL,
              ForthTokenType.BLANK, ForthTokenType.BLK, ForthTokenType.BLOCK, ForthTokenType.BUFFER, ForthTokenType.BUFFER_COLON,
              ForthTokenType.BYE,
            ForthTokenType.C_STORE, ForthTokenType.C_QUOTE, ForthTokenType.C_COMMA, ForthTokenType.C_FETCH,
              ForthTokenType.CASE, ForthTokenType.CATCH, ForthTokenType.CELL_PLUS, ForthTokenType.CELLS,
              ForthTokenType.CFIELD_COLON, ForthTokenType.CHAR, ForthTokenType.CHAR_PLUS, ForthTokenType.CHARS,
              ForthTokenType.CLOSE_FILE, ForthTokenType.C_MOVE, ForthTokenType.C_MOVE_UP, ForthTokenType.CODE,
              ForthTokenType.COMPARE, ForthTokenType.COMPILE_COMMA, ForthTokenType.CONSTANT, ForthTokenType.COUNT,
              ForthTokenType.CARRIAGE_RETURN, ForthTokenType.CREATE, ForthTokenType.CREATE_FILE, ForthTokenType.C_S_PICK,
              ForthTokenType.C_S_ROLL,
            ForthTokenType.D_PLUS, ForthTokenType.D_MINUS, ForthTokenType.D_DOT, ForthTokenType.D_DOT_R, ForthTokenType.D_ZERO_LESS,
              ForthTokenType.D_ZERO_EQUALS, ForthTokenType.D_TWO_STAR, ForthTokenType.D_TWO_SLASH, ForthTokenType.D_LESS_THAN,
              ForthTokenType.D_EQUALS, ForthTokenType.D_TO_F, ForthTokenType.D_TO_S, ForthTokenType.D_ABS, ForthTokenType.DECIMAL,
              ForthTokenType.DEFER, ForthTokenType.DEFER_STORE, ForthTokenType.DEFER_FETCH, ForthTokenType.DEFINITIONS,
              ForthTokenType.DELETE_FILE, ForthTokenType.DEPTH, ForthTokenType.D_F_STORE, ForthTokenType.D_F_FETCH,
              ForthTokenType.D_F_ALIGN, ForthTokenType.D_F_ALIGNED, ForthTokenType.D_F_FIELD_COLON, ForthTokenType.D_FLOAT_PLUS,
              ForthTokenType.D_FLOATS, ForthTokenType.D_MAX, ForthTokenType.D_MIN, ForthTokenType.D_NEGATE, ForthTokenType.DO,
              ForthTokenType.DOES, ForthTokenType.DROP, ForthTokenType.D_U_LESS, ForthTokenType.DUMP, ForthTokenType.DUPE,
            ForthTokenType.EDITOR, ForthTokenType.E_KEY, ForthTokenType.E_KEY_TO_CHAR, ForthTokenType.E_KEY_TO_F_KEY,
              ForthTokenType.E_KEY_TO_X_CHAR, ForthTokenType.E_KEY_QUESTION, ForthTokenType.ELSE, ForthTokenType.EMIT,
              ForthTokenType.EMIT_QUESTION, ForthTokenType.EMPTY_BUFFERS, ForthTokenType.END_STRUCTURE, ForthTokenType.END_CASE,
              ForthTokenType.END_OF, ForthTokenType.ENVIRONMENT_QUERY, ForthTokenType.ERASE, ForthTokenType.EVALUATE,
              ForthTokenType.EXECUTE, ForthTokenType.EXIT,
            ForthTokenType.F_STORE, ForthTokenType.F_STAR, ForthTokenType.F_STAR_STAR, ForthTokenType.F_PLUS, ForthTokenType.F_MINUS,
              ForthTokenType.F_DOT, ForthTokenType.F_SLASH, ForthTokenType.F_ZERO_LESS_THAN, ForthTokenType.F_ZERO_EQUALS,
              ForthTokenType.F_LESS_THAN, ForthTokenType.F_TO_D, ForthTokenType.F_TO_S, ForthTokenType.F_FETCH,
              ForthTokenType.F_ABS, ForthTokenType.F_A_COS, ForthTokenType.F_A_COSH, ForthTokenType.F_ALIGN,
              ForthTokenType.F_ALIGNED, ForthTokenType.F_A_LOG,
            ForthTokenType.FALSE,
            ForthTokenType.F_A_SIN, ForthTokenType.F_A_SINH, ForthTokenType.F_A_TAN, ForthTokenType.F_A_TAN_TWO,
              ForthTokenType.F_A_TANH, ForthTokenType.F_CONSTANT, ForthTokenType.F_COS, ForthTokenType.F_COSH,
              ForthTokenType.F_DEPTH, ForthTokenType.F_DROP, ForthTokenType.F_DUPE, ForthTokenType.F_E_DOT, ForthTokenType.F_EXP,
              ForthTokenType.F_EXP_M_ONE, ForthTokenType.F_FIELD_COLON,
            ForthTokenType.FIELD_COLON, ForthTokenType.FILE_POSITION, ForthTokenType.FILE_SIZE, ForthTokenType.FILE_STATUS,
              ForthTokenType.FILL, ForthTokenType.FIND, ForthTokenType.F_LITERAL, ForthTokenType.F_L_N, ForthTokenType.F_L_N_P_ONE,
              ForthTokenType.FLOAT_PLUS, ForthTokenType.FLOATS, ForthTokenType.F_LOG, ForthTokenType.FLOOR, ForthTokenType.FLUSH,
              ForthTokenType.FLUSH_FILE, ForthTokenType.F_M_SLASH_MOD, ForthTokenType.F_MAX, ForthTokenType.F_MIN,
              ForthTokenType.F_NEGATE, ForthTokenType.FORGET, ForthTokenType.FORTH, ForthTokenType.FORTH_WORDLIST, ForthTokenType.F_OVER,
              ForthTokenType.FREE, ForthTokenType.F_ROT, ForthTokenType.F_ROUND,
              ForthTokenType.F_S_DOT, ForthTokenType.F_SIN, ForthTokenType.F_SIN_COS, ForthTokenType.F_SINH, ForthTokenType.F_SQRT,
              ForthTokenType.F_SWAP, ForthTokenType.F_TAN, ForthTokenType.F_TANH, ForthTokenType.F_TRUNC,
              ForthTokenType.F_VALUE, ForthTokenType.F_VARIABLE, ForthTokenType.F_PROXIMATE,
            ForthTokenType.GET_CURRENT, ForthTokenType.GET_ORDER,
            ForthTokenType.HERE, ForthTokenType.HEX, ForthTokenType.HOLD, ForthTokenType.HOLDS,
            ForthTokenType.I, ForthTokenType.IF, ForthTokenType.IMMEDIATE, ForthTokenType.INCLUDE, ForthTokenType.INCLUDE_FILE,
              ForthTokenType.INCLUDED, ForthTokenType.INVERT, ForthTokenType.IS,
            ForthTokenType.J,
            ForthTokenType.K_ALT_MASK, ForthTokenType.K_CTRL_MASK, ForthTokenType.K_DELETE, ForthTokenType.K_DOWN,
              ForthTokenType.K_END, ForthTokenType.K_F_1, ForthTokenType.K_F_10, ForthTokenType.K_F_11, ForthTokenType.K_F_12,
              ForthTokenType.K_F_2, ForthTokenType.K_F_3, ForthTokenType.K_F_4, ForthTokenType.K_F_5, ForthTokenType.K_F_6,
              ForthTokenType.K_F_7, ForthTokenType.K_F_8, ForthTokenType.K_F_9, ForthTokenType.K_HOME, ForthTokenType.K_INSERT,
              ForthTokenType.K_LEFT, ForthTokenType.K_NEXT, ForthTokenType.K_PRIOR, ForthTokenType.K_RIGHT, ForthTokenType.K_SHIFT_MASK,
              ForthTokenType.K_UP, ForthTokenType.KEY, ForthTokenType.KEY_QUESTION,
            ForthTokenType.LEAVE, ForthTokenType.LIST, ForthTokenType.LITERAL, ForthTokenType.LOAD, ForthTokenType.LOCALS_BAR,
              ForthTokenType.LOOP, ForthTokenType.L_SHIFT,
            ForthTokenType.M_STAR, ForthTokenType.M_STAR_SLASH, ForthTokenType.M_PLUS, ForthTokenType.MARKER, ForthTokenType.MAX,
              ForthTokenType.MIN, ForthTokenType.MOD, ForthTokenType.MOVE, ForthTokenType.MS,
            ForthTokenType.N_TO_R, ForthTokenType.NAME_TO_COMPILE, ForthTokenType.NAME_TO_INTERPRET, ForthTokenType.NAME_TO_STRING,
              ForthTokenType.NEGATE, ForthTokenType.NIP, ForthTokenType.N_R_FROM,
            ForthTokenType.OF, ForthTokenType.ONLY, ForthTokenType.OPEN_FILE, ForthTokenType.OR, ForthTokenType.ORDER,
              ForthTokenType.OVER,
            ForthTokenType.PAD, ForthTokenType.PAGE, ForthTokenType.PARSE, ForthTokenType.PARSE_NAME, ForthTokenType.PICK,
              ForthTokenType.POSTPONE, ForthTokenType.PRECISION, ForthTokenType.PREVIOUS,
            ForthTokenType.QUIT,
            ForthTokenType.R_O, ForthTokenType.R_W, ForthTokenType.R_FROM, ForthTokenType.R_FETCH, ForthTokenType.READ_FILE,
              ForthTokenType.READ_LINE, ForthTokenType.RECURSE, ForthTokenType.REFILL, ForthTokenType.RENAME_FILE,
              ForthTokenType.REPEAT, ForthTokenType.REPLACES, ForthTokenType.REPOSITION_FILE, ForthTokenType.REPRESENT,
              ForthTokenType.REQUIRE, ForthTokenType.REQUIRED, ForthTokenType.RESIZE, ForthTokenType.RESIZE_FILE, ForthTokenType.RESTORE_INPUT,
              ForthTokenType.ROLL, ForthTokenType.ROT, ForthTokenType.R_SHIFT,
            ForthTokenType.S_QUOTE, ForthTokenType.S_TO_D, ForthTokenType.S_TO_F, ForthTokenType.SAVE_BUFFERS, ForthTokenType.SAVE_INPUT,
              ForthTokenType.S_C_R, ForthTokenType.SEARCH, ForthTokenType.SEARCH_WORDLIST, ForthTokenType.SEE, ForthTokenType.SET_CURRENT,
              ForthTokenType.SET_ORDER, ForthTokenType.SET_PRECISION, ForthTokenType.S_F_STORE, ForthTokenType.S_F_FETCH,
              ForthTokenType.S_F_ALIGN, ForthTokenType.S_F_ALIGNED, ForthTokenType.S_F_FIELD_COLON, ForthTokenType.S_FLOAT_PLUS,
              ForthTokenType.S_FLOATS, ForthTokenType.SIGN, ForthTokenType.SLITERAL, ForthTokenType.S_M_SLASH_REM,
              ForthTokenType.SOURCE, ForthTokenType.SOURCE_ID, ForthTokenType.SPACE, ForthTokenType.SPACES, ForthTokenType.STATE,
              ForthTokenType.SUBSTITUTE, ForthTokenType.SWAP, ForthTokenType.SYNONYM, ForthTokenType.S_BACKSLASH_QUOTE,
            ForthTokenType.THEN, ForthTokenType.THROW, ForthTokenType.THRU, ForthTokenType.TIME_AND_DATE, ForthTokenType.TO,
              ForthTokenType.TRAVERSE_WORDLIST, ForthTokenType.TRUE, ForthTokenType.TUCK, ForthTokenType.TYPE,
            ForthTokenType.U_DOT, ForthTokenType.U_DOT_R, ForthTokenType.U_LESS_THAN, ForthTokenType.U_GREATER_THAN,
              ForthTokenType.U_M_STAR, ForthTokenType.U_M_SLASH_MOD, ForthTokenType.UNESCAPE, ForthTokenType.UNLOOP,
              ForthTokenType.UNTIL, ForthTokenType.UNUSED, ForthTokenType.UPDATE,
            ForthTokenType.VALUE, ForthTokenType.VARIABLE,
            ForthTokenType.W_SLASH_O, ForthTokenType.WHILE, ForthTokenType.WITHIN, ForthTokenType.WORD, ForthTokenType.WORDLIST,
              ForthTokenType.WORDS, ForthTokenType.WRITE_FILE, ForthTokenType.WRITE_LINE,
            ForthTokenType.X_SIZE, ForthTokenType.X_WIDTH, ForthTokenType.X_C_STORE_PLUS, ForthTokenType.X_C_STORE_PLUS_QUERY,
              ForthTokenType.X_C_COMMA, ForthTokenType.X_C_SIZE, ForthTokenType.X_C_WIDTH, ForthTokenType.X_C_FETCH_PLUS,
              ForthTokenType.X_CHAR_PLUS, ForthTokenType.X_CHAR_MINUS, ForthTokenType.X_EMIT, ForthTokenType.X_HOLD,
              ForthTokenType.X_KEY, ForthTokenType.X_KEY_QUERY, ForthTokenType.X_OR, ForthTokenType.X_STRING_MINUS,
            ForthTokenType.LEFT_BRACKET, ForthTokenType.BRACKET_TICK, ForthTokenType.BRACKET_CHAR, ForthTokenType.BRACKET_COMPILE,
              ForthTokenType.BRACKET_DEFINED, ForthTokenType.BRACKET_ELSE, ForthTokenType.BRACKET_IF, ForthTokenType.BRACKET_THEN,
              ForthTokenType.BRACKET_UNDEFINED, ForthTokenType.COMMENT, ForthTokenType.RIGHT_BRACKET, ForthTokenType.BRACE_COLON,
            ForthTokenType.EMPTY_LINE
};

    /**
     * getIndex returns the index of the token based on an entered reserved word.
     *
     * @param strTokenType name of the token
     * @return index found for the token
     */
    public static int getIndex(final String strTokenType) {
        int iReturn = -1;
        int iIndex = 0;

        for (String strReserveWord: _astrReservedWords) {
            if (strTokenType.toUpperCase().matches(Pattern.quote(strReserveWord))) {
                iReturn = iIndex;
                break;
            }

            iIndex++;
        }

        return iReturn;
    }

    /**
     * getTokenIndex returns the index of the token based on an entered token type.
     *
     * @param strTokenType name of the token
     * @return index found for the token
     */
    public static int getTokenIndex(final String strTokenType) {
        int iReturn = -1;
        int iIndex = 0;

        for (ForthTokenType oReserveWord: _aeTokenTypes) {
            if (strTokenType.toUpperCase(Locale.ROOT).matches(oReserveWord.toString())) {
                iReturn = iIndex;
                break;
            }

            iIndex++;
        }

        return iReturn;
    }

    /**
     * getTokenType returns the type of the token based on an entered index value.
     *
     * @param iIndex index of the token
     * @return token type found for the index
     */
    public static ForthTokenType getTokenType(final int iIndex) {
        return _aeTokenTypes[iIndex];
    }

    /**
     * getReservedWord returns the reserved word based on an entered index value.
     *
     * @param iIndex index of the token
     * @return reserved word found for the index
     */
    public static String getReservedWord(final int iIndex) {
        return _astrReservedWords[iIndex];
    }
}
