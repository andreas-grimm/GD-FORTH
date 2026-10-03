# Forth Interpreter Test Suite Development Plan

## Overview

- Total Test Programs: 45+ organized in 9 categories
- Testing Format: Standard T{ code -> expected_results }T from Forth standard
- Strategy: Progressive validation—early tests verify foundational words used by later tests

## Test Categories & Programs
### 1. CORE NUMBER & BOOLEAN OPERATIONS (5 programs)
   Purpose: Verify basic numerical representation and logical operations

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 1.1 |	test-numbers.fs | Number parsing and representation (0, -1, positive, negative, large numbers) | Correct stack values after parsing |
| 1.2 | test-boolean.fs | Boolean logic (TRUE/FALSE constants, logical NOT, AND, OR operations) | Flag representations (-1 for true, 0 for false) |
| 1.3 | test-bitwise.fs | Bitwise operations (AND, OR, XOR, bit shifts LEFT, RIGHT) | Correct bit manipulation results |
| 1.4 | test-comparisons.fs | Comparison operators (=, <, >, <=, >=, 0=, 0<, 0>) | Correct boolean results on stack |
| 1.5 | test-radix.fs | Number base conversions (HEX, DECIMAL, binary parsing) | Correct number interpretation in different bases |


### 2. STACK MANIPULATION (6 programs)
   Purpose: Verify all core stack operations—essential for all subsequent tests

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 2.1 | test-stack-basics.fs | DUP, DROP, OVER, SWAP, ROT | Correct stack contents after each operation |
| 2.2 | test-stack-advanced.fs | -ROT, NIP, TUCK, 2DUP, 2DROP, 2OVER, 2SWAP | Stack manipulation with multiple cells |
| 2.3 | test-stack-depth.fs | Depth checking (DEPTH, stack state at various points) | Correct stack depth calculations |
| 2.4 | test-stack-edge-cases.fs | Edge cases (empty stack, single item, underflow handling) | Graceful handling or proper error reporting |
| 2.5 | test-return-stack.fs | Return stack operations (>R, R>, R@, RDROP) | Correct data movement between stacks |
| 2.6 | test-stack-load-patterns.fs | Common stack patterns (nested pushes, pattern shuffling) | Complex real-world stack scenarios |

### 3. ARITHMETIC OPERATIONS (5 programs)
   Purpose: Verify integer and optionally floating-point math

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 3.1 | test-arithmetic-basic.fs | +, -, *, / with positive/negative numbers | Correct basic math results |
| 3.2 | test-arithmetic-division.fs | Division modes (floored, symmetric), remainder, MOD, /MOD | Correct division semantics per Forth standard |
| 3.3 | test-arithmetic-wide.fs | Double-number arithmetic (*, /MOD with wide numbers) | Correct handling of 64-bit results |
| 3.4 | test-arithmetic-edge.fs | Edge cases (division by zero, overflow, zero multiplication) | Proper error/exception handling |
| 3.5 | test-float-arithmetic.fs | Floating-point (F+, F-, F*, F/, FABS if supported) | Correct float results with tolerance settings |

### 4. MEMORY ACCESS & OPERATIONS (5 programs)
   Purpose: Verify memory reading/writing and data layout

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 4.1 | test-memory-cells.fs |	@, !, cell-based memory operations | Correct read/write of 32/64-bit cells |
| 4.2 | test-memory-chars.fs |	C@, C!, character-level access | Correct byte-level memory access |
| 4.3 | test-memory-alignment.fs |	Alignment checks (ALIGNED, ALIGN), cell vs. char access | Proper alignment handling |
| 4.4 | test-memory-move.fs | MOVE, CMOVE, block copy operations | Correct memory movement (overlapping/non-overlapping) |
| 4.5 | test-memory-fill.fs | FILL, ERASE, initialization patterns | Correct memory initialization |

### 5. CONTROL FLOW - CONDITIONALS (5 programs)
   Purpose: Verify conditional execution paths

| #   | Test Program | Content | Expected Validations |
|-----|--------------|---------|----------------------|
| 5.1 | test-if-then.fs | Simple IF/THEN (true and false branches) | Correct conditional execution |
| 5.2 | test-if-else.fs | IF/ELSE/THEN (both branches, nesting) | Correct branch selection |
| 5.3 | test-nested-conditionals.fs | Multiple nested IF/ELSE/THEN structures | Proper nesting and branch isolation |
| 5.4 | test-conditional-edge.fs | Edge cases (empty branches, complex conditions) | Proper handling of unusual structures | 
| 5.5 | test-case-of.fs | CASE/OF/ENDOF (multi-way branching) | Correct case selection and default handling |

