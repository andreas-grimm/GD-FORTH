# Test Harness Documentation

## Overview

The **test-harness.fs** file provides the foundational testing framework for the Forth interpreter test suite. It implements the standard Forth testing format (`T{ ... -> ... }T`) and provides error tracking, floating-point tolerance support, and test result reporting.

## Test Cases

The test harness doesn't contain traditional "test cases" but rather provides testing utilities and macros used by all other test files.

## What Is Being Tested

The test harness validates:
1. **Test macro execution** — T{ }T syntax processing
2. **Stack depth tracking** — Verification of expected vs. actual stack states
3. **Error handling** — ERROR-XT variable for custom error management
4. **Floating-point tolerance** — Precision comparison for inexact arithmetic
5. **Test result aggregation** — Counting passed/failed tests and reporting

## Structure of Test Cases

### Test Macro Framework

```forth
T{ ... -> ... }T
```

- **T{** — Opens a test block, records initial stack depth
- **->** — Separator between code execution and expected results
- **}T** — Closes test block, verifies stack contents

### Components

#### 1. Test Tracking Variables

```forth
VARIABLE TESTS-RUN      \ Total tests executed
VARIABLE TESTS-PASSED   \ Count of successful tests
VARIABLE TESTS-FAILED   \ Count of failed tests
VARIABLE VERBOSE        \ Debug output control
```

#### 2. Floating-Point Support

```forth
VARIABLE SET-EXACT      \ Flag: exact comparison mode
VARIABLE SET-NEAR       \ Tolerance value for approximate comparison
```

#### 3. Error Handling

```forth
VARIABLE ERROR-XT       \ Execution token for error handler
: HANDLE-ERROR ( n -- ) \ Execute custom error handling
   ERROR-XT @ ?DUP IF EXECUTE ELSE THROW THEN ;
```

#### 4. Test Macros

**T{ macro:**
```forth
: T{
   DEPTH >R             \ Save initial stack depth on return stack
;
```
Records the stack depth before executing test code.

**-> macro:**
```forth
: ->
   DEPTH R@ = IF        \ Compare current depth with saved depth
      R> DROP           \ Depths match, clean up
   ELSE
      R> DROP
      CR ." Stack depth mismatch: expected " . ." got " DEPTH .
      1 TESTS-FAILED +! \ Increment failure counter
      ABORT             \ Stop test
   THEN
;
```
Validates that the stack depth after execution matches expectations.

**}T macro:**
```forth
: }T
   TESTS-RUN @ 1+ TESTS-RUN !      \ Increment total test count
   TESTS-PASSED @ 1+ TESTS-PASSED ! \ Increment passed count
;
```
Finalizes the test and updates counters.

#### 5. Floating-Point Comparison

```forth
: SET-EXACT ( -- )
   0 SET-EXACT !        \ Switch to exact comparison
   1 SET-NEAR ! ;       \ Reset tolerance

: SET-NEAR ( tol -- )
   1 SET-EXACT !        \ Switch to approximate comparison
   SET-NEAR ! ;         \ Store tolerance value

: F-WITHIN? ( f1 f2 -- flag )
   SET-EXACT @ IF
      F=                 \ Exact floating-point comparison
   ELSE
      SET-NEAR @ F* FABS FROT FROT F- FABS F< IF TRUE ELSE FALSE THEN
   THEN ;
```

Provides approximate floating-point comparison for tests where exact equality isn't expected (e.g., FSQRT).

#### 6. Test Reporting

```forth
: TEST-SUMMARY ( -- )
   CR CR ." ===== TEST SUMMARY ====="
   CR ." Total: " TESTS-RUN @ .
   CR ." Passed: " TESTS-PASSED @ .
   CR ." Failed: " TESTS-FAILED @ .
   TESTS-FAILED @ 0= IF
      CR ." All tests passed!"
   ELSE
      CR ." Some tests failed!"
   THEN
   CR
;
```

Prints aggregated test results at the end of each test file.

#### 7. Simple Assertion

