# Test Documentation Index

## Overview

This directory contains comprehensive documentation for the Forth Interpreter Test Suite. Each file documents one test program, including test names, purpose, structure, code explanations, and expected outcomes.

## Quick Navigation

### By Category

#### 1. Core Numbers & Boolean Operations (5 test files)

- **[test-numbers.md](test-numbers.md)** — Number parsing and representation
  - Tests: 27 | Priority: HIGH | Tests positive, negative, zero, and large numbers
  
- **[test-boolean.md](test-boolean.md)** — Boolean logic operations
  - Tests: 44 | Priority: HIGH | Tests NOT, AND, OR, XOR, flag representation
  
- test-bitwise.md — Bitwise operations
  - Tests: 29 | Priority: MEDIUM | Tests LSHIFT, RSHIFT, bitwise AND/OR/XOR
  
- test-comparisons.md — Comparison operators
  - Tests: 37 | Priority: HIGH | Tests =, <, >, <=, >=, 0=, 0<, 0>
  
- test-radix.md — Number base conversions
  - Tests: 17 | Priority: MEDIUM | Tests HEX, DECIMAL, BIN, OCT, radix operations

#### 2. Stack Manipulation (6 test files)

- **[test-stack-basics.md](test-stack-basics.md)** — Basic stack operations
  - Tests: 24 | Priority: CRITICAL | Tests DUP, DROP, SWAP, OVER, ROT
  
- **[test-stack-advanced.md](test-stack-advanced.md)** — Advanced stack operations
  - Tests: 23 | Priority: HIGH | Tests 2DUP, TUCK, NIP, ?DUP, PICK, ROLL
  
- test-stack-depth.md — Stack depth checking
  - Tests: 13 | Priority: MEDIUM | Tests DEPTH, stack state verification
  
- test-stack-edge-cases.md — Stack edge cases
  - Tests: 19 | Priority: MEDIUM | Tests edge cases, large values, underflow handling
  
- test-return-stack.md — Return stack operations
  - Tests: 19 | Priority: HIGH | Tests >R, R>, R@, RDROP, return stack management
  
- test-stack-load-patterns.md — Real-world stack patterns
  - Tests: 12 | Priority: MEDIUM | Tests complex stack manipulation scenarios

#### 3. Arithmetic Operations (5 test files)

- **[test-arithmetic-basic.md](test-arithmetic-basic.md)** — Basic arithmetic
  - Tests: 45 | Priority: HIGH | Tests +, -, *, /, 1+, 1-
  
- test-arithmetic-division.md — Division modes and remainder
  - Tests: 24 | Priority: HIGH | Tests MOD, /MOD, FM/MOD, SM/REM
  
- test-arithmetic-wide.md — Double-precision arithmetic
  - Tests: 14 | Priority: MEDIUM | Tests M*, D+, D-, D*, */, wide numbers
  
- test-arithmetic-edge.md — Arithmetic edge cases
  - Tests: 21 | Priority: MEDIUM | Tests overflow, zero multiplication, boundary values
  
- test-float-arithmetic.md — Floating-point operations
  - Tests: 26 | Priority: MEDIUM (optional) | Tests F+, F-, F*, F/, FABS, FSQRT

#### 4. Memory Access & Operations (5 test files)

- **[test-memory-cells.md](test-memory-cells.md)** — Cell read/write operations
  - Tests: 21 | Priority: HIGH | Tests @, !, +!, variable storage
  
- test-memory-chars.md — Character-level memory access
  - Tests: 15 | Priority: HIGH | Tests C@, C!, byte-level operations
  
- test-memory-alignment.md — Memory alignment
  - Tests: 14 | Priority: MEDIUM | Tests ALIGNED, ALIGN, alignment checks
  
- test-memory-move.md — Memory block operations
  - Tests: 13 | Priority: HIGH | Tests MOVE, CMOVE, block copying
  
- test-memory-fill.md — Memory initialization
  - Tests: 12 | Priority: HIGH | Tests FILL, ERASE, memory initialization

#### 5. Control Flow - Conditionals (5 test files)

- **[test-if-then.md](test-if-then.md)** — Simple IF/THEN conditionals
  - Tests: 18 | Priority: HIGH | Tests true/false branching, nesting
  
