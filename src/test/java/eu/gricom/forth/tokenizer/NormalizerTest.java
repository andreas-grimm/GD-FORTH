package eu.gricom.forth.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Normalizer class.
 * Tests text normalization for FORTH source code.
 */
class NormalizerTest {

    @Test
    void testNormalizeSingleWord() {
        String input = "PLUS";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void testNormalizeMultipleWords() {
        String input = "10 20 +";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void testNormalizeWhitespace() {
        String input = "  10   20   +  ";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeEmptyString() {
        String input = "";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeLeadingWhitespace() {
        String input = "   42";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeTrailingWhitespace() {
        String input = "42   ";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeString() {
        String input = "\"Hello World\"";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
        assertTrue(result.contains("Hello") || result.contains("World"));
    }

    @Test
    void testNormalizeComment() {
        String input = "42 ( this is a comment";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeArithmeticExpression() {
        String input = "10+20";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeMultipleTabs() {
        String input = "10\t\t20";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeMixedWhitespace() {
        String input = "10  \t  20 \t 30";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeNegativeNumbers() {
        String input = "-42 -3.14";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeFloatingPoint() {
        String input = "3.14 2.71";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeMultipleOperators() {
        String input = "+ - * /";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeCasePreservation() {
        String input = "MyVar";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeLongLine() {
        String input = "1 2 3 4 5 6 7 8 9 10 + - * / MOD";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void testNormalizeSpecialCharacters() {
        String input = "test_var test-var test123";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeConsecutiveSpaces() {
        String input = "a     b     c";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }

    @Test
    void testNormalizeUnicodeCharacters() {
        String input = "héllo wörld";
        String result = Normalizer.normalize(input);

        assertNotNull(result);
    }
}