```forth
: ASSERT ( flag -- )
   IF
      1 TESTS-PASSED +!  \ Increment passed count
   ELSE
      CR ." Test failed!"
      1 TESTS-FAILED +! \ Increment failed count
   THEN
   1 TESTS-RUN +!       \ Increment total count
;
```

Alternative testing mechanism for non-standard assertions.

## Code Documentation

### Initialization

When test-harness.fs is loaded:

```forth
0 TESTS-RUN !           \ Start with 0 tests run
0 TESTS-PASSED !        \ Start with 0 tests passed
0 TESTS-FAILED !        \ Start with 0 tests failed
1 VERBOSE !             \ Enable output
0 SET-EXACT !           \ Floating-point mode
1 SET-NEAR !            \ Default tolerance
```

### Test Execution Flow

1. **Record Depth**: `T{` saves the stack depth
2. **Execute Code**: Forth code between `T{` and `->` executes normally
3. **Verify Depth**: `->` checks that current depth matches expected
4. **Finalize**: `}T` increments counters
5. **Report**: `TEST-SUMMARY` displays results

### Error Handling Flow

If a test fails:
1. `->` detects stack depth mismatch
2. Prints diagnostic message
3. Increments `TESTS-FAILED`
4. Calls `ABORT` to stop processing

If custom error handling is needed:
1. Store handler execution token in `ERROR-XT`
2. Use `HANDLE-ERROR` to invoke it
3. Handler can log, skip, or rethrow errors

## Expected Outcomes

### Successful Test Run

```
===== TEST SUMMARY =====
Total: 25
Passed: 25
Failed: 0
All tests passed!
```

### Test with Failures

```
Stack depth mismatch: expected 1 got 2
===== TEST SUMMARY =====
Total: 25
Passed: 23
Failed: 2
Some tests failed!
```

### Floating-Point Test

When using `SET-NEAR 0.0001`:

```forth
T{ 1.1 2.2 F+ -> 3.3 }T
```

Accepts results within ±0.0001 tolerance instead of exact equality.

## Usage Examples

### Basic Test Execution

```forth
INCLUDE test-harness.fs

T{ 5 3 + -> 8 }T       \ Test addition
T{ 1 2 SWAP -> 2 1 }T  \ Test SWAP
T{ 0 DUP -> 0 0 }T     \ Test DUP

TEST-SUMMARY            \ Print results
```

### Floating-Point Tests

```forth
SET-NEAR 0.0001         \ Set tolerance to 0.0001

T{ 4.0 FSQRT -> 2.0 }T  \ Floating-point result
T{ 9.0 FSQRT -> 3.0 }T

TEST-SUMMARY
```

### Custom Error Handling

```forth
: MY-ERROR-HANDLER
   CR ." Custom error: " . CR ;

['] MY-ERROR-HANDLER ERROR-XT !

T{ 5 DUP -> 5 5 }T      \ Uses custom handler if error

TEST-SUMMARY
```

## Integration with Test Files

All test files include the harness at the beginning:

```forth
INCLUDE test-harness.fs
```

This provides:
- `T{...->...}T` macros
- `TEST-SUMMARY` reporting
- Floating-point tolerance support
- Error tracking infrastructure

## Notes

- **Stack depth validation**: The harness validates stack contents by checking depth before and after code execution
- **Non-destructive verification**: The `->` operator doesn't consume the expected results; it merely validates the stack state
- **Floating-point precision**: Default tolerance is 0.0001 (0.01%), adjustable with `SET-NEAR`
- **Test isolation**: Each test file initializes counters, so per-file summaries are independent
- **Error recovery**: `ABORT` halts the current test file; multi-file runners (run-all-tests.fs) catch this

## Limitations

1. **Stack-only validation**: Only verifies stack depth and content; doesn't validate memory state or I/O output
2. **Single-file scope**: `TESTS-RUN`, `TESTS-PASSED`, `TESTS-FAILED` are local to each included file
3. **No debugging symbols**: Failed tests don't indicate which line in the code failed
4. **Floating-point limitations**: Tolerance is relative to the value; very small numbers may need smaller tolerances

---

**Last Updated**: 2026-07-20
**Standard Reference**: ANS Forth (forth-standard.org)
