package eu.gricom.forth.statements.controlFlow;

import eu.gricom.forth.error.EmptyStackException;
import eu.gricom.forth.memoryManager.Stack;
import eu.gricom.forth.statements.NumberStatement;
import eu.gricom.forth.statements.Statement;
import eu.gricom.forth.statements.inOut.PrintStatement;
import eu.gricom.forth.tokenizer.ForthTokenType;
import eu.gricom.forth.tokenizer.Token;
import eu.gricom.forth.variableTypes.BooleanValue;
import eu.gricom.forth.variableTypes.IntegerValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for IfStatement class.
 * Tests conditional execution with IF...THEN and IF...ELSE...THEN blocks.
 * <p>
 * Stack notation: ( flag -- ) means pop one flag value.
 * TRUE = -1, FALSE = 0; any non-zero is treated as true.
 */
@DisplayName("IfStatement Tests")
class IfStatementTest {

    private Stack stack;

    @BeforeEach
    void setUp() {
        stack = new Stack();
        stack.reset();
    }

    // ================= CONSTRUCTOR & BASIC TESTS =================

    @Test
    @DisplayName("Should create IfStatement with token and branches")
    void testConstructor() {
        List<Statement> aoTrue = new ArrayList<>();
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        assertNotNull(stmt);
        assertTrue(stmt instanceof Statement);
    }

    @Test
    @DisplayName("Should return correct token number")
    void testGetTokenNumber() {
        List<Statement> aoTrue = new ArrayList<>();
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        IfStatement stmt = new IfStatement(oToken, 42, aoTrue, aoFalse);
        assertEquals(42, stmt.getTokenNumber());
    }

