package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Directory class.
 * Tests word storage and retrieval in the FORTH word dictionary.
 */
class DirectoryTest {
    private Directory directory;

    @BeforeEach
    void setUp() {
        directory = new Directory();
    }

    @Test
    void testDirectoryCreation() {
        assertNotNull(directory);
    }

    @Test
    void testStoreAndRetrieveSimpleWord() throws SyntaxErrorException {
        directory.storeWord("DUP", "dup");
        String result = directory.getWord("DUP");
        assertNotNull(result);
        assertEquals("dup", result);
    }

    @Test
    void testStoreAndRetrieveComplexWord() throws SyntaxErrorException {
        directory.storeWord("SQUARE", "DUP *");
        String result = directory.getWord("SQUARE");
        assertNotNull(result);
        assertEquals("DUP *", result);
    }

    @Test
    void testGetNonExistentWord() {
        String result = directory.getWord("NONEXISTENT");
        assertNull(result);
    }

    @Test
    void testStoreMultipleWords() throws SyntaxErrorException {
        directory.storeWord("DOUBLE", "2 *");
        directory.storeWord("TRIPLE", "3 *");
        directory.storeWord("SQUARE", "DUP *");

        assertEquals("2 *", directory.getWord("DOUBLE"));
        assertEquals("3 *", directory.getWord("TRIPLE"));
        assertEquals("DUP *", directory.getWord("SQUARE"));
    }

    @Test
    void testOverwriteExistingWord() throws SyntaxErrorException {
        directory.storeWord("SQUARE", "DUP *");
        directory.storeWord("SQUARE", "DUP DUP *");

        String result = directory.getWord("SQUARE");
        assertEquals("DUP DUP *", result);
    }

    @Test
    void testGetTokensForWord() throws SyntaxErrorException {
        directory.storeWord("ADD2", "2 +");
        List<Token> tokens = directory.getToken("ADD2");
        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
    }

    @Test
    void testGetTokensForNonExistentWord() {
        List<Token> tokens = directory.getToken("NONEXISTENT");
        assertNull(tokens);
    }

    @Test
    void testListWordsEmpty() {
        String[] words = directory.listWords();
        assertNotNull(words);
    }

    @Test
    void testListWordsSingleWord() throws SyntaxErrorException {
        directory.storeWord("DUP", "dup");
        String[] words = directory.listWords();
        assertNotNull(words);
        assertTrue(words.length > 0);
    }

    @Test
    void testListWordsMultipleWords() throws SyntaxErrorException {
        directory.storeWord("DUP", "dup");
        directory.storeWord("SWAP", "swap");
        directory.storeWord("OVER", "over");

        String[] words = directory.listWords();
        assertNotNull(words);
        assertTrue(words.length >= 3);
    }

