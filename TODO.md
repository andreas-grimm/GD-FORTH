# GD-FORTH Implementation TO-DO List

**Last Updated:** 2026-08-03  
**Status:** 38 FORTH words implemented with comprehensive documentation (9.0% of 420+ standard words)  
**Test Coverage:** 1642 unit tests, 100% passing ✓

---

## Overview

This document maintains a comprehensive list of all FORTH reserved words and their implementation status in the GD-FORTH interpreter. The interpreter currently implements **38 words** out of **420+ reserved words**, with comprehensive unit test coverage and full documentation for all implemented features.

### Session Accomplishments (2026-08-03)

**Documentation Enhancement:**
- ✅ Enhanced VariableStatement with dual-operation support (VARIABLE/WORD tokens)
- ✅ Fixed WordStatement with correct single-purpose documentation
- ✅ Fixed QuestionStatement with proper "?" operator documentation
- ✅ Enhanced ForthParser with detailed variable architecture explanation
- ✅ Added comprehensive inline code comments throughout parser

**Test Suite Expansion:**
- ✅ VariableStatementTest: 25 comprehensive tests
- ✅ WordStatementTest: 34 comprehensive tests (NEW)
- ✅ QuestionStatementTest: 19 comprehensive tests (NEW)
- ✅ ForthParserTest: 75 total tests (added 15 new variable/question tests)
- ✅ **Total New Tests:** 93 tests added this session
- ✅ **Total Test Suite:** 1642 tests, all passing

**Variable Operations Enhancement:**
- ✅ Full documentation of dual-storage architecture
- ✅ Variables.java: Comprehensive class-level and method-level JavaDoc
- ✅ FetchStatement: Comprehensive documentation and 34 tests
- ✅ StoreStatement: Complete implementation with proper documentation
- ✅ VariableStatement: Support for variable definition (VARIABLE token)
- ✅ WordStatement: Support for variable access (WORD token)
- ✅ QuestionStatement: Support for variable debugging (QUESTION token)

---

## Currently Implemented Words (38)

**Arithmetic Operations (5 implemented):**
- `+` (PlusStatement)
- `-` (MinusStatement)
- `*` (MultiplyStatement)
- `/` (DivideStatement)
- `MOD` (ModuloStatement)

**Comparison Operations (6 implemented):**
- `=` (EqualsStatement)
- `<>` (NotEqualsStatement)
- `<` (LessThanStatement)
- `>` (GreaterThanStatement)
- `<=` (LessEqualStatement)
- `>=` (GreaterEqualStatement)

**Zero Comparison (4 implemented):**
- `0=` (ZeroEqualsStatement)
- `0<>` (ZeroNotEqualsStatement)
- `0<` (ZeroLessStatement)
- `0>` (ZeroGreaterStatement)

**Stack Operations (16 implemented):**
- `DUP`, `?DUP`, `DROP`, `2DROP`, `SWAP`, `2SWAP`, `OVER`, `2OVER`
- `ROT`, `2ROT`, `-ROT`, `NIP`, `TUCK`, `PICK`, `ROLL`, `DEPTH`

**I/O Operations (4 implemented):**
- `.` (PrintStatement - pop and print)
- `.S` (PrintKeepStackStatement - peek and print, keeps on stack)
- `CR` (CarriageReturnStatement - print newline)
- `?` (QuestionStatement - fetch variable and print, debugging operator)

**Variable/Memory Operations (7 implemented):**
- `VARIABLE` (VariableStatement - define variable)
- `WORD` (WordStatement - access variable, implicit via FORTH words)
- `@` (FetchStatement - fetch value from variable)
- `!` (StoreStatement - store value in variable)
- `2@` (TWO_FETCH - fetch 2-cell value)
- `2!` (TWO_STORE - store 2-cell value)
- `C@` / `C!` (Character variants of fetch/store)

---

## Architectural Overview

### Dual-Storage Variables Architecture
The GD-FORTH interpreter implements a sophisticated variable management system:

