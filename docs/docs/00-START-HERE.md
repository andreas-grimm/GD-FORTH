# Start Here: Forth Test Suite Documentation

Welcome to the comprehensive documentation for the Forth Interpreter Test Suite!

## What You Have

This directory contains complete documentation for a test suite with **45+ test programs** validating a Forth interpreter implementation.

### Files in This Directory

**14 Documentation Files | ~4,000 Lines | 3 Categories**

#### Framework & Navigation (4 files)
- 📖 **[README.md](README.md)** — Quick start guide and navigation
- 📋 **[INDEX.md](INDEX.md)** — Complete index of all 45+ test files with statistics
- 🔧 **[TEST-HARNESS.md](TEST-HARNESS.md)** — How the testing framework works
- 📊 **[DOCUMENTATION-SUMMARY.md](DOCUMENTATION-SUMMARY.md)** — Current documentation status

#### Test Documentation (10 files, 8 categories)
- 🔢 **[test-numbers.md](test-numbers.md)** — Number parsing (27 tests)
- ✓ **[test-boolean.md](test-boolean.md)** — Boolean logic (44 tests)
- 📚 **[test-stack-basics.md](test-stack-basics.md)** — Stack operations (24 tests)
- ➕ **[test-arithmetic-basic.md](test-arithmetic-basic.md)** — Arithmetic (45 tests)
- 💾 **[test-memory-cells.md](test-memory-cells.md)** — Memory access (21 tests)
- ↔️ **[test-if-then.md](test-if-then.md)** — Conditionals (18 tests)
- 🔄 **[test-do-loop.md](test-do-loop.md)** — Loops (13 tests)
- 📝 **[test-word-definition.md](test-word-definition.md)** — Word definitions (15 tests)
- ⚠️ **[test-exception-handling.md](test-exception-handling.md)** — Exceptions (12 tests)

#### Templates & Guides (1 file)
- 🎯 **[DOCUMENTATION-TEMPLATE.md](DOCUMENTATION-TEMPLATE.md)** — Template for creating more documentation

## Quick Navigation

### I Want To...

**Understand what tests are available:**
→ Start with [INDEX.md](INDEX.md)

**Learn how tests work:**
→ Read [TEST-HARNESS.md](TEST-HARNESS.md)

**Implement a Forth interpreter:**
→ Read [README.md](README.md) section "For Interpreter Implementers"

**Run tests:**
→ Read [README.md](README.md) section "For Test Suite User"

**Fix a failing test:**
→ Find the test in [INDEX.md](INDEX.md) → click to detailed docs → read "Expected Outcome"

**Understand a specific feature (e.g., loops):**
→ Search [INDEX.md](INDEX.md) for "loop" → click test documentation → read "Code Documentation"

**Add more documentation:**
→ Copy [DOCUMENTATION-TEMPLATE.md](DOCUMENTATION-TEMPLATE.md) → follow the guide

## Getting Started in 5 Minutes

### Step 1: Understand the Test Suite (2 min)
Read the first section of [INDEX.md](INDEX.md) - it shows:
- All 45+ test files organized by category
- Statistics on tests and coverage
- Priority levels (CRITICAL, HIGH, MEDIUM, LOW)

### Step 2: Read a Sample Test (2 min)
Pick any documented test, e.g., [test-numbers.md](test-numbers.md):
- Overview: What's being tested
- Test Cases: List of all tests
- Code Documentation: How tests work

### Step 3: Run the Tests (1 min)
```bash
cd /Users/andreas/forth-tests
gforth run-all-tests.fs
```

## Documentation Overview

### What's Documented?

| Category | Documented | Total | Status |
|----------|-----------|-------|--------|
| Core Numbers & Boolean | 2/5 | 154 tests | 46% |
| Stack Manipulation | 1/6 | 110 tests | 22% |
| Arithmetic | 1/5 | 130 tests | 35% |
| Memory Access | 1/5 | 75 tests | 28% |
| Control Flow | 2/11 | 161 tests | 19% |
| Word Definitions | 1/5 | 63 tests | 24% |
| I/O Operations | 0/4 | 44 tests | 0% |
| Advanced Features | 1/4 | 47 tests | 26% |
| **TOTAL** | **9/45** | **784** | **28%** |

### What Each Test File Includes

Every documented test file has:

1. **Overview** — Purpose of the test
2. **Test Cases** — Complete list of all tests with names
3. **What Is Being Tested** — Detailed functionality checklist
4. **Structure** — How tests are organized and formatted
5. **Code Documentation** — Detailed examples with execution flow
6. **Expected Outcome** — Success and failure examples
7. **Dependency Chain** — Prerequisites and dependents
8. **Notes** — Implementation-specific details and gotchas

## The Test Suite Itself

### What's in the Test Directory?

```
/Users/andreas/forth-tests/
├── test-harness.fs           ← Core testing framework
├── test-numbers.fs           ← 27 number parsing tests
├── test-boolean.fs           ← 44 boolean logic tests
├── test-stack-basics.fs      ← 24 stack operation tests
├── ... (45+ test files total)
├── run-all-tests.fs          ← Master test runner
├── README.md                 ← Test suite user guide
└── docs/                     ← (You are here)
    ├── README.md             ← Documentation getting started
    ├── INDEX.md              ← Complete test index
    ├── TEST-HARNESS.md       ← Framework documentation
    ├── test-*.md             ← Individual test docs
    └── ...
```

### Test Format

All tests use the standard Forth format:

```forth
T{ 5 3 + -> 8 }T
```