- test-if-else.md — IF/ELSE/THEN branching
  - Tests: 18 | Priority: HIGH | Tests both branches, conditional execution
  
- test-nested-conditionals.md — Nested conditional structures
  - Tests: 18 | Priority: MEDIUM | Tests deep nesting, branch isolation
  
- test-conditional-edge.md — Conditional edge cases
  - Tests: 18 | Priority: MEDIUM | Tests empty branches, complex conditions
  
- test-case-of.md — CASE/OF multi-way branching
  - Tests: 13 | Priority: MEDIUM | Tests case selection, default handling

#### 6. Control Flow - Loops (6 test files)

- test-begin-until.fs — BEGIN/UNTIL post-test loops
  - Tests: 12 | Priority: MEDIUM | Tests post-test loop iteration
  
- test-begin-while-repeat.fs — BEGIN/WHILE/REPEAT pre-test loops
  - Tests: 12 | Priority: MEDIUM | Tests pre-test loop iteration
  
- **[test-do-loop.md](test-do-loop.md)** — DO/LOOP counter loops
  - Tests: 13 | Priority: HIGH | Tests I, J, loop indices, nesting
  
- test-do-plusloop.md — +LOOP variable stepping
  - Tests: 13 | Priority: MEDIUM | Tests variable steps, negative stepping
  
- test-nested-loops.md — Nested loops and LEAVE
  - Tests: 14 | Priority: MEDIUM | Tests LEAVE, J access, early exit
  
- test-unloop.md — UNLOOP abnormal exit
  - Tests: 12 | Priority: MEDIUM | Tests UNLOOP, stack cleanup, abnormal exit

#### 7. Word Definitions & Dictionary (5 test files)

- **[test-word-definition.md](test-word-definition.md)** — Basic word definitions
  - Tests: 15 | Priority: HIGH | Tests :, ; word creation, composition
  
- test-word-parameters.md — Parameter passing
  - Tests: 12 | Priority: HIGH | Tests stack parameters, local variables
  
- test-recursion.md — Recursive definitions
  - Tests: 12 | Priority: HIGH | Tests recursion, tail recursion, GCD
  
- test-word-shadowing.md — Word shadowing and redefinition
  - Tests: 12 | Priority: MEDIUM | Tests redefining words, latest definition
  
- test-defer.md — DEFER dynamic execution
  - Tests: 12 | Priority: MEDIUM (optional) | Tests IS, deferred behavior

#### 8. I/O Operations (4 test files)

- test-emit.fs — Character output
  - Tests: 12 | Priority: LOW | Tests EMIT, character emission
  
- test-string-output.fs — String output
  - Tests: 12 | Priority: LOW | Tests ." string literals
  
- test-dot-notation.fs — Number printing
  - Tests: 10 | Priority: LOW | Tests . and U., number output
  
- test-io-buffering.fs — Output buffering
  - Tests: 10 | Priority: LOW | Tests output sequencing, FLUSH

#### 9. Advanced Features (4 test files)

- test-evaluate.fs — Runtime interpretation
  - Tests: 12 | Priority: MEDIUM (optional) | Tests EVALUATE, string parsing
  
- test-create-does.fs — CREATE/DOES> data structures
  - Tests: 11 | Priority: MEDIUM (optional) | Tests data structure creation
  
- **[test-exception-handling.md](test-exception-handling.md)** — Exception handling
  - Tests: 12 | Priority: MEDIUM (optional) | Tests CATCH/THROW, error recovery
  
- test-colon-definitions-advanced.fs — Advanced colon definitions
  - Tests: 12 | Priority: LOW (optional) | Tests IMMEDIATE, compile mode

## Framework Documentation

- **[TEST-HARNESS.md](TEST-HARNESS.md)** — Testing framework
  - Describes T{ }T macro system, error handling, floating-point support
  - Essential reading for understanding how tests work

## Test Statistics

| Category | Files | Tests | Priority |
|----------|-------|-------|----------|
| Core Numbers & Boolean | 5 | 154 | HIGH |
| Stack Manipulation | 6 | 110 | CRITICAL |
| Arithmetic Operations | 5 | 130 | HIGH |
| Memory Access | 5 | 75 | HIGH |
| Control Flow - Conditionals | 5 | 85 | HIGH |
| Control Flow - Loops | 6 | 76 | HIGH |
| Word Definitions | 5 | 63 | HIGH |
| I/O Operations | 4 | 44 | LOW |
| Advanced Features | 4 | 47 | MEDIUM |
| **TOTAL** | **45** | **784** | — |