### 6. CONTROL FLOW - LOOPS (6 programs)
   Purpose: Verify iterative execution and loop constructs

| #   | Test Program | Content                        | Expected Validations      |
|-----|--------------|--------------------------------|---------------------------|
| 6.1 | test-begin-until.fs | BEGIN/UNTIL loops (post-test ) | Correct loop iteration and exit |
| 6.2 | test-begin-while-repeat.fs | BEGIN/WHILE/REPEAT loops (pre-test) | Correct condition testing |
| 6.3 | test-do-loop.fs | DO/LOOP, loop counters (I), normal exit | Correct iteration count and index |
| 6.4 | test-do-plusloop.fs | +LOOP (variable step, negative steps) Correct loop stepping |
| 6.5 | test-nested-loops.fs | Nested loops, J (outer loop index), LEAVE | Proper loop nesting and early exit |
| 6.6 | test-unloop.fs | UNLOOP, LEAVE with complex nesting	Stack cleanup on abnormal exit |

### 7. WORD DEFINITIONS & DICTIONARY (5 programs)
   Purpose: Verify word creation and execution

| # | Test Program | Content                        | Expected Validations |
|---|--------------|--------------------------------|----------------------|
| 7.1 | test-word-definition.fs | `:` word definition, simple words | Correct word creation and execution |
| 7.2 | test-word-parameters.fs | Words with parameters, local variables if supported |Correct parameter passing |
| 7.3 | test-recursion.fs | Recursive word definitions, tail recursion | Correct recursion handling |
| 7.4 | test-word-shadowing.fs | Redefining words, shadowing, scope | Latest definition takes precedence |
| 7.5 | test-defer.fs | DEFER, IS (deferred execution) | Correct dynamic word execution |

### 8. I/O OPERATIONS (4 programs)
   Purpose: Verify character and string output (input testing may be manual)

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 8.1 | test-emit.fs | EMIT (character output), multiple chars | Correct character emission |
| 8.2 | test-string-output.fs | String literals, ." string output | Correct string printing |
| 8.3 | test-dot-notation.fs | `.` (print number), U. (unsigned) | Correct number formatting |
| 8.4 | test-io-buffering.fs | Output sequencing, FLUSH if supported | Correct output order and buffering |

### 9. ADVANCED FEATURES (4 programs)
   Purpose: Verify complex language features

| # | Test Program | Content | Expected Validations |
|---|--------------|---------|----------------------|
| 9.1 | test-evaluate.fs | EVALUATE (runtime interpretation), parsing strings | Correct interpretation of string as code |
| 9.2 | test-create-does.fs | CREATE/DOES> (data structures) | Correct compile-time/runtime splitting |
| 9.3 | test-exception-handling.fs | CATCH/THROW, error handling | Correct exception propagation |
| 9.4 | test-colon-definitions-advanced.fs | Immediate words, compilation state	| Compile vs. interpret mode behavior |

## Test Execution Strategy

### Phase 1: Foundation (Programs 1.1-2.6)

- Build stack manipulation tests first
- Use these verified operations in all subsequent tests
- Validate output using stack state assertions

### Phase 2: Arithmetic & Memory (Programs 3.1-4.5)

- Once stack operations verified, test math operations
- Use arithmetic in complex memory tests
- Verify alignment and edge cases

### Phase 3: Control Flow (Programs 5.1-6.6)

- Implement conditional and loop tests
- Use arithmetic/stack ops to verify branches/iterations
- Test nesting and complex patterns

### Phase 4: Dictionary & Advanced (Programs 7.1-9.4)

- Test word definition and execution
- Verify I/O and advanced features
- Recursive tests depend on earlier control flow
- Test Format Template
- Each test program follows the standard:

```Forth
T{ <forth-code> -> <expected-stack-contents> }T
```

**Example:**

```Forth
T{ 5 3 + -> 8 }T
T{ DUP 4 * -> 12 12 }T
T{ OVER -> 5 3 5 }T
```

## Success Criteria

- ✓ All 45+ programs run without unhandled errors
- ✓ Stack-based results match expected values exactly
- ✓ Control flow branches correctly
- ✓ Memory operations preserve data integrity
- ✓ Edge cases handled gracefully
- ✓ Floating-point results within tolerance (SET-NEAR)

## Implementation Notes
- Use the test harness framework to define T{...}T blocks
- Implement error tracking with ERROR-XT variable
- Support floating-point tolerance with SET-EXACT and SET-NEAR
- Print pass/fail summary at end of each program
- Create combined test runner to execute all 45+ programs sequentially