# Forth Interpreter Test Suite

A comprehensive test suite for validating Forth interpreter implementations based on the ANS Forth standard.

## Overview

**45+ Test Programs** organized in **9 categories** covering all core Forth functionality.

### Test Categories

1. **Core Numbers & Boolean Operations** (5 programs)
   - `test-numbers.fs` — Number parsing and representation
   - `test-boolean.fs` — Boolean logic and flags
   - `test-bitwise.fs` — Bitwise operations
   - `test-comparisons.fs` — Comparison operators
   - `test-radix.fs` — Number base conversions

2. **Stack Manipulation** (6 programs)
   - `test-stack-basics.fs` — DUP, DROP, OVER, SWAP, ROT
   - `test-stack-advanced.fs` — Advanced stack ops (2DUP, TUCK, NIP, etc.)
   - `test-stack-depth.fs` — Stack depth checking
   - `test-stack-edge-cases.fs` — Edge cases and error handling
   - `test-return-stack.fs` — Return stack operations (>R, R>, etc.)
   - `test-stack-load-patterns.fs` — Real-world stack patterns

3. **Arithmetic Operations** (5 programs)
   - `test-arithmetic-basic.fs` — +, -, *, / operations
   - `test-arithmetic-division.fs` — Division modes, MOD, /MOD
   - `test-arithmetic-wide.fs` — Double-precision and wide numbers
   - `test-arithmetic-edge.fs` — Edge cases and overflow handling
   - `test-float-arithmetic.fs` — Floating-point operations

4. **Memory Access** (5 programs)
   - `test-memory-cells.fs` — Cell read/write (@, !)
   - `test-memory-chars.fs` — Character memory access (C@, C!)
   - `test-memory-alignment.fs` — Alignment operations
   - `test-memory-move.fs` — MOVE, CMOVE block operations
   - `test-memory-fill.fs` — FILL, ERASE operations

5. **Control Flow - Conditionals** (5 programs)
   - `test-if-then.fs` — Simple IF/THEN
   - `test-if-else.fs` — IF/ELSE/THEN branching
   - `test-nested-conditionals.fs` — Nested conditionals
   - `test-conditional-edge.fs` — Edge cases
   - `test-case-of.fs` — CASE/OF multi-way branching

6. **Control Flow - Loops** (6 programs)
   - `test-begin-until.fs` — BEGIN/UNTIL post-test loops
   - `test-begin-while-repeat.fs` — BEGIN/WHILE/REPEAT pre-test loops
   - `test-do-loop.fs` — DO/LOOP counter loops
   - `test-do-plusloop.fs` — +LOOP variable stepping
   - `test-nested-loops.fs` — Nested loops with J and LEAVE
   - `test-unloop.fs` — UNLOOP abnormal exit handling

7. **Word Definitions** (5 programs)
   - `test-word-definition.fs` — Basic word definitions
   - `test-word-parameters.fs` — Parameter passing
   - `test-recursion.fs` — Recursive definitions
   - `test-word-shadowing.fs` — Word redefinition and shadowing
   - `test-defer.fs` — DEFER dynamic execution

8. **I/O Operations** (4 programs)
   - `test-emit.fs` — Character output with EMIT
   - `test-string-output.fs` — String output with ."
   - `test-dot-notation.fs` — Number printing (., U.)
   - `test-io-buffering.fs` — Output sequencing and buffering

9. **Advanced Features** (4 programs)
   - `test-evaluate.fs` — Runtime string interpretation
   - `test-create-does.fs` — CREATE/DOES> data structures
   - `test-exception-handling.fs` — CATCH/THROW exceptions
   - `test-colon-definitions-advanced.fs` — Advanced compilation behavior

## Test Framework

### Test Harness

The `test-harness.fs` file provides the testing infrastructure:

- **T{ ... -> ... }T** — Standard test macro format
- **Error tracking** with ERROR-XT variable
- **Floating-point tolerance** support (SET-EXACT, SET-NEAR)
- **Test summary reporting**

### Test Format

Each test follows the standard Forth test format:

```forth
T{ code -> expected-stack-contents }T
```

**Examples:**
```forth
T{ 5 3 + -> 8 }T
T{ DUP 4 * -> 12 12 }T
T{ 1 2 SWAP -> 2 1 }T
```

## Running the Tests

### Run All Tests

```bash
gforth run-all-tests.fs
```

or

```bash
forth < run-all-tests.fs
```

### Run Individual Test Categories

```bash
gforth test-stack-basics.fs
gforth test-arithmetic-basic.fs
gforth test-if-then.fs
```

## Test Execution

Tests execute **sequentially**, with:
- Early tests validating foundational words (stack ops)
- Later tests building on verified functionality
- Progressive validation from simple to complex

### Expected Output

Each test file outputs:
```
=== Test X.Y: Description ===
Total: NN
Passed: NN
Failed: 0
All tests passed!
```

## Test Coverage

- **Stack operations**: All core stack manipulation words
- **Arithmetic**: Integer, division modes, wide numbers, floating-point
- **Memory**: Cell and character access, alignment, block operations
- **Control flow**: Conditionals, loops, nesting, early exit
- **Word definitions**: Creation, recursion, shadowing, deferred execution
- **I/O**: Character output, string printing, number formatting
- **Advanced**: Runtime evaluation, data structures, exception handling

## Validation Criteria

✓ All tests run without unhandled errors
✓ Stack-based results match expected values exactly
✓ Control flow branches execute correctly
✓ Memory operations preserve data integrity
✓ Edge cases handled gracefully
✓ Floating-point results within tolerance

## Implementation Notes

- Tests assume **ANS Forth** compatibility
- Some advanced tests (floating-point, EVALUATE, DEFER, CATCH/THROW) are implementation-dependent
- Character output tests (EMIT, EMIT) produce visible output for manual verification
- Stack-based I/O tests verify correct sequencing

## Requirements

- Forth interpreter with ANS Forth core word set
- Support for:
  - Basic arithmetic and stack operations
  - Control flow (IF/ELSE, loops)
  - Word definitions (colon definitions)
  - Memory access (@, !, C@, C!)
  - I/O (EMIT, .")

## Optional Features

- Floating-point support (F+, F-, F*, F/)
- Advanced features (DEFER, CATCH/THROW, EVALUATE, CREATE/DOES>)
- Exception handling

## Troubleshooting

**Test fails on floating-point operations:**
- Your Forth may not have FP support. Comment out `test-float-arithmetic.fs`

**DEFER/CATCH tests fail:**
- These are ANS optional words. Some implementations don't support them.

**Output tests show nothing:**
- Character output (EMIT, .) produces side effects. Check interpreter console/output.

## Contributing

To add new tests:
1. Create `test-category-name.fs`
2. Include `test-harness.fs`
3. Write tests in `T{ code -> expected }T` format
4. Add to `run-all-tests.fs`
5. Verify with `gforth test-file.fs`

---

**Test Suite Generated**: 2026-07-20
**Standard Reference**: forth-standard.org/standard/testsuite