    @Test
    void testListWordsSorted() throws SyntaxErrorException {
        directory.storeWord("ZEBRA", "z");
        directory.storeWord("APPLE", "a");
        directory.storeWord("MIDDLE", "m");

        String[] words = directory.listWords();
        assertTrue(words.length >= 3);
        // Verify entries are in sorted order by word name
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].split(" ")[0];
            String word2 = words[i + 1].split(" ")[0];
            assertTrue(word1.compareTo(word2) <= 0);
        }
    }

    @Test
    void testTokenizeNewWord() throws SyntaxErrorException {
        List<Token> tokens = directory.tokenizeNewWord("2 3 +");
        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
    }

    @Test
    void testTokenizeNewWordWithNumbers() throws SyntaxErrorException {
        List<Token> tokens = directory.tokenizeNewWord("42");
        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals(ForthTokenType.NUMBER, tokens.get(0).getType());
    }

    @Test
    void testTokenizeNewWordWithOperators() throws SyntaxErrorException {
        List<Token> tokens = directory.tokenizeNewWord("+");
        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
        assertEquals(ForthTokenType.PLUS, tokens.get(0).getType());
    }

    @Test
    void testTokenizeComplexDefinition() throws SyntaxErrorException {
        List<Token> tokens = directory.tokenizeNewWord("DUP 2 * SWAP 3 +");
        assertNotNull(tokens);
        assertTrue(tokens.size() > 0);
    }

    @Test
    void testWordDefinitionWithArithmetic() throws SyntaxErrorException {
        directory.storeWord("ADDTEN", "10 +");
        List<Token> tokens = directory.getToken("ADDTEN");
        assertNotNull(tokens);
        assertTrue(tokens.size() >= 2);
    }

    @Test
    void testEmptyWordDefinition() throws SyntaxErrorException {
        // Empty string is a valid (though useless) definition
        directory.storeWord("EMPTY", "");
        String result = directory.getWord("EMPTY");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void testWordWithSpecialCharacters() throws SyntaxErrorException {
        directory.storeWord("SPECIAL", "DUP DUP DUP");
        String result = directory.getWord("SPECIAL");
        assertEquals("DUP DUP DUP", result);
    }

    @Test
    void testCaseSensitiveWordNames() throws SyntaxErrorException {
        directory.storeWord("DUP", "duplicate");
        directory.storeWord("dup", "lowercase");

        assertEquals("duplicate", directory.getWord("DUP"));
        assertEquals("lowercase", directory.getWord("dup"));
    }

    @Test
    void testListWordsIncludesDefinedWords() throws SyntaxErrorException {
        directory.storeWord("TEST1", "1");
        directory.storeWord("TEST2", "2");

        String[] words = directory.listWords();
        boolean foundTest1 = false;
        boolean foundTest2 = false;

        for (String word : words) {
            if (word.startsWith("TEST1")) foundTest1 = true;
            if (word.startsWith("TEST2")) foundTest2 = true;
        }

        assertTrue(foundTest1);
        assertTrue(foundTest2);
    }

    @Test
    void testWordFormatInListWords() throws SyntaxErrorException {
        directory.storeWord("WORD", "definition");
        String[] words = directory.listWords();

        boolean foundCorrectFormat = false;
        for (String word : words) {
            if (word.equals("WORD -> definition")) {
                foundCorrectFormat = true;
                break;
            }
        }
        assertTrue(foundCorrectFormat);
    }

    @Test
    void testDirectoryWithStackIntegration() throws SyntaxErrorException {
        Directory dir = new Directory();
        Stack stack = new Stack();

        dir.storeWord("DOUBLE", "2 *");

        assertNotNull(dir);
        assertNotNull(stack);
        assertNotNull(dir.getWord("DOUBLE"));
    }

    @Test
    void testDirectoryWithProgramIntegration() throws SyntaxErrorException {
        Directory dir = new Directory();
        Program program = new Program();

        dir.storeWord("TRIPLE", "3 *");

        assertNotNull(dir);
        assertNotNull(program);
        assertNotNull(dir.getWord("TRIPLE"));
    }

    @Test
    void testMultiWordDefinitions() throws SyntaxErrorException {
        directory.storeWord("ADDMUL", "2 + 3 *");
        List<Token> tokens = directory.getToken("ADDMUL");

        assertNotNull(tokens);
        // "2 + 3 *" tokenizes to: NUMBER(2), PLUS, NUMBER(3), MULTIPLY, and possibly EOP
        assertTrue(tokens.size() >= 4);
    }

    @Test
    void testRetrieveWordAfterMultipleStores() throws SyntaxErrorException {
        directory.storeWord("WORD1", "def1");
        directory.storeWord("WORD2", "def2");
        directory.storeWord("WORD3", "def3");
        directory.storeWord("WORD4", "def4");

        assertEquals("def1", directory.getWord("WORD1"));
        assertEquals("def2", directory.getWord("WORD2"));
        assertEquals("def3", directory.getWord("WORD3"));
        assertEquals("def4", directory.getWord("WORD4"));
    }
}