**Static Storage (shared across all instances):**
- `_astrVariableName`: List<String> - Variable names indexed by position
- `_aoVariable`: Map<Integer, Value> - Variable values indexed by position

**Variable Workflow:**
1. **Definition** (VARIABLE token): VariableStatement → Variables.define(name)
   - Creates new variable entry
   - Initializes value to "empty" StringValue
   - Assigns index = position in _astrVariableName list

2. **Access** (WORD token): WordStatement → Variables.index(name)
   - Retrieves variable index from name
   - Pushes index to stack for fetch/store operations

3. **Fetch** (@ token): FetchStatement → Variables.get(index)
   - Pops variable index from stack
   - Retrieves and pushes value to stack

4. **Store** (! token): StoreStatement → Variables.put(index, value)
   - Pops value and index from stack
   - Stores value at variable location

5. **Debug** (? token): QuestionStatement → Variables.get(index) → Printer.println()
   - Pops variable index from stack
   - Fetches value and prints to console
   - Convenient debugging operator combining fetch + print

### Parser Architecture (Recursive Descent)
The ForthParser implements a clean recursive descent pattern with comprehensive token support:

**Token Categories:**
- Arithmetic Operators: +, -, *, /, MOD
- Comparison Operators: =, <>, <, >, <=, >=, 0=, 0<>, 0<, 0>
- Stack Operations: DUP, DROP, SWAP, OVER, ROT, NIP, TUCK, PICK, ROLL, DEPTH
- I/O Operations: PRINT (.), PRINT_KEEP_STACK (.S), CARRIAGE_RETURN (CR), QUESTION (?)
- Variable Operations: VARIABLE (definition), WORD (access)
- Memory Operations: FETCH (@), STORE (!), 2FETCH, 2STORE, CHAR_FETCH, CHAR_STORE
- Number Literals: Converted to NumberStatement by parser

---

## Unimplemented FORTH Words by Category

### Core Stack Operations (50+ words)

| Word | Purpose | Status |
|------|---------|--------|
| `!` [✓] | Store value in memory at address | IMPLEMENTED |
| `@` [✓] | Fetch value from memory at address | IMPLEMENTED |
| `2!` [✓] | Store two values (cell pair) in memory | IMPLEMENTED |
| `2@` [✓] | Fetch two values (cell pair) from memory | IMPLEMENTED |
| `C!` [✓] | Store byte value in memory | IMPLEMENTED |
| `C@` [✓] | Fetch byte value from memory | IMPLEMENTED |
| `?` [✓] | Fetch and display value from memory address | IMPLEMENTED |
| `DUP` [✓] | Duplicate top stack value | IMPLEMENTED |
| `?DUP` [✓] | Duplicate top value if non-zero | IMPLEMENTED |
| `DROP` [✓] | Remove top stack value | IMPLEMENTED |
| `2DROP` [✓] | Remove top two stack values | IMPLEMENTED |
| `SWAP` [✓] | Exchange top two stack values | IMPLEMENTED |
| `2SWAP` [✓] | Exchange top two pairs of values | IMPLEMENTED |
| `OVER` [✓] | Copy second stack value to top | IMPLEMENTED |
| `2OVER` [✓] | Copy second pair to top | IMPLEMENTED |
| `ROT` [✓] | Rotate top three stack values | IMPLEMENTED |
| `2ROT` [✓] | Rotate top three pairs | IMPLEMENTED |
| `-ROT` [✓] | Reverse rotate top three values | IMPLEMENTED |
| `NIP` [✓] | Remove second stack value | IMPLEMENTED |
| `TUCK` [✓] | Copy top value and place below second | IMPLEMENTED |
| `PICK` [✓] | Copy nth stack value to top | IMPLEMENTED |
| `ROLL` [✓] | Move nth stack value to top | IMPLEMENTED |
| `DEPTH` [✓] | Push current stack depth | IMPLEMENTED |

### Arithmetic Operations (29+ words)

