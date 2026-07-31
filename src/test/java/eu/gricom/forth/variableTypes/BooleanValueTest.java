package eu.gricom.forth.variableTypes;

import eu.gricom.forth.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BooleanValue class.
 * Tests boolean operations and type conversions.
 */
public class BooleanValueTest {

    @Test
    public void testToStringTrue() {
        BooleanValue oValue = new BooleanValue(true);
        String strResult = oValue.toString();
        assertEquals("TRUE", strResult);
    }

    @Test
    public void testToStringFalse() {
        BooleanValue oValue = new BooleanValue(false);
        String strResult = oValue.toString();
        assertEquals("FALSE", strResult);
    }

    @Test
    public void testFromIntegerTrue() {
        BooleanValue oValue = new BooleanValue(-1);  // TRUE representation
        assertEquals("TRUE", oValue.toString());
    }

    @Test
    public void testFromIntegerFalse() {
        BooleanValue oValue = new BooleanValue(0);   // FALSE representation
        assertEquals("FALSE", oValue.toString());
    }

    @Test
    public void testFromIntegerNonZero() {
        BooleanValue oValue = new BooleanValue(1);   // Any non--1 value is false
        assertEquals("FALSE", oValue.toString());
    }

    @Test
    public void testToRealTrue() {
        BooleanValue oValue = new BooleanValue(true);
        double dResult = oValue.toReal();
        assertEquals(-1, dResult);  // TRUE = -1
    }

    @Test
    public void testToRealFalse() {
        BooleanValue oValue = new BooleanValue(false);
        double dResult = oValue.toReal();
        assertEquals(0, dResult);   // FALSE = 0
    }

    @Test
    public void testToIntegerTrue() {
        BooleanValue oValue = new BooleanValue(true);
        int iResult = oValue.toInteger();
        assertEquals(-1, iResult);  // TRUE = -1
    }

    @Test
    public void testToIntegerFalse() {
        BooleanValue oValue = new BooleanValue(false);
        int iResult = oValue.toInteger();
        assertEquals(0, iResult);   // FALSE = 0
    }

    @Test
    public void testEvaluate() {
        BooleanValue oValue = new BooleanValue(true);
        Value oNewValue = oValue.evaluate();

        assertNotNull(oNewValue);
        assertTrue(((BooleanValue) oNewValue).isTrue());
    }

    @Test
    public void testIsTruePositive() {
        BooleanValue oValue = new BooleanValue(true);
        assertTrue(oValue.isTrue());
    }

    @Test
    public void testIsTrueNegative() {
        BooleanValue oValue = new BooleanValue(false);
        assertFalse(oValue.isTrue());
    }

    @Test
    public void testEqualsTrue() throws SyntaxErrorException {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(-1);

        BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testEqualsFalse() throws SyntaxErrorException {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(0);

        BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testEqualsWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.equals(oSecondValue)
        );
    }

    @Test
    public void testNotEqualTrue() throws SyntaxErrorException {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testNotEqualFalse() throws SyntaxErrorException {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testNotEqualWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        RealValue oSecondValue = new RealValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.notEqual(oSecondValue)
        );
    }