Meaning:
- `T{` — Start test
- `5 3 +` — Code to execute
- `->` — Separator
- `8` — Expected result
- `}T` — End test

See [TEST-HARNESS.md](TEST-HARNESS.md) for details.

## Documentation Quality

Each documented test file includes:

✓ Overview of purpose  
✓ Complete list of test case names  
✓ Functionality being tested  
✓ Test case structure explanation  
✓ Detailed code documentation with examples  
✓ Expected outcomes (success and failure)  
✓ Dependency analysis  
✓ Implementation notes and gotchas  

## How to Use This Documentation

### For Learning

1. Read [TEST-HARNESS.md](TEST-HARNESS.md) to understand testing
2. Pick a documented test file
3. Read the overview
4. Walk through "Code Documentation" section
5. Run the actual test: `gforth test-filename.fs`

### For Implementation

1. Read [INDEX.md](INDEX.md) section "Test Execution Order"
2. Follow the order: Start with numbers, then stack, then arithmetic
3. For each test category:
   - Read the documentation
   - Implement required Forth words
   - Run the test
   - Fix failures using docs as guide
   - Move to next category

### For Debugging

1. Find failing test in [INDEX.md](INDEX.md)
2. Click its documentation link
3. Go to "Expected Outcome" section
4. Compare actual output vs. expected
5. Read "Code Documentation" to understand why
6. Check "Dependency Chain" for prerequisites

## Statistics

- **Total test files**: 45+
- **Total test cases**: 784
- **Documented test files**: 10 (with detailed docs)
- **Framework docs**: 4 files
- **Total documentation**: ~4,000 lines
- **Categories**: 8 major categories

## What Gets Tested

The test suite covers all core Forth functionality:

- ✓ Number parsing and representation
- ✓ Boolean operations and flags
- ✓ Stack manipulation (DUP, DROP, SWAP, OVER, ROT, etc.)
- ✓ Arithmetic (+, -, *, /, MOD, /MOD)
- ✓ Memory access (@, !, C@, C!)
- ✓ Control flow (IF/THEN, DO/LOOP, BEGIN/UNTIL)
- ✓ Word definitions and recursion
- ✓ I/O (EMIT, .", number printing)
- ✓ Advanced features (exceptions, deferred execution)

## Next Steps

### To Implement a Forth Interpreter

1. Clone or link the test suite
2. Create your Forth interpreter
3. Run: `gforth run-all-tests.fs`
4. Work through tests in order following [INDEX.md](INDEX.md)
5. Use documentation as your specification

### To Add More Documentation

1. Copy [DOCUMENTATION-TEMPLATE.md](DOCUMENTATION-TEMPLATE.md)
2. Pick an undocumented test file
3. Follow the template guide
4. Commit and share!

### To Run Specific Tests

```bash
# Run specific test file
gforth test-numbers.fs

# Run entire category
for f in test-arithmetic-*.fs; do gforth $f; done

# Run all tests
gforth run-all-tests.fs
```

## File Guide

| File | Purpose | Audience |
|------|---------|----------|
| README.md | Getting started, navigation | Everyone |
| INDEX.md | Complete test index, statistics | Reference |
| TEST-HARNESS.md | Framework details, how T{ }T works | Developers |
| DOCUMENTATION-SUMMARY.md | What's documented, what's TODO | Maintainers |
| DOCUMENTATION-TEMPLATE.md | Template for new docs | Contributors |
| test-*.md | Individual test documentation | Implementation |

## Key Concepts

### Test Priorities

- **CRITICAL** — Foundation tests (must pass first)
- **HIGH** — Essential functionality
- **MEDIUM** — Important but optional features
- **LOW** — Nice-to-have (usually I/O)

### Test Format

```
T{ <code> -> <expected-stack> }T
```

### Dependency Order

Most important → Least important:
1. Numbers & Stack
2. Arithmetic & Memory
3. Control Flow
4. Word Definitions
5. I/O & Advanced

## Support

### Finding Information

**Topic** → **Location**
- Test overview → [INDEX.md](INDEX.md)
- How tests work → [TEST-HARNESS.md](TEST-HARNESS.md)
- Specific test → Find in [INDEX.md](INDEX.md), click link
- Implementation guide → [README.md](README.md)
- Missing docs? → See [DOCUMENTATION-SUMMARY.md](DOCUMENTATION-SUMMARY.md)

### Common Questions

**Q: How many tests are there?**  
A: 45+ test files with 784 total test cases. See [INDEX.md](INDEX.md).

**Q: Where should I start?**  
A: With test-numbers.fs and test-stack-basics.fs. See [README.md](README.md).

**Q: How do I run a test?**  
A: `gforth test-filename.fs` or `gforth run-all-tests.fs` for all.

**Q: What if a test fails?**  
A: Find the test docs, read "Expected Outcome", check "Code Documentation".

**Q: Can I skip some tests?**  
A: Prioritize: CRITICAL > HIGH > MEDIUM > LOW. See [INDEX.md](INDEX.md).

---

**Welcome to the Forth Test Suite Documentation!**

👉 **Next: Read [README.md](README.md)** for your specific use case

Or jump to:
- 📖 [INDEX.md](INDEX.md) — Browse all tests
- 🔧 [TEST-HARNESS.md](TEST-HARNESS.md) — Learn the framework
- 🎯 Pick a test: [test-numbers.md](test-numbers.md), [test-stack-basics.md](test-stack-basics.md), or [test-if-then.md](test-if-then.md)

**Documentation Version**: 1.0  
**Created**: 2026-07-20  
**Status**: Active, maintained