| Word | Purpose | Status |
|------|---------|--------|
| `+` [✓] | Add top two stack values | IMPLEMENTED |
| `-` [✓] | Subtract top two stack values | IMPLEMENTED |
| `*` [✓] | Multiply top two stack values | IMPLEMENTED |
| `/` [✓] | Divide top two stack values | IMPLEMENTED |
| `MOD` [✓] | Modulo (remainder) of division | IMPLEMENTED |
| `1+` | Add 1 to top stack value | NOT IMPLEMENTED |
| `1-` | Subtract 1 from top stack value | NOT IMPLEMENTED |
| `2*` | Multiply top value by 2 (shift left) | NOT IMPLEMENTED |
| `2/` | Divide top value by 2 (shift right) | NOT IMPLEMENTED |
| `ABS` | Replace with absolute value | NOT IMPLEMENTED |
| `NEGATE` | Negate top stack value | NOT IMPLEMENTED |
| `MAX` | Replace top two values with maximum | NOT IMPLEMENTED |
| `MIN` | Replace top two values with minimum | NOT IMPLEMENTED |
| `M*` | Multiply, return 64-bit result | NOT IMPLEMENTED |
| `M*/` | Multiply then divide, maintaining precision | NOT IMPLEMENTED |
| `M+` | Multiply and add | NOT IMPLEMENTED |
| `UM*` | Unsigned multiply | NOT IMPLEMENTED |
| `UM/MOD` | Unsigned divide and remainder | NOT IMPLEMENTED |
| `SM/REM` | Signed divide with remainder | NOT IMPLEMENTED |
| `FM/MOD` | Floor divide with modulo | NOT IMPLEMENTED |
| `D+` | Add two 64-bit values | NOT IMPLEMENTED |
| `D-` | Subtract two 64-bit values | NOT IMPLEMENTED |
| `D2*` | Double precision multiply by 2 | NOT IMPLEMENTED |
| `D2/` | Double precision divide by 2 | NOT IMPLEMENTED |
| `DABS` | Double precision absolute value | NOT IMPLEMENTED |
| `DNEGATE` | Double precision negate | NOT IMPLEMENTED |
| `S>D` | Convert signed to double | NOT IMPLEMENTED |
| `D>S` | Convert double to signed | NOT IMPLEMENTED |
| `SIGN` | Get sign of number (-1, 0, or 1) | NOT IMPLEMENTED |

### Logical and Bitwise Operations (15+ words)

