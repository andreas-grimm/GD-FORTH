package eu.gricom.forth.variableTypes;

import eu.gricom.forth.error.DivideByZeroException;
import eu.gricom.forth.error.SyntaxErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LongValueTest {

    @Test
    public void testToString() {
        LongValue oNumberValue = new LongValue(999);

        String strResult = oNumberValue.toString();
        assertTrue(strResult.matches("999"));
    }

    @Test
    public void testToNumber() {
        LongValue oNumberValue = new LongValue(999);

        double dResult = oNumberValue.toReal();
        assertEquals(dResult, 999);
    }

    @Test
    public void testEvaluate() {
        LongValue oNumberValue = new LongValue(999);
        LongValue oNewValue = (LongValue) oNumberValue.evaluate();

        assertEquals(oNumberValue, oNewValue);
    }

    @Test
    public void testEquals() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResult = (BooleanValue) oFirstValue.equals(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testNotEqual() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResult = (BooleanValue) oFirstValue.notEqual(oSecondValue);
            assertTrue(oResult.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPlus() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.plus(oSecondValue);
            assertTrue(oResultValue.toLong() == 3);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPlusTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.plus(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testMinus() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.minus(oSecondValue);
            assertTrue(oResultValue.toLong() == -1);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMinusTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.minus(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testMultiply() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.multiply(oSecondValue);
            assertTrue(oResultValue.toLong() == 2);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testMultiplyTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.multiply(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testDivide() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            assertTrue(oResultValue.toLong() == 0);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testDivideTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testDivideByZero() {
        LongValue oFirstValue = new LongValue(1);
        LongValue oSecondValue = new LongValue(0);

        assertThrows(DivideByZeroException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.divide(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testModulo() {
        try {
            LongValue oFirstValue = new LongValue(3);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.modulo(oSecondValue);
            assertTrue(oResultValue.toLong() == 1);

        } catch (SyntaxErrorException | DivideByZeroException e) {
            fail();
        }
    }

    @Test
    public void testPower() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(2);

            LongValue oResultValue = (LongValue) oFirstValue.power(oSecondValue);
            assertTrue(oResultValue.toLong() == 4);

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testPowerTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            LongValue oResultValue = (LongValue) oFirstValue.power(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerThan() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerThanTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testSmallerEqualThan() {
        try {
            LongValue oFirstValue = new LongValue(1);
            LongValue oSecondValue = new LongValue(2);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testSmallerEqualThanTypeException() {
        LongValue oFirstValue = new LongValue(1);
        RealValue oSecondValue = new RealValue(2);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.smallerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerThan() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerThanTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    @Test
    public void testLargerEqualThan() {
        try {
            LongValue oFirstValue = new LongValue(2);
            LongValue oSecondValue = new LongValue(1);

            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            assertTrue(oResultValue.isTrue());

        } catch (SyntaxErrorException e) {
            fail();
        }
    }

    @Test
    public void testLargerEqualThanTypeException() {
        LongValue oFirstValue = new LongValue(2);
        RealValue oSecondValue = new RealValue(1);


        assertThrows(SyntaxErrorException.class, () -> {
            BooleanValue oResultValue = (BooleanValue) oFirstValue.largerEqualThan(oSecondValue);
            System.out.println(oResultValue.toString());
        });
    }

    // ============================================================================
    // toInteger() Tests
    // ============================================================================

    @Test
    public void testToIntegerZero() {
        LongValue oValue = new LongValue(0);
        int iResult = oValue.toInteger();
        assertEquals(0, iResult, "toInteger() should convert 0 to 0");
    }

    @Test
    public void testToIntegerPositiveSmallValue() {
        LongValue oValue = new LongValue(42);
        int iResult = oValue.toInteger();
        assertEquals(42, iResult, "toInteger() should convert positive 42 to int 42");
    }

    @Test
    public void testToIntegerNegativeSmallValue() {
        LongValue oValue = new LongValue(-42);
        int iResult = oValue.toInteger();
        assertEquals(-42, iResult, "toInteger() should convert negative -42 to int -42");
    }

    @Test
    public void testToIntegerOne() {
        LongValue oValue = new LongValue(1);
        int iResult = oValue.toInteger();
        assertEquals(1, iResult, "toInteger() should convert 1 to 1");
    }

    @Test
    public void testToIntegerNegativeOne() {
        LongValue oValue = new LongValue(-1);
        int iResult = oValue.toInteger();
        assertEquals(-1, iResult, "toInteger() should convert -1 to -1");
    }

    @Test
    public void testToIntegerMaxValue() {
        LongValue oValue = new LongValue(Integer.MAX_VALUE);
        int iResult = oValue.toInteger();
        assertEquals(Integer.MAX_VALUE, iResult, "toInteger() should convert Integer.MAX_VALUE successfully");
    }

    @Test
    public void testToIntegerMinValue() {
        LongValue oValue = new LongValue(Integer.MIN_VALUE);
        int iResult = oValue.toInteger();
        assertEquals(Integer.MIN_VALUE, iResult, "toInteger() should convert Integer.MIN_VALUE successfully");
    }

    @Test
    public void testToIntegerMaxValuePlus1() {
        LongValue oValue = new LongValue((long) Integer.MAX_VALUE + 1);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for value > Integer.MAX_VALUE");
    }

    @Test
    public void testToIntegerMinValueMinus1() {
        LongValue oValue = new LongValue((long) Integer.MIN_VALUE - 1);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for value < Integer.MIN_VALUE");
    }

    @Test
    public void testToIntegerLargePositiveValue() {
        LongValue oValue = new LongValue(Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for Long.MAX_VALUE");
    }

    @Test
    public void testToIntegerLargeNegativeValue() {
        LongValue oValue = new LongValue(Long.MIN_VALUE);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for Long.MIN_VALUE");
    }

    @Test
    public void testToIntegerJustBelowMaxValue() {
        long lValue = (long) Integer.MAX_VALUE - 1;
        LongValue oValue = new LongValue(lValue);
        int iResult = oValue.toInteger();
        assertEquals((int) lValue, iResult, "toInteger() should convert value just below MAX_VALUE");
    }

    @Test
    public void testToIntegerJustAboveMinValue() {
        long lValue = (long) Integer.MIN_VALUE + 1;
        LongValue oValue = new LongValue(lValue);
        int iResult = oValue.toInteger();
        assertEquals((int) lValue, iResult, "toInteger() should convert value just above MIN_VALUE");
    }

    @Test
    public void testToIntegerLargeValueOutOfRange() {
        LongValue oValue = new LongValue(1_000_000_000_000L);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for very large positive value");
    }

    @Test
    public void testToIntegerNegativeLargeValueOutOfRange() {
        LongValue oValue = new LongValue(-1_000_000_000_000L);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException for very large negative value");
    }

    @Test
    public void testToIntegerTypicalValue() {
        LongValue oValue = new LongValue(12345);
        int iResult = oValue.toInteger();
        assertEquals(12345, iResult, "toInteger() should convert typical positive value");
    }

    @Test
    public void testToIntegerTypicalNegativeValue() {
        LongValue oValue = new LongValue(-12345);
        int iResult = oValue.toInteger();
        assertEquals(-12345, iResult, "toInteger() should convert typical negative value");
    }

    @Test
    public void testToIntegerHundredThousand() {
        LongValue oValue = new LongValue(100_000);
        int iResult = oValue.toInteger();
        assertEquals(100_000, iResult, "toInteger() should convert 100,000");
    }

    @Test
    public void testToIntegerMillionValue() {
        LongValue oValue = new LongValue(1_000_000);
        int iResult = oValue.toInteger();
        assertEquals(1_000_000, iResult, "toInteger() should convert 1,000,000");
    }

    @Test
    public void testToIntegerBoundaryJustAboveMax() {
        long lValue = (long) Integer.MAX_VALUE + 100;
        LongValue oValue = new LongValue(lValue);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw for MAX_VALUE + 100");
    }

    @Test
    public void testToIntegerBoundaryJustBelowMin() {
        long lValue = (long) Integer.MIN_VALUE - 100;
        LongValue oValue = new LongValue(lValue);
        assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw for MIN_VALUE - 100");
    }

    @Test
    public void testToIntegerExceptionMessage() {
        LongValue oValue = new LongValue(Long.MAX_VALUE);
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            oValue.toInteger();
        }, "toInteger() should throw ArithmeticException");
        assertTrue(exception.getMessage().contains("too small or too large"),
            "Exception message should indicate value is out of range");
    }

    @Test
    public void testToIntegerSmallestPositiveInt() {
        LongValue oValue = new LongValue(1);
        int iResult = oValue.toInteger();
        assertEquals(1, iResult, "toInteger() should convert smallest positive int");
    }

    @Test
    public void testToIntegerLargestNegativeInt() {
        LongValue oValue = new LongValue(-1);
        int iResult = oValue.toInteger();
        assertEquals(-1, iResult, "toInteger() should convert largest negative int");
    }

    @Test
    public void testToIntegerPowerOfTwo() {
        LongValue oValue = new LongValue(1024);
        int iResult = oValue.toInteger();
        assertEquals(1024, iResult, "toInteger() should convert power of 2");
    }

    @Test
    public void testToIntegerNegativePowerOfTwo() {
        LongValue oValue = new LongValue(-1024);
        int iResult = oValue.toInteger();
        assertEquals(-1024, iResult, "toInteger() should convert negative power of 2");
    }

    @Test
    public void testToIntegerMultipleConversions() {
        LongValue oValue = new LongValue(999);
        int iResult1 = oValue.toInteger();
        int iResult2 = oValue.toInteger();
        int iResult3 = oValue.toInteger();

        assertEquals(999, iResult1, "First conversion should be 999");
        assertEquals(999, iResult2, "Second conversion should be 999");
        assertEquals(999, iResult3, "Third conversion should be 999");
    }
}
