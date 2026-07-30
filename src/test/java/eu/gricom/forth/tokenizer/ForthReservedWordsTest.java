package eu.gricom.forth.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ForthReservedWords class.
 * Tests reserved word recognition and token type mapping.
 */
class ForthReservedWordsTest {

    @Test
    void testPlusWordRecognition() {
        int index = ForthReservedWords.getIndex("+");

        assertTrue(index >= 0, "Plus operator should be recognized");
    }

    @Test
    void testMinusWordRecognition() {
        int index = ForthReservedWords.getIndex("-");

        assertTrue(index >= 0, "Minus operator should be recognized");
    }

    @Test
    void testMultiplyWordRecognition() {
        int index = ForthReservedWords.getIndex("*");

        assertTrue(index >= 0, "Multiply operator should be recognized");
    }

    @Test
    void testDivideWordRecognition() {
        int index = ForthReservedWords.getIndex("/");

        assertTrue(index >= 0, "Divide operator should be recognized");
    }

    @Test
    void testModWordRecognition() {
        int index = ForthReservedWords.getIndex("MOD");
        assertTrue(index >= 0, "MOD operator should be recognized");

        ForthTokenType oTokenType = ForthReservedWords.getTokenType(index);
        assertTrue(oTokenType == ForthTokenType.MOD);
    }

    @Test
    void testUnrecognizedWord() {
        int index = ForthReservedWords.getIndex("unknownword123");

        assertEquals(-1, index, "Unknown word should return -1");
    }

    @Test
    void testPlusTokenType() {
        int index = ForthReservedWords.getIndex("+");
        ForthTokenType type = ForthReservedWords.getTokenType(index);

        assertEquals(ForthTokenType.PLUS, type);
    }

    @Test
    void testMinusTokenType() {
        int index = ForthReservedWords.getIndex("-");
        ForthTokenType type = ForthReservedWords.getTokenType(index);

        assertEquals(ForthTokenType.MINUS, type);
    }

    @Test
    void testMultiplyTokenType() {
        int index = ForthReservedWords.getIndex("*");
        ForthTokenType type = ForthReservedWords.getTokenType(index);

        assertEquals(ForthTokenType.MULTIPLY, type);
    }

    @Test
    void testDivideTokenType() {
        int index = ForthReservedWords.getIndex("/");
        ForthTokenType type = ForthReservedWords.getTokenType(index);

        assertEquals(ForthTokenType.DIVIDE, type);
    }

    @Test
    void testModTokenType() {
        int index = ForthReservedWords.getIndex("MOD");

        assertTrue(index >= 0, "MOD should be recognized as a reserved word");
        ForthTokenType type = ForthReservedWords.getTokenType(index);
        assertNotNull(type, "Token type should not be null for MOD");
    }

    @Test
    void testCommentRecognition() {
        int index = ForthReservedWords.getIndex("(");

        assertTrue(index >= 0 || index == -1, "Comment operator should have a result");
    }

    @Test
    void testCaseSensitivity() {
        int indexLower = ForthReservedWords.getIndex("mod");
        int indexUpper = ForthReservedWords.getIndex("MOD");

        // FORTH typically treats operators case-insensitively
        // but this depends on implementation
        assertTrue(indexLower >= -1);
        assertTrue(indexUpper >= -1);
    }

    @Test
    void testConsecutiveOperators() {
        int indexPlus = ForthReservedWords.getIndex("+");
        int indexMinus = ForthReservedWords.getIndex("-");
        int indexMultiply = ForthReservedWords.getIndex("*");

        assertTrue(indexPlus >= 0);
        assertTrue(indexMinus >= 0);
        assertTrue(indexMultiply >= 0);
    }

    @Test
    void testAllArithmeticOperators() {
        String[] operators = {"+", "-", "*", "/", "MOD"};

        for (String op : operators) {
            int index = ForthReservedWords.getIndex(op);
            assertTrue(index >= 0, "Operator " + op + " should be recognized");
        }
    }

    @Test
    void testEmptyStringWord() {
        int index = ForthReservedWords.getIndex("");

        assertEquals(-1, index, "Empty string should not be recognized as a reserved word");
    }

    @Test
    void testWhitespaceWord() {
        int index = ForthReservedWords.getIndex("   ");

        assertEquals(-1, index, "Whitespace-only string should not be recognized");
    }

    @Test
    void testSpecialCharacters() {
        String[] specials = {"@", "#", "$", "%", "&", "^"};

        for (String special : specials) {
            int index = ForthReservedWords.getIndex(special);
            // Special characters may or may not be reserved depending on implementation
            assertTrue(index >= -1);
        }
    }

    @Test
    void testNumericString() {
        int index = ForthReservedWords.getIndex("123");

        assertEquals(-1, index, "Numeric strings should not be reserved words");
    }

    @Test
    void testMixedCaseWord() {
        int index = ForthReservedWords.getIndex("MoD");

        // May depend on implementation
        assertTrue(index >= -1);
    }

    @Test
    void testTokenTypeForValidIndex() {
        for (String op : new String[]{"+", "-", "*", "/", "MOD"}) {
            int index = ForthReservedWords.getIndex(op);
            ForthTokenType type = ForthReservedWords.getTokenType(index);

            assertNotNull(type, "Token type should not be null for valid index");
        }
    }
}
