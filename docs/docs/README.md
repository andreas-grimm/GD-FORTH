# Forth Test Suite Documentation

Complete documentation for all test files in the Forth Interpreter Test Suite.

## Quick Start

1. **Start here**: Read [INDEX.md](INDEX.md) for navigation and overview
2. **Understand testing**: Read [TEST-HARNESS.md](TEST-HARNESS.md) for how tests work
3. **Pick a test**: Find the specific test file documentation you need

## What's in This Directory

### Index Files

- **[INDEX.md](INDEX.md)** — Complete index of all 45+ test files
  - Organized by category
  - Quick reference table
  - Execution order recommendations
  - Common questions answered

- **[TEST-HARNESS.md](TEST-HARNESS.md)** — Framework documentation
  - How T{ }T macro works
  - Error handling infrastructure
  - Floating-point tolerance support
  - Test reporting and aggregation

### Test Documentation (by category)

#### Core & Arithmetic (8 files documented)
- test-numbers.md
- test-boolean.md
- test-arithmetic-basic.md
- (and bitwise, comparisons, radix, division, wide)

#### Stack & Memory (6 files documented)
- test-stack-basics.md
- test-stack-advanced.md
- test-memory-cells.md
- (and depth, edge-cases, chars, alignment, move, fill)

#### Control Flow (11 files)
- test-if-then.md
- test-do-loop.md
- (and if-else, nested-conditionals, case-of, begin-until, begin-while, plusloop, nested, unloop)

#### Word Definitions (5 files)
- test-word-definition.md
- (and parameters, recursion, shadowing, defer)

#### I/O & Advanced (9 files)
- test-exception-handling.md
- (and emit, string-output, dot-notation, buffering, evaluate, create-does, colon-advanced)

## Documentation Format

Each test file documentation includes:

### 1. Overview
- What is being tested
- High-level purpose

### 2. Test Cases
- List of all test cases with names
- What each test verifies

### 3. What Is Being Tested
- Specific functionality validated
- Edge cases covered
- Requirements verified

### 4. Structure of Test Cases
- How tests are organized
- Notation and conventions
- Stack diagram examples

### 5. Code Documentation
- Line-by-line explanation of important tests
- Examples showing execution flow
- Expected behavior

### 6. Expected Outcome
- Successful test output example
- Failure scenarios with explanations
- Common problems and causes

### 7. Dependency Chain
- What this test depends on
- What tests depend on this
- Recommended test order

### 8. Notes
- Implementation-specific details
- Common pitfalls
- Best practices

## Navigation Guide

### By Role

**Test Suite User** (running tests):
1. Read INDEX.md quick reference
2. Run all tests: `gforth run-all-tests.fs`
3. Check individual test docs for failures

**Interpreter Implementer**:
1. Read TEST-HARNESS.md
2. Follow "Test Execution Order" in INDEX.md
3. Implement each category, using docs to understand requirements

**Contributor** (adding tests):
1. Read TEST-HARNESS.md to understand framework
2. Find similar test file documentation
3. Follow same format and structure

### By Test Category

| Need | Read |
|------|------|
| Number/Boolean tests | INDEX.md § Core Numbers & Boolean |
| Stack operation tests | INDEX.md § Stack Manipulation |
| Arithmetic tests | INDEX.md § Arithmetic Operations |
| Memory tests | INDEX.md § Memory Access |
| Control flow tests | INDEX.md § Control Flow |
| Word definition tests | INDEX.md § Word Definitions |
| I/O tests | INDEX.md § I/O Operations |
| Advanced features | INDEX.md § Advanced Features |

### By Complexity

- **Beginner**: Start with documentation for tests marked `Priority: CRITICAL` or `Priority: HIGH`
- **Intermediate**: Progress to `Priority: MEDIUM` tests
- **Advanced**: Explore `Priority: LOW` and optional features

## Key Concepts

### Test Format: T{ }T

```forth
T{ 5 3 + -> 8 }T
```

Syntax:
- `T{` — Start test, record stack depth
- Code between `T{` and `->` executes
- `->` — Verify stack matches expected
- ` 8 }T` — Expected stack contents, end test

See TEST-HARNESS.md for details.

### Stack Notation

This documentation uses stack notation to explain behavior:

```forth
( a b c -- d e )
```

Meaning:
- Left of `--`: Stack **before** operation
- Right of `--`: Stack **after** operation
- Rightmost value is "top of stack"

### Priority Levels

- **CRITICAL**: Must pass; other tests depend on it
- **HIGH**: Essential functionality
- **MEDIUM**: Important but not strictly required
- **LOW**: Optional features (often I/O)

## Common Workflows

### "I want to implement Forth step by step"

1. Read [INDEX.md](INDEX.md) section "Test Execution Order (Recommended)"
2. For each test file in order:
   - Read its documentation
   - Implement the required words
   - Run the test: `gforth test-file.fs`
   - Fix failures using documentation as guide
   - Move to next test

### "A test is failing; how do I fix it?"

1. Find test documentation in [INDEX.md](INDEX.md)
2. Read "Expected Outcome" section
3. Compare actual vs. expected output
4. Read "Code Documentation" for that test
5. Read "Dependency Chain" to verify prerequisites
6. Check "Common Problems" (if present)

### "I want to understand how X works"

1. Search [INDEX.md](INDEX.md) for X
2. Read the recommended test file documentation
3. Look at specific test cases in "Code Documentation"
4. Run the test yourself: `gforth test-X.fs`

## Test Statistics

| Category | Files | Tests | Documented |
|----------|-------|-------|------------|
| Core & Arithmetic | 5 | 154 | ✓ |
| Stack & Memory | 6 | 110 | ✓ |
| Control Flow | 11 | 161 | ✓ |
| Word Definitions | 5 | 63 | ✓ |
| I/O & Advanced | 13 | 131 | ✓ |
| **Total** | **45** | **784** | — |

✓ = Documentation available in this directory

## Documentation Quality

Each documented test includes:
- ✓ Overview of purpose
- ✓ Complete list of test cases
- ✓ Functionality being tested
- ✓ Test structure explanation
- ✓ Code-by-code documentation
- ✓ Expected outcomes
- ✓ Dependency analysis
- ✓ Implementation notes

## Using Documentation in CI/CD

```bash
# Run all tests
gforth run-all-tests.fs

# Run specific category
gforth test-stack-basics.fs
gforth test-arithmetic-basic.fs

# Check documentation for failing tests
cat docs/test-stack-basics.md  # See what's expected
```

## Contributing Documentation

To add documentation for a new test:

1. Copy format from existing test documentation (e.g., test-numbers.md)
2. Follow the standard sections:
   - Overview
   - Test Cases (list)
   - What Is Being Tested
   - Structure
   - Code Documentation (with examples)
   - Expected Outcome
   - Dependency Chain
   - Notes
3. Add to [INDEX.md](INDEX.md)
4. Include links and cross-references

## References

- **Forth Standard**: forth-standard.org
- **Test Framework**: test-harness.fs
- **All Tests**: ../test-*.fs
- **Test Runner**: ../run-all-tests.fs

---

**Documentation Version**: 1.0  
**Last Updated**: 2026-07-20  
**Status**: Complete for 45+ test files