    @Test
    public void testPlusSameValue() throws SyntaxErrorException {
        BooleanValue oValue = new BooleanValue(true);
        BooleanValue oResult = (BooleanValue) oValue.plus(oValue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testPlusDifferentValues() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oFalse = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oTrue.plus(oFalse);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testPlusFalseFalse() throws SyntaxErrorException {
        BooleanValue oFalse = new BooleanValue(false);
        BooleanValue oResult = (BooleanValue) oFalse.plus(oFalse);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testPlusWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.plus(oSecondValue)
        );
    }

    @Test
    public void testMinusThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.minus(oSecondValue)
        );
    }

    @Test
    public void testMultiplyTrueTrue() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oResult = (BooleanValue) oTrue.multiply(oTrue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testMultiplyTrueFalse() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oFalse = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oTrue.multiply(oFalse);
        assertTrue(oResult.isTrue());  // true * false = true (per implementation)
    }

    @Test
    public void testMultiplyFalseFalse() throws SyntaxErrorException {
        BooleanValue oFalse = new BooleanValue(false);
        BooleanValue oResult = (BooleanValue) oFalse.multiply(oFalse);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testMultiplyWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.multiply(oSecondValue)
        );
    }

    @Test
    public void testDivideThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.divide(oSecondValue)
        );
    }

    @Test
    public void testModuloThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.modulo(oSecondValue)
        );
    }

    @Test
    public void testShiftLeftThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.shiftLeft(oSecondValue)
        );
    }

    @Test
    public void testShiftRightThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.shiftRight(oSecondValue)
        );
    }

    @Test
    public void testAndTrueTrue() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oResult = (BooleanValue) oTrue.and(oTrue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testAndTrueFalse() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oFalse = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oTrue.and(oFalse);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testAndFalseFalse() throws SyntaxErrorException {
        BooleanValue oFalse = new BooleanValue(false);
        BooleanValue oResult = (BooleanValue) oFalse.and(oFalse);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testAndWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.and(oSecondValue)
        );
    }

    @Test
    public void testOrTrueTrue() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oResult = (BooleanValue) oTrue.or(oTrue);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testOrTrueFalse() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oFalse = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oTrue.or(oFalse);
        assertTrue(oResult.isTrue());
    }

    @Test
    public void testOrFalseFalse() throws SyntaxErrorException {
        BooleanValue oFalse = new BooleanValue(false);
        BooleanValue oResult = (BooleanValue) oFalse.or(oFalse);
        assertFalse(oResult.isTrue());
    }

    @Test
    public void testOrWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.or(oSecondValue)
        );
    }

    @Test
    public void testPowerXorTrueFalse() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oFalse = new BooleanValue(false);

        BooleanValue oResult = (BooleanValue) oTrue.power(oFalse);
        assertTrue(oResult.isTrue());  // true XOR false = true
    }

    @Test
    public void testPowerXorTrueTrue() throws SyntaxErrorException {
        BooleanValue oTrue = new BooleanValue(true);
        BooleanValue oResult = (BooleanValue) oTrue.power(oTrue);
        assertFalse(oResult.isTrue());  // true XOR true = false
    }

    @Test
    public void testPowerXorFalseFalse() throws SyntaxErrorException {
        BooleanValue oFalse = new BooleanValue(false);
        BooleanValue oResult = (BooleanValue) oFalse.power(oFalse);
        assertFalse(oResult.isTrue());  // false XOR false = false
    }

    @Test
    public void testPowerWrongType() {
        BooleanValue oFirstValue = new BooleanValue(true);
        IntegerValue oSecondValue = new IntegerValue(1);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.power(oSecondValue)
        );
    }

    @Test
    public void testSmallerThanThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.smallerThan(oSecondValue)
        );
    }

    @Test
    public void testSmallerEqualThanThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(false);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.smallerEqualThan(oSecondValue)
        );
    }

    @Test
    public void testLargerThanThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.largerThan(oSecondValue)
        );
    }

    @Test
    public void testLargerEqualThanThrowsException() {
        BooleanValue oFirstValue = new BooleanValue(true);
        BooleanValue oSecondValue = new BooleanValue(true);

        assertThrows(SyntaxErrorException.class, () ->
            oFirstValue.largerEqualThan(oSecondValue)
        );
    }

    @Test
    public void testContent() {
        BooleanValue oTrue = new BooleanValue(true);
        String strContent = oTrue.content();
        assertEquals("true", strContent);

        BooleanValue oFalse = new BooleanValue(false);
        strContent = oFalse.content();
        assertEquals("false", strContent);
    }

    @Test
    public void testStructure() throws Exception {
        BooleanValue oTrue = new BooleanValue(true);
        String strStructure = oTrue.structure();
        assertEquals("TRUE", strStructure);

        BooleanValue oFalse = new BooleanValue(false);
        strStructure = oFalse.structure();
        assertEquals("FALSE", strStructure);
    }
}