    @Test
    @DisplayName("Should return empty string for content()")
    void testContent() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        assertEquals("", stmt.content());
    }

    @Test
    @DisplayName("Should return empty string for structure()")
    void testStructure() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        assertEquals("", stmt.structure());
    }

    // ================= TRUE BRANCH TESTS =================

    @Test
    @DisplayName("Should execute true-branch when flag is TRUE (-1)")
    void testExecuteTrueBranchWithTrue() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.TRUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Flag should be consumed, 42 should be on stack
        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    @Test
    @DisplayName("Should execute true-branch when flag is non-zero (5)")
    void testExecuteTrueBranchWithNonZeroPositive() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(99, 0));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(5));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(99, result.toInteger());
    }

    @Test
    @DisplayName("Should execute true-branch when flag is non-zero negative (-5)")
    void testExecuteTrueBranchWithNonZeroNegative() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(77, 0));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(-5));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(77, result.toInteger());
    }

    // ================= FALSE BRANCH TESTS (NO ELSE) =================

    @Test
    @DisplayName("Should execute nothing when flag is FALSE (0) with no else-branch")
    void testExecuteNothingWhenFalseNoElse() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.FALSE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Flag consumed, no statements executed
        assertEquals(0, stack.size());
    }

    // ================= ELSE BRANCH TESTS =================

    @Test
    @DisplayName("Should execute false-branch (else) when flag is FALSE (0)")
    void testExecuteFalseBranchWithFalse() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));

        List<Statement> aoFalse = new ArrayList<>();
        aoFalse.add(new NumberStatement(99, 1));

        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.FALSE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Should execute else-branch (99)
        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(99, result.toInteger());
    }

    @Test
    @DisplayName("Should execute true-branch (not else) when flag is TRUE with ELSE present")
    void testSkipElseBranchWhenTrue() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));

        List<Statement> aoFalse = new ArrayList<>();
        aoFalse.add(new NumberStatement(99, 1));

        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.TRUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Should execute true-branch only (42, not 99)
        assertEquals(1, stack.size());
        IntegerValue result = (IntegerValue) stack.pop();
        assertEquals(42, result.toInteger());
    }

    // ================= MULTIPLE STATEMENT BRANCHES =================

    @Test
    @DisplayName("Should execute multiple statements in true-branch")
    void testMultipleStatementsInTrueBranch() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(1, 0));
        aoTrue.add(new NumberStatement(2, 1));
        aoTrue.add(new NumberStatement(3, 2));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.TRUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // All three numbers pushed
        assertEquals(3, stack.size());
        assertEquals(3, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(2, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(1, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    @DisplayName("Should execute multiple statements in false-branch")
    void testMultipleStatementsInFalseBranch() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(99, 0));

        List<Statement> aoFalse = new ArrayList<>();
        aoFalse.add(new NumberStatement(10, 1));
        aoFalse.add(new NumberStatement(20, 2));

        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.FALSE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Execute false-branch: 10 and 20
        assertEquals(2, stack.size());
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
    }

    // ================= NESTED IF TESTS =================

    @Test
    @DisplayName("Should handle nested IF inside true-branch")
    void testNestedIfInTrueBranch() throws Exception {
        // Inner IF: pushes 100 if true
        List<Statement> aoInnerTrue = new ArrayList<>();
        aoInnerTrue.add(new NumberStatement(100, 0));
        List<Statement> aoInnerFalse = new ArrayList<>();
        Token oInnerToken = new Token("IF", ForthTokenType.IF, 1);
        IfStatement oInnerIf = new IfStatement(oInnerToken, 1, aoInnerTrue, aoInnerFalse);

        // Outer IF: contains inner IF
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(oInnerIf);
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        // Outer condition TRUE, inner condition TRUE
        stack.push(new IntegerValue(BooleanValue.TRUE));   // for inner IF
        stack.push(new IntegerValue(BooleanValue.TRUE));   // for outer IF

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Both branches executed: 100 on stack
        assertEquals(1, stack.size());
        assertEquals(100, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    @DisplayName("Should handle nested IF inside false-branch")
    void testNestedIfInFalseBranch() throws Exception {
        // Inner IF: pushes 200 if true
        List<Statement> aoInnerTrue = new ArrayList<>();
        aoInnerTrue.add(new NumberStatement(200, 0));
        List<Statement> aoInnerFalse = new ArrayList<>();
        Token oInnerToken = new Token("IF", ForthTokenType.IF, 1);
        IfStatement oInnerIf = new IfStatement(oInnerToken, 1, aoInnerTrue, aoInnerFalse);

        // Outer IF: false-branch contains inner IF
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(99, 0));
        List<Statement> aoFalse = new ArrayList<>();
        aoFalse.add(oInnerIf);
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        // Outer condition FALSE, inner condition TRUE
        stack.push(new IntegerValue(BooleanValue.TRUE));   // for inner IF
        stack.push(new IntegerValue(BooleanValue.FALSE));  // for outer IF

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Execute outer false-branch (inner IF) with true condition: 200 on stack
        assertEquals(1, stack.size());
        assertEquals(200, ((IntegerValue) stack.pop()).toInteger());
    }

    // ================= STACK STATE TESTS =================

    @Test
    @DisplayName("Should preserve stack below condition flag")
    void testStackPreservation() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));

        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        // Stack: [100, TRUE]
        stack.push(new IntegerValue(100));
        stack.push(new IntegerValue(BooleanValue.TRUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Flag consumed, 100 and 42 remain
        assertEquals(2, stack.size());
        assertEquals(42, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(100, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    @DisplayName("Should handle empty stack for flag gracefully")
    void testEmptyStack() {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.reset(); // Empty stack

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);

        // Should throw EmptyStackException
        assertThrows(Exception.class, stmt::execute);
    }

    // ================= EDGE CASE TESTS =================

    @Test
    @DisplayName("Should treat Integer.MAX_VALUE as true")
    void testMaxIntegerAsTrue() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(55, 0));
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(Integer.MAX_VALUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        assertEquals(1, stack.size());
        assertEquals(55, ((IntegerValue) stack.pop()).toInteger());
    }

    @Test
    @DisplayName("Should treat Integer.MIN_VALUE (except 0) as true")
    void testMinIntegerAsTrue() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(66, 0));
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(Integer.MIN_VALUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        assertEquals(1, stack.size());
        assertEquals(66, ((IntegerValue) stack.pop()).toInteger());
    }

    // ================= EMPTY BRANCH TESTS =================

    @Test
    @DisplayName("Should handle empty true-branch")
    void testEmptyTrueBranch() throws Exception {
        List<Statement> aoTrue = new ArrayList<>(); // Empty
        List<Statement> aoFalse = new ArrayList<>();
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.TRUE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Flag consumed, nothing added
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("Should handle empty false-branch")
    void testEmptyFalseBranch() throws Exception {
        List<Statement> aoTrue = new ArrayList<>();
        aoTrue.add(new NumberStatement(42, 0));
        List<Statement> aoFalse = new ArrayList<>(); // Empty
        Token oToken = new Token("IF", ForthTokenType.IF, 1);

        stack.push(new IntegerValue(BooleanValue.FALSE));

        IfStatement stmt = new IfStatement(oToken, 0, aoTrue, aoFalse);
        stmt.execute();

        // Flag consumed, nothing on stack (false-branch is empty)
        assertEquals(0, stack.size());
    }

    // ================= COMBINED OPERATIONS =================

    @Test
    @DisplayName("Should work in sequence with multiple IF statements")
    void testMultipleIfStatementsInSequence() throws Exception {
        // First IF: condition TRUE, pushes 10 and then BooleanValue.TRUE for second IF
        List<Statement> aoTrue1 = new ArrayList<>();
        aoTrue1.add(new NumberStatement(10, 0));
        aoTrue1.add(new NumberStatement(BooleanValue.TRUE, 1));  // condition for second IF
        List<Statement> aoFalse1 = new ArrayList<>();
        Token oToken1 = new Token("IF", ForthTokenType.IF, 1);
        IfStatement stmt1 = new IfStatement(oToken1, 0, aoTrue1, aoFalse1);

        // Second IF: condition comes from stack (TRUE), pushes 20
        List<Statement> aoTrue2 = new ArrayList<>();
        aoTrue2.add(new NumberStatement(20, 1));
        List<Statement> aoFalse2 = new ArrayList<>();
        Token oToken2 = new Token("IF", ForthTokenType.IF, 2);
        IfStatement stmt2 = new IfStatement(oToken2, 1, aoTrue2, aoFalse2);

        // First IF condition: TRUE
        stack.push(new IntegerValue(BooleanValue.TRUE));

        stmt1.execute();
        stmt2.execute();

        // Both branches executed: 10 and 20 on stack
        assertEquals(2, stack.size());
        assertEquals(20, ((IntegerValue) stack.pop()).toInteger());
        assertEquals(10, ((IntegerValue) stack.pop()).toInteger());
    }
}