## How to Use This Documentation

### For Test Developers

1. Start with **[TEST-HARNESS.md](TEST-HARNESS.md)** to understand the testing framework
2. Read the specific test file documentation for the test you want to understand
3. Each documentation includes:
   - **Test Cases**: Named tests and what they verify
   - **Structure**: How tests are organized
   - **Code Documentation**: Line-by-line explanation of important tests
   - **Expected Outcomes**: What success and failure look like
   - **Dependencies**: What must be tested first

### For Interpreter Implementers

1. Read tests in **dependency order**:
   - Start with **test-numbers.md** and **test-stack-basics.md**
   - Progress through arithmetic and memory
   - Then control flow (conditionals first, then loops)
   - Finally word definitions and advanced features

2. Use the **Priority** field to focus on critical tests first:
   - **CRITICAL**: Must pass (stack operations)
   - **HIGH**: Essential functionality
   - **MEDIUM**: Important but optional features
   - **LOW**: Nice-to-have features (usually I/O)

3. Run tests incrementally:
   - Implement one category at a time
   - Run its test suite completely before moving on
   - Use passing tests to verify higher-level features

### For CI/CD Integration

1. Group tests by category (see categories above)
2. Use priority levels to gate deployment:
   - All CRITICAL tests must pass
   - All HIGH tests must pass for release
   - MEDIUM/LOW can be warnings
3. Each test file can be run independently: `gforth test-file.fs`

## Test Execution Order (Recommended)

**Phase 1: Foundation** (must pass first)
1. test-numbers.fs
2. test-stack-basics.fs

**Phase 2: Core Operations**
3. test-boolean.fs
4. test-arithmetic-basic.fs

**Phase 3: Extended Operations**
5. test-stack-advanced.fs
6. test-memory-cells.fs

**Phase 4: Control Flow**
7. test-if-then.fs
8. test-do-loop.fs

**Phase 5: Word Definitions**
9. test-word-definition.fs

**Phase 6: Everything Else**
10. All remaining tests in any order

## Finding a Specific Test

### By Functionality

| I want to test... | See |
|------------------|-----|
| Number parsing | test-numbers.md |
| Stack operations | test-stack-basics.md, test-stack-advanced.md |
| Arithmetic | test-arithmetic-basic.md, test-arithmetic-division.md |
| Memory | test-memory-cells.md, test-memory-chars.md |
| Conditionals | test-if-then.md, test-if-else.md |
| Loops | test-do-loop.md, test-begin-until.fs |
| Word definitions | test-word-definition.md |
| Exceptions | test-exception-handling.md |

### By Complexity Level

| Level | Tests |
|-------|-------|
| Beginner | numbers, boolean, stack-basics, arithmetic-basic |
| Intermediate | stack-advanced, memory-cells, if-then, do-loop, word-definition |
| Advanced | memory-move, nested-loops, recursion, exception-handling, create-does |

## Common Questions

**Q: Which tests are most important to implement first?**
A: Start with test-numbers.fs and test-stack-basics.fs. These are foundational; everything else depends on them.

**Q: Can I skip the floating-point tests?**
A: Yes, test-float-arithmetic.fs is optional. It's marked MEDIUM priority.

**Q: Can I skip advanced features?**
A: Yes, tests 9 (Advanced Features) are optional. DEFER, EVALUATE, CREATE/DOES>, and CATCH/THROW are not in the core ANS Forth requirement.

**Q: How do I know if my implementation is correct?**
A: Run `gforth run-all-tests.fs`. All tests should show "All tests passed!" in their summary.

**Q: What's the difference between CRITICAL, HIGH, MEDIUM, LOW?**
A: CRITICAL tests are blockers (stack operations). HIGH tests are essential. MEDIUM tests are important but not strictly necessary. LOW tests are nice-to-have (usually I/O, which is not critical for algorithms).

---

**Documentation Version**: 1.0  
**Last Updated**: 2026-07-20  
**Test Suite Standard**: forth-standard.org/standard/testsuite