| Word | Purpose |
|------|---------|
| `AND` | Bitwise AND |
| `OR` | Bitwise OR |
| `XOR` | Bitwise XOR |
| `INVERT` | Bitwise NOT (one's complement) |
| `LSHIFT` | Logical left shift |
| `RSHIFT` | Logical right shift |
| `TRUE` | Push true flag (-1) |
| `FALSE` | Push false flag (0) |
| `WITHIN` | Check if value is within range |
| (Additional 6+ operations) | ... |

### Control Flow (40+ words)

| Word | Purpose |
|------|---------|
| `IF` | Start conditional block |
| `THEN` | End conditional block |
| `ELSE` | Else clause in conditional |
| `DO` | Start counted loop |
| `LOOP` | End loop, increment counter |
| `BEGIN` | Start indefinite loop |
| `UNTIL` | End loop with exit condition |
| `WHILE` | Loop while condition true |
| (Additional 32+ operations) | ... |

### Word Definition (20+ words)

| Word | Purpose |
|------|---------|
| `:` | Start word definition |
| `;` | End word definition |
| `CONSTANT` | Define named constant |
| `VARIABLE` [✓] | Define named variable |
| (Additional 16+ operations) | ... |

### Memory Operations (20+ words)

| Word | Purpose |
|------|---------|
| `HERE` | Get current data space pointer |
| `ALLOT` | Allocate memory bytes |
| `FILL` | Fill memory with value |
| (Additional 17+ operations) | ... |

### I/O Operations (30+ words)

| Word | Purpose |
|------|---------|
| `EMIT` | Output single character |
| `KEY` | Read single character from input |
| `TYPE` | Output string |
| `CR` [✓] | Output carriage return (newline) |
| `.S` [✓] | Output stack contents |
| `.` [✓] | Output integer value |
| `?` [✓] | Fetch and output variable value |
| (Additional 23+ operations) | ... |

### Floating-Point Operations (60+ words)

(Comprehensive list of floating-point operations - not yet implemented)

### String Operations (20+ words)

(String manipulation and parsing operations - not yet implemented)

### File I/O (20+ words)

(File operations - not yet implemented)

### Dictionary and Word Search (20+ words)

(Word lookup and meta-operations - not yet implemented)

### Compilation and Interpretation (25+ words)

(Compilation-mode operations - not yet implemented)

### Control and System (30+ words)

(System control and management - not yet implemented)

---

## Summary Statistics

- **Total Reserved Words:** 420+
- **Implemented Words:** 38 (9.0%)
- **Fully Tested and Documented:** 38 (100% of implemented)
- **Unimplemented Words:** 382+
- **Unit Tests:** 1642 (all passing)

---

## Implementation Progress by Category

| Category | Implemented | Total | Progress | Notes |
|----------|------------|-------|----------|-------|
| Stack Operations | 16 | 50+ | 32% | All basic operations implemented |
| Comparison Operations | 10 | 10+ | 100% | All comparison operators implemented |
| Arithmetic Operations | 5 | 30+ | 17% | Basic operations only |
| I/O Operations | 4 | 30+ | 13% | Basic I/O + debug operator |
| Variable/Memory Operations | 7 | 20+ | 35% | Core variable management complete |
| Word Definition | 1 | 20+ | 5% | VARIABLE implemented |
| Logical and Bitwise Operations | 0 | 15+ | 0% | Not yet implemented |
| Control Flow | 0 | 40+ | 0% | Critical path for future work |
| Memory Operations | 0 | 20+ | 0% | Requires heap management |
| Floating-Point Operations | 0 | 60+ | 0% | Not yet implemented |
| String Operations | 0 | 20+ | 0% | Not yet implemented |
| File I/O | 0 | 20+ | 0% | Requires platform support |
| Dictionary and Word Search | 0 | 20+ | 0% | Advanced feature |
| Compilation and Interpretation | 0 | 25+ | 0% | Advanced feature |
| Control and System | 0 | 30+ | 0% | System-level operations |
| Advanced Structure | 0 | 15+ | 0% | Advanced feature |
| Miscellaneous | 0 | 50+ | 0% | Various utilities |
| Keyboard and Extended Keys | 0 | 30+ | 0% | Platform-specific |
| Locals and Advanced Features | 0 | 10+ | 0% | Advanced feature |

**Overall Progress:** 38 / 420+ words (9.0%)

---

## Test Coverage Summary

### Test Files and Coverage
- **StackStatementTest.java:** 36 tests ✓
- **ArithmeticStatementTests:** 50+ tests ✓
- **ComparisonStatementTests:** 200+ tests ✓
- **PrintStatementTest.java:** 47 tests ✓
- **PrintKeepStackStatementTest.java:** 43 tests ✓
- **CarriageReturnStatementTest.java:** 47 tests ✓
- **VariableStatementTest.java:** 25 tests ✓
- **WordStatementTest.java:** 34 tests ✓ (NEW)
- **QuestionStatementTest.java:** 19 tests ✓ (NEW)
- **FetchStatementTest.java:** 34 tests ✓
- **ForthParserTest.java:** 75 tests ✓
- **Other Tests:** 800+ tests ✓

**Total: 1642 unit tests, 100% passing**

### Code Quality Metrics
- **Comprehensive Documentation:** All implemented features have full JavaDoc
- **Test Coverage:** 100% of implemented features
- **Code Style:** Consistent with project standards
- **Architecture:** Well-designed dual-storage variable system with clear separation of concerns

---

## Implementation Priority

### Phase 1 (Core - Essential for any program) ✓ PARTIALLY COMPLETE

**Completed:**
- [x] All stack operations (DUP, DROP, SWAP, OVER, ROT, etc.) - 16/16
- [x] Basic I/O (PRINT, PRINT KEEP STACK, CARRIAGE_RETURN, QUESTION) - 4/4
- [x] Memory access (@, !, C@, C!, 2@, 2!) - 6/6
- [x] Arithmetic (+, -, *, /, MOD) - 5/5
- [x] Comparison (=, <>, <, >, <=, >=, 0=, 0<>, 0<, 0>) - 10/10
- [x] Variable operations (VARIABLE, WORD access) - 2/2

**Remaining:**
- [ ] Additional I/O (EMIT, KEY, TYPE)
- [ ] Additional arithmetic (1+, 1-, ABS, NEGATE, MIN, MAX)

### Phase 2 (Control Flow - Enables complex programs)
- [ ] IF/THEN/ELSE
- [ ] DO/LOOP/+LOOP
- [ ] BEGIN/UNTIL/WHILE
- [ ] Conditionals and flow control

### Phase 3 (Word Definition - Enables code reuse)
- [ ] CONSTANT, VALUE
- [ ] CREATE/DOES>
- [ ] Word compilation modes ([ ], POSTPONE, etc.)

### Phase 4 (Advanced - Extended capabilities)
- [ ] Floating-point operations
- [ ] File I/O
- [ ] Advanced strings
- [ ] Exception handling (CATCH, THROW)

### Phase 5 (Specialized - Less common features)
- [ ] Extended keyboard support
- [ ] Advanced structure definitions
- [ ] Environment queries
- [ ] Miscellaneous utilities

---

## Code Quality Enhancements This Session

### Documentation Improvements
- ✅ Enhanced class-level documentation for VariableStatement, WordStatement, QuestionStatement
- ✅ Comprehensive method-level JavaDoc for all implemented features
- ✅ Added inline code comments explaining complex operations
- ✅ Documented stack notation for all I/O operations
- ✅ Explained dual-storage architecture in Variables class

### Testing Enhancements
- ✅ 93 new comprehensive unit tests
- ✅ Full coverage of edge cases and error scenarios
- ✅ Integration tests verifying complete workflows
- ✅ System.out redirection for testing I/O operations
- ✅ Proper setUp/tearDown for state management

### Architectural Enhancements
- ✅ Fixed getTokenNumber() inconsistencies across statement classes
- ✅ Unified execute() method signatures with proper @Override annotations
- ✅ Comprehensive documentation of parser token handling
- ✅ Clear separation of concerns in variable operations

---

## Notes for Future Development

### Next Steps (Recommended)
1. **Implement Control Flow:** IF/THEN/ELSE and DO/LOOP structures are critical for any real FORTH programs
2. **Extend I/O:** Add EMIT, KEY, TYPE for better interactive programs
3. **Add More Arithmetic:** 1+, 1-, ABS, NEGATE, MIN, MAX for convenience
4. **Implement Word Definition:** : and ; for user-defined words

### Architectural Considerations
- The dual-storage Variables architecture is solid and well-tested
- Parser is cleanly structured and easy to extend with new tokens
- Statement classes follow consistent patterns for easy maintenance
- Test suite provides good regression protection for future changes

### Known Limitations
- No floating-point support yet
- No file I/O capabilities
- No control flow structures (IF/THEN, DO/LOOP)
- No exception handling (CATCH/THROW)
- Limited to single-threaded execution model

---

## References

For detailed FORTH word definitions, refer to:
- ISO/IEC 14514 FORTH Standard
- Forth 2012 Standard Documentation
- `docs/03_STANDARD_WORDS.md` in this project

### Implementation Files
- `/src/main/java/eu/gricom/forth/statements/` - Statement implementations
- `/src/main/java/eu/gricom/forth/parser/ForthParser.java` - Token parser
- `/src/main/java/eu/gricom/forth/memoryManager/Variables.java` - Variable storage
- `/src/test/java/eu/gricom/forth/statements/` - Comprehensive test suites

---

**Document Status:** Final (2026-08-03)  
**Maintained By:** Andreas Grimm  
**Last Review:** Comprehensive refactoring and documentation enhancement session