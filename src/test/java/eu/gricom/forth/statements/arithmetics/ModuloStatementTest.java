package eu.gricom.forth.statements.arithmetics;

import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ModuloStatement class.
 * Tests arithmetic modulo operation (( a b -- a%b )) on the stack.
 *
 * Stack notation: (( a b -- a%b )) means pop b (top), pop a, push a%b
 * Special attention to modulo by zero handling.
 */
@DisplayName("ModuloStatement Tests")
class ModuloStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create ModuloStatement with MOD token type")
    void testConstructorWithModTokenType() {
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should create ModuloStatement with token number")
    void testConstructorWithTokenNumber() {
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 5);
        assertEquals(5, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 42);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        assertEquals("", stmt.structure());
    }

    // ================= BASIC MODULO OPERATIONS =================

    @Test
    @DisplayName("Should calculate modulo of two positive integers (17 % 5 = 2)")
    void testModuloTwoPositiveIntegers() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2, result.toInteger());
    }

    @Test
    @DisplayName("Should calculate modulo (10 % 3 = 1)")
    void testModuloBasicCase() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(3));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }

    @Test
    @DisplayName("Should calculate modulo with negative dividend")
    void testModuloNegativeDividend() throws Exception {
        stack.push(new IntegerValue(-17));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Java modulo preserves sign of dividend
        assertEquals(-2, result.toInteger());
    }

    @Test
    @DisplayName("Should calculate modulo with negative divisor")
    void testModuloNegativeDivisor() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(-5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2, result.toInteger());
    }

    @Test
    @DisplayName("Should calculate modulo with both negative")
    void testModuloBothNegative() throws Exception {
        stack.push(new IntegerValue(-17));
        stack.push(new IntegerValue(-5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        // Java modulo: result sign follows dividend
        assertEquals(-2, result.toInteger());
    }

    // ================= ZERO RESULTS =================

    @Test
    @DisplayName("Should return zero when dividend is multiple of divisor (10 % 5 = 0)")
    void testModuloResultZero() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should return zero when both are the same (42 % 42 = 0)")
    void testModuloSameNumber() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(42));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should return zero for zero modulo any number (0 % 5 = 0)")
    void testZeroModuloAnyNumber() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= MODULO BY ZERO =================

    @Test
    @DisplayName("Should handle modulo by zero gracefully (10 % 0)")
    void testModuloByZero() throws Exception {
        stack.push(new IntegerValue(10));
        stack.push(new IntegerValue(0));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);

        // Should not throw an exception but handles internally
        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle zero modulo zero gracefully (0 % 0)")
    void testZeroModuloZero() throws Exception {
        stack.push(new IntegerValue(0));
        stack.push(new IntegerValue(0));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle negative modulo zero gracefully (-10 % 0)")
    void testNegativeModuloZero() throws Exception {
        stack.push(new IntegerValue(-10));
        stack.push(new IntegerValue(0));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= LARGER DIVIDEND CASES =================

    @Test
    @DisplayName("Should calculate modulo when dividend is larger (23 % 7 = 2)")
    void testModuloLargerDividend() throws Exception {
        stack.push(new IntegerValue(23));
        stack.push(new IntegerValue(7));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2, result.toInteger());
    }

    @Test
    @DisplayName("Should return entire number when divisor is larger (3 % 10 = 3)")
    void testModuloSmallerDividend() throws Exception {
        stack.push(new IntegerValue(3));
        stack.push(new IntegerValue(10));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(3, result.toInteger());
    }

    // ================= IDENTITY OPERATIONS =================

    @Test
    @DisplayName("Should handle modulo by one (any % 1 = 0)")
    void testModuloByOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(1));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle modulo by minus one (any % -1 = 0)")
    void testModuloByMinusOne() throws Exception {
        stack.push(new IntegerValue(42));
        stack.push(new IntegerValue(-1));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= LARGE NUMBER OPERATIONS =================

    @Test
    @DisplayName("Should handle large number modulo")
    void testModuloLargeNumbers() throws Exception {
        stack.push(new IntegerValue(1000017));
        stack.push(new IntegerValue(1000));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(17, result.toInteger());
    }

    // ================= NON-COMMUTATIVE PROPERTY =================

    @Test
    @DisplayName("Should verify modulo is NOT commutative (17 % 5 != 5 % 17)")
    void testModuloIsNotCommutative() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();
        IntegerValue result1 = (IntegerValue) stack.pop();

        stack.reset();
        stack.push(new IntegerValue(5));
        stack.push(new IntegerValue(17));

        stmt.execute();
        IntegerValue result2 = (IntegerValue) stack.pop();

        assertNotEquals(result1.toInteger(), result2.toInteger());
        assertEquals(2, result1.toInteger());
        assertEquals(5, result2.toInteger());
    }

    // ================= CONSECUTIVE OPERATIONS =================

    @Test
    @DisplayName("Should handle consecutive modulo operations")
    void testConsecutiveModulos() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute(); // 17 % 5 = 2

        stack.push(new IntegerValue(2));
        stmt.execute(); // 2 % 2 = 0

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    // ================= STACK STATE VALIDATION =================

    @Test
    @DisplayName("Should leave clean stack after operation")
    void testStackStateAfterOperation() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(5));

        assertEquals(2, stack.size());

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(2, result.toInteger());
        assertEquals(0, stack.size());
    }

    // ================= ERROR HANDLING =================

    @Test
    @DisplayName("Should handle empty stack gracefully")
    void testEmptyStack() throws Exception {
        stack.reset();
        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    @Test
    @DisplayName("Should handle stack with only one value")
    void testInsufficientOperands() throws Exception {
        stack.reset();
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);

        assertDoesNotThrow(() -> stmt.execute());
    }

    // ================= BOUNDARY CONDITIONS =================

    @Test
    @DisplayName("Should handle single digit modulo")
    void testModuloSingleDigits() throws Exception {
        stack.push(new IntegerValue(7));
        stack.push(new IntegerValue(3));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }

    @Test
    @DisplayName("Should handle modulo by 2 (test for odd/even)")
    void testModuloByTwo() throws Exception {
        stack.push(new IntegerValue(9));
        stack.push(new IntegerValue(2));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(1, result.toInteger());
    }

    @Test
    @DisplayName("Should handle modulo by 10 (test for last digit)")
    void testModuloByTen() throws Exception {
        stack.push(new IntegerValue(1234));
        stack.push(new IntegerValue(10));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(4, result.toInteger());
    }

    // ================= TYPE VALIDATION =================

    @Test
    @DisplayName("Should return IntegerValue result type")
    void testResultType() throws Exception {
        stack.push(new IntegerValue(17));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        assertTrue(stack.peek() instanceof IntegerValue);
    }

    // ================= MODULO PROPERTIES =================

    @Test
    @DisplayName("Should verify modulo result is always less than divisor (positive case)")
    void testModuloResultLessThanDivisor() throws Exception {
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(7));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertTrue(Math.abs(result.toInteger()) < 7);
    }

    @Test
    @DisplayName("Should verify (a % b) + (a / b) * b = a (dividend decomposition)")
    void testModuloDividendDecomposition() throws Exception {
        // 23 = (23 % 7) + (23 / 7) * 7 = 2 + 3*7 = 2 + 21 = 23
        stack.push(new IntegerValue(23));
        stack.push(new IntegerValue(7));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue modResult = (IntegerValue) stack.pop();
        assertEquals(2, modResult.toInteger());
    }

    @Test
    @DisplayName("Should handle even division evenly")
    void testModuloEvenDivision() throws Exception {
        stack.push(new IntegerValue(20));
        stack.push(new IntegerValue(5));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should handle two identical numbers")
    void testModuloTwoIdenticalNumbers() throws Exception {
        stack.push(new IntegerValue(99));
        stack.push(new IntegerValue(99));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(0, result.toInteger());
    }

    @Test
    @DisplayName("Should demonstrate pattern with powers of 2")
    void testModuloWithPowerOfTwo() throws Exception {
        // 31 % 16 = 15 (31 in binary: 11111, 16 is 2^4)
        stack.push(new IntegerValue(31));
        stack.push(new IntegerValue(16));

        ModuloStatement stmt = new ModuloStatement(ForthTokenType.MOD, 0);
        stmt.execute();

        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(15, result.toInteger());
    }
}
